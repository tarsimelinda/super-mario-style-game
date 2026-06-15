import styles from "./Footer.module.css";

export default function Footer() {
    return (
        <footer className={styles.footer}>
            <p>
                Game icon by{" "}
                <a
                    href="https://icons8.com/icon/0FvHf0Nf8v0t/ps-controller"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Icons8
                </a>
            </p>
        </footer>
    );
}