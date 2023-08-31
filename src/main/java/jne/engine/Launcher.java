package jne.engine;

import jne.editor.nodes.NodeEditor;
import jne.engine.core.JNE;
import jne.engine.utils.GameConfiguration;
import jne.editor.scenes.SceneUnit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Launcher {

    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        ArrayList<String> options = new ArrayList<>(Arrays.asList(args));

        boolean flag = options.contains("fullscreen");
        boolean flag1 = options.contains("checkGlErrors");
        GameConfiguration.DisplayInformation displayInformation = new GameConfiguration.DisplayInformation(0, 0, flag, flag1);

        GameConfiguration.FolderInformation folderInformation;
        if (!DEBUG) {
            File jarLauncher = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String launcherFolderPath = jarLauncher.getParent();

            File launcherDir = new File(launcherFolderPath + "/launcher");
            File assetsDir = new File(launcherFolderPath + "/launcher/resources");

            if (!launcherDir.exists()) {
                launcherDir.mkdirs();
            }

            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
            }

            folderInformation = new GameConfiguration.FolderInformation(launcherDir, assetsDir);
        } else {
            File launcherDir = new File("./launcher");
            File assetsDir = new File("./launcher/resources");

            if (!launcherDir.exists()) {
                launcherDir.mkdirs();
            }

            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
            }

            folderInformation = new GameConfiguration.FolderInformation(launcherDir, assetsDir);
        }

        GameConfiguration configuration = new GameConfiguration(displayInformation, folderInformation);

        (new JNE(configuration, new NodeEditor())).run();
    }

}
