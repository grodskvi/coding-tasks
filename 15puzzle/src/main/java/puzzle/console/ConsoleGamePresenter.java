package puzzle.console;

import puzzle.domain.GameBoard;
import puzzle.game.GameBoardFormatter;
import puzzle.game.GamePresenter;

public class ConsoleGamePresenter implements GamePresenter {

    private final GameBoardFormatter<String> formatter;

    public ConsoleGamePresenter(GameBoardFormatter<String> formatter) {
        this.formatter = formatter;
    }

    @Override
    public void showGameBoard(GameBoard gameBoard) {
        String formattedGameBoard = formatter.format(gameBoard);
        System.out.println(formattedGameBoard);
    }

    @Override
    public void notifyPlayer(String message) {
        System.out.println(message);
    }
}
