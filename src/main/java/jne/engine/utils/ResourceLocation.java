package jne.engine.utils;

import java.io.File;

public class ResourceLocation implements ICore {

    public File file;
    public String path;

    public ResourceLocation(String path) {
        this.path = ENGINE.assetsDir.getAbsolutePath() + "/" + path;
    }

    public ResourceLocation(File dir, String path) {
        this.path = dir.getAbsolutePath() + "/" + path;
    }

    public String getName() {
        return getFile().getName();
    }

    public File getFile() {
        if (file == null) {
            file = new File(path);
        }
        return file;
    }

}
