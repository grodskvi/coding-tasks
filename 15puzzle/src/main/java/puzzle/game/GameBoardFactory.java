package puzzle.game;

import puzzle.domain.GameBoard;

public interface GameBoardFactory {
    GameBoard createGameBoard(int width, int height);
}
