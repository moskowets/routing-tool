package p.moskovets.routing.dataimport.models;

import com.beust.jcommander.IStringConverter;

public enum Level implements IStringConverter<Level> {
    U2(-2),
    U1(-1),
    UG(-1),
    EG(0),
    G1(1),
    G2(2),
    G3(3),
    G4(4),
    G5(5),
    G6(6),
    OG1(1),
    OG2(2),
    OG3(3),
    OG4(4),
    OG5(5),
    OG6(6),
    Undef(Integer.MIN_VALUE);

    private int level;

    Level(int level) {
        this.level = level;
    }

    @Override
    public Level convert(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException e) {
            return Level.Undef;
        }
    }

    public int getLevel() {
        return level;
    }

    public double getHeight(double height) {
        return height + level * 4;
    }
}
