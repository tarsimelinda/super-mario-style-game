import { useEffect, useRef } from "react";

export default function useGameLoop(callback, active = true) {
    const rafRef = useRef(null);
    const callbackRef = useRef(callback);

    useEffect(() => {
        callbackRef.current = callback;
    }, [callback]);

    useEffect(() => {
        if (!active) {
            if (rafRef.current) {
                cancelAnimationFrame(rafRef.current);
                rafRef.current = null;
            }
            return;
        }

        let mounted = true;

        const loop = () => {
            if (!mounted) return;

            try {
                callbackRef.current && callbackRef.current();
            } catch (err) {

                console.error("Error in game loop callback:", err);
            }

            rafRef.current = requestAnimationFrame(loop);
        };

        rafRef.current = requestAnimationFrame(loop);

        return () => {
            mounted = false;
            if (rafRef.current) {
                cancelAnimationFrame(rafRef.current);
                rafRef.current = null;
            }
        };
    }, [active]);
}
