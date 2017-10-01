package puzzle.game;

import org.junit.Test;
import puzzle.domain.GameBoard;
import puzzle.domain.Tile;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RandomizedGameBoardFactoryTest {

    private static final int DEFAULT_WIDTH = 4;
    private static final int DEFAULT_HEIGHT = 4;
    private static final int DEFAULT_NON_EMPTY_TILES_NUMBER = DEFAULT_WIDTH * DEFAULT_HEIGHT - 1;

    private GameBoardFactory gameBoardFactory = new RandomizedGameBoardFactory();

    @Test
    public void failsOnAttemptToInitializeBoardWithZeroWidth() {
        assertThatThrownBy(() -> gameBoardFactory.createGameBoard(0, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't initialize game board with width 0");
    }

    @Test
    public void failsOnAttemptToInitializeBoardWithNegativeWidth() {
        assertThatThrownBy(() -> gameBoardFactory.createGameBoard(-1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't initialize game board with width -1");
    }

    @Test
    public void failsOnAttemptToInitializeBoardWithZeroHeight() {
        assertThatThrownBy(() -> gameBoardFactory.createGameBoard(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't initialize game board with height 0");
    }

    @Test
    public void failsOnAttemptToInitializeBoardWithNegativHeight() {
        assertThatThrownBy(() -> gameBoardFactory.createGameBoard(1, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't initialize game board with height -1");
    }

    @Test
    public void createsGameBoardAccordingToSpecifiedWidth() {
        GameBoard gameBoard = gameBoardFactory.createGameBoard(3, 4);
        assertThat(gameBoard.getWidth()).isEqualTo(3);
    }

    @Test
    public void createsGameBoardAccordingToSpecifiedHeight() {
        GameBoard gameBoard = gameBoardFactory.createGameBoard(3, 4);
        assertThat(gameBoard.getHeight()).isEqualTo(4);
    }

    @Test
    public void createsGameBoardWithNumberOfTilesAccordingToSpecifiedSizes() {
        GameBoard gameBoard = gameBoardFactory.createGameBoard(3, 3);
        assertThat(gameBoard.getTiles()).hasSize(9);
    }

    @Test
    public void createsGameBoardWithEmptyTileInTheLastPosition() {
        GameBoard gameBoard = gameBoardFactory.createGameBoard(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        List<Tile> tiles = gameBoard.getTiles();
        Tile lastTile = tiles.get(DEFAULT_NON_EMPTY_TILES_NUMBER);
        assertThat(lastTile.isEmpty()).isTrue();
    }

    @Test
    public void createsGameBoardWithAllTileValuesFromTheRange() {
        GameBoard gameBoard = gameBoardFactory.createGameBoard(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        List<Tile> tiles = gameBoard.getTiles();
        Set<Integer> tileValues = tiles.stream()
                .filter(tile -> !tile.isEmpty())
                .map(tile -> tile.getTileValue().get())
                .collect(Collectors.toSet());

        Set<Integer> expectedTileValues = new HashSet<>();
        for(int i = 1; i <= DEFAULT_NON_EMPTY_TILES_NUMBER; i++ ) {
            expectedTileValues.add(i);
        }
        assertThat(tileValues).isEqualTo(expectedTileValues);
    }

    @Test
    public void createsGameBoardWithShuffledTiles() {
        GameBoard gameBoard = gameBoardFactory.createGameBoard(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        List<Tile> tiles = gameBoard.getTiles();
        List<Integer> tileValues = tiles.stream()
                .filter(tile -> !tile.isEmpty())
                .map(tile -> tile.getTileValue().get())
                .collect(Collectors.toList());

        List<Integer> sortedTileValues = new ArrayList<>(tileValues);
        Collections.sort(sortedTileValues);
        assertThat(tileValues).isNotEqualTo(sortedTileValues);
    }

    @Test
    public void createsDifferentGameBoardsOnDifferentInvocations() {
        final int INVOCATIONS = 5;
        Set<GameBoard> gameBoards = new HashSet<>();
        for(int i = 0; i < INVOCATIONS; i++) {
            GameBoard gameBoard = gameBoardFactory.createGameBoard(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            gameBoards.add(gameBoard);
        }

        assertThat(gameBoards.size()).isGreaterThan(1);
    }
}