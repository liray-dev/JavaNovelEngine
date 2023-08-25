package jne.engine.core;

import jne.engine.api.ICore;
import jne.engine.errors.DebugManager;
import jne.engine.events.types.TextureRegistryEvent;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.scripts.ScriptController;
import jne.engine.text.Font;
import jne.engine.texture.TextureContainer;
import jne.engine.utils.*;
import jne.engine.events.utils.Novel;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.List;

import static jne.engine.utils.Util.getSystemTime;

/**
 * The heart of the project, regulates all the vital processes of the engine. To create a launcher - create an instance of the class, specify the configuration and the first ScreenListener, and then call the run() method;
 */
public class JNE implements ICore {

    private final Timer timer = new Timer(20.0F, 0L);
    public final Settings settings = new Settings();
    private ScriptController scriptcontroller;
    public final File launcherDir;
    public final File assetsDir;
    public Font font;

    private volatile boolean running = false;
    private long debugUpdateTime = getSystemTime();
    private int fpsCounter;
    public int debugFPS;

    public JNE(GameConfiguration configuration, ComponentsListener initScreen) {
        instance = this;
        this.launcherDir = configuration.folderInfo.launcherDir;
        this.assetsDir = configuration.folderInfo.assetsDir;
        WINDOW.fullscreen = configuration.displayInfo.fullscreen;
        WINDOW.displayWidth = configuration.displayInfo.width;
        WINDOW.displayHeight = configuration.displayInfo.height;
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
        } catch (Exception e) {
            DebugManager.error(e);
        }

        WINDOW.changeOrtho();
        while (true) {
            try {
                while (this.running) {
                    this.loop();
                }
            } catch (Exception e) {
                DebugManager.error(e);
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
        Discord.launch();
        this.scriptcontroller = new ScriptController();
        this.font = new Font(Files.newInputStream(new ResourceLocation("PressStart2P-Regular.ttf").getFile().toPath()), 16, true, java.awt.Font.PLAIN);
        List<Class<?>> classes = ClassScanner.scanClassesWithAnnotation(Novel.class);

        for (Class<?> clazz : classes) {
            try {
                Constructor<?> declaredConstructors = clazz.getDeclaredConstructor();
                declaredConstructors.newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                System.out.println("The class does not have an empty constructor: " + clazz.getName());
            }
        }

        new TextureRegistryEvent(TextureContainer.getInstance()).post();


        WINDOW.screenManager.setScreen(WINDOW.screenManager.initScreen);
    }

    /**
     * a constant loop that manages all the vital processes of the engine
     */
    public void loop() throws LWJGLException {
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

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        WINDOW.screenManager.render(this.timer.partialTick);

        DebugManager.render();
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glDisable(GL11.GL_DEPTH_TEST);

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
