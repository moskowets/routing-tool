package p.moskovets.routing.dataimport.models;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "-p", description = "Path to files")
    private String path = "";

    @Parameter(names = "-f", description = "Function or procedure to run")
    private String function = "";

    @Parameter(names = "-l", description = "Current level i.e. UG, EG, G1", converter = Level.class)
    private Level level = Level.Undef;

    @Parameter(names = "-e", description = "File extension (default txt)")
    private String extension = "txt";

    @Parameter(names = "-exit", description = "Exit command")
    private boolean exit = false;


    public String getPath() {
        return path;
    }

    public String getFunction() {
        return function;
    }

    public Level getLevel() {
        return level;
    }

    public String getExtension() {
        return extension;
    }

    public boolean isExit() {
        return exit;
    }
}
