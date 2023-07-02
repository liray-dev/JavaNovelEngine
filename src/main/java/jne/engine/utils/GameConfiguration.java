package jne.engine.utils;

import java.io.File;

public class GameConfiguration {

    public DisplayInformation displayInfo;
    public FolderInformation folderInfo;

    public GameConfiguration(DisplayInformation displayInfo, FolderInformation folderInfo) {
        this.displayInfo = displayInfo;
        this.folderInfo = folderInfo;
    }

    public static class DisplayInformation
    {
        public final int width;
        public final int height;
        public final boolean fullscreen;
        public final boolean checkGlErrors;

        public DisplayInformation(int widthIn, int heightIn, boolean fullscreenIn, boolean checkGlErrorsIn)
        {
            this.width = widthIn;
            this.height = heightIn;
            this.fullscreen = fullscreenIn;
            this.checkGlErrors = checkGlErrorsIn;
        }
    }

    public static class FolderInformation
    {
        public final File launcherDir;
        public final File assetsDir;

        public FolderInformation(File launcherDirIn, File assetsDirIn)
        {
            this.launcherDir = launcherDirIn;
            this.assetsDir = assetsDirIn;
        }
    }

}
