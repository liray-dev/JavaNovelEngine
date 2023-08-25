package jne.engine.core;

import jne.engine.screens.ScreenManager;
import jne.engine.api.ICore;
import jne.engine.utils.Util;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Window implements ICore {

    public final String TITLE = "JNE";
    public final ScreenManager screenManager = new ScreenManager();

    public DisplayMode desktop;

    public boolean fullscreen;
    public boolean resizable = true;
    public boolean vSync = false;

    public int displayWidth;
    public int displayHeight;

    public float scaleWidth;
    public float scaleHeight;

    /**
     * A method to set the icon for the main window of the engine
     */
    public void setIcon() throws LWJGLException {

    }

    /**
     * A method for initializing window resolution under certain circumstances.
     * It is not recommended to over-call this method!
     */
    public void initDisplayMode() throws LWJGLException {

        desktop = Display.getDesktopDisplayMode();

        if (displayWidth == 0 || displayHeight == 0) {
            displayWidth = desktop.getWidth() / 2;
            displayHeight = desktop.getHeight() / 2;
        }

        if (fullscreen) {
            Display.setFullscreen(true);
            fullScreenDisplayMode();
        } else {
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
        }
        setDisplayResolution(displayWidth, displayHeight);
    }

    /**
     * Method for creating the main window of the engine
     * It is not recommended to over-call this method!
     */
    public void createDisplay() throws LWJGLException {
        Display.setTitle(TITLE);
        Display.setResizable(resizable);

        if (fullscreen) {
            this.fullScreenDisplayMode();
        }

        Display.create();
    }

    /**
     * Update window method
     */
    public void updateDisplay() throws LWJGLException {
        Display.update();
        this.checkWindowResize();
    }

    /**
     * The method that sets the full screen resolution
     */
    public void fullScreenDisplayMode() throws LWJGLException {
        DisplayMode displaymode = Display.getDesktopDisplayMode();
        Display.setDisplayMode(displaymode);
        displayWidth = displaymode.getWidth();
        displayHeight = displaymode.getHeight();
    }

    /**
     * Method to change full screen mode
     */
    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;

            if (this.fullscreen) {
                WINDOW.fullScreenDisplayMode();
                this.setDisplayResolution(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
            } else {
                DisplayMode displaymode = Display.getDesktopDisplayMode();
                this.setDisplayResolution(displaymode.getWidth() / 2, displaymode.getHeight() / 2);
                Display.setDisplayMode(new DisplayMode(WINDOW.displayWidth, WINDOW.displayHeight));
            }
            Display.setFullscreen(this.fullscreen);
            if (!this.fullscreen && (Util.getOSType() == Util.EnumOS.WINDOWS)) // Screen is not resizeable after exiting fullscreen due to LWJGL bug https://github.com/LWJGL/lwjgl/issues/142 which is fixed, but not in the version MC ships
            {
                Display.setResizable(resizable);
                Display.setResizable(!resizable);
            }
            Display.setVSyncEnabled(vSync);
            this.updateDisplay();
        } catch (Exception exception) {
            System.out.println("Couldn't toggle fullscreen " + exception);
        }
    }

    /**
     * A method for checking if the resolution of the current window needs to be changed
     */
    protected void checkWindowResize() throws LWJGLException {
        if (!this.fullscreen && Display.wasResized()) {
            this.setDisplayResolution(Display.getWidth(), Display.getHeight());
        }
    }

    /**
     * Method for setting the current window resolution
     */
    public void setDisplayResolution(int displayWidth, int displayHeight) throws LWJGLException {
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.screenManager.resize(displayWidth, displayHeight);
        if (Display.isCreated()) {
            this.changeOrtho();
        }
    }

    private int calculateScale(int guiScale, boolean forceUnicodeFont) {
        int i;
        for(i = 1; i != guiScale && i < this.displayWidth && i < this.displayHeight && this.displayWidth / (i + 1) >= 320 && this.displayHeight / (i + 1) >= 240; ++i) {}
        if (forceUnicodeFont && i % 2 != 0) {
            ++i;
        }

        return i;
    }

    private void calculateScale() {
        DisplayMode desktop = Display.getDesktopDisplayMode();
        this.scaleWidth = (float) desktop.getWidth() / displayWidth;
        this.scaleHeight = (float) desktop.getHeight() / displayHeight;
    }

    /**
     * The projection matrix setting method for the correct coordinate system, namely top left. as well as the Z coordinate threshold
     */
    public void changeOrtho() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, displayWidth, displayHeight, 0, -10000, 10000);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Method for setting the title name for the current window
     */
    public void setTitle(String title) {
        Display.setTitle(title);
    }

    private final static Window INSTANCE = new Window();

    public static Window getInstance() {
        return INSTANCE;
    }

}
