import styles from "./GameOver.module.css";

function GameOver({ onRestart, onExit, finalScore }) {
    return (
        <div className={styles.overlay}>
            <h1 className={styles.title}>GAME OVER</h1>

            {finalScore !== undefined && finalScore !== null && (
                <div className={styles.score}>
                    <strong>Final scores:</strong>
                    <pre style={{ textAlign: "left", marginTop: 8 }}>
                        {Array.isArray(finalScore)
                            ? finalScore.map((s, i) => `Player ${i + 1}: ${s} coins`).join("\n")
                            : `${finalScore}`}
                    </pre>
                </div>
            )}

            <div className={styles.buttons}>
                <button type="button" className={styles.button} onClick={onRestart}>
                    Play again
                </button>
                <button type="button" className={styles.button} onClick={onExit}>
                    Main menu
                </button>
            </div>
        </div>
    );
}

export default GameOver;
