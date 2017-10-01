package puzzle.utils;

import puzzle.domain.Tile;

public class MessageUtils {

    public static String invalidTileValue(String value) {
        return String.format("'%s' is not valid tile value", value);
    }

    public static String nonExistingTile(Tile tile) {
        return String.format("Tile '%s' does not exist", tile.getTileTextValue());
    }

    public static String invalidMove(Tile tile) {
        return String.format("Tile '%s' can not be moved", tile.getTileTextValue());
    }

    public static String gameCompleted(int rounds) {
        return String.format("Congratulations! You've solved puzzle in %d rounds", rounds);
    }
}
