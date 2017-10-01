package puzzle.game;

import puzzle.domain.GameBoard;
import puzzle.domain.Position;
import puzzle.domain.Tile;
import puzzle.domain.TileStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static puzzle.domain.Tile.emptyTile;
import static puzzle.domain.TileStatus.ELIGIBLE_TO_MOVE;
import static puzzle.domain.TileStatus.NON_ELIGIBLE_TO_MOVE;
import static puzzle.domain.TileStatus.NON_EXISTING;

public class GameEngine {

    private static final Tile EMPTY_TILE = emptyTile();
    private static final List<Function<Position, Position>> DEFAULT_ALLOWED_MOVES = new ArrayList<Function<Position, Position>>() {{
        add(p -> new Position(p.getX() + 1, p.getY())); //left
        add(p -> new Position(p.getX() - 1, p.getY())); //right
        add(p -> new Position(p.getX(), p.getY() - 1)); //up
        add(p -> new Position(p.getX(), p.getY() + 1)); //down
    }};

    private GameBoardFactory gameBoardFactory;
    private List<Function<Position, Position>> allowedMoves;

    public GameEngine(GameBoardFactory gameBoardFactory) {
        this(gameBoardFactory, DEFAULT_ALLOWED_MOVES);
    }

    public GameEngine(GameBoardFactory gameBoardFactory, List<Function<Position, Position>> allowedMoves) {
        this.gameBoardFactory = gameBoardFactory;
        this.allowedMoves = allowedMoves;
    }

    public GameBoard startGame(int width, int height) {
        return gameBoardFactory.createGameBoard(width, height);
    }

    public TileStatus getTileStatus(Tile tile, GameBoard currentBoard) {
        if(!currentBoard.hasTile(tile)) {
            return NON_EXISTING;
        }

        return isValidMove(tile, currentBoard) ? ELIGIBLE_TO_MOVE : NON_ELIGIBLE_TO_MOVE;
    }

    public GameBoard moveTile(Tile tile, GameBoard currentBoard) {
        return currentBoard.swapTiles(tile, EMPTY_TILE);
    }

    private boolean isValidMove(Tile movingTile, GameBoard currentBoard) {
        Optional<Position> movingTilePosition = currentBoard.getTilePosition(movingTile);
        if(!movingTilePosition.isPresent()) {
            return false;
        }
        List<Tile> reachableTiles = allowedMoves.stream()
                .map(move -> move.apply(movingTilePosition.get()))
                .map(position -> currentBoard.getTileAt(position))
                .filter(tile -> tile.isPresent())
                .map(Optional::get)
                .collect(Collectors.toList());

        return containEmptyTile(reachableTiles);
    }

    public boolean isGameFinished(GameBoard currentBoard) {
        List<Tile> tiles = currentBoard.getTiles();
        Tile previousTile = null;
        for(Tile tile: tiles) {
            if(!isNextTileIsGreater(previousTile, tile)) {
                return false;
            }
            previousTile = tile;
        }
        return true;
    }

    private boolean containEmptyTile(List<Tile> tiles) {
        return tiles.stream()
                .filter(Tile::isEmpty)
                .findFirst()
                .isPresent();
    }

    private boolean isNextTileIsGreater(Tile tile, Tile nextTile) {
        if(tile == null && nextTile != null) {
            return true;
        }
        int tileValue = tile.getTileValue().orElse(Integer.MAX_VALUE);
        int nextTileValue = nextTile.getTileValue().orElse(Integer.MAX_VALUE);
        return tileValue < nextTileValue;
    }
}
