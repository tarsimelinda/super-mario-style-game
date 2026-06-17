import { apiFetch } from "./client";

export function patchPlayer(playerId, patch) {
    return apiFetch(`/players/${encodeURIComponent(playerId)}`, {
        method: "PATCH",
        body: JSON.stringify(patch),
    });
}