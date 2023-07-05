package jne.engine.constants;

public enum EnumScriptType {

    INIT("init"),
    TOOLTIP("tooltip"),
    UPDATE("update"),
    PRESS("press"),
    FAIL_PRESS("failPress");

    public String function;

    EnumScriptType(String function) {
        this.function = function;
    }
}

