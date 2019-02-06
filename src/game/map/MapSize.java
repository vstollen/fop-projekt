package game.map;

import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

public enum MapSize {

    SMALL("Klein"),
    MEDIUM("Mittel"),
    LARGE("Groß");

    private String label;
    MapSize(String lbl) {
        this.label = lbl;
    }

    @Override
    public String toString() {
        return this.label;
    }

    public static Vector<String> getMapSizes() {
        return Arrays.stream(values()).map(MapSize::toString).collect(Collectors.toCollection(Vector::new));
    }

}
