package de.timbone.injector;

/**
 * Created by Tim on 24.03.2015.
 */
public class Main {

    public static final String VERSION = "v1.0";

    public static void main(String args[]) {
        if(args.length == 4 && args[0].equalsIgnoreCase("--no-gui"))
            new ConsoleInjector(new String[]{args[1], args[2], args[3]});
        else
            System.out.println("");
    }

}
