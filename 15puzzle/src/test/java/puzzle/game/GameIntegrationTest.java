package puzzle.game;

import org.junit.Test;
import puzzle.domain.GameBoard;
import puzzle.domain.Position;
import puzzle.domain.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static puzzle.domain.Tile.emptyTile;
import static puzzle.domain.Tile.tileOf;

public class GameIntegrationTest {

    private Game game;

    private List<String> gameEvents = new ArrayList<>();
    private GamePresenter presenter = loggingEventsPresenter();

    @Test
    public void controlsGameProcess() {
        GameBoardFactory initialBoard = predefinedGameBoard(
                tileOf(1), tileOf(2), tileOf(3),
                tileOf(4), tileOf(5), tileOf(7),
                tileOf(8), emptyTile(), tileOf(6)
        );
        GameEngine gameEngine = new GameEngine(initialBoard);
        PlayerMoveSupplier playerMoveSupplier = predefinedMoves(
                tileOf(8),
                tileOf(10), //non existent tile
                tileOf(4),
                tileOf(5),
                tileOf(1), // wrong move
                tileOf(7),
                tileOf(6),
                tileOf(8),
                tileOf(7),
                tileOf(5),
                tileOf(4),
                tileOf(7),
                tileOf(8));

        game = new Game(presenter, playerMoveSupplier, gameEngine);

        game.play(3, 3);
        assertThat(gameEvents).containsExactly(
                "Empty tile at (1,2)", //initial board
                "Empty tile at (0,2)", //first move
                "Tile '10' does not exist",
                "Empty tile at (0,1)",
                "Empty tile at (1,1)",
                "Tile '1' can not be moved",
                "Empty tile at (2,1)",
                "Empty tile at (2,2)",
                "Empty tile at (1,2)",
                "Empty tile at (1,1)",
                "Empty tile at (0,1)",
                "Empty tile at (0,2)",
                "Empty tile at (1,2)",
                "Empty tile at (2,2)",
                "Congratulations! You've solved puzzle in 11 rounds"
        );
    }

    private PlayerMoveSupplier predefinedMoves(Tile... moves) {
        return new PlayerMoveSupplier() {
            private int round = 0;

            @Override
            public Tile nextMove() {
                if(round > moves.length) {
                    throw new IllegalStateException("All moves are exceeded");
                }

                return moves[round++];
            }
        };
    }

    private GameBoardFactory predefinedGameBoard(Tile... tiles) {
        return (width, height) -> new GameBoard(width, height, Arrays.asList(tiles));
    }

    private GamePresenter loggingEventsPresenter() {
        Tile emptyTile = Tile.emptyTile();
        return new GamePresenter() {
            @Override
            public void showGameBoard(GameBoard gameBoard) {
                Position emptyTilePosition = gameBoard.getTilePosition(emptyTile).get();
                String movedTileEvent = String.format("Empty tile at (%d,%d)", emptyTilePosition.getX(), emptyTilePosition.getY());
                gameEvents.add(movedTileEvent);
            }

            @Override
            public void notifyPlayer(String message) {
                gameEvents.add(message);
            }
        };
    }

}