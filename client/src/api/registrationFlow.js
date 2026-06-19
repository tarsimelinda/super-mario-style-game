import { registerPlayer } from "./registrations";

export async function registerPlayers(playersData) {
    const registeredPlayers = [];

    for (const player of playersData) {
        const registrationResponse = await registerPlayer({
            name: player.name,
            character: player.character,
        });

        if (!registrationResponse.ok) {
            console.error("Registration failed:", registrationResponse);
            throw new Error("One or more player registrations failed.");
        }

        registeredPlayers.push({
            name: registrationResponse.data.name,
            character: registrationResponse.data.character,
            characterColor: registrationResponse.data.characterColor,
            userId: registrationResponse.data.userId,
            playerId: registrationResponse.data.playerId,
            hp: registrationResponse.data.hp,
            coins: registrationResponse.data.coins,
            status: registrationResponse.data.status,
        });
    }

    return registeredPlayers;
}