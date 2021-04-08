package p.moskovets.routing.alg;

import p.moskovets.routing.models.*;
import p.moskovets.utils.Pair;

import java.util.*;

public class RoutingGraph {

    private final Set<GraphNode> nodes = new HashSet<>();
    private final List<Route> routes = new LinkedList<>();
    // short connections
    private final Map<Pair<GraphNode, GraphNode>, Connection> connections = new HashMap<>();
    // full connections
    private final Map<Pair<GraphNode, GraphNode>, LinkedList<GraphNode>> paths = new HashMap<>();

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

    public void findRoute(GraphNode current, GraphNode dest, RouteHelper helper) {
        for (Link link: current.getLinks()) {
            if (helper.canAddNode(link.getDestinationNode()))
            {
                if (link.getDestinationNode() == dest){
                    helper.putResult(link.getDestinationNode());
                } else {
                    findRoute(link.getDestinationNode(), dest, helper.clone(link.getDestinationNode()));
                }
            }
        }
    }

    public void process() {

        for (Route route: routes) {
            RouteHelper helper = new RouteHelper(route.getVorbiddenPath(), route.getBegin());
            if (route.getEnd() == null) {
                System.out.println("Destination point doesnt exist: " + route);
                continue;
            }
            var pair = getPair(route.getBegin(), route.getEnd());
            LinkedList<GraphNode> shortestPath = null;
            if (paths.containsKey(pair)) {
                shortestPath = paths.get(pair);
            } else {
                findRoute(route.getBegin(), route.getEnd(), helper);
                if (helper.getPaths().size() == 0) {
                    System.out.println("Unable to find path from: " + route.getBegin().getName() + " to: " + route.getEnd().getName());
                    continue;
                } else {
                    shortestPath = getShortestPath(helper.getPaths());
                    paths.put(pair, shortestPath);
                }
            }
            mergeConnections(shortestPath, route.getCables());
        }
        System.out.println("Finish finding");
    }

    public Map<Pair<GraphNode, GraphNode>, Connection> getConnections() {
        return connections;
    }

    public Map<Pair<GraphNode, GraphNode>, LinkedList<GraphNode>> getPaths() {
        return paths;
    }
}
