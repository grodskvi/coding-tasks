package puzzle.domain;

import java.util.*;

public class GameBoard {
    private final int width;
    private final int height;
    private final Map<Tile, Position> tilePositions;

    public GameBoard(int width, int height, List<Tile> tiles) {
        if(width < 1 || height < 1) {
            throw new IllegalArgumentException("Width and height can't be less than 1, but got width=" + width + ", height=" + height);
        }
        if(tiles.size() != width * height) {
            throw new IllegalArgumentException("Incorrect number of tiles. Expected " + (width * height) + ", but got " + tiles.size());
        }
        this.width = width;
        this.height = height;
        this.tilePositions = new HashMap<>();
        for (int i = 0; i < tiles.size(); i++) {
            Position position = new Position(i % width, i / width);
            tilePositions.put(tiles.get(i), position);
        }
    }

    private GameBoard(int width, int height, Map<Tile, Position> tilePositions) {
        this.width = width;
        this.height = height;
        this.tilePositions = tilePositions;
    }

    public boolean hasTile(Tile tile) {
        return tilePositions.containsKey(tile);
    }

    public Optional<Position> getTilePosition(Tile tile) {
        Position tilePosition = tilePositions.get(tile);
        return Optional.ofNullable(tilePosition);
    }

    public Optional<Tile> getTileAt(Position position) {
        if(position.getX() < 0 || position.getY() < 0 || position.getX() >= width || position.getY() >= height) {
            return Optional.empty();
        }

        Optional<Map.Entry<Tile, Position>> tileEntry = tilePositions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(position))
                .findFirst();

        return tileEntry.map(Map.Entry::getKey);
    }

    public GameBoard swapTiles(Tile tile, Tile otherTile) {
        Optional<Position> tilePosition = getTilePosition(tile);
        Optional<Position> otherTilePosition = getTilePosition(otherTile);
        if(!tilePosition.isPresent() || !otherTilePosition.isPresent()) {
            throw new IllegalArgumentException("Can't swap tiles");
        }

        Map<Tile, Position> updatedTilePositions = new HashMap<>(tilePositions);
        updatedTilePositions.put(tile, otherTilePosition.get());
        updatedTilePositions.put(otherTile, tilePosition.get());

        return new GameBoard(getWidth(), getHeight(), updatedTilePositions);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<Tile> getTiles() {
        Tile[] tiles = new Tile[tilePositions.size()];
        for (Map.Entry<Tile, Position> tileEntry: tilePositions.entrySet()) {
            Position position = tileEntry.getValue();
            int index = position.getY() * height + position.getX();
            tiles[index] = tileEntry.getKey();
        }
        return Arrays.asList(tiles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameBoard gameBoard = (GameBoard) o;
        return width == gameBoard.width &&
                height == gameBoard.height &&
                Objects.equals(tilePositions, gameBoard.tilePositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, tilePositions);
    }
}
