package p.moskovets.routing.models;

import com.google.gson.annotations.Expose;
import p.moskovets.utils.TextHelpers;

import java.util.Objects;

public class Tray {
/*    h38w350("38x350"),
    h28w190("28x190"),
    h60w400("60x400"),
    h60w300("60x300"),
    h60w200("60x200"),
    h60w100("60x100"),
    none("");
*/
    @Expose
    private final String type;
    private final int capacity;

    Tray(String type, int capacity) {
        this.type = type;
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return TextHelpers.gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tray tray = (Tray) o;
        return Objects.equals(type, tray.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
