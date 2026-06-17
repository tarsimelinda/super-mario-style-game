import { apiFetch } from "./client";

export function patchUserCheckpoint(userId, checkpoint) {
    return apiFetch(`/users/${encodeURIComponent(userId)}/checkpoint`, {
        method: "PATCH",
        body: JSON.stringify({ checkpoint }),
    });
}