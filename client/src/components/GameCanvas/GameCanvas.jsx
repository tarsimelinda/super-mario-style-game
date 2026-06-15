import { useRef, useState, useMemo, useEffect, useCallback } from "react";
import useKeyboardControls from "../../hooks/useKeyboardControls";
import useGameLoop from "../../hooks/useGameLoop";
import usePhysicsEngine from "../../hooks/usePhysicsEngine";
import Player from "../../game/Player";
import Enemy from "../../game/Enemy";
import Platform from "../../game/Platform";
import Coins from "../../game/Coins";
import GameOver from "../GameOver/GameOver";
import Scoreboard from "../Scoreboard/Scoreboard";
import { GAME_CONSTANTS } from "../../game/config";
import { fetchRandomLevel } from "../../api/levelApi";
import styles from "./GameCanvas.module.css";

const GameCanvas = ({ players, onExit }) => {
    const [gameOver, setGameOver] = useState(false);
    const [coinsCollected, setCoinsCollected] = useState([]);
    const [currentLevelCoinsCollected, setCurrentLevelCoinsCollected] = useState(0);
    const [lives, setLives] = useState(3);
    const [level, setLevel] = useState(null);
    const [loadingLevel, setLoadingLevel] = useState(true);
    const [levelError, setLevelError] = useState(null);
    const [currentLevelNumber, setCurrentLevelNumber] = useState(1);

    const canvasRef = useRef(null);

    const playerCount = Array.isArray(players) ? players.length : players;
    const player1 = useRef(null);
    const player2 = useRef(null);

    const { keys1, keys2, resetKeys } = useKeyboardControls(playerCount);
    const { physics, applyGravity } = usePhysicsEngine(
        GAME_CONSTANTS.gravity,
        GAME_CONSTANTS.jumpStrength
    );

    const platforms = useMemo(() => {
        if (!level) return [];

        return level.platforms.map(
            (platform) =>
                new Platform(
                    platform.x,
                    platform.y,
                    platform.width,
                    platform.height
                )
        );
    }, [level]);

    const enemies = useRef([]);
    const coins = useRef([]);
    const levelTransitioning = useRef(false);

    const loadLevel = useCallback(async (levelNumber) => {
        try {
            setLoadingLevel(true);
            setLevelError(null);
            levelTransitioning.current = true;

            const loadedLevel = await fetchRandomLevel();

            setLevel(loadedLevel);
            setCurrentLevelNumber(levelNumber);
        } catch (error) {
            console.error("Could not load level:", error);
            setLevelError("Could not load level.");
        } finally {
            setLoadingLevel(false);
            levelTransitioning.current = false;
        }
    }, []);

    const startNewRun = useCallback(() => {
        setCoinsCollected(Array.from({ length: playerCount }, () => 0));
        setCurrentLevelCoinsCollected(0);
        setLives(3);
        setGameOver(false);
        resetKeys();
        loadLevel(1);
    }, [playerCount, resetKeys, loadLevel]);

    useEffect(() => {
        startNewRun();
    }, [startNewRun]);

    const initGame = useCallback(() => {
        if (!level) return;

        const playerStart = level.playerStart || { x: 50, y: 200 };

        player1.current = new Player(
            playerStart.x,
            playerStart.y,
            50,
            50,
            5
        );

        player2.current =
            playerCount > 1
                ? new Player(playerStart.x + 100, playerStart.y, 50, 50, 5)
                : null;

        enemies.current = level.enemies.map(
            (enemy) =>
                new Enemy(
                    enemy.x,
                    enemy.y,
                    enemy.width,
                    enemy.height,
                    enemy.speed ?? 2,
                    GAME_CONSTANTS.canvasWidth
                )
        );

        coins.current = level.coins.map(
            (coin) =>
                new Coins(
                    coin.x,
                    coin.y,
                    coin.width,
                    coin.height
                )
        );

        setCurrentLevelCoinsCollected(0);
        setLives(level.lives ?? 3);
        resetKeys();
        setGameOver(false);
    }, [level, playerCount, resetKeys]);

    useEffect(() => {
        if (level) {
            initGame();
        }
    }, [level, initGame]);

    const drawFrame = useCallback(() => {
        if (gameOver || !level) return;

        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext && canvas.getContext("2d");
        if (!ctx) return;

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        ctx.fillStyle = "#87ceeb";
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        ctx.fillStyle = "green";
        ctx.fillRect(
            0,
            GAME_CONSTANTS.groundY,
            canvas.width,
            canvas.height - GAME_CONSTANTS.groundY
        );

        if (keys1.current.up && player1.current) {
            physics.jump(player1.current);
        }

        if (playerCount > 1 && player2.current && keys2.current.up) {
            physics.jump(player2.current);
        }

        const playersArr = [];

        if (player1.current) {
            playersArr.push({ player: player1.current, idx: 0 });
        }

        if (player2.current) {
            playersArr.push({ player: player2.current, idx: 1 });
        }

        applyGravity(
            playersArr.map((p) => p.player),
            GAME_CONSTANTS.groundY,
            platforms
        );

        if (player1.current) {
            player1.current.move(keys1.current, canvas.width);
        }

        if (playerCount > 1 && player2.current) {
            player2.current.move(keys2.current, canvas.width);
        }

        platforms.forEach((platform) => platform.draw(ctx));

        enemies.current.forEach((enemy) => {
            enemy.move();
            enemy.draw(ctx);
        });

        coins.current.forEach((coin) => coin.draw(ctx));

        playersArr.forEach((pObj) =>
            pObj.player.draw(ctx, pObj.idx === 0 ? "red" : "cyan")
        );

        let collisionHandled = false;

        for (const enemy of enemies.current) {
            if (collisionHandled) break;

            for (const pObj of playersArr) {
                if (enemy.checkCollision(pObj.player)) {
                    collisionHandled = true;

                    setLives((prevLives) => {
                        const next = prevLives - 1;

                        if (next <= 0) {
                            setGameOver(true);
                        } else {
                            const playerStart = level.playerStart || { x: 50, y: 200 };

                            if (player1.current) {
                                player1.current.reset(playerStart.x, playerStart.y);
                            }

                            if (player2.current) {
                                player2.current.reset(playerStart.x + 100, playerStart.y);
                            }

                            if (player1.current) {
                                player1.current.velocityY = 0;
                                player1.current.canJump =
                                    player1.current.y + player1.current.height >=
                                    player1.current.groundY;
                            }

                            if (player2.current) {
                                player2.current.velocityY = 0;
                                player2.current.canJump =
                                    player2.current.y + player2.current.height >=
                                    player2.current.groundY;
                            }

                            resetKeys();
                        }

                        return next;
                    });

                    break;
                }
            }
        }

        const remaining = [];

        for (const coin of coins.current) {
            let collectedBy = -1;

            for (const pObj of playersArr) {
                const p = pObj.player;

                if (
                    p.x < coin.x + coin.width &&
                    p.x + p.width > coin.x &&
                    p.y < coin.y + coin.height &&
                    p.y + p.height > coin.y
                ) {
                    collectedBy = pObj.idx;
                    break;
                }
            }

            if (collectedBy !== -1) {
                setCoinsCollected((prev) => {
                    const copy = [...prev];
                    copy[collectedBy] = (copy[collectedBy] || 0) + 1;
                    return copy;
                });

                setCurrentLevelCoinsCollected((prev) => prev + 1);
            } else {
                remaining.push(coin);
            }
        }

        coins.current = remaining;

        if (
            remaining.length === 0 &&
            level.coins.length > 0 &&
            !levelTransitioning.current
        ) {
            levelTransitioning.current = true;
            resetKeys();
            loadLevel(currentLevelNumber + 1);
        }

    }, [
        gameOver,
        level,
        playerCount,
        keys1,
        keys2,
        physics,
        applyGravity,
        platforms,
        resetKeys,
        loadLevel,
        currentLevelNumber,
    ]);

    useGameLoop(drawFrame, !gameOver && !!level);

    const handleRestart = () => {
        startNewRun();
    };

    const handleExit = () => {
        if (typeof onExit === "function") {
            onExit();
        } else {
            initGame();
        }
    };

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
        <div className={styles.container}>
            <Scoreboard
                players={players}
                coinsCollected={coinsCollected}
                currentLevelCoinsCollected={currentLevelCoinsCollected}
                totalCoins={level.coins.length}
                lives={lives}
                playerCount={playerCount}
                levelNumber={currentLevelNumber}
            />

            {gameOver && (
                <GameOver
                    players={players}
                    onRestart={handleRestart}
                    onExit={handleExit}
                    finalScore={coinsCollected}
                />
            )}

            <canvas
                ref={canvasRef}
                width={GAME_CONSTANTS.canvasWidth}
                height={GAME_CONSTANTS.canvasHeight}
                className={styles.canvas}
                role="application"
                aria-label="Game canvas"
            />
        </div>
    );
};

export default GameCanvas;