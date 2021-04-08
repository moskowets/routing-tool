package p.moskovets.routing.models;

public enum TrayColour {
    Blue(25),
    Green(50),
    Yellow(75),
    Red(100),
    DarkRed(1000);

    private int colour;

    TrayColour(int colour) {
        this.colour = colour;
    }

    public int getColour() {
        return colour;
    }

    public static TrayColour getEnum(int percent) {
        if (percent < 25)
            return Blue;
        else if (percent < 50)
            return Green;
        else if (percent < 75)
            return Yellow;
        else if (percent < 100)
            return Red;
        else
            return DarkRed;
    }
}
