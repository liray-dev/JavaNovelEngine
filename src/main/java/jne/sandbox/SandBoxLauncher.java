package jne.sandbox;

import jne.engine.core.JNE;
import jne.engine.utils.GameConfiguration;
import jne.sandbox.sreens.MainScreen;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SandBoxLauncher {

    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        ArrayList<String> options = new ArrayList<>(Arrays.asList(args));

        boolean flag = options.contains("fullscreen");
        boolean flag1 = options.contains("checkGlErrors");
        GameConfiguration.DisplayInformation displayInformation = new GameConfiguration.DisplayInformation(0, 0, flag, flag1);

        GameConfiguration.FolderInformation folderInformation;
        if (!DEBUG) {
            File jarLauncher = new File(SandBoxLauncher.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String launcherFolderPath = jarLauncher.getParent();

            File launcherDir = new File(launcherFolderPath + "/launcher/SandBox");
            File assetsDir = new File(launcherFolderPath + "/launcher/SandBox/resources");

            if (!launcherDir.exists()) {
                launcherDir.mkdirs();
            }

            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
            }

            folderInformation = new GameConfiguration.FolderInformation(launcherDir, assetsDir);
        } else {
            File launcherDir = new File("./launcher/SandBox");
            File assetsDir = new File("./launcher/SandBox/resources");

            if (!launcherDir.exists()) {
                launcherDir.mkdirs();
            }

            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
            }

            folderInformation = new GameConfiguration.FolderInformation(new File("./launcher/SandBox"), new File("./launcher/SandBox/resources"));
        }

        GameConfiguration configuration = new GameConfiguration(displayInformation, folderInformation);

        (new JNE(configuration, new MainScreen())).run();
    }

}
