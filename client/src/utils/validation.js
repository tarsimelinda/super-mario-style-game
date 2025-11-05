export function validatePlayers(playersData) {
    for (let i = 0; i < playersData.length; i++) {
        const p = playersData[i];
        if (!p.name?.trim() || !p.character?.trim()) {
            return { ok: false, message: `Please fill out both name and character for Player ${i + 1}.` };
        }
    }
    return { ok: true };
}
