import { useNavigate } from "react-router-dom";
import styles from "./HomePage.module.css";

export default function HomePage() {
    const navigate = useNavigate();

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Welcome!</h1>
            <h2 className={styles.subtitle}>Select number of players</h2>

            <div className={styles.rules}>
                <h3>How to play</h3>

                <p>
                    Collect all coins to move to the next level. Avoid enemies
                    and try to collect as many coins as possible.
                </p>

                <ul>
                    <li>Most enemies deal 1 damage.</li>
                    <li>Black enemies deal 2 damage.</li>
                    <li>In 2 Player mode, players share 3 team lives.</li>
                    <li>Player 1 moves with the arrow keys.</li>
                    <li>Player 2 moves with W, A, S, D.</li>
                </ul>
            </div>

            <div className={styles.buttons}>
                <button className="btn" onClick={() => navigate("/register/1")}>
                    1 Player
                </button>
                <button className="btn" onClick={() => navigate("/register/2")}>
                    2 Players
                </button>
            </div>
        </div>
    );
}