import { registerPlayer } from "./registrations";

export async function registerPlayers(playersData) {
    const registeredPlayers = [];

    for (const player of playersData) {
        const response = await registerPlayer({
            name: player.name,
            character: player.character,
        });

        if (!response.ok) {
            console.error("Registration failed:", response);
            throw new Error("One or more player registrations failed.");
        }

        registeredPlayers.push({
            name: response.data.name,
            character: response.data.character,
            characterColor: response.data.characterColor,
            userId: response.data.userId,
            playerId: response.data.playerId,
            hp: response.data.hp,
            coins: response.data.coins,
            status: response.data.status,
        });
    }

    return registeredPlayers;
}