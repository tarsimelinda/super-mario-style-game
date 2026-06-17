import Player from "./Player";
import Enemy from "./Enemy";
import Platform from "./Platform";
import Coins from "./Coins";
import { GAME_CONSTANTS } from "./config";

export function createPlatforms(level) {
    if (!level?.platforms) return [];

    return level.platforms.map(
        (platform) =>
            new Platform(
                platform.x,
                platform.y,
                platform.width,
                platform.height
            )
    );
}

export function createPlayers(level, playerCount) {
    const playerStart = level?.playerStart || { x: 50, y: 200 };

    const player1 = new Player(
        playerStart.x,
        playerStart.y,
        50,
        50,
        5,
        GAME_CONSTANTS.groundY
    );

    const player2 =
        playerCount > 1
            ? new Player(
                  playerStart.x + 100,
                  playerStart.y,
                  50,
                  50,
                  5,
                  GAME_CONSTANTS.groundY
              )
            : null;

    return {
        player1,
        player2,
    };
}

export function createEnemies(level) {
    if (!level?.enemies) return [];

    return level.enemies.map(
        (enemy) =>
            new Enemy(
                enemy.x,
                enemy.y,
                enemy.width,
                enemy.height,
                enemy.speed ?? 2,
                GAME_CONSTANTS.canvasWidth,
                enemy.damage ?? 1,
                enemy.hp ?? 1,
                enemy.color ?? "blue",
                enemy.canJump ?? false
            )
    );
}

export function createCoins(level) {
    if (!level?.coins) return [];

    return level.coins.map(
        (coin) =>
            new Coins(
                coin.x,
                coin.y,
                coin.width,
                coin.height
            )
    );
}

export function getActivePlayers(player1, player2) {
    const players = [];

    if (player1) {
        players.push({ player: player1, idx: 0 });
    }

    if (player2) {
        players.push({ player: player2, idx: 1 });
    }

    return players;
}