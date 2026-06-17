import { apiFetch } from "./client";

export async function fetchCharacters() {
    const response = await apiFetch("/characters");

    if (!response.ok) {
        throw new Error("Failed to load characters");
    }

    return response.data;
}