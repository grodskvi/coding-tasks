package puzzle.game;

import puzzle.domain.GameBoard;
import puzzle.domain.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static puzzle.domain.Tile.emptyTile;
import static puzzle.domain.Tile.tileOf;

public class RandomizedGameBoardFactory implements GameBoardFactory {

    @Override
    public GameBoard createGameBoard(int width, int height) {
        if(width < 1) {
            throw new IllegalArgumentException("Can't initialize game board with width " + width);
        }
        if(height < 1) {
            throw new IllegalArgumentException("Can't initialize game board with height " + height);
        }

        List<Tile> tiles = new ArrayList<>();
        for(int i = 1; i < width * height; i++) {
            tiles.add(tileOf(i));
        }
        Collections.shuffle(tiles);
        tiles.add(emptyTile());

        return new GameBoard(width, height, tiles);
    }
}
