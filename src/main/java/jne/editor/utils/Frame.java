package jne.editor.utils;

import jne.engine.utils.IComponent;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Frame {

    private final int id;
    private List<IComponent> components;

    public Frame(int id) {
        this.id = id;
        this.components = new ArrayList<>();
    }


}
