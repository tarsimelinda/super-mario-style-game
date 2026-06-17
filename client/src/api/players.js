import { apiFetch } from "./client";

export function createPlayer(player) {
    return apiFetch("/players", {
        method: "POST",
        body: JSON.stringify(player),
    });
}

export function patchPlayer(playerId, patch) {
    return apiFetch(`/players/${encodeURIComponent(playerId)}`, {
        method: "PATCH",
        body: JSON.stringify(patch),
    });
}