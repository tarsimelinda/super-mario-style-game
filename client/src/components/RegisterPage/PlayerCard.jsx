import styles from "./RegisterPage.module.css";

export default function PlayerCard({ index, data, onChange }) {
    return (
        <div className={styles.card}>
            <h3 className={styles.h3}>Player {index + 1}</h3>

            <label className={styles.label}>
                Name
                <input
                    type="text"
                    className={styles.input}
                    placeholder={`Player ${index + 1} name`}
                    value={data.name}
                    onChange={(e) => onChange(index, "name", e.target.value)}
                    required
                />
            </label>

            <label className={styles.label}>
                Character
                <input
                    type="text"
                    className={styles.input}
                    placeholder="Character"
                    value={data.character}
                    onChange={(e) => onChange(index, "character", e.target.value)}
                    required
                />
            </label>
        </div>
    );
}
