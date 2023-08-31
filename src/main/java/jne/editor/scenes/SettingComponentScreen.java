package jne.editor.scenes;

import jne.editor.utils.constructors.AbstractComponentConstructor;
import jne.engine.constants.Direction;
import jne.engine.constants.EventPriority;
import jne.engine.constants.MouseClickType;
import jne.engine.debug.DebugManager;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.widgets.Component;
import jne.engine.screens.components.constructor.ComponentConstructorHelper;
import jne.engine.screens.components.constructor.MethodConstructor;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.screens.widgets.Button;
import jne.engine.screens.widgets.CheckBox;
import jne.engine.screens.widgets.Panel;
import jne.engine.screens.widgets.TextBox;
import jne.engine.texture.TextureContainer;
import jne.engine.api.IComponent;
import jne.engine.utils.Util;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static jne.engine.constants.Colors.darkBarColor;
import static jne.engine.constants.Colors.toolColor;

public class SettingComponentScreen extends ComponentsListener {

    public boolean init = false;
    protected final int Z_LEVEL = 300;
    protected final HashMap<MethodConstructor, Component> builderComponents = new HashMap<>();
    protected boolean errored = false;
    protected final ComponentConstructorHelper builderHelper;
    protected final Area area;
    protected final Area previewArea;

    public Component<? extends Component<?>> component;

    public SettingComponentScreen(ComponentConstructorHelper builderHelper, Area area) {
        this.builderHelper = builderHelper;
        this.area = area;
        this.previewArea = new Area(area.x2 - 465, area.y2 / 2, Z_LEVEL, 460, area.height / 2 - 5);
    }

    public void collect() {
        errored = false;
        Component.Builder builder = builderHelper.builder;
        Class<? extends Component.Builder> clazz = builder.getClass();

        boolean build = build(clazz, builder);

        if (build) {
            init = true;

            if (this.component != null) {
                remove(this.component);
            }

            this.component = (Component<? extends Component<?>>) builder.build();
            Area center = this.area.getCenter();
            this.component.setArea(previewArea);
            add(this.component);
        }
    }

    public boolean build(Class<? extends Component.Builder> clazz, Component.Builder<? extends Component.Builder<?, ?>, ? extends Component<?>> builder) {
        try {
            Set<Map.Entry<MethodConstructor, Component>> entries = builderComponents.entrySet();

            for (Map.Entry<MethodConstructor, Component> entry : entries) {
                Component value = entry.getValue();
                MethodConstructor key = entry.getKey();

                if (key.getTypes().length > 1) {
                    System.out.println(value.getClass().getName() + " does not know how to handle multiple arguments");
                    continue;
                }

                Method method = clazz.getMethod(key.getMethod().getName(), key.getTypes());
                method.setAccessible(true);
                if (value instanceof TextBox) {
                    String text = ((TextBox<?>) value).text;
                    try {
                        method.invoke(builder, Util.convert(text, key.getTypes()[0]));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Can't find the argument in the parameter " + key.getInfoText());
                    }
                } else if (value instanceof CheckBox) {
                    boolean flag = ((CheckBox) value).flag;
                    try {
                        method.invoke(builder, flag);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Checkbox was broken, very strange " + key.getInfoText());
                    }
                }
            }
        } catch (Exception e) {
            errored = true;
            DebugManager.error(e);
            return false;
        }

        return true;
    }

