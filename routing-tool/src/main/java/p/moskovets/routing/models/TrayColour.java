package p.moskovets.routing.models;

public enum TrayColour {
    Blue("0,191,255"),
    Green("0,255,0"),
    Yellow("255,255,0"),
    Red("255,127,0"),
    DarkRed("255,0,0");

    private String colour;

    TrayColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
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
