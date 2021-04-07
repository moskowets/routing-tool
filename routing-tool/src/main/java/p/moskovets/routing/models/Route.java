package p.moskovets.routing.models;

import p.moskovets.utils.Pair;

import java.util.LinkedList;
import java.util.List;

public class Route {

    private final GraphNode begin;
    private final GraphNode end;

    private final List<Cable> cables;

    private final List<Pair<GraphNode, GraphNode>> vorbiddenPath;

    private LinkedList<GraphNode> path;

    public Route(GraphNode begin, GraphNode end, List<Pair<GraphNode, GraphNode>> vorbiddenPath, List<Cable> cables) {
        this.begin = begin;
        this.end = end;
        this.vorbiddenPath = vorbiddenPath;
        this.cables = cables;
    }

    public GraphNode getBegin() {
        return begin;
    }

    public GraphNode getEnd() {
        return end;
    }

    public List<Pair<GraphNode, GraphNode>> getVorbiddenPath() {
        return vorbiddenPath;
    }

    public LinkedList<GraphNode> getPath() {
        return path;
    }

    public void setPath(LinkedList<GraphNode> path) {
        this.path = path;
    }

    public List<Cable> getCables() {
        return cables;
    }
}
