package p.moskovets.routing.dataimport;

import com.beust.jcommander.JCommander;
import com.google.gson.annotations.Expose;
import p.moskovets.routing.alg.RoutingGraph;
import p.moskovets.routing.dataExport.AutocadExporter;
import p.moskovets.routing.dataimport.models.Commands;
import p.moskovets.routing.dataimport.models.Level;
import p.moskovets.routing.models.*;
import p.moskovets.utils.Pair;
import p.moskovets.utils.TextHelpers;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class ICHotelImporter implements Importer {

    private final String NODE_NAME = "NODE_NAME";
    private final String X = "X";
    private final String Y = "Y";
    private final String Z = "Z";
    private final String NEXT = "NEXT";
    private final String TYPE = "TYPE";
    private final String DESTINATION = "DESTINATION";
    private final String CABEL_COUNT = "CABEL_COUNT";

    private String path = "C:\\Users\\pmosk\\IdeaProjects\\data";
    private Scanner scanner = new Scanner(System.in);
    private Set<GraphNode> nodes;
    private List<Route> routes;

    private Map<String, Pair<Level, Map<String, String>>> rawMap;
    private Map<String, GraphNode> destinations;

    void process(Commands commands) {
        if (commands.getPath().length() > 0) {
            path = commands.getPath();
        }
        switch (commands.getFunction()) {
            case "load":
                load(commands.getExtension(), commands.getLevel());
                break;
            case "loadJSON":
                loadJSON(commands.getLevel());
                break;
            case "import":
                processImport();
                break;
            case "exportACAD":
                exportACAD();
                break;
            case "exportJSON":
                exportJSON();
                break;
            case "find":
                findRoutes();
                break;
        }
    }


    void loadJSON(Level level) {}
    void exportACAD() {}
    void exportJSON() {}

    void findRoutes() {
        var graph = new RoutingGraph(nodes, routes);
        graph.process();
        //println(TextHelpers.gson.toJson(graph.getPaths()));
        Predicate<Connection> bodenEG = connection -> connection.getBegin().getZ() > -0.1 && connection.getBegin().getZ() < 3.0;
        Predicate<Connection> trassenEG = connection -> connection.getBegin().getZ() > 3.1 && connection.getBegin().getZ() < 3.8;
        Predicate<Connection> trassenUG = connection -> connection.getBegin().getZ() < 0;

        String scrEG =  export(graph.getConnections().values(), bodenEG);
        String scrEG2 =  export(graph.getConnections().values(), trassenEG);
        String scrUG =  export(graph.getConnections().values(), trassenUG);

        String all =  export(graph.getConnections().values(), item -> true);

        println("Done");
    }



    String export(Collection<Connection> connections, Predicate<Connection> predicate) {
        AutocadExporter exporter = new AutocadExporter();
        StringBuilder builder = new StringBuilder();
        //creating layers
        for (TrayColour colour: TrayColour.values()) {
            builder.append(exporter.createLayer(colour));
            builder.append(exporter.setLayerColour(colour));
        }

        for (Connection connection: connections.stream().filter(predicate).toArray(Connection[]::new)) {
            if (connection.getBegin().positionEquals(connection.getEnd())) {
                //continue;
            }
            builder.append(exporter.setLayer(connection
                    .getTray()
                    .getColour(connection
                            .getCables()
                            .size())));
            builder.append(exporter.createPline(connection));
        }
        return builder.toString();
    }

    double getDouble(String value) {
        value = value.replaceAll("\\D", "");
        try {
            int integer = 0;
            if (value.length() > 4) {
                integer = Integer.parseInt(value.substring(0, Math.max(value.length() - 4, 0)));
            }
            int fractional = Integer.parseInt(value.substring(Math.max(value.length() - 4, 0)));
            return integer + fractional/10000.0;
        } catch (Exception e) {
            println("getDouble error: " + value);
            println(e.toString());
        }
        return 0;
    }


    private Map<String, String> getNextNodesRaw(String nodeName) {
        Map<String, String> map = new HashMap<>();
        var value = rawMap.get(nodeName);
        for (int i = 1; i < 5; ++i) {
            if (value.snd.containsKey(NEXT + i)) {
                String nextNode = value.snd.get(NEXT + i).trim();
                if ("".equals(nextNode))
                    continue;
                if (isNodeNameCorrect(nextNode)) {
                    String nextType = value.snd.get(TYPE + i).trim();
                    if ("".equals(nextType) || isTrayTypeCorrect(nextType)) {
                        map.put(nextNode, nextType);
                    } else {
                        println("Node type is incorrect: " + nextNode + ", parent node is: " + nodeName + ", type: " + nextType);
                    }
                } else {
                    println("Node name is incorrect: " + nextNode + ", parent node is: " + nodeName);
                }
            }
        }
        return map;
    }

    private boolean containsOppositeNode(String nodeName, String tray, String otherNodeName) {
        Map<String, String> otherNextNodes = null;
        boolean result = false;

        otherNextNodes = getNextNodesRaw(otherNodeName);
        result = Objects.equals(otherNextNodes.get(nodeName), tray);

        return result;
    }

    private GraphNode getNode(String name) {
        for (var node: nodes) {
            if (node.getName().equals(name))
                return node;
        }
        return null;
    }

    void processImport() {
        //creating nodes
        nodes = new HashSet<>();
        for (var item: rawMap.keySet()) {
            var value = rawMap.get(item);
            Position position = new Position(
                    getDouble(value.snd.get(X)),
                    getDouble(value.snd.get(Y)),
                    value.fst.getHeight(Double.parseDouble(value.snd.get(Z))));
            nodes.add(new GraphNode(item, position));
        }
        //adding links
        for (var node: nodes) {
            if (node.getName().equals("E323")) {
                println("E323");
            }
            Map<String, String> nextNodes = getNextNodesRaw(node.getName());
            nextNodes.forEach((nodename, traytype) -> {
                if (!containsOppositeNode(node.getName(), traytype, nodename)) {
                    println("No opposite Node found, or wrong Tray: " + node.getName() + "-" + traytype + "-" + nodename);
                } else {
                    node.addLink(new Link(new Tray(traytype), getNode(nodename)));
                }
            });
        }
        //adding destinations
        destinations = new HashMap<>();
        rawMap.forEach((nodeName, pair) -> {
            String dest = pair.snd.get(DESTINATION).trim().toUpperCase();
            if (dest.startsWith("S_")) {
                destinations.put(dest.substring(2), getNode(nodeName));
            }
        });


        //adding routes
        routes = new LinkedList<>();
        rawMap.forEach((nodeName, pair) -> {
            String dest = pair.snd.get(DESTINATION).trim().toUpperCase();
            if (dest.length() > 0 && !dest.startsWith("S_")) {
                String cnt = pair.snd.get(CABEL_COUNT).trim().toUpperCase();
                int count = 0;
                try {
                    count = Integer.parseInt(cnt);
                } catch (NumberFormatException e) {
                    println("Wrong format of cable count: " + nodeName + " , " + e.toString());
                }
                if (count > 0)
                    routes.add(new Route(getNode(nodeName), destinations.get(dest), null, getCables(count)));
            }
        });
        println("Import done!");
    }

    private List<Cable> getCables(int number) {
        ArrayList<Cable> cables = new ArrayList<>(number);
        for (int i = 0; i < number; ++i) {
            cables.add(new Cable());
        }
        return cables;
    }

    private boolean isNodeNameCorrect(String name) {
        return name.length() == 4;
    }

    private boolean isTrayTypeCorrect(String name) {
        return name.matches("\\d{2}x\\d{3}");
    }

    private void getParams(String[] header, String[] line, Level level) {
        if (rawMap == null)
            rawMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        String nodeName = null;
        for (int i = 0; i < header.length; ++i) {
            if (NODE_NAME.equals(header[i])) {
                nodeName = line[i];
                if (!isNodeNameCorrect(nodeName)) {
                    println("Wrong name by reading RAW data " + Arrays.toString(line));
                    return;
                }
            }
            map.put(header[i], line[i]);
        }
        rawMap.put(nodeName, new Pair<>(level, map));
    }

    void load(String ext, Level defaultLevel) {
        try {
            File folder = new File(path);
            if (folder.isDirectory()) {
                File f = new File(folder.getParent() + "\\completed\\");
                f.mkdir();
                for (var file : folder.listFiles()) {
                    boolean goodFile = false;
                    if (file.getName().endsWith("." + ext)) {
                        Level level = defaultLevel;
                        for (var lev: Arrays.stream(Level.values())
                                .map(item -> item.toString())
                                .toArray(String[]::new)) {
                            if (file.getName().contains("_" + lev + "_")) {
                                level = Level.valueOf(lev);
                                break;
                            }
                        }
                        try (Scanner scan = new Scanner(file)) {
                            goodFile = true;
                            String[] header = scan.nextLine().toUpperCase().split("\t");
                            while (scan.hasNextLine()) {
                                String line = scan.nextLine().trim();
                                if ("".equals(line)) {
                                    break;
                                } else {
                                    getParams(header, line.split("\t"), level);
                                }
                            }
                        } catch (Exception e) {
                            println("Problem read file: " + file.getPath());
                            goodFile = false;
                        }
                    }
                    if (goodFile)
                        file.renameTo(new File(file.getParent() + "\\completed\\" + file.getName()));
                }


            } else {
                println("Wrong path: " + folder.getPath());
            }
        } catch (Exception e) {
            println(e);
        }
    }


    public void run() {
        Commands commands = null;
        do {
            if (commands != null)
                process(commands);
            commands = new Commands();
            var commander = JCommander.newBuilder()
                    .addObject(commands)
                    .build();
            commander.usage();
            print("> ");
            commander.parse(scanner.nextLine().split("\\s+"));
        }
        while (!commands.isExit());
    }

    void println(Object o) {
        System.out.println(o.toString());
    }

    void print(Object o) {
        System.out.print(o.toString());
    }
}
