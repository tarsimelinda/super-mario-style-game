import { apiFetch } from "./client";

export function createUser(player) {
    return apiFetch("/users", {
        method: "POST",
        body: JSON.stringify(player),
    });
}

export function patchUserCheckpoint(userId, checkpoint) {
    return apiFetch(`/users/${encodeURIComponent(userId)}/checkpoint`, {
        method: "PATCH",
        body: JSON.stringify({ checkpoint }),
    });
}