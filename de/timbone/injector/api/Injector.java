package de.timbone.injector.api;

import javassist.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarFile;

/**
 * Created by Tim on 24.03.2015.
 */
public abstract class Injector {

    private JarFile plugin, extension;
    private File pluginFile, extensionFile, pluginFolder;

    public Injector(File plugin, File extension) {
        this.pluginFile = plugin;
        this.extensionFile = extension;
        setPlugin(pluginFile);
        setExtension(extensionFile);
    }

    public Injector() {

    }

    public void inject(File destination) {
        String pluginMain, extensionMain;
        try {
            log("Reading plugin.yml ...");
            pluginMain = new JarYamlReader(plugin, plugin.getJarEntry("plugin.yml")).find("main: ").get(0).substring(6);
            log("Found plugin main class: " + pluginMain);
            log("Reading extension.yml ...");
            extensionMain = new JarYamlReader(extension, extension.getJarEntry("extension.yml")).find("main: ").get(0).substring(6);
            log("Found extension main class: " + extensionMain);
        } catch (IOException e) {
            log("Could not load classpath!");
            return;
        }

        log("Loading plugin main class ... ");
        Class<?> pluginMainClass;
        URLClassLoader pluginClassLoader = null;
        try {
            pluginClassLoader = new URLClassLoader(new URL[]{pluginFile.toURI().toURL()});
            log("Loaded classloader!");
            pluginMainClass = pluginClassLoader.loadClass(pluginMain);
            log("Loaded plugin main class: " + pluginMainClass.getName());
        } catch (MalformedURLException e) {
            log("Could not load classloader!");
            return;
        } catch (ClassNotFoundException e) {
            log("Could not load main class!");
            return;
        }

        log("Loading extension main class ... ");
        Class<?> extensionMainClass;
        URLClassLoader extensionClassLoader = null;
        try {
            extensionClassLoader = new URLClassLoader(new URL[]{extensionFile.toURI().toURL()});
            log("Loaded classloader!");
            extensionMainClass = extensionClassLoader.loadClass(extensionMain);
            log("Loaded extension main class: " + extensionMainClass.getName());
        } catch (MalformedURLException e) {
            log("Could not load classloader!");
            return;
        } catch (ClassNotFoundException e) {
            log("Could not load main class!");
            return;
        }

        File baseDir = new File(System.getenv("APPDATA"), "Injector");
        if(!baseDir.exists())
            baseDir.mkdirs();
        pluginFolder = new File(baseDir, "plugin");
        pluginFolder.mkdirs();

        log("Unpacking jar files ...");

        try {
            JarUnziper pluginUnziper, extensionUnziper;

            pluginUnziper = new JarUnziper(plugin);
            pluginUnziper.unpack(pluginFolder);
            log(pluginFolder.getName());
            extensionUnziper = new JarUnziper(extension);
            extensionUnziper.unpack(pluginFolder);
        } catch (IOException e) {
            e.printStackTrace();
            log("Could not unpack jar files!");
            return;
        }

        log("Unpacked files!");

        log("Injecting code to onEnable() ...");

        ClassPool.getDefault().insertClassPath(new ClassClassPath(pluginMainClass));
        ClassPool.getDefault().insertClassPath(new ClassClassPath(extensionMainClass));

        try {
            CtClass pluginMainCtClass = ClassPool.getDefault().getCtClass(pluginMainClass.getName());

            pluginMainCtClass.getDeclaredMethod("onEnable").insertBefore(extensionMain + ".onEnable();");
            pluginMainCtClass.getDeclaredMethod("onDisable").insertBefore(extensionMain + ".onDisable();");

            saveCtClass(pluginMainCtClass);
        } catch (NotFoundException e) {
            log("Could not load CtClass from plugin main class!");
            return;
        } catch (CannotCompileException e) {
            log("Could not compile injected Code!");
            return;
        } catch (IOException e) {
            log("Could not save CtClass!");
            return;
        }

        log("Code injected!");
        log("Packing files to jar ...");

        try {
            JarPacker jarPacker = new JarPacker(destination);
            for(File file : pluginFolder.listFiles())
                if(!file.getName().equalsIgnoreCase("extension.yml")
                        && !file.getName().equalsIgnoreCase("meta-inf"))
                    jarPacker.pack(file);
            jarPacker.closeOutputStream();
        } catch (IOException e) {
            log("Could not pack files!");
            e.printStackTrace();
            return;
        }

        log("Packed files!");
        log("Deleting files ...");
        log("Plugin folder " + ((delete(pluginFolder)) ? "deleted!" : "not deleted"));
    }


    private boolean delete(File dir) {
        if(dir.isDirectory()){
           for(File file : dir.listFiles())
               delete(file);
        }
        return(dir.delete());
    }

    private void saveCtClass(CtClass ctClass) throws CannotCompileException, IOException {
        ctClass.defrost();
        ctClass.writeFile(pluginFolder.getPath());
    }

    public void setPlugin(File plugin) {
        try {
            this.plugin = new JarFile(plugin);
        } catch (IOException e) {
            log("Could not load plugin file!");
        }
    }

    public void setExtension(File extension) {
        try {
            this.extension = new JarFile(extension);
        } catch (IOException e) {
            log("Could not load extension file!");
        }
    }

    protected abstract void log(String msg);

}
