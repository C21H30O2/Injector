package de.timbone.injector.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Tim on 24.03.2015.
 */
public class JarYamlReader {

    private JarFile jarFile;
    private JarEntry jarEntry;
    private HashSet<String> content = new HashSet<String>();

    public JarYamlReader(JarFile jarFile, JarEntry jarEntry) {
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
    }

    public void read() throws IOException {
        if(content.size() != 0)
            content.clear();
        BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarEntry)));
        String line;
        while((line = reader.readLine()) != null)
            content.add(line);
        reader.close();
    }

    public ArrayList<String> find(String search) throws IOException {
        if(content.size() == 0)
            read();
        ArrayList<String> found = new ArrayList<String>();
        for(String s : content)
            if(s.contains(search))
                found.add(s);

        return found;
    }

}
