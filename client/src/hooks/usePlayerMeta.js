import { useCallback, useMemo } from "react";

export default function usePlayerMeta(players) {
    const playerCount = useMemo(() => {
        return Array.isArray(players) ? players.length : Number(players) || 1;
    }, [players]);

    const getPlayerIdByIndex = useCallback(
        (index) => {
            if (Array.isArray(players) && players[index]?.playerId) {
                return players[index].playerId;
            }

            return null;
        },
        [players]
    );

    const getUserIdByIndex = useCallback(
        (index) => {
            if (Array.isArray(players) && players[index]?.userId) {
                return players[index].userId;
            }

            return null;
        },
        [players]
    );

    const getPlayerColor = useCallback(
        (index) => {
            if (Array.isArray(players) && players[index]?.characterColor) {
                return players[index].characterColor;
            }

            return index === 0 ? "red" : "cyan";
        },
        [players]
    );

    return {
        playerCount,
        getPlayerIdByIndex,
        getUserIdByIndex,
        getPlayerColor,
    };
}