    @Override
    public void init() {
        isInit = true;
        add(GRAPHICS.button()
                .area(new Area(area.x2 - 55, area.y + 5, Z_LEVEL, 50, 50))
                .texture(TextureContainer.get("exit"))
                .color(toolColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        this.component = null;
                        kill();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .area(new Area(area.x2 - 155, area.y2 - 55, Z_LEVEL, 150, 50))
                .color(toolColor)
                .label(GRAPHICS.label().text("Preview").centered(true).build(), true)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        collect();
                    }
                })
                .build());


        add(GRAPHICS.button()
                .area(new Area(area.x2 - 310, area.y2 - 55, Z_LEVEL, 150, 50))
                .color(toolColor)
                .label(GRAPHICS.label().text("Push").centered(true).build(), true)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        collect();
                        if (!errored) {
                            kill();
                        }
                    }
                })
                .build());

        add(GRAPHICS.button()
                .area(new Area(area.x2 - 465, area.y2 - 55, Z_LEVEL, 150, 50))
                .color(toolColor)
                .label(GRAPHICS.label().text("Scripting").centered(true).build(), true)
                .onPress((component, type) -> {

                })
                .build());

        Area nullableArea = new Area(this.area.x, this.area.y, Z_LEVEL, 0, 0);

        Collection<MethodConstructor> values = builderHelper.methodHashMap.values();

        int textboxWidth = 250;
        int textboxHeight = 30;
        Area areaConstructor = nullableArea.offset(15, -textboxHeight, textboxWidth, textboxHeight);
        for (MethodConstructor constructor : values) {
            areaConstructor = areaConstructor.offset(-textboxWidth, textboxHeight);
            if (constructor.getComponent().equals(TextBox.class)) {
                TextBox<? extends TextBox<?>> textBox = GRAPHICS.textbox().area(areaConstructor).size(0.8F).ghostText(constructor.getGhostText()).build();
                add(textBox);

                AbstractComponentConstructor constructorOption = constructor.getOption();
                constructorOption.setDepth(Z_LEVEL);
                IComponent option = constructorOption.collect();

                if (option != null) {
                    Area finalAreaConstructor = areaConstructor.offset(35, 2.5F, 250, 250, Direction.RIGHT);
                    Button<? extends Button<?>> optionButton = GRAPHICS.button()
                            .area(areaConstructor.offset(5, 2.5F, 25, 25, Direction.RIGHT))
                            .texture(TextureContainer.get("plus"))
                            .onPress((component, type) -> {
                                if (type != MouseClickType.CLICKED) return;
                                if (getComponents().stream().anyMatch(iComponent -> iComponent.getID().equals(option.getID())))
                                    return;
                                option.setArea(finalAreaConstructor);
                                if (option instanceof Panel) {
                                    ((Panel<?>) option).setScrollHandler(null);
                                }
                                add(option);
                            })
                            .onFailPress((component, type) -> {
                                if (type != MouseClickType.CLICKED) return;
                                if (option.inFocus()) {
                                    if (option instanceof Panel) {
                                        IComponent scrollHandler = ((Panel<?>) option).getScrollHandler();
                                        if (scrollHandler != null) {
                                            textBox.text = (String) scrollHandler.getAnswer();
                                            textBox.cursorPositionX = textBox.text.length();
                                            remove(option);
                                            collect();
                                        }
                                    }
                                    return;
                                }
                                remove(option);
                            })
                            .build();
                    add(optionButton);
                }

                builderComponents.put(constructor, textBox);
            } else if (constructor.getComponent().equals(CheckBox.class)) {
                CheckBox<? extends CheckBox<?>> checkBox = GRAPHICS.checkbox().area(areaConstructor.offset(-textboxWidth, -textboxHeight, textboxHeight, textboxHeight)).build();
                add(checkBox);

                builderComponents.put(constructor, checkBox);
            }
            String infoText = constructor.getInfoText();
            add(GRAPHICS.label().area(areaConstructor.offset(-textboxWidth, -textboxHeight - FONT.getHeight(infoText))).size(0.5F).text(constructor.getInfoText()).build());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void render(ScreenEvent.Render event) {
        float partialTick = event.getPartialTick();

        RENDER.color(new Color(0f, 0f, 0f, 0.5F), () -> {
            RENDER.drawQuad(0, 0, Z_LEVEL, width, height);
        });

        RENDER.color(darkBarColor, () -> {
            RENDER.drawQuad(area.x, area.y, Z_LEVEL, area.x2, area.y2);
        });

        RENDER.color(darkBarColor.brighter(), () -> {
            RENDER.drawQuad(previewArea.x, previewArea.y, Z_LEVEL, previewArea.x2, previewArea.y2);
        });

        if (component == null) {
            Area previewAreaCenter = previewArea.getCenter();
            RENDER.color(darkBarColor, () -> {
                RENDER.drawTexturedQuad(previewAreaCenter.x - 100, previewAreaCenter.y - 100, previewArea.z, previewAreaCenter.x + 100, previewAreaCenter.y + 100, TextureContainer.get("question"));
            });
        }

        render(partialTick);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, exclusion = {SceneUnit.class, FrameStorage.class, AddComponentScreen.class})
    public void move(ScreenEvent.MouseMove event) {
        this.mouseMove(event.getMouseX(), event.getMouseY());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, exclusion = {SceneUnit.class, FrameStorage.class, AddComponentScreen.class})
    public void clickMove(ScreenEvent.MouseClickMove event) {
        this.mouseClickMove(event.getMouseX(), event.getMouseY(), event.getButton(), event.getLast());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, exclusion = {SceneUnit.class, FrameStorage.class, AddComponentScreen.class})
    public void input(ScreenEvent.MouseInput event) {
        MouseClickType type = event.getType();
        if (type == MouseClickType.CLICKED) {
            this.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
        }
        if (type == MouseClickType.RELEASED) {
            this.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton());
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, exclusion = {SceneUnit.class, FrameStorage.class, AddComponentScreen.class})
    public void keyboard(ScreenEvent.Keyboard event) {
        this.keyTyped(event.getCharacter(), event.getButton(), event.getType());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, exclusion = {SceneUnit.class, FrameStorage.class})
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
    }

}