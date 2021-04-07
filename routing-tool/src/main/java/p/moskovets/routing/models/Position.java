package p.moskovets.routing.models;

import com.google.gson.annotations.Expose;
import p.moskovets.utils.TextHelpers;

import java.util.Objects;

public class Position {

    private final double PRECISION = 0.1;

    @Expose
    private final double x;
    @Expose
    private final double y;
    @Expose
    private final double z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(Position other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getDistance(Position other) {
        return Math.abs(other.x - this.x) +
                Math.abs(other.y - this.y) +
                Math.abs(other.z - this.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.x, x) == 0 &&
                Double.compare(position.y, y) == 0 &&
                Double.compare(position.z, z) == 0;
    }

    public boolean positionEquals(Position other) {
        return positionEquals(other, PRECISION);
    }

    public boolean positionEquals(Position other, double precision) {
        if (this == other) return true;
        if (other == null) return false;
        return other.x - x < precision &&
                other.y - y < precision  &&
                other.z - z < precision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return TextHelpers.gson.toJson(this);
    }
}
