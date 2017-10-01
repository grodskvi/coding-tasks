package puzzle.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Tile {

    private static final Tile EMPTY_TILE = new Tile(Optional.empty());
    private static final Map<Integer, Tile> TILES = new HashMap<>();

    private final Optional<Integer> tileValue;

    private Tile(Optional<Integer> tileValue) {
        this.tileValue = tileValue;
    }

    public Optional<Integer> getTileValue() {
        return tileValue;
    }

    public String getTileTextValue() {
        return tileValue
                .map(String::valueOf)
                .orElse("");
    }

    public boolean isEmpty() {
        return !tileValue.isPresent();
    }

    public static Tile tileOf(int value) {
        Tile tile = TILES.get(value);
        if(tile == null) {
            tile = new Tile(Optional.of(value));
            TILES.put(value, tile);
        }
        return tile;
    }

    public static Tile emptyTile() {
        return EMPTY_TILE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return Objects.equals(tileValue, tile.tileValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tileValue);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "tileValue=" + tileValue +
                '}';
    }
}
