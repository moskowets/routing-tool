package p.moskovets.routing.alg;

import p.moskovets.routing.models.GraphNode;
import p.moskovets.utils.Pair;

import java.util.LinkedList;
import java.util.List;

public class RouteHelper {

    private final LinkedList<GraphNode> passedNodes = new LinkedList<>();
    private List<Pair<GraphNode, GraphNode>> vorbiddenPath = new LinkedList<>();
    private LinkedList<LinkedList<GraphNode>> paths = new LinkedList<>();

    public RouteHelper(List<Pair<GraphNode, GraphNode>> vorbiddenPath, GraphNode firstNode) {
        if (vorbiddenPath != null)
            this.vorbiddenPath = vorbiddenPath;
        if (firstNode != null)
            passedNodes.add(firstNode);
    }

    public RouteHelper clone(GraphNode node) {
        var helper = new RouteHelper(vorbiddenPath, null);
        helper.paths = this.paths;
        helper.passedNodes.addAll(this.passedNodes);
        helper.passedNodes.add(node);
        return helper;
    }

    public void putResult(GraphNode node) {
        LinkedList<GraphNode> result = new LinkedList<>(passedNodes);
        result.add(node);
        paths.add(result);
    }

    public boolean canAddNode(GraphNode node) {
         if (passedNodes.contains(node)) {
             return false;
         } else {
             if (passedNodes.size() > 0) {
                 for (Pair<GraphNode, GraphNode> line: vorbiddenPath) {
                     if ((line.fst.equals(passedNodes.getLast()) && node.equals(line.snd))
                             || (line.snd.equals(passedNodes.getLast()) && node.equals(line.fst))) {
                         return false;
                     }
                 }
             }
             return true;
         }
    }

    public LinkedList<LinkedList<GraphNode>> getPaths() {
        return paths;
    }
}
