package p.moskovets.routing.models;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Link {

    private final Tray tray;
    private final GraphNode destinationNode;

    public Link(Tray tray, GraphNode destinationNode) {
        this.tray = tray;
        this.destinationNode = destinationNode;
    }

    public Tray getTray() {
        return tray;
    }

    public GraphNode getDestinationNode() {
        return destinationNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(destinationNode, link.destinationNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationNode);
    }
}
