package puzzle.console;

import puzzle.domain.Tile;
import puzzle.game.PlayerMoveSupplier;

import java.util.Scanner;

import static org.apache.commons.lang.StringUtils.isNumeric;
import static puzzle.domain.Tile.tileOf;
import static puzzle.utils.MessageUtils.invalidTileValue;

public class ConsolePlayerMoveSupplier implements PlayerMoveSupplier {

    private Scanner scanner = new Scanner(System.in);

    @Override
    public Tile nextMove() {
        String input = requestInput();
        while(!isNumeric(input)) {
            System.out.println(invalidTileValue(input));
            input = requestInput();
        }
        return tileOf(Integer.parseInt(input));
    }

    private String requestInput() {
        System.out.println("Enter tile to be moved ");
        return scanner.nextLine();
    }
}
