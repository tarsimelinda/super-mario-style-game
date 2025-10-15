import React, { useEffect, useRef, useState } from "react";
import Enemy from "../game/Enemies";
import Player from "../game/Player";
import Physics from "../game/Physics";
import Platform from "../game/Platform";
import Coins from "../game/Coins";
import GameOver from "./GameOver";

const GameCanvas = ({ players }) => {
    const [gameOver, setGameOver] = useState(false);
    
    const canvasRef = useRef(null);
    const physics = new Physics(0.5, 10);
    
    const playerCount = Array.isArray(players) ? players.length : players;

    const player1Ref = useRef(new Player(50, 200, 50, 50, 5));
    const player2Ref = useRef(playerCount > 1 ? new Player(150, 200, 50, 50, 5) : null);

    const keys1 = useRef({ right: false, left: false });
    const keys2 = useRef({ right: false, left: false });

    const groundY = 550;

    const coinsRef = useRef(
        Array.from({ length: 10 }, () => new Coins(Math.random() * 1300, Math.random() * 500, 20, 20))
    );

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");

        const platforms = [
            new Platform(100, 450, 200, 20),
            new Platform(400, 380, 150, 20),
            new Platform(650, 320, 200, 20),
            new Platform(950, 250, 150, 20),
            new Platform(1250, 200, 50, 20),
            new Platform(650, 150, 200, 20),
            new Platform(400, 100, 100, 20),
            new Platform(100, 150, 70, 20)
        ];

        const enemies = [
            new Enemy(500, 310, 40, 40),
            new Enemy(300, 160, 40, 40)
        ];

        const draw = () => {
            if (gameOver) return;

            const player1 = player1Ref.current;
            const player2 = player2Ref.current;
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            ctx.fillStyle = "green";
            ctx.fillRect(0, groundY, canvas.width, canvas.height - groundY);

            platforms.forEach(platform => platform.draw(ctx));
            enemies.forEach(enemy => {
                enemy.move();
                enemy.draw(ctx);
            });

            coinsRef.current.forEach(coin => coin.draw(ctx));

            player1.draw(ctx, "red");
            if (playerCount > 1 && player2) player2.draw(ctx, "cyan");

            physics.applyGravity(player1, groundY);
            if (playerCount > 1 && player2) physics.applyGravity(player2, groundY);

            [player1, player2].forEach(player => {
                if (!player) return;

                platforms.forEach(platform => {
                    if (
                        player.y + player.height <= platform.y + 5 &&
                        player.y + player.height + player.velocityY >= platform.y &&
                        player.x + player.width > platform.x &&
                        player.x < platform.x + platform.width
                    ) {
                        player.velocityY = 0;
                        player.y = platform.y - player.height;
                        player.canJump = true;
                    }
                });

                enemies.forEach(enemy => {
                    if (enemy.checkCollision(player)) {
                        setGameOver(true);
                    }
                });

                coinsRef.current = coinsRef.current.filter(coin => {
                    const collected =
                        player.x < coin.x + coin.width &&
                        player.x + player.width > coin.x &&
                        player.y < coin.y + coin.height &&
                        player.y + player.height > coin.y;

                    return !collected;
                });
            });

            player1.move(keys1.current, canvas.width);
            if (playerCount > 1 && player2) player2.move(keys2.current, canvas.width);

            requestAnimationFrame(draw);
        };

        draw();

        const handleKeyDown = (e) => {
            if (e.key === "ArrowRight") keys1.current.right = true;
            if (e.key === "ArrowLeft") keys1.current.left = true;
            if ((e.key === " " || e.key === "ArrowUp") && player1Ref.current.canJump) {
                physics.jump(player1Ref.current);
            }

            if (playerCount > 1) {
                if (e.key.toLowerCase() === "d") keys2.current.right = true;
                if (e.key.toLowerCase() === "a") keys2.current.left = true;
                if (e.key.toLowerCase() === "w" && player2Ref.current.canJump) {
                    physics.jump(player2Ref.current);
                }
            }
        };

        const handleKeyUp = (e) => {
            if (e.key === "ArrowRight") keys1.current.right = false;
            if (e.key === "ArrowLeft") keys1.current.left = false;

            if (playerCount > 1) {
                if (e.key.toLowerCase() === "d") keys2.current.right = false;
                if (e.key.toLowerCase() === "a") keys2.current.left = false;
            }
        };

        window.addEventListener("keydown", handleKeyDown);
        window.addEventListener("keyup", handleKeyUp);
        return () => {
            window.removeEventListener("keydown", handleKeyDown);
            window.removeEventListener("keyup", handleKeyUp);
        };
    }, [players]);

    if (gameOver) {
        return <GameOver setGameOver={setGameOver} />;
    }

    return <canvas ref={canvasRef} width={1350} height={600} style={{ border: "1px solid black" }} />;
};

export default GameCanvas;
