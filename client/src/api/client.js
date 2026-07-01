const API_BASE = import.meta.env.VITE_API_BASE_URL;

export async function apiFetch(path, options = {}) {
    const apiKey = import.meta.env.VITE_API_KEY;

    const headers = {
        "Content-Type": "application/json",
        "X-API-KEY": apiKey,
        ...(options.headers || {}),
    };

    const response = await fetch(API_BASE + path, {
        ...options,
        headers,
    });

    const data = await response.json().catch(() => null);

    return {
        ok: response.ok,
        status: response.status,
        data,
    };
}