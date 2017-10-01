package puzzle.game;

import org.junit.Test;
import puzzle.domain.GameBoard;
import puzzle.domain.Tile;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static puzzle.domain.Tile.emptyTile;
import static puzzle.domain.Tile.tileOf;

public class PlainTextGameBoardFormatterTest {

    private PlainTextGameBoardFormatter formatter = new PlainTextGameBoardFormatter();

    @Test
    public void formatsSingleTile() {
        GameBoard gameBoard = new GameBoard(1, 1, tiles(tileOf(1)));
        assertThat(formatter.format(gameBoard)).isEqualTo("| 1 |\n");
    }

    @Test
    public void formatsSingleEmptyTile() {
        GameBoard gameBoard = new GameBoard(1, 1, tiles(emptyTile()));
        assertThat(formatter.format(gameBoard)).isEqualTo("|  |\n");
    }

    @Test
    public void formatsSingleLine() {
        GameBoard gameBoard = new GameBoard(2, 1, tiles(tileOf(1), emptyTile()));
        assertThat(formatter.format(gameBoard)).isEqualTo("| 1 |   |\n");
    }

    @Test
    public void formatsMultiLine() {
        GameBoard gameBoard = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("| 1 | 2 |\n");
        expectedOutput.append("| 3 | 4 |\n");

        assertThat(formatter.format(gameBoard)).isEqualTo(expectedOutput.toString());
    }

    @Test
    public void alignsRowWidthAccordingToValues() {
        GameBoard gameBoard = new GameBoard(2, 2, tiles(tileOf(100), tileOf(1), tileOf(2), tileOf(10)));

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("| 100 |   1 |\n");
        expectedOutput.append("|   2 |  10 |\n");

        assertThat(formatter.format(gameBoard)).isEqualTo(expectedOutput.toString());
    }

    private List<Tile> tiles(Tile... tiles) {
        return Arrays.asList(tiles);
    }
}