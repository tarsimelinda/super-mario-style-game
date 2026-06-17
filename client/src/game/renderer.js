import { GAME_CONSTANTS } from "./config";

export function drawBackground(ctx, canvas) {
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
}

export function drawGameObjects({
    ctx,
    canvas,
    platforms,
    enemies,
    coins,
    players,
    getPlayerColor,
}) {
    drawBackground(ctx, canvas);

    platforms.forEach((platform) => platform.draw(ctx));

    enemies.forEach((enemy) => {
        enemy.move();
        enemy.draw(ctx);
    });

    coins.forEach((coin) => coin.draw(ctx));

    players.forEach(({ player, idx }) => {
        player.draw(ctx, getPlayerColor(idx));
    });
}