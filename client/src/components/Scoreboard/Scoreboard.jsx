import styles from "./Scoreboard.module.css";

export default function Scoreboard({
    coinsCollected = [],
    totalCoins = 0,
    lives = 3,
    playerCount = 1,
}) {
    const totalCollected = coinsCollected.reduce((acc, n) => acc + (n ?? 0), 0);

    return (
        <div className={styles.container} aria-live="polite" aria-label="Scoreboard">
            <div className={styles.title}>Score</div>
            <div className={styles.lives}>Lives: {lives}</div>

            <div>
                {Array.from({ length: playerCount }).map((_, i) => (
                    <div key={i} className={styles.playerRow}>
                        <span className={`${styles.dot} ${i === 0 ? styles.red : styles.cyan}`} aria-hidden />
                        Player {i + 1}: {coinsCollected[i] ?? 0} coins
                    </div>
                ))}
            </div>

            <div className={styles.total}>
                Collected: {totalCollected} / {totalCoins}
            </div>
        </div>
    );
}
