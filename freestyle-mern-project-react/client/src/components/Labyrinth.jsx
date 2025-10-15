import React, { useEffect, useRef } from "react";
import Platform from '../game/Platform';
import Player from '../game/Player';

const Maze = () => {
    const canvasRef = useRef(null);

    const player1Ref = useRef(new Player(50, 200, 50, 50, 5));

    const keys1 = useRef({ right: false, left: false, up: false, down: false });



    const platforms = [
        new Platform(5, 5, 1340, 20),
        new Platform(5, 5, 20, 575),
        new Platform(5, 575, 1340, 20),
        new Platform(1325, 5, 20, 575),
    ];

    useEffect(() => {
        const player1 = player1Ref.current;
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");
    
        const update = () => {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            
            if (keys1.current.right) player1.x += player1.speed;
            if (keys1.current.left) player1.x -= player1.speed;
            if (keys1.current.up) player1.y -= player1.speed;
            if (keys1.current.down) player1.y += player1.speed;
    
            player1.draw(ctx, "red");
    
            platforms.forEach(platform => platform.draw(ctx));
    
            requestAnimationFrame(update);
        };
    
        update();
    
        const handleKeyDown = (e) => {
            if (e.key === "ArrowRight") keys1.current.right = true;
            if (e.key === "ArrowLeft") keys1.current.left = true;
            if (e.key === "ArrowUp") keys1.current.up = true;
            if (e.key === "ArrowDown") keys1.current.down = true;
        };
    
        const handleKeyUp = (e) => {
            if (e.key === "ArrowRight") keys1.current.right = false;
            if (e.key === "ArrowLeft") keys1.current.left = false;
            if (e.key === "ArrowUp") keys1.current.up = false;
            if (e.key === "ArrowDown") keys1.current.down = false;
        };
    
        window.addEventListener("keydown", handleKeyDown);
        window.addEventListener("keyup", handleKeyUp);
    
        return () => {
            window.removeEventListener("keydown", handleKeyDown);
            window.removeEventListener("keyup", handleKeyUp);
        };
    }, []);
    

    return <canvas ref={canvasRef} width={1350} height={600} style={{ border: "1px solid black" }}></canvas>;
};

export default Maze;
