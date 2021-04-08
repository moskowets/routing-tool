package p.moskovets.routing.dataimport;

import com.beust.jcommander.JCommander;
import p.moskovets.routing.dataimport.models.Commands;
import p.moskovets.routing.dataimport.models.Level;
import p.moskovets.routing.models.*;
import p.moskovets.utils.Pair;

import java.io.File;
import java.util.*;

public class ICHotelImporter implements Importer {

    private final String NODE_NAME = "NODE_NAME";
    private final String X = "X";
    private final String Y = "Y";
    private final String Z = "Z";
    private final String NEXT = "NEXT";
    private final String TYPE = "TYPE";

    private String path = "";
    private Scanner scanner = new Scanner(System.in);
    private Set<GraphNode> nodes;
    private List<Route> routes;

    private Map<String, Pair<Level, Map<String, String>>> rawMap = new HashMap<>();

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
            case "processImport":
                processImport();
                break;
            case "exportACAD":
                exportACAD();
                break;
            case "exportJSON":
                exportJSON();
                break;
            case "findRoutes":
                findRoutes();
                break;
        }
    }


    void loadJSON(Level level) {}
    void exportACAD() {}
    void exportJSON() {}
    void findRoutes() {}

    double getDouble(String value) {
        value = value.replaceAll("\\D", "");
        int integer = Integer.parseInt(value.substring(0, Math.max(value.length() - 4, 0)));
        int fractional = Integer.parseInt(value.substring(Math.max(value.length() - 4, 0)));
        return integer + fractional/10000.0;
    }


    private Map<String, String> getNextNodesRaw(String nodeName) {
        Map<String, String> map = new HashMap<>();
        var value = rawMap.get(nodeName);
        for (int i = 0; i < 4; ++i) {
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
    }

    private boolean containsOppositeNode(String nodeName, String tray, String otherNodeName) {
        Map<String, String> otherNextNodes = getNextNodesRaw(otherNodeName);
        return otherNextNodes.get(nodeName).equals(tray);
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
        for (var item: rawMap.keySet()) {
            var value = rawMap.get(item);
            Position position = new Position(
                    getDouble(value.snd.get(X)),
                    getDouble(value.snd.get(Y)),
                    value.fst.getHeight(getDouble(value.snd.get(Z))));
            nodes.add(new GraphNode(item, position));
        }
        //adding links
        for (var node: nodes) {
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

        //adding routes

    }

    private boolean isNodeNameCorrect(String name) {
        return name.length() == 4;
    }

    private boolean isTrayTypeCorrect(String name) {
        return name.matches("\\d{2}x\\d{3}");
    }

    private void getParams(String[] header, String[] line, Level level) {
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
                for (var file : folder.listFiles()) {
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
                        }
                    }
                }


            } else {
                println("Wrong path: " + folder.getPath());
            }
        } catch (Exception e) {
            println(e);
        }
    }


    void run() {
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
