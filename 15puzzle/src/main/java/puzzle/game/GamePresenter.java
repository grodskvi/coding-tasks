package puzzle.game;

import puzzle.domain.GameBoard;

public interface GamePresenter {

    void showGameBoard(GameBoard gameBoard);
    void notifyPlayer(String message);
}
