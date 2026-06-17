import { apiFetch } from "./client";

export function registerPlayer(player) {
    return apiFetch("/registrations", {
        method: "POST",
        body: JSON.stringify(player),
    });
}