package p.moskovets.routing.dataExport;

import p.moskovets.routing.models.Connection;
import p.moskovets.routing.models.TrayColour;

public class AutocadExporter {
    private final String LAYER = "СЛОЙ";
    private final String CREATE = "Создать";
    private final String END_LINE = "\n";
    private final String COLOUR = "Цвет";
    private final String FROM_PALETTE = "изПалитры";
    private final String SET = "Установить";
    private final String PLINE = "ПЛИНИЯ";

    public String createPline(Connection connection) {
        return new StringBuilder()
                .append(PLINE)
                .append(END_LINE)
                .append(connection.getBegin().getX())
                .append(',')
                .append(connection.getBegin().getY())
                .append(END_LINE)
                .append(connection.getEnd().getX())
                .append(',')
                .append(connection.getEnd().getY())
                .append(END_LINE)
                .append(END_LINE)
                .toString();
    }

    public String createLayer(TrayColour colour) {
        return new StringBuilder()
                .append(LAYER)
                .append(END_LINE)
                .append(CREATE)
                .append(END_LINE)
                .append(colour.toString())
                .append(END_LINE)
                .append(END_LINE)
                .toString();
    }

    public String setLayer(TrayColour colour) {
        return new StringBuilder()
                .append(LAYER)
                .append(END_LINE)
                .append(SET)
                .append(END_LINE)
                .append(colour.toString())
                .append(END_LINE)
                .append(END_LINE)
                .toString();
    }

    public String setLayerColour(TrayColour colour) {
        return new StringBuilder()
                .append(LAYER)
                .append(END_LINE)
                .append(COLOUR)
                .append(END_LINE)
                .append(FROM_PALETTE)
                .append(END_LINE)
                .append(colour.getColour())
                .append(END_LINE)
                .append(colour.toString())
                .append(END_LINE)
                .append(END_LINE)
                .toString();
    }
}
