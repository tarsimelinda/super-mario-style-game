import styles from "./RegisterPage.module.css";

export default function PlayerCard({ index, data, onChange }) {
    const nameId = `player-${index}-name`;
    const characterId = `player-${index}-character`;

    return (
        <div className={styles.card}>
            <h3 className={styles.h3}>Player {index + 1}</h3>

            <label className={styles.label} htmlFor={nameId}>
                Name
            </label>
            <input
                id={nameId}
                type="text"
                className={styles.input}
                placeholder={`Player ${index + 1} name`}
                value={data.name}
                onChange={(e) => onChange(index, "name", e.target.value)}
                maxLength={50}
                autoComplete="off"
                required
            />

            <label className={styles.label} htmlFor={characterId}>
                Character
            </label>
            <input
                id={characterId}
                type="text"
                className={styles.input}
                placeholder="Character"
                value={data.character}
                onChange={(e) => onChange(index, "character", e.target.value)}
                maxLength={50}
                autoComplete="off"
                required
            />
        </div>
    );
}