package p.moskovets.routing.models;

import java.util.Objects;

public class Cable {

    private static int counter = 0;
    private int id = 0;

    public Cable() {
        id = ++counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cable cable = (Cable) o;
        return id == cable.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
