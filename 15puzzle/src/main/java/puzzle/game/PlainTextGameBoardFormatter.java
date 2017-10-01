package puzzle.game;

import puzzle.domain.GameBoard;
import puzzle.domain.Tile;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.leftPad;

public class PlainTextGameBoardFormatter implements GameBoardFormatter<String> {

    private final String CELL_LEFT_PAD = " ";
    private final String CELL_RIGHT_PAD = " ";
    private final String CELL_DELIMITER = "|";

    @Override
    public String format(GameBoard gameBoard) {
        List<String> textTileValues = getTilesTextValues(gameBoard.getTiles());
        int maxTextLength = getMaxTextLength(textTileValues);

        StringBuilder formattedBoard = new StringBuilder();

        int currentTileIndex = 1;
        for(String textValue: textTileValues) {
            formattedBoard.append(CELL_DELIMITER);
            formattedBoard.append(CELL_LEFT_PAD);
            formattedBoard.append(leftPad(textValue, maxTextLength));
            formattedBoard.append(CELL_RIGHT_PAD);

            if(currentTileIndex % gameBoard.getWidth() == 0) {
                formattedBoard.append(CELL_DELIMITER + "\n");
            }
            currentTileIndex++;
        }
        return formattedBoard.toString();
    }

    private Integer getMaxTextLength(List<String> textTileValues) {
        return textTileValues.stream()
                .max((left, right) -> left.length() - right.length())
                .map(String::length)
                .orElse(0);
    }

    private List<String> getTilesTextValues(List<Tile> tiles) {
        return tiles.stream()
                .map(Tile::getTileTextValue)
                .collect(Collectors.toList());
    }
}
