import { useCallback, useRef, useState } from "react";
import { fetchRandomLevel } from "../api/levelApi";

export default function useLevelManager() {
    const [level, setLevel] = useState(null);
    const [loadingLevel, setLoadingLevel] = useState(true);
    const [levelError, setLevelError] = useState(null);
    const [currentLevelNumber, setCurrentLevelNumber] = useState(1);
    const [levelComplete, setLevelComplete] = useState(false);

    const levelTransitioning = useRef(false);
    const levelTransitionTimeout = useRef(null);

    const clearLevelTransitionTimeout = useCallback(() => {
        if (levelTransitionTimeout.current) {
            clearTimeout(levelTransitionTimeout.current);
            levelTransitionTimeout.current = null;
        }
    }, []);

    const loadLevel = useCallback(async (levelNumber) => {
        try {
            setLevelComplete(false);
            setLoadingLevel(true);
            setLevelError(null);
            levelTransitioning.current = true;

            const loadedLevel = await fetchRandomLevel();

            setLevel(loadedLevel);
            setCurrentLevelNumber(levelNumber);
        } catch (error) {
            console.error("Could not load level:", error);
            setLevelError("Could not load level.");
        } finally {
            setLoadingLevel(false);
            levelTransitioning.current = false;
        }
    }, []);

    return {
        level,
        loadingLevel,
        levelError,
        currentLevelNumber,
        levelComplete,
        setLevelComplete,
        setCurrentLevelNumber,
        loadLevel,
        levelTransitioning,
        levelTransitionTimeout,
        clearLevelTransitionTimeout,
    };
}