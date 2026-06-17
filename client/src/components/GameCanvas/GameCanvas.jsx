import { useRef, useState, useMemo, useEffect, useCallback } from "react";

import useKeyboardControls from "../../hooks/useKeyboardControls";
import useGameLoop from "../../hooks/useGameLoop";
import usePhysicsEngine from "../../hooks/usePhysicsEngine";
import useLevelManager from "../../hooks/useLevelManager";
import usePlayerMeta from "../../hooks/usePlayerMeta";

import { GAME_CONSTANTS } from "../../game/config";
import {
    createPlatforms,
    createPlayers,
    createEnemies,
    createCoins,
    getActivePlayers,
} from "../../game/factories";
import { drawGameObjects } from "../../game/renderer";
import { collectCoins } from "../../game/collisions";

import { patchPlayer } from "../../api/players";
import { patchUserCheckpoint } from "../../api/users";

import GameStatusOverlay from "./GameStatusOverlay";
import GameSidebar from "./GameSidebar";
import styles from "./GameCanvas.module.css";

const GameCanvas = ({ players, onExit }) => {
    const canvasRef = useRef(null);

    const player1 = useRef(null);
    const player2 = useRef(null);
    const enemies = useRef([]);
    const coins = useRef([]);
    const didStartRun = useRef(false);

    const [gameOver, setGameOver] = useState(false);
    const [coinsCollected, setCoinsCollected] = useState([]);
    const [currentLevelCoinsCollected, setCurrentLevelCoinsCollected] = useState(0);
    const [lives, setLives] = useState(3);
    const [paused, setPaused] = useState(false);

    const {
        playerCount,
        getPlayerIdByIndex,
        getUserIdByIndex,
        getPlayerColor,
    } = usePlayerMeta(players);

    const {
        level,
        loadingLevel,
        levelError,
        currentLevelNumber,
        levelComplete,
        setLevelComplete,
        loadLevel,
        levelTransitioning,
        levelTransitionTimeout,
        clearLevelTransitionTimeout,
    } = useLevelManager();

    const { keys1, keys2, resetKeys } = useKeyboardControls(playerCount);

    const { physics, applyGravity } = usePhysicsEngine(
        GAME_CONSTANTS.gravity,
        GAME_CONSTANTS.jumpStrength
    );

    const platforms = useMemo(() => {
        return createPlatforms(level);
    }, [level]);

    const initGame = useCallback(() => {
        if (!level) return;

        const createdPlayers = createPlayers(level, playerCount);

        player1.current = createdPlayers.player1;
        player2.current = createdPlayers.player2;
        enemies.current = createEnemies(level);
        coins.current = createCoins(level);

        setCurrentLevelCoinsCollected(0);
        setLives(level.lives ?? 3);
        setGameOver(false);
        resetKeys();
    }, [level, playerCount, resetKeys]);

    const startNewRun = useCallback(() => {
        clearLevelTransitionTimeout();

        setCoinsCollected(Array.from({ length: playerCount }, () => 0));
        setCurrentLevelCoinsCollected(0);
        setLives(3);
        setGameOver(false);
        setPaused(false);
        setLevelComplete(false);

        resetKeys();
        loadLevel(1);
    }, [
        playerCount,
        resetKeys,
        loadLevel,
        setLevelComplete,
        clearLevelTransitionTimeout,
    ]);

    useEffect(() => {
        if (didStartRun.current) return;

        didStartRun.current = true;
        startNewRun();
    }, [startNewRun]);

    useEffect(() => {
        return () => {
            clearLevelTransitionTimeout();
        };
    }, [clearLevelTransitionTimeout]);

    useEffect(() => {
        if (level) {
            initGame();
        }
    }, [level, initGame]);

    const handleTogglePause = useCallback(() => {
        setPaused((prev) => !prev);
        resetKeys();
    }, [resetKeys]);

    const handleRestart = useCallback(() => {
        startNewRun();
    }, [startNewRun]);

    const handleExit = useCallback(() => {
        setPaused(false);
        setLevelComplete(false);
        clearLevelTransitionTimeout();

        if (typeof onExit === "function") {
            onExit();
        } else {
            initGame();
        }
    }, [
        onExit,
        initGame,
        setLevelComplete,
        clearLevelTransitionTimeout,
    ]);

    const syncPlayerHp = useCallback(
        (nextLives, activePlayers) => {
            activePlayers.forEach(({ idx }) => {
                const playerId = getPlayerIdByIndex(idx);

                if (!playerId) return;

                patchPlayer(playerId, {
                    hp: nextLives,
                    status: nextLives <= 0 ? "dead" : "playing",
                }).catch((error) => {
                    console.error("Could not update player hp:", error);
                });
            });
        },
        [getPlayerIdByIndex]
    );

    const resetHitPlayer = useCallback(
        (playerObj) => {
            if (!level) return;

            const playerStart = level.playerStart || { x: 50, y: 200 };
            const resetX =
                playerObj.idx === 0
                    ? playerStart.x
                    : playerStart.x + 100;

            playerObj.player.reset(resetX, playerStart.y);
            playerObj.player.velocityY = 0;
            playerObj.player.canJump =
                playerObj.player.y + playerObj.player.height >=
                playerObj.player.groundY;

            resetKeys();
        },
        [level, resetKeys]
    );

    const handleEnemyCollisions = useCallback(
        (activePlayers) => {
            let collisionHandled = false;

            for (const enemy of enemies.current) {
                if (collisionHandled) break;

                for (const playerObj of activePlayers) {
                    const player = playerObj.player;

                    if (!enemy.checkCollision(player)) {
                        continue;
                    }

                    collisionHandled = true;

                    if (enemy.isStompedBy(player)) {
                        enemy.takeDamage(1);

                        player.velocityY = -8;
                        player.canJump = false;

                        enemies.current = enemies.current.filter((e) => e.hp > 0);
                        break;
                    }

                    setLives((prevLives) => {
                        const nextLives = Math.max(
                            0,
                            prevLives - (enemy.damage ?? 1)
                        );

                        syncPlayerHp(nextLives, activePlayers);

                        if (nextLives <= 0) {
                            setGameOver(true);
                        } else {
                            resetHitPlayer(playerObj);
                        }

                        return nextLives;
                    });

                    break;
                }
            }
        },
        [syncPlayerHp, resetHitPlayer]
    );

    const handleCoinCollisions = useCallback(
        (activePlayers) => {
            const { remainingCoins, collected } = collectCoins(
                coins.current,
                activePlayers
            );

            coins.current = remainingCoins;

            collected.forEach(({ playerIndex }) => {
                setCoinsCollected((prev) => {
                    const copy = [...prev];
                    const nextCoins = (copy[playerIndex] || 0) + 1;

                    copy[playerIndex] = nextCoins;

                    const playerId = getPlayerIdByIndex(playerIndex);

                    if (playerId) {
                        patchPlayer(playerId, { coins: nextCoins }).catch((error) => {
                            console.error("Could not update player coins:", error);
                        });
                    }

                    return copy;
                });

                setCurrentLevelCoinsCollected((prev) => prev + 1);
            });

            return remainingCoins;
        },
        [getPlayerIdByIndex]
    );

    const completeLevelIfNeeded = useCallback(
        (remainingCoins, activePlayers) => {
            if (!level) return;

            if (
                remainingCoins.length > 0 ||
                (level.coins?.length ?? 0) === 0 ||
                levelTransitioning.current
            ) {
                return;
            }

            levelTransitioning.current = true;
            setLevelComplete(true);
            resetKeys();

            const nextLevelNumber = currentLevelNumber + 1;

            activePlayers.forEach(({ idx }) => {
                const userId = getUserIdByIndex(idx);

                if (!userId) return;

                patchUserCheckpoint(userId, nextLevelNumber).catch((error) => {
                    console.error("Could not update user checkpoint:", error);
                });
            });

            levelTransitionTimeout.current = window.setTimeout(() => {
                levelTransitionTimeout.current = null;
                loadLevel(nextLevelNumber);
            }, 1000);
        },
        [
            level,
            currentLevelNumber,
            getUserIdByIndex,
            loadLevel,
            resetKeys,
            setLevelComplete,
            levelTransitioning,
            levelTransitionTimeout,
        ]
    );

    const drawFrame = useCallback(() => {
        if (gameOver || !level) return;

        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext?.("2d");
        if (!ctx) return;

        if (keys1.current.up && player1.current) {
            physics.jump(player1.current);
        }

        if (playerCount > 1 && player2.current && keys2.current.up) {
            physics.jump(player2.current);
        }

        const activePlayers = getActivePlayers(
            player1.current,
            player2.current
        );

        applyGravity(
            activePlayers.map((p) => p.player),
            GAME_CONSTANTS.groundY,
            platforms
        );

        if (player1.current) {
            player1.current.move(keys1.current, canvas.width);
        }

        if (playerCount > 1 && player2.current) {
            player2.current.move(keys2.current, canvas.width);
        }

        drawGameObjects({
            ctx,
            canvas,
            platforms,
            enemies: enemies.current,
            coins: coins.current,
            players: activePlayers,
            getPlayerColor,
        });

        handleEnemyCollisions(activePlayers);

        const remainingCoins = handleCoinCollisions(activePlayers);

        completeLevelIfNeeded(remainingCoins, activePlayers);
    }, [
        gameOver,
        level,
        keys1,
        keys2,
        playerCount,
        physics,
        applyGravity,
        platforms,
        getPlayerColor,
        handleEnemyCollisions,
        handleCoinCollisions,
        completeLevelIfNeeded,
    ]);

    useGameLoop(drawFrame, !gameOver && !!level && !paused && !levelComplete);

    if (loadingLevel) {
        return <div className={styles.container}>Loading level...</div>;
    }

    if (levelError) {
        return <div className={styles.container}>{levelError}</div>;
    }

    if (!level) {
        return <div className={styles.container}>No level data.</div>;
    }

    return (
        <div className={styles.page}>
            <div className={styles.gameArea}>
                <GameStatusOverlay
                    paused={paused}
                    gameOver={gameOver}
                    levelComplete={levelComplete}
                    players={players}
                    coinsCollected={coinsCollected}
                    onRestart={handleRestart}
                    onExit={handleExit}
                />

                <canvas
                    ref={canvasRef}
                    width={GAME_CONSTANTS.canvasWidth}
                    height={GAME_CONSTANTS.canvasHeight}
                    className={styles.canvas}
                    role="application"
                    aria-label="Game canvas"
                />
            </div>

            <GameSidebar
                players={players}
                coinsCollected={coinsCollected}
                currentLevelCoinsCollected={currentLevelCoinsCollected}
                totalCoins={level.coins?.length ?? 0}
                lives={lives}
                playerCount={playerCount}
                levelNumber={currentLevelNumber}
                paused={paused}
                gameOver={gameOver}
                onTogglePause={handleTogglePause}
                onRestart={handleRestart}
                onExit={handleExit}
            />
        </div>
    );
};

export default GameCanvas;