import { apiFetch } from "./client";

export function createUser(player) {
    return apiFetch("/users", {
        method: "POST",
        body: JSON.stringify(player),
    });
}
