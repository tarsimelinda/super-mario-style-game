import { apiFetch } from "./client";

export async function fetchDefaultLevel() {
    const response = await apiFetch("/levels/default");

    if (!response.ok) {
        throw new Error("Failed to load default level");
    }

    return response.data;
}

export async function fetchRandomLevel() {
    const response = await apiFetch("/levels/random");

    if (!response.ok) {
        throw new Error("Failed to load random level");
    }

    return response.data;
}