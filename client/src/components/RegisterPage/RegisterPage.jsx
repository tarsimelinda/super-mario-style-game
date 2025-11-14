import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import styles from "./RegisterPage.module.css";
import PlayerCard from "./PlayerCard";
import { validatePlayers } from "../../utils/validation";
import { createUser } from "../../api/users";

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
            const responses = await Promise.all(
                playersData.map((player) =>
                    createUser({
                        name: player.name,
                        character: player.character,
                    })
                )
            );

            const failed = responses.find((res) => !res.ok);
            if (failed) {
                console.error(failed.data || failed.status);
                throw new Error("One or more player registrations failed.");
            }


            sessionStorage.setItem("playersData", JSON.stringify(playersData));
            navigate("/game", { state: { playersData } });
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
                    <button type="submit" disabled={loading}>
                        {loading ? "Starting..." : "Start Game"}
                    </button>
                    <button type="button" onClick={() => navigate("/")}>
                        Back
                    </button>
                </div>
            </form>
        </div>
    );
}
