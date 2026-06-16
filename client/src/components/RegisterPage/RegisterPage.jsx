import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import styles from "./RegisterPage.module.css";
import PlayerCard from "./PlayerCard";
import { validatePlayers } from "../../utils/validation";
import { createUser } from "../../api/users";
import { createPlayer } from "../../api/players";

export default function RegisterPage() {
    const { players } = useParams();
    const playerCount = Math.max(1, Number(players) || 1);
    const navigate = useNavigate();

    const [playersData, setPlayersData] = useState(
        Array.from({ length: playerCount }, () => ({ name: "", character: "" }))
    );
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        setPlayersData(Array.from({ length: playerCount }, () => ({ name: "", character: "" })));
        setError(null);
    }, [playerCount]);

    const handleChange = (index, field, value) => {
        setPlayersData((prev) =>
            prev.map((p, i) => (i === index ? { ...p, [field]: value } : p))
        );
    };

    const onSubmit = async (e) => {
        if (e) e.preventDefault();
        const { ok, message } = validatePlayers(playersData);
        if (!ok) {
            setError(message);
            return;
        }

        setLoading(true);
        setError(null);

        try {
            const registeredPlayers = [];

            for (const player of playersData) {
                const userResponse = await createUser({
                    name: player.name,
                    character: player.character,
                });

                if (!userResponse.ok) {
                    console.error("User creation failed:", userResponse);
                    throw new Error("One or more user registrations failed.");
                }

                const playerResponse = await createPlayer({
                    name: player.name,
                    hp: 3,
                    coins: 0,
                    shield: false,
                });

                if (!playerResponse.ok) {
                    console.error("Player creation failed:", playerResponse);
                    throw new Error("One or more player registrations failed.");
                }

                registeredPlayers.push({
                    ...player,
                    userId: userResponse.data.id,
                    playerId: playerResponse.data.id,
                    hp: playerResponse.data.hp,
                    coins: playerResponse.data.coins,
                    status: playerResponse.data.status,
                });
            }

            sessionStorage.setItem("playersData", JSON.stringify(registeredPlayers));
            navigate("/game", { state: { playersData: registeredPlayers } });
        } catch (err) {
            console.error(err);
            setError("Failed to register players. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <h2>Register Players</h2>
            <form className={styles.form} onSubmit={onSubmit}>
                {playersData.map((p, i) => (
                    <PlayerCard key={i} index={i} data={p} onChange={handleChange} />
                ))}

                {error && (
                    <div role="alert" className={styles.error}>
                        {error}
                    </div>
                )}

                <div className={styles.actions}>
                    <button type="submit" className="btn btn-primary" disabled={loading}>
                        {loading ? "Starting..." : "Start Game"}
                    </button>
                    <button type="button" className="btn" onClick={() => navigate("/")}>
                        Back
                    </button>
                </div>
            </form>
        </div>
    );
}
