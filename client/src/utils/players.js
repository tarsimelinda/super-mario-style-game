export function getPlayerName(players, index) {
    if (Array.isArray(players) && players[index]?.name) {
        return players[index].name;
    }

    return `Player ${index + 1}`;
}

export function getPlayerColor(players, index) {
    if (Array.isArray(players) && players[index]?.characterColor) {
        return players[index].characterColor;
    }

    return index === 0 ? "red" : "cyan";
}