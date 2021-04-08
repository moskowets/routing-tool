package p.moskovets.routing.models;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

public class Connection {
    @Expose
    private Tray tray;
    private Set<Cable> cables = new HashSet<>();
    @Expose
    private final Position begin;
    @Expose
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
