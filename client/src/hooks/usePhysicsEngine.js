import { useMemo } from "react";
import Physics from "../game/Physics";

export default function usePhysicsEngine(gravity = 0.5, jumpStrength = 10) {
    const physics = useMemo(() => new Physics(gravity, jumpStrength), [gravity, jumpStrength]);

    const applyGravity = (players, groundY, platforms = []) => {
        players.forEach((player) => {
            if (!player) return;
            physics.applyGravity(player, groundY);

            for (const platform of platforms) {
                const playerBottom = player.y + player.height;
                const platformTop = platform.y;

                if (
                    playerBottom <= platformTop + 5 &&
                    playerBottom + player.velocityY >= platformTop &&
                    player.x + player.width > platform.x &&
                    player.x < platform.x + platform.width
                ) {
                    player.velocityY = 0;
                    player.y = platformTop - player.height;
                    player.canJump = true;
                    break;
                }
            }
        });
    };

    return { physics, applyGravity };
}
