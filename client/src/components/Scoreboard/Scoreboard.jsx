import styles from "./Scoreboard.module.css";
import { getPlayerName, getPlayerColor } from "../../utils/players";

export default function Scoreboard({
    players = [],
    coinsCollected = [],
    currentLevelCoinsCollected = 0,
    totalCoins = 0,
    lives = 3,
    playerCount = 1,
    levelNumber = 1,
}) {
    const totalCollected = coinsCollected.reduce((acc, n) => acc + (n ?? 0), 0);

    return (
        <div className={styles.container} aria-live="polite" aria-label="Scoreboard">
            <div className={styles.title}>Score</div>
            <div>Level: {levelNumber}</div>
            <div className={styles.lives}>
                {playerCount > 1 ? "Team lives" : "Lives"}: {lives}
            </div>

            <div>
                {Array.from({ length: playerCount }).map((_, i) => (
                    <div key={i} className={styles.playerRow}>
                        <span
                            className={styles.dot}
                            style={{ backgroundColor: getPlayerColor(players, i) }}
                            aria-hidden
                        />
                        {getPlayerName(players, i)}: {coinsCollected[i] ?? 0} coins
                    </div>
                ))}
            </div>

            <div className={styles.total}>
                Coins this level: {currentLevelCoinsCollected} / {totalCoins}
            </div>

            <div className={styles.total}>
                Total collected: {totalCollected}
            </div>
        </div>
    );
}