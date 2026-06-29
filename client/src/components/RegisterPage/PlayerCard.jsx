import styles from "./RegisterPage.module.css";

export default function PlayerCard({ index, data, onChange, characters = [] }) {
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

            <select
                id={characterId}
                className={styles.input}
                value={data.character}
                onChange={(e) => onChange(index, "character", e.target.value)}
                required
            >
                <option value="" disabled hidden>
                    Choose a character
                </option>

                {characters.map((character) => (
                    <option key={character.key} value={character.key}>
                        {character.displayName}
                    </option>
                ))}
            </select>
        </div>
    );
}