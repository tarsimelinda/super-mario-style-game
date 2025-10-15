import { useState } from "react";
import RegisterPage from "./RegisterPage";

function HomePage() {

    const [players, setPlayers] = useState(null);

    if (players) {
        return <RegisterPage players={players} />;
    }

    return (
        <div>
            <h1>Welcome!</h1>
            <h2>Players?</h2>
            <button className="1player" onClick={() => setPlayers(1)}>1 player</button>
            <button className="2player" onClick={() => setPlayers(2)}>2 player</button>
        </div>
    )
}

export default HomePage; 