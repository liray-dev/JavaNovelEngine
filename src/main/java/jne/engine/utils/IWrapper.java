package jne.engine.utils;

import jne.engine.renderer.RenderHelper;
import jne.engine.renderer.Tessellator;
import jne.engine.renderer.buffer.BufferBuilder;
import jne.engine.renderer.buffer.BufferHelper;
import jne.engine.renderer.Graphics;
import jne.engine.text.Font;

public interface IWrapper extends ICore {

    Tessellator TESSELLATOR = Tessellator.getInstance();

    BufferBuilder BUILDER = TESSELLATOR.getBuffer();
    BufferHelper BUFFER_HELPER = TESSELLATOR.getHelper();

    RenderHelper RENDER = RenderHelper.getInstance();

    Font FONT = ENGINE.font;

    Graphics GRAPHICS = Graphics.getInstance();

}
