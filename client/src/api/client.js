const API_BASE = "/api";

export async function apiFetch(path, options = {}) {
    const token = import.meta.env.VITE_DEV_TOKEN;

    const headers = {
        "Content-Type": "application/json",
        "X-API-KEY": token,
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

