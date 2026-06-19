import styles from "./GameOver.module.css";
import { getPlayerName } from "../../utils/players";

function GameOver({ players = [], onRestart, onExit, finalScore }) {

    return (
        <div className={styles.overlay}>
            <h1 className={styles.title}>GAME OVER</h1>

            {finalScore !== undefined && finalScore !== null && (
                <div className={styles.score}>
                    <strong>Final scores:</strong>
                    <pre style={{ textAlign: "left", marginTop: 8 }}>
                        {Array.isArray(finalScore)
                            ? finalScore.map((s, i) => `${getPlayerName(players, i)}: ${s} coins`).join("\n")
                            : `${finalScore}`}
                    </pre>
                </div>
            )}

            <div className={styles.buttons}>
                <button type="button" className="btn btn-primary" onClick={onRestart}>
                    Play again
                </button>
                <button type="button" className="btn" onClick={onExit}>
                    Main menu
                </button>
            </div>
        </div>
    );
}

export default GameOver;