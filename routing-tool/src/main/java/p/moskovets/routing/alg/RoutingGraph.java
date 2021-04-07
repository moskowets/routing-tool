package p.moskovets.routing.alg;

import p.moskovets.routing.models.*;
import p.moskovets.utils.Pair;

import java.util.*;

public class RoutingGraph {

    private final Set<GraphNode> nodes = new HashSet<>();
    private final Map<Pair<GraphNode, GraphNode>, Connection> connections = new HashMap<>();
    private final List<Route> routes = new LinkedList<>();

    public RoutingGraph(Set<GraphNode> nodes, List<Route> routes) {
        this.nodes.addAll(nodes);
        this.routes.addAll(routes);
    }

    private Pair<GraphNode, GraphNode> getPair(GraphNode fst, GraphNode snd) {
        if (fst.isPrior(snd))
            return new Pair<>(fst, snd);
        else
            return new Pair<>(snd, fst);
    }

    private void mergeConnections(LinkedList<GraphNode> path, List<Cable> cables) {
        int iteration = 0;
        GraphNode lastNode = null;
        for (var node: path) {
            if (iteration++ != 0) {
                mergeConnection(lastNode, node, cables);
            }
            lastNode = node;
        }
    }

    private void mergeConnection(GraphNode last, GraphNode next, List<Cable> cables) {
        var key = getPair(last, next);
        if (connections.containsKey(key)) {
            var value = connections.get(key);
            value.getCables().addAll(cables);
        } else {
            var value = new Connection(last.getTray(next), last.getPosition(), next.getPosition());
            value.getCables().addAll(cables);
            connections.put(key, value);
        }
    }

    private double getPathLen(LinkedList<GraphNode> path) {
        double length = 0;
        int iteration = 0;
        GraphNode lastNode = null;
        for (var node: path) {
            if (iteration++ != 0) {
                length += lastNode.getPosition().getDistance(node.getPosition());
            }
            lastNode = node;
        }
        return length;
    }

    private LinkedList<GraphNode> getShortestPath(LinkedList<LinkedList<GraphNode>> paths) {
        LinkedList<GraphNode> shortestPath = null;
        double minLength = Double.MAX_VALUE;
        int iteration = 0;
        for (LinkedList<GraphNode> path: paths) {
            if (iteration++ == 0) {
                shortestPath = path;
                minLength = getPathLen(path);
            } else {
                double currentLength = getPathLen(path);
                if (currentLength < minLength) {
                    shortestPath = path;
                    minLength = currentLength;
                }
            }
        }
        return shortestPath;
    }

    public void process() {

        for (Route route: routes) {
            RouteHelper helper = new RouteHelper(route.getVorbiddenPath(), route.getBegin());
            route.getBegin().findRoute(route.getEnd(), helper);
            LinkedList<GraphNode> shortestPath = getShortestPath(helper.getPaths());
            mergeConnections(shortestPath, route.getCables());
        }
    }
}
