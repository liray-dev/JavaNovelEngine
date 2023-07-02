package jne.engine.core;

import jne.engine.events.Event;
import jne.engine.events.EventListenerHelper;
import jne.engine.events.test.CoolEvent;
import jne.engine.events.test.TestEvents;
import jne.engine.events.test.TestEvents2;
import jne.engine.screens.listeners.ScreenListener;
import jne.engine.text.Font;
import jne.engine.utils.*;
import jne.engine.events.Novel;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static jne.engine.utils.Util.getSystemTime;

/**
 * The heart of the project, regulates all the vital processes of the engine. To create a launcher - create an instance of the class, specify the configuration and the first ScreenListener, and then call the run() method;
 */
public class JNE implements ICore {

    private final Timer timer = new Timer(20.0F, 0L);
    public final Settings settings = new Settings();
    public final File launcherDir;
    public final File assetsDir;
    public Font font;

    private volatile boolean running = false;
    private long debugUpdateTime = getSystemTime();
    private int fpsCounter;
    public int debugFPS;

    public JNE(GameConfiguration configuration, ScreenListener initScreen) {
        instance = this;
        this.launcherDir = configuration.folderInfo.launcherDir;
        this.assetsDir = configuration.folderInfo.assetsDir;

        WINDOW.setDisplayResolution(configuration.displayInfo.width, configuration.displayInfo.height);
        WINDOW.fullscreen = configuration.displayInfo.fullscreen;
        WINDOW.screenManager.initScreen = initScreen;
    }

    /**
     * Engine start method, call it after the initialization of the JNE class of the engine
     */
    public void run() {
        if (running) {
            return;
        }

        this.running = true;

        try {
            this.init();
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
        }

        WINDOW.screenManager.setScreen(WINDOW.screenManager.initScreen);

        WINDOW.changeOrtho();
        while (true) {
            try {
                while (this.running) {
                    this.loop();
                }
            } catch (Throwable throwable) {
                System.out.println(throwable.getMessage());
            } finally {
                this.shutdown();
            }
            return;
        }
    }

    /**
     * Display initialization method, sets the icon and resolution and then creates it
     */
    private void init() throws LWJGLException, IOException, FontFormatException {
        WINDOW.setIcon();
        WINDOW.initDisplayMode();
        WINDOW.createDisplay();

        List<Class<?>> classes = ClassScanner.scanClassesWithAnnotation(Novel.class);

        for (Class<?> clazz : classes) {
            System.out.println("Found class with annotation: " + clazz.getName());
        }

        this.font = new Font(new FileInputStream(assetsDir.getAbsoluteFile() + "/PressStart2P-Regular.ttf"), 16, true, java.awt.Font.BOLD);
    }

    /**
     * a constant loop that manages all the vital processes of the engine
     */
    public void loop() {
        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }

        for (int j = 0; j < Math.min(10, timer.updateTimer(Util.getMillis())); ++j) {
            this.tick();
        }

        this.render();
        WINDOW.updateDisplay();
        ++this.fpsCounter;
        while (getSystemTime() >= this.debugUpdateTime + 1000L) {
            debugFPS = this.fpsCounter;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
        }

        Display.sync(this.settings.limitFramerate);
    }

    /**
     * Root rendering method that cleans the depth and color buffer, sets zero texture, includes color blending and a depth test
     */
    private void render() {
        GL11.glViewport(0, 0, WINDOW.displayWidth, WINDOW.displayHeight);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        WINDOW.screenManager.render(this.timer.partialTick);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glDisable(GL11.GL_BLEND);

    }

    /**
     * A method that handles quarter-millisecond updates, as well as triggers the reading of mouse and keyboard actions
     */
    private void tick() {
        if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
            WINDOW.toggleFullscreen();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
            this.shutdown();
        }

        WINDOW.screenManager.tick();
    }

    /**
     * Method of stopping the program under emergency circumstances
     */
    private void shutdown() {
        this.running = false;
        Display.destroy();
        System.exit(0);
    }


    private static JNE instance;

    public static JNE getInstance() {
        return instance;
    }

}
