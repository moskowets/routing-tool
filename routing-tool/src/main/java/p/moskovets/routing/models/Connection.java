package p.moskovets.routing.models;

import java.util.HashSet;
import java.util.Set;

public class Connection {
    private Tray tray;
    private Set<Cable> cables = new HashSet<>();
    private final Position begin;
    private final Position end;

    public Connection(Tray tray, Position begin, Position end) {
        this.tray = tray;
        this.begin = begin;
        this.end = end;
    }

    public Tray getTray() {
        return tray;
    }

    public void setTray(Tray tray) {
        this.tray = tray;
    }

    public Set<Cable> getCables() {
        return cables;
    }

    public Position getBegin() {
        return begin;
    }

    public Position getEnd() {
        return end;
    }
}
