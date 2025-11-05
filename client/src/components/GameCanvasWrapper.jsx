import { useLocation, useNavigate } from "react-router-dom";
import GameCanvas from "./GameCanvas/GameCanvas";

export default function GameCanvasWrapper() {
    const location = useLocation();
    const navigate = useNavigate();

    const statePlayers = location.state && location.state.playersData;
    let playersData = statePlayers;

    if (!playersData) {
        try {
            const raw = sessionStorage.getItem("playersData");
            if (raw) playersData = JSON.parse(raw);
        } catch (err) {
            console.error("Failed to parse playersData from sessionStorage", err);
        }
    }

    if (!playersData) {
        navigate("/", { replace: true });
        return null;
    }

    const handleExit = () => {
        sessionStorage.removeItem("playersData");
        navigate("/", { replace: true });
    };

    return <GameCanvas players={playersData} onExit={handleExit} />;
}
