package puzzle.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static puzzle.domain.Tile.tileOf;

public class GameBoardTest {

    @Test
    public void failsOnAttemptToInitializeBoardWithZeroWidth() {
        assertThatThrownBy(() -> new GameBoard(0, 1, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Width and height can't be less than 1");
    }

    @Test
    public void failsOnAttemptToInitializeBoardWithNegativeWidth() {
        assertThatThrownBy(() -> new GameBoard(-1, 1, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Width and height can't be less than 1");
    }

    @Test
    public void failsOnAttemptToInitializeBoardWithZeroHeight() {
        assertThatThrownBy(() -> new GameBoard(1, 0, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Width and height can't be less than 1");
    }

    @Test
    public void failsOnAttemptToInitializeBoardWithNegativHeight() {
        assertThatThrownBy(() -> new GameBoard(1, -1, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Width and height can't be less than 1");
    }

    @Test
    public void failsOnAttemptToInitializeBoardWithWrongNumberOfTiles() {
        List<Tile> tiles = tiles(
                mock(Tile.class),
                mock(Tile.class),
                mock(Tile.class));

        assertThatThrownBy(() -> new GameBoard(2, 2, tiles))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect number of tiles");
    }

    @Test
    public void retrievesPositionOfExistingTile() {
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.getTilePosition(tileOf(1))).hasValue(new Position(0, 0));
        assertThat(board.getTilePosition(tileOf(2))).hasValue(new Position(1, 0));
        assertThat(board.getTilePosition(tileOf(3))).hasValue(new Position(0, 1));
        assertThat(board.getTilePosition(tileOf(4))).hasValue(new Position(1, 1));
    }

    @Test
    public void retrievesEmptyPositionForNonExistingTile() {
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.getTilePosition(tileOf(5))).isEmpty();
    }

    @Test
    public void retrievesTileByItsPosition() {
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.getTileAt(new Position(0, 0))).hasValue(tileOf(1));
        assertThat(board.getTileAt(new Position(1, 0))).hasValue(tileOf(2));
        assertThat(board.getTileAt(new Position(0, 1))).hasValue(tileOf(3));
        assertThat(board.getTileAt(new Position(1, 1))).hasValue(tileOf(4));
    }

    @Test
    public void retrievesEmptyTileForPositionWithNegativeX() {
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.getTileAt(new Position(-1, 0))).isEmpty();
    }

    @Test
    public void retrievesEmptyTileForPositionWithNegativeY() {
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.getTileAt(new Position(0, -1))).isEmpty();
    }

    @Test
    public void retrievesEmptyTileForPositionWithOutOfRangeX() {
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.getTileAt(new Position(2, 0))).isEmpty();
    }

    @Test
    public void retrievesEmptyTileForPositionWithOutOfRangeY() {
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.getTileAt(new Position(0, 2))).isEmpty();
    }

    @Test
    public void swapsTiles() {
        Tile tile = tileOf(1);
        Tile otherTile = tileOf(3);
        GameBoard board = new GameBoard(2, 2, tiles(tile, tileOf(2), otherTile, tileOf(4)));

        GameBoard expectedBoard = new GameBoard(2, 2, tiles(otherTile, tileOf(2), tile, tileOf(4)));
        assertThat(board.swapTiles(tile, otherTile)).isEqualTo(expectedBoard);
    }

    @Test
    public void failsToSwapNonExistingTile() {
        Tile tile = tileOf(1);
        Tile nonExistingTile = tileOf(5);
        GameBoard board = new GameBoard(2, 2, tiles(tile, tileOf(2), tileOf(3), tileOf(4)));

        assertThatThrownBy(() -> board.swapTiles(tile, nonExistingTile))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> board.swapTiles(nonExistingTile, tile))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void indicatesThatSpecificTileExists() {
        Tile tile = tileOf(1);
        GameBoard board = new GameBoard(2, 2, tiles(tile, tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.hasTile(tile)).isTrue();
    }

    @Test
    public void indicatesMissingTile() {
        Tile missingTile = tileOf(5);
        GameBoard board = new GameBoard(2, 2, tiles(tileOf(1), tileOf(2), tileOf(3), tileOf(4)));

        assertThat(board.hasTile(missingTile)).isFalse();
    }

    private List<Tile> tiles(Tile... tiles) {
        return Arrays.asList(tiles);
    }


}