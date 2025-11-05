import { useNavigate } from "react-router-dom";
import styles from "./HomePage.module.css";

export default function HomePage() {
    const navigate = useNavigate();
    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Welcome!</h1>
            <h2 className={styles.subtitle}>Select number of players</h2>

            <div className={styles.buttons}>
                <button className="btn btn-primary" onClick={() => navigate("/register/1")}>
                    1 Player
                </button>
                <button className="btn" onClick={() => navigate("/register/2")}>
                    2 Players
                </button>
            </div>
        </div>
    );
}
