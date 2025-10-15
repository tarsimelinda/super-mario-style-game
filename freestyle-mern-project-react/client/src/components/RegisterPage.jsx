import { useState, useEffect } from "react";
import GameCanvas from "./GameCanvas";

function RegisterPage({ players }) {
    const [startButton, setStartButton] = useState(false);
    const [playersData, setPlayersData] = useState([]);

    useEffect(() => {
        if (players > 0) {
            setPlayersData(Array.from({ length: players }, () => ({ name: "", character: "" })));
        }
    }, [players]);

    const handleChange = (index, field, value) => {
        setPlayersData((prevData) =>
            prevData.map((player, i) =>
                i === index ? { ...player, [field]: value } : player
            )
        );
    };

    const handleClick = async (e) => {
        e.preventDefault();
        console.log("Submitting players:", playersData);

        try {
            for (const player of playersData) {
                const response = await fetch("/api/user/post", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(player),
                });

                if (!response.ok) throw new Error("Failed to submit player");

                const data = await response.json();
                console.log("Response from server:", data);
            }

            console.log("All players submitted:", playersData);
            setStartButton(true);
        } catch (error) {
            console.error("Error during fetch:", error);
            alert("An error occurred. Please try again.");
        }
    };


    if (startButton) {
        return <GameCanvas players={playersData} />;
    }


    return (
        <div className="register">
            <h2>Register</h2>
            {playersData.map((player, index) => (
                <div key={index} className="player">
                    <p>Player {index + 1}</p>
                    <input
                        type="text"
                        placeholder="Name"
                        className="nameInput"
                        value={player.name}
                        onChange={(e) => handleChange(index, "name", e.target.value)}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Character"
                        className="characterInput"
                        value={player.character}
                        onChange={(e) => handleChange(index, "character", e.target.value)}
                        required
                    />
                </div>
            ))}
            <button className="registerButton" onClick={handleClick}>OK</button>
        </div>
    );
}

export default RegisterPage;