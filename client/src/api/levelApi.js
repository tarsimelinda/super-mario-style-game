import { apiFetch } from "./client";

export async function fetchRandomLevel() {
    const response = await apiFetch("/levels/random");

    if (!response.ok) {
        throw new Error("Failed to load random level");
    }

    return response.data;
}