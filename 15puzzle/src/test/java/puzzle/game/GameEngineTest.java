package puzzle.game;

import org.junit.Before;
import org.junit.Test;
import puzzle.domain.GameBoard;
import puzzle.domain.Position;
import puzzle.domain.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static puzzle.domain.Tile.emptyTile;
import static puzzle.domain.Tile.tileOf;
import static puzzle.domain.TileStatus.ELIGIBLE_TO_MOVE;
import static puzzle.domain.TileStatus.NON_ELIGIBLE_TO_MOVE;
import static puzzle.domain.TileStatus.NON_EXISTING;

public class GameEngineTest {
    private GameBoardFactory gameBoardFactory;
    private GameEngine gameEngine;

    @Before
    public void setUp() {
        gameBoardFactory = mock(GameBoardFactory.class);

        Function<Position, Position> upMove = p -> new Position(p.getX(), p.getY() - 1);
        Function<Position, Position> downMove = p -> new Position(p.getX(), p.getY() + 1);
        List<Function<Position, Position>> allowedMoves = new ArrayList<>();
        allowedMoves.add(upMove);
        allowedMoves.add(downMove);

        gameEngine = new GameEngine(gameBoardFactory, allowedMoves);
    }

    @Test
    public void initializesGameBoardOnStart() {
        GameBoard board = mock(GameBoard.class);
        when(gameBoardFactory.createGameBoard(4, 4)).thenReturn(board);

        assertThat(gameEngine.startGame(4, 4)).isEqualTo(board);
    }

    @Test
    public void resolvesStatusForNonExistingTile() {
        GameBoard board = mock(GameBoard.class);
        Tile nonExistingTile = mock(Tile.class);

        when(board.hasTile(nonExistingTile)).thenReturn(false);
        assertThat(gameEngine.getTileStatus(nonExistingTile, board)).isEqualTo(NON_EXISTING);
    }

    @Test
    public void resolvesStatusOfNonMovableTile() {
        GameBoard board = mock(GameBoard.class);
        when(board.getTilePosition(any())).thenReturn(Optional.empty());
        when(board.getTileAt(any())).thenReturn(Optional.empty());

        Tile unmovableTile = tileOf(2);

        addTile(board, tileOf(1), 0, 0);
        addTile(board, unmovableTile, 1, 0);
        addTile(board, emptyTile(), 0, 1);
        addTile(board, tileOf(4), 1, 1);

        assertThat(gameEngine.getTileStatus(unmovableTile, board)).isEqualTo(NON_ELIGIBLE_TO_MOVE);
    }

    @Test
    public void resolvesStatusOfMovableTile() {
        GameBoard board = mock(GameBoard.class);
        when(board.getTilePosition(any())).thenReturn(Optional.empty());
        when(board.getTileAt(any())).thenReturn(Optional.empty());

        Tile movableTile = tileOf(1);

        addTile(board, movableTile, 0, 0);
        addTile(board, tileOf(2), 1, 0);
        addTile(board, emptyTile(), 0, 1);
        addTile(board, tileOf(4), 1, 1);

        assertThat(gameEngine.getTileStatus(movableTile, board)).isEqualTo(ELIGIBLE_TO_MOVE);
    }

    @Test
    public void indicatesThenGameCanBeFinishedWhenTilesAreOrdered() {
        GameBoard board = mock(GameBoard.class);
        List<Tile> orderedTiles = tiles(tileOf(1), tileOf(2), tileOf(3), emptyTile());
        when(board.getTiles()).thenReturn(orderedTiles);

        assertThat(gameEngine.isGameFinished(board)).isTrue();
    }

    @Test
    public void indicatesThenGameIsNotCompletedWhenTilesAreOrdered() {
        GameBoard board = mock(GameBoard.class);
        List<Tile> orderedTiles = tiles(tileOf(1), tileOf(2), emptyTile(), tileOf(3));
        when(board.getTiles()).thenReturn(orderedTiles);

        assertThat(gameEngine.isGameFinished(board)).isFalse();
    }

    private List<Tile> tiles(Tile... tiles) {
        return Arrays.asList(tiles);
    }

    private void addTile(GameBoard board, Tile tile, int x, int y) {
        Position tilePosition = new Position(x, y);
        when(board.hasTile(tile)).thenReturn(true);
        when(board.getTilePosition(tile)).thenReturn(Optional.of(tilePosition));
        when(board.getTileAt(tilePosition)).thenReturn(Optional.of(tile));
    }
}