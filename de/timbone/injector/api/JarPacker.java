package de.timbone.injector.api;

import java.io.*;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Created by Tim on 25.03.2015.
 */
public class JarPacker {

    private JarOutputStream outputStream;

    public JarPacker(File jarFile) throws IOException {
        if (!jarFile.exists())
            jarFile.createNewFile();
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        outputStream = new JarOutputStream(new FileOutputStream(jarFile), manifest);
    }

    public void pack(File fromFile) throws IOException {
        if(fromFile.isDirectory()) {
            String name = getParent(fromFile) /* TODO Path#resolve() anschauen */ + fromFile.getName();
            if(!name.isEmpty()) {
                if(!name.endsWith("/"))
                    name += "/" ;
                JarEntry jarEntry = new JarEntry(name);
                jarEntry.setTime(fromFile.lastModified());
                outputStream.putNextEntry(jarEntry);
                outputStream.closeEntry();
            }
            for(File file : fromFile.listFiles())
                pack(file);
            return;
        }

        JarEntry jarEntry = new JarEntry(getParent(fromFile) + fromFile.getName());
        jarEntry.setTime(fromFile.lastModified());
        outputStream.putNextEntry(jarEntry);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fromFile));
        byte[] buffer = new byte[1024];
        int count = 0;
        while(count != -1) {
            count = inputStream.read(buffer);
            outputStream.write(buffer);
        }
        outputStream.closeEntry();
        inputStream.close();
    }

    private String getParent(File fromFile) {
        ArrayList<String> parents = new ArrayList<String>();
        File parent = new File(fromFile.getParent());
        while(!parent.getName().equalsIgnoreCase("plugin")
                && !parent.getName().equalsIgnoreCase("extension")) {
            parents.add(parent.getName());
            parent = new File(parent.getParent());
        }

        StringBuilder sb = new StringBuilder();
        for(int i = parents.size() - 1; i >= 0; i--) {
            sb.append(parents.get(i));
            sb.append("/");
        }
        return sb.toString();
    }

    public void closeOutputStream() throws IOException {
        outputStream.close();
    }

}

