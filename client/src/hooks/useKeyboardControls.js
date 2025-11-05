import { useEffect, useRef, useCallback } from "react";

export default function useKeyboardControls(playerCount = 1) {
    const keys1 = useRef({ right: false, left: false, up: false, down: false });
    const keys2 = useRef({ right: false, left: false, up: false, down: false });

    useEffect(() => {
        const setKeyState = (e, value) => {
            const key = e.key.toLowerCase();

            if (["arrowright", "arrowleft", "arrowup", "arrowdown", " "].includes(key)) {
                if (key === "arrowright") keys1.current.right = value;
                if (key === "arrowleft") keys1.current.left = value;
                if (key === "arrowup" || key === " ") keys1.current.up = value;
                if (key === "arrowdown") keys1.current.down = value;

                if (value) e.preventDefault();
            }

            if (playerCount > 1) {
                if (key === "d") keys2.current.right = value;
                if (key === "a") keys2.current.left = value;
                if (key === "w") keys2.current.up = value;
                if (key === "s") keys2.current.down = value;
            }
        };

        const onKeyDown = (e) => setKeyState(e, true);
        const onKeyUp = (e) => setKeyState(e, false);

        window.addEventListener("keydown", onKeyDown, { passive: false });
        window.addEventListener("keyup", onKeyUp);
        return () => {
            window.removeEventListener("keydown", onKeyDown, { passive: false });
            window.removeEventListener("keyup", onKeyUp);
        };
    }, [playerCount]);


    const resetKeys = useCallback(() => {
        keys1.current = { right: false, left: false, up: false, down: false };
        keys2.current = { right: false, left: false, up: false, down: false };
    }, []);

    return { keys1, keys2, resetKeys };
}
