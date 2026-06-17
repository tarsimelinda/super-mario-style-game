export function rectanglesOverlap(a, b) {
    return (
        a.x < b.x + b.width &&
        a.x + a.width > b.x &&
        a.y < b.y + b.height &&
        a.y + a.height > b.y
    );
}

export function collectCoins(coins, players) {
    const remainingCoins = [];
    const collected = [];

    for (const coin of coins) {
        let collectedBy = -1;

        for (const playerObj of players) {
            if (rectanglesOverlap(playerObj.player, coin)) {
                collectedBy = playerObj.idx;
                break;
            }
        }

        if (collectedBy !== -1) {
            collected.push({
                coin,
                playerIndex: collectedBy,
            });
        } else {
            remainingCoins.push(coin);
        }
    }

    return {
        remainingCoins,
        collected,
    };
}