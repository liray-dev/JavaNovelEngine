package jne.editor.utils;

import jne.engine.screens.components.Component;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Frame {

    private final int id;
    private List<Component> components;

    public Frame(int id) {
        this.id = id;
        this.components = new ArrayList<>();
    }



}
