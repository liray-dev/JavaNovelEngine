package jne.engine.utils;

import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.LongSupplier;

public class Util {

    public static <T> Object convert(String text, Class<T> targetType) {
        if (text.isEmpty()) return "";
        if (targetType == int.class) {
            return Integer.parseInt(text);
        } else if (targetType == float.class) {
            return Float.parseFloat(text);
        } else if (targetType == double.class) {
            return Double.parseDouble(text);
        } else if (targetType == boolean.class) {
            return Boolean.parseBoolean(text);
        } else if (targetType == String.class) {
            return text;
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targetType.getName());
        }
    }

    public static int offsetByCodepoints(String text, int cursorPos, int moveTo) {
        int i
                = text.length();
        if (moveTo >= 0) {
            for(int j = 0; cursorPos < i && j < moveTo; ++j) {
                if (Character.isHighSurrogate(text.charAt(cursorPos++)) && cursorPos < i && Character.isLowSurrogate(text.charAt(cursorPos))) {
                    ++cursorPos;
                }
            }
        } else {
            for(int k = moveTo; cursorPos > 0 && k < 0; ++k) {
                --cursorPos;
                if (Character.isLowSurrogate(text.charAt(cursorPos)) && cursorPos > 0 && Character.isHighSurrogate(text.charAt(cursorPos - 1))) {
                    --cursorPos;
                }
            }
        }

        return cursorPos;
    }

    public static String readText(File file) throws FileNotFoundException {
        return readText(new FileInputStream(file));
    }

    public static String readText(String path) {
        try {
            InputStream in = Util.class.getResourceAsStream(path);

            return readText(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + path);
        }
    }

    public static String readText(InputStream in) {
        Scanner scanner = new Scanner(new InputStreamReader(in, StandardCharsets.UTF_8));
        String result = scanner.useDelimiter("\\A").next();

        scanner.close();

        return result;
    }

    public static void writeText(File file, String string) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

        writer.write(string);
        writer.close();
    }

    public static List<String> readLines(String fileName) throws Exception {
        return readLines(Util.class.getClass().getResourceAsStream(fileName));
    }

    public static List<String> readLines(InputStream stream) throws Exception {
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;

            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }

        return list;
    }

    public static byte[] readBytes(InputStream stream) throws IOException {
        return readBytes(stream, 4 * 1024);
    }

    public static byte[] readBytes(InputStream stream, int bufferSize) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int bytesRead;
        byte[] data = new byte[bufferSize];

        while ((bytesRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    public static void deleteFolder(File folder) {
        if (!folder.isDirectory()) {
            return;
        }

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                deleteFolder(file);
            } else {
                file.delete();
            }
        }

        folder.delete();
    }

    public static File findNonExistingFile(File file) {
        String name = file.getName();
        int index = name.lastIndexOf('.');
        String baseName = name.substring(0, index);
        String extension = name.substring(index);

        int i = 1;

        while (file.exists()) {
            file = new File(file.getParentFile().getAbsolutePath(), baseName + "_" + i + extension);

            i += 1;
        }

        return file;
    }





    public static void openWebLink(URI url) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke((Object) null);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        } catch (Throwable throwable) {
            System.out.println("Couldn't open link: " + throwable.getCause().getMessage());
        }
    }

    public static Util.EnumOS getOSType() {
        String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (s.contains("win")) {
            return EnumOS.WINDOWS;
        } else if (s.contains("mac")) {
            return EnumOS.OSX;
        } else if (s.contains("solaris")) {
            return EnumOS.SOLARIS;
        } else if (s.contains("sunos")) {
            return EnumOS.SOLARIS;
        } else if (s.contains("linux")) {
            return EnumOS.LINUX;
        } else {
            return s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN;
        }
    }

    public static <V> V runTask(FutureTask<V> task, Logger logger) {
        try {
            task.run();
            return task.get();
        } catch (ExecutionException executionexception) {
            logger.fatal("Error executing task", (Throwable) executionexception);
        } catch (InterruptedException interruptedexception) {
            logger.fatal("Error executing task", (Throwable) interruptedexception);
        }

        return (V) null;
    }

    public static <T> T getLastElement(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static enum EnumOS {
        LINUX,
        SOLARIS,
        WINDOWS,
        OSX,
        UNKNOWN;
    }

    public static LongSupplier timeSource = System::nanoTime;

    public static long getMillis() {
        return getNanos() / 1000000L;
    }

    public static long getNanos() {
        return timeSource.getAsLong();
    }

    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

}
