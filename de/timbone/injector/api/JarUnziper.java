package de.timbone.injector.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Tim on 25.03.2015.
 */
public class JarUnziper {

    private JarFile jarFile;

    public JarUnziper(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public void unpack(File destinationFile) throws IOException {
        for(Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements();) {
            String fileName = destinationFile.getAbsolutePath() + File.separator + enumeration.nextElement().getName();
            fileName = fileName.replace("\\", "/");
            if(fileName.endsWith("/")) {
                File file = new File(fileName);
                file.mkdirs();
            }
        }

        for(Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements();) {
            JarEntry jarEntry = enumeration.nextElement();
            String fileName = destinationFile.getAbsolutePath() + File.separator + jarEntry.getName();
            fileName = fileName.replace("\\", "/");
            if(!fileName.endsWith("/")) {
                File file = new File(fileName);
                file.createNewFile();
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                FileOutputStream outputStream = new FileOutputStream(file);

                while(inputStream.available() > 0)
                    outputStream.write(inputStream.read());

                inputStream.close();
                outputStream.close();
            }
        }
    }

}
