import Scoreboard from "../Scoreboard/Scoreboard";
import styles from "./GameCanvas.module.css";

export default function GameSidebar({
    players,
    coinsCollected,
    currentLevelCoinsCollected,
    totalCoins,
    lives,
    playerCount,
    levelNumber,
    paused,
    gameOver,
    onTogglePause,
    onRestart,
    onExit,
}) {
    return (
        <aside className={styles.sidebar}>
            <Scoreboard
                players={players}
                coinsCollected={coinsCollected}
                currentLevelCoinsCollected={currentLevelCoinsCollected}
                totalCoins={totalCoins}
                lives={lives}
                playerCount={playerCount}
                levelNumber={levelNumber}
            />

            <div className={styles.sidebarActions}>
                <button
                    type="button"
                    className="btn"
                    onClick={onTogglePause}
                    disabled={gameOver}
                >
                    {paused ? "Resume" : "Pause"}
                </button>

                <button
                    type="button"
                    className="btn"
                    onClick={onRestart}
                >
                    Restart run
                </button>

                <button
                    type="button"
                    className="btn btn-primary"
                    onClick={onExit}
                >
                    Main menu
                </button>
            </div>
        </aside>
    );
}