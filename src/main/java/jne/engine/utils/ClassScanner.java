package jne.engine.utils;

import jne.engine.errors.ErrorManager;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> scanClassesWithAnnotation(Class<?> annotationClass) {
        List<Class<?>> classes = new ArrayList<>();

        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        for (String classpathEntry : classpathEntries) {
            File classpathEntryFile = new File(classpathEntry);
            if (classpathEntryFile.isDirectory()) {
                scanClassesWithAnnotationInDirectory(classpathEntryFile, "", annotationClass, classes);
            } else {
                scanClassesWithAnnotationInJar(classpathEntryFile, annotationClass, classes);
            }
        }

        return classes;
    }

    private static void scanClassesWithAnnotationInDirectory(File directory, String packageName, Class<?> annotationClass, List<Class<?>> classes) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String subPackageName = packageName + "." + file.getName();
                    scanClassesWithAnnotationInDirectory(file, subPackageName, annotationClass, classes);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    className = className.replace('/', '.');
                    className = className.substring(1);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent((Class<? extends Annotation>) annotationClass)) {
                            classes.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        ErrorManager.error(e);
                    }
                }
            }
        }
    }

    private static void scanClassesWithAnnotationInJar(File jarFile, Class<?> annotationClass, List<Class<?>> classes) {
        try {
            Enumeration<URL> jarEntries = ClassLoader.getSystemClassLoader().getResources(jarFile.getName());
            while (jarEntries.hasMoreElements()) {
                URL jarEntryUrl = jarEntries.nextElement();
                try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
                    Enumeration<java.util.jar.JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        java.util.jar.JarEntry entry = entries.nextElement();
                        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                            String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                            try {
                                Class<?> clazz = Class.forName(className);
                                if (clazz.isAnnotationPresent((Class<? extends Annotation>) annotationClass)) {
                                    classes.add(clazz);
                                }
                            } catch (ClassNotFoundException e) {
                                ErrorManager.error(e);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            ErrorManager.error(e);
        }
    }
}
