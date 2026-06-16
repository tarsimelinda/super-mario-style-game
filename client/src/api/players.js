import { apiFetch } from "./client";

export function createPlayer(player) {
    return apiFetch("/players", {
        method: "POST",
        body: JSON.stringify(player),
    });
}

export function getPlayer(name) {
    return apiFetch(`/players/${encodeURIComponent(name)}`);
}

export function patchPlayer(name, patch) {
    return apiFetch(`/players/${encodeURIComponent(name)}`, {
        method: "PATCH",
        body: JSON.stringify(patch),
    });
}