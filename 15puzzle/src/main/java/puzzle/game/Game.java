package puzzle.game;

import puzzle.domain.GameBoard;
import puzzle.domain.Tile;
import puzzle.domain.TileStatus;

import static puzzle.domain.TileStatus.ELIGIBLE_TO_MOVE;
import static puzzle.domain.TileStatus.NON_EXISTING;
import static puzzle.utils.MessageUtils.*;

public class Game {

    private final GamePresenter presenter;
    private final PlayerMoveSupplier playerMoveSupplier;
    private final GameEngine gameEngine;

    public Game(GamePresenter presenter, PlayerMoveSupplier playerMoveSupplier, GameEngine gameEngine) {
        this.presenter = presenter;
        this.playerMoveSupplier = playerMoveSupplier;
        this.gameEngine = gameEngine;
    }

    public void play(int width, int height) {
        int rounds = 0;

        GameBoard gameBoard = gameEngine.startGame(width, height);
        presenter.showGameBoard(gameBoard);

        do {
            Tile nextMove = playerMoveSupplier.nextMove();

            TileStatus tileStatus = gameEngine.getTileStatus(nextMove, gameBoard);

            if (tileStatus == ELIGIBLE_TO_MOVE) {
                gameBoard = gameEngine.moveTile(nextMove, gameBoard);
                presenter.showGameBoard(gameBoard);
                rounds++;
            } else {
                String errorMessage = tileStatus == NON_EXISTING ?
                        nonExistingTile(nextMove) :
                        invalidMove(nextMove);

                presenter.notifyPlayer(errorMessage);
            }
        } while (!gameEngine.isGameFinished(gameBoard));

        presenter.notifyPlayer(gameCompleted(rounds));
    }
}
