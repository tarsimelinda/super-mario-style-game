import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import styles from "./RegisterPage.module.css";
import PlayerCard from "./PlayerCard";
import { validatePlayers } from "../../utils/validation";
import { fetchCharacters } from "../../api/characters";
import { registerPlayer } from "../../api/registrations";
import { registerPlayers } from "../../api/registrationFlow";

export default function RegisterPage() {
    const { players } = useParams();
    const playerCount = Math.max(1, Number(players) || 1);
    const navigate = useNavigate();

    const [playersData, setPlayersData] = useState(
        Array.from({ length: playerCount }, () => ({ name: "", character: "" }))
    );
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [characters, setCharacters] = useState([]);
    const [charactersLoading, setCharactersLoading] = useState(true);

    useEffect(() => {
        async function loadCharacters() {
            try {
                setCharactersLoading(true);
                const loadedCharacters = await fetchCharacters();
                setCharacters(loadedCharacters);
            } catch (error) {
                console.error("Could not load characters:", error);
                setError("Failed to load characters.");
            } finally {
                setCharactersLoading(false);
            }
        }

        loadCharacters();
    }, []);

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
            const registeredPlayers = await registerPlayers(playersData);

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
            <p className={styles.helpText}>
                Enter a name and choose a character for each player. Names and character
                names can be up to 50 characters long.
            </p>
            <div className={styles.controlsHint}>
                <strong>Controls:</strong> Player 1: Arrow keys
                {playerCount > 1 && " | Player 2: W, A, S, D"}
            </div>
            <form className={styles.form} onSubmit={onSubmit}>
                {playersData.map((p, i) => (
                    <PlayerCard
                        key={i}
                        index={i}
                        data={p}
                        onChange={handleChange}
                        characters={characters}
                    />
                ))}

                {error && (
                    <div role="alert" className={styles.error}>
                        {error}
                    </div>
                )}

                <div className={styles.actions}>
                    <button type="submit" className="btn btn-primary" disabled={loading || charactersLoading}>
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
