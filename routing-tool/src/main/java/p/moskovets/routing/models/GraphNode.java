package p.moskovets.routing.models;

import com.google.gson.annotations.Expose;
import p.moskovets.routing.alg.RouteHelper;
import p.moskovets.utils.TextHelpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class GraphNode {

    @Expose
    private final String name;
    @Expose
    private final Position position;
    private final HashSet<Link> links = new HashSet<>();

    public GraphNode(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public void addAllLinks(Collection<Link> links) {
        this.links.addAll(links);
    }

    public Tray getTray(GraphNode other){
        for (Link link: links) {
            if (link.getDestinationNode() == other) {
                return link.getTray();
            }
        }
        return null;
    }

    public void findRoute(GraphNode node, RouteHelper helper) {
        for (Link link: links) {
            if (helper.canAddNode(link.getDestinationNode()))
            {
                if (link.getDestinationNode() == node){
                    helper.putResult(link.getDestinationNode());
                } else {
                    findRoute(node, helper.clone(link.getDestinationNode()));
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode graphNode = (GraphNode) o;
        return Objects.equals(name, graphNode.name) && Objects.equals(position, graphNode.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position);
    }

    @Override
    public String toString() {
        return TextHelpers.gson.toJson(this);
    }

    public boolean isPrior(GraphNode other) {
        int thisHash = this.hashCode();
        int otherHash = other.hashCode();
        if (thisHash != otherHash)
            return thisHash > otherHash;
        else
            return this.toString().compareTo(other.toString()) > 0;
    }

    public Position getPosition() {
        return position;
    }
}
