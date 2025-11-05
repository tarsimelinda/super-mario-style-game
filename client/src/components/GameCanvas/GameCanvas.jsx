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
import { GAME_CONSTANTS, GAME_CONFIG } from "../../game/config";
import styles from "./GameCanvas.module.css";

const GameCanvas = ({ players, onExit }) => {
    const [gameOver, setGameOver] = useState(false);
    const [coinsCollected, setCoinsCollected] = useState([]);
    const [lives, setLives] = useState(3);
    const canvasRef = useRef(null);

    const playerCount = Array.isArray(players) ? players.length : players;
    const player1 = useRef(null);
    const player2 = useRef(null);

    const { keys1, keys2, resetKeys } = useKeyboardControls(playerCount);
    const { physics, applyGravity } = usePhysicsEngine(
        GAME_CONSTANTS.gravity,
        GAME_CONSTANTS.jumpStrength
    );

    const platforms = useMemo(
        () => GAME_CONFIG.platforms.map(([x, y, w, h]) => new Platform(x, y, w, h)),
        []
    );

    const enemies = useRef([]);
    const coins = useRef([]);

    const initGame = useCallback(() => {
        player1.current = new Player(50, 200, 50, 50, 5);
        player2.current = playerCount > 1 ? new Player(150, 200, 50, 50, 5) : null;

        enemies.current = GAME_CONFIG.enemies.map(([x, y, w, h]) => new Enemy(x, y, w, h));

        const margin = 20;
        coins.current = Array.from({ length: GAME_CONFIG.coinCount }, () => {
            const x = Math.random() * (GAME_CONSTANTS.canvasWidth - margin * 2) + margin;
            const y = Math.random() * (GAME_CONSTANTS.groundY - margin * 2) + margin;
            return new Coins(x, y, 20, 20);
        });

        setCoinsCollected(Array.from({ length: playerCount }, () => 0));
        setLives(3);
        resetKeys();
        setGameOver(false);
    }, [playerCount, resetKeys]);

    useEffect(() => {
        initGame();
    }, [initGame]);

    const drawFrame = useCallback(() => {
        if (gameOver) return;

        const canvas = canvasRef.current;
        if (!canvas) return;
        const ctx = canvas.getContext && canvas.getContext("2d");
        if (!ctx) return;

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = "#87ceeb";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = "green";
        ctx.fillRect(0, GAME_CONSTANTS.groundY, canvas.width, canvas.height - GAME_CONSTANTS.groundY);

        if (keys1.current.up && player1.current) physics.jump(player1.current);
        if (playerCount > 1 && player2.current && keys2.current.up) physics.jump(player2.current);

        const playersArr = [];
        if (player1.current) playersArr.push({ player: player1.current, idx: 0 });
        if (player2.current) playersArr.push({ player: player2.current, idx: 1 });

        applyGravity(playersArr.map((p) => p.player), GAME_CONSTANTS.groundY, platforms);

        if (player1.current) player1.current.move(keys1.current, canvas.width);
        if (playerCount > 1 && player2.current) player2.current.move(keys2.current, canvas.width);

        platforms.forEach((platform) => platform.draw(ctx));
        enemies.current.forEach((enemy) => {
            enemy.move();
            enemy.draw(ctx);
        });
        coins.current.forEach((coin) => coin.draw(ctx));
        playersArr.forEach((pObj) => pObj.player.draw(ctx, pObj.idx === 0 ? "red" : "cyan"));

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
                            if (player1.current) player1.current.reset(50, 200);
                            if (player2.current) player2.current.reset(150, 200);
                            if (player1.current) {
                                player1.current.velocityY = 0;
                                player1.current.canJump = player1.current.y + player1.current.height >= player1.current.groundY;
                            }
                            if (player2.current) {
                                player2.current.velocityY = 0;
                                player2.current.canJump = player2.current.y + player2.current.height >= player2.current.groundY;
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
            } else {
                remaining.push(coin);
            }
        }
        coins.current = remaining;
    }, [
        gameOver,
        playerCount,
        keys1,
        keys2,
        physics,
        applyGravity,
        platforms,
        resetKeys,
    ]);

    useGameLoop(drawFrame, !gameOver);

    const handleRestart = () => {
        initGame();
    };

    const handleExit = () => {
        if (typeof onExit === "function") {
            onExit();
        } else {
            initGame();
        }
    };

    return (
        <div className={styles.container}>
            <Scoreboard
                coinsCollected={coinsCollected}
                totalCoins={GAME_CONFIG.coinCount}
                lives={lives}
                playerCount={playerCount}
            />
            {gameOver && (
                <GameOver onRestart={handleRestart} onExit={handleExit} finalScore={coinsCollected} />
            )}
            <canvas
                ref={canvasRef}
                width={GAME_CONSTANTS.canvasWidth}
                height={GAME_CONSTANTS.canvasHeight}
                className={`${styles.canvas} ${gameOver ? styles.hidden : ""}`}
                role="application"
                aria-label="Game canvas"
            />
        </div>
    );
};

export default GameCanvas;
