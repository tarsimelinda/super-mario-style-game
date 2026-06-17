import GameOver from "../GameOver/GameOver";
import styles from "./GameCanvas.module.css";

export default function GameStatusOverlay({
    paused,
    gameOver,
    levelComplete,
    players,
    coinsCollected,
    onRestart,
    onExit,
}) {
    return (
        <>
            {paused && !gameOver && (
                <div className={styles.pauseOverlay}>
                    PAUSED
                </div>
            )}

            {levelComplete && !gameOver && (
                <div className={styles.levelCompleteOverlay}>
                    <div>Level complete!</div>
                    <small>Loading next level...</small>
                </div>
            )}

            {gameOver && (
                <GameOver
                    players={players}
                    onRestart={onRestart}
                    onExit={onExit}
                    finalScore={coinsCollected}
                />
            )}
        </>
    );
}