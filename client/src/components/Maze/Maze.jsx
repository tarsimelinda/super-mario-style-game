import { useRef, useCallback, useMemo } from "react";
import Platform from "../../game/Platform";
import Player from "../../game/Player";
import { GAME_CONSTANTS } from "../../game/config";
import useKeyboardControls from "../../hooks/useKeyboardControls";
import useGameLoop from "../../hooks/useGameLoop";
import styles from "./Maze.module.css";

const Maze = () => {
    const canvasRef = useRef(null);

    const playerRef = useRef(null);
    if (!playerRef.current) {
        playerRef.current = new Player(50, 200, 50, 50, 5);
    }

    const { keys1 } = useKeyboardControls(1);

    const platforms = useMemo(
        () => [
            new Platform(5, 5, GAME_CONSTANTS.canvasWidth - 10, 20),
            new Platform(5, 5, 20, GAME_CONSTANTS.canvasHeight - 10),
            new Platform(5, GAME_CONSTANTS.canvasHeight - 25, GAME_CONSTANTS.canvasWidth - 10, 20),
            new Platform(GAME_CONSTANTS.canvasWidth - 25, 5, 20, GAME_CONSTANTS.canvasHeight - 10),
        ],
        []
    );

    const draw = useCallback(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;
        const ctx = canvas.getContext("2d");
        if (!ctx) return;

        const player = playerRef.current;

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        const ks = keys1.current;
        if (ks.right) player.x += player.speed;
        if (ks.left) player.x -= player.speed;
        if (ks.up) player.y -= player.speed;
        if (ks.down) player.y += player.speed;

        player.x = Math.max(0, Math.min(player.x, canvas.width - player.width));
        player.y = Math.max(0, Math.min(player.y, canvas.height - player.height));

        platforms.forEach((p) => p.draw(ctx));
        player.draw(ctx, "red");
    }, [keys1, platforms]);

    useGameLoop(draw, true);

    return (
        <canvas
            ref={canvasRef}
            width={GAME_CONSTANTS.canvasWidth}
            height={GAME_CONSTANTS.canvasHeight}
            className={styles.canvas}
            role="application"
            aria-label="Maze canvas"
        />
    );
};

export default Maze;
