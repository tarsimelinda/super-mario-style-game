const API_BASE_URL = "http://localhost:7070";

export async function fetchDefaultLevel() {
    const response = await fetch(`${API_BASE_URL}/api/levels/default`);

    if (!response.ok) {
        throw new Error("Failed to load default level");
    }

    return response.json();
}

export async function fetchRandomLevel() {
    const response = await fetch(`${API_BASE_URL}/api/levels/random`);

    if (!response.ok) {
        throw new Error("Failed to load random level");
    }

    return response.json();
}