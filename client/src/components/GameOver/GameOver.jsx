import styles from "./GameOver.module.css";

function GameOver({ players = [], onRestart, onExit, finalScore }) {
    const getPlayerName = (index) => {
        if (Array.isArray(players) && players[index]?.name) {
            return players[index].name;
        }

        return `Player ${index + 1}`;
    };

    return (
        <div className={styles.overlay}>
            <h1 className={styles.title}>GAME OVER</h1>

            {finalScore !== undefined && finalScore !== null && (
                <div className={styles.score}>
                    <strong>Final scores:</strong>
                    <pre style={{ textAlign: "left", marginTop: 8 }}>
                        {Array.isArray(finalScore)
                            ? finalScore.map((s, i) => `${getPlayerName(i)}: ${s} coins`).join("\n")
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