package puzzle.launcher;

import puzzle.console.ConsoleGamePresenter;
import puzzle.console.ConsolePlayerMoveSupplier;
import puzzle.game.*;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class GameLauncher {

    public static void main(String[] args) {
        Game game = new Game(
                consolePresenter(),
                consoleMoveSupplier(),
                defaultGameEngine());

        int boardWidth = 4;
        int boardHeight = 4;

        if(args.length >= 2 && isNumeric(args[0]) & isNumeric(args[1])) {
            boardWidth = Integer.parseInt(args[0]);
            boardHeight = Integer.parseInt(args[1]);
        } else {
            System.out.println("Using default board sizes");
        }

        game.play(boardWidth, boardHeight);
    }

    private static PlayerMoveSupplier consoleMoveSupplier() {
        return new ConsolePlayerMoveSupplier();
    }

    private static GamePresenter consolePresenter() {
        GameBoardFormatter<String> formatter = new PlainTextGameBoardFormatter();
        return new ConsoleGamePresenter(formatter);
    }

    private static GameEngine defaultGameEngine() {
        return new GameEngine(new RandomizedGameBoardFactory());
    }
}
