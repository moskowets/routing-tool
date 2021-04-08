package p.moskovets.routing.models;

import com.google.gson.annotations.Expose;
import p.moskovets.utils.TextHelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tray {

    @Expose
    private final String type;
    private final int capacity;

    private static Map<String, Integer> capacities;
    static {
        capacities = new HashMap<>();
        capacities.put("28x190", 26);
        capacities.put("38x350", 90);
        capacities.put("60x400", 148);
        capacities.put("60x300", 112);
        capacities.put("60x200", 72);
        capacities.put("60x100", 36);
    }

    public Tray(String type) {
        this.type = type;
        capacity = capacities.get(type);
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public TrayColour getColour(int cables) {
        return TrayColour.getEnum ((int) (capacity / (double) cables * 100.0));
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
