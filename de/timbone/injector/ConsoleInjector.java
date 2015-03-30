package de.timbone.injector;

import de.timbone.injector.api.Injector;

import java.io.File;

/**
 * Created by Tim on 25.03.2015.
 */
public class ConsoleInjector extends Injector {

    public ConsoleInjector(String[] args) {
        super(new File(args[0]), new File(args[1]));
        super.inject(new File(args[2]));
    }

    @Override
    protected void log(String msg) {
        System.out.println(msg);
    }

}
