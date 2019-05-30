package com.enjin.java_commons;

import com.enjin.java_commons.io.ResourceURLFilter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ResourceUtils {

    private static void collectURL(ResourceURLFilter f, Set<URL> s, URL u) {
        if (f == null || f.accept(u)) {
            s.add(u);
        }
    }

    private static void iterateFileSystem(File r, ResourceURLFilter f, Set<URL> s) throws MalformedURLException, IOException {
        File[] files = r.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                iterateFileSystem(file, f, s);
            } else if (file.isFile()) {
                collectURL(f, s, file.toURI().toURL());
            }
        }
    }

    private static void iterateJarFile(File file, ResourceURLFilter f, Set<URL> s) throws MalformedURLException, IOException {
        JarFile jFile = new JarFile(file);
        for (Enumeration<JarEntry> je = jFile.entries(); je.hasMoreElements();) {
            JarEntry j = je.nextElement();
            if (!j.isDirectory()) {
                collectURL(f, s, new URL("jar", "", file.toURI() + "!/" + j.getName()));
            }
        }
    }

    private static void iterateEntry(File p, ResourceURLFilter f, Set<URL> s) throws MalformedURLException, IOException {
        if (p.isDirectory()) {
            iterateFileSystem(p, f, s);
        } else if (p.isFile() && p.getName().toLowerCase().endsWith(".jar")) {
            iterateJarFile(p, f, s);
        }
    }

    public static Set<URL> getResourceURLs(Class rootClass, ResourceURLFilter filter) throws IOException, URISyntaxException {
        Set<URL> collectedURLs = new HashSet<>();
        CodeSource src = rootClass.getProtectionDomain().getCodeSource();
        iterateEntry(new File(src.getLocation().toURI()), filter, collectedURLs);
        return collectedURLs;
    }

    public static Set<Path> walkClassLoaderResources(ClassLoader classLoader, String dir, int depth) throws URISyntaxException, IOException {
        URI uri = classLoader.getResource(dir).toURI();

        Path path;
        if (uri.getScheme().equals("jar")) {
            FileSystems.newFileSystem(uri, Collections.emptyMap());
            path = Paths.get(uri);
        } else {
            path = Paths.get(uri);
        }

        Set<Path> resources = new HashSet<>();
        Stream<Path> walk = Files.walk(path, depth);
        for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
            resources.add(it.next());
        }

        return resources;
    }

}
