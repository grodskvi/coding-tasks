package puzzle.game;

import puzzle.domain.GameBoard;

public interface GameBoardFormatter<T> {
    T format(GameBoard gameBoard);
}
