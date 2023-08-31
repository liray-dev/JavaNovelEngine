package jne.engine.scripts;

import jne.engine.constants.EnumScriptType;
import jne.engine.debug.DebugManager;
import jne.engine.events.types.Event;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.function.Function;

public class ScriptContainer {

    public String fullscript;
    public String script;

    public boolean errored;
    private boolean init;

    private String currentScriptLanguage;
    private ScriptEngine engine;

    public TreeMap<Long, String> console;
    public final HashSet<String> unknownFunctions = new HashSet<>();

    public ScriptContainer() {
        this.fullscript = "";
        this.script = "";
        this.console = new TreeMap<>();
        this.errored = false;
        this.engine = ScriptController.instance.getEngineByName("ecmascript");
        this.init = false;
    }

    public void run(EnumScriptType type, Event event) {
        this.run(type.function, event);
    }

    public void run(String type, Object event) {
        if (!this.hasCode() || this.errored) {
            if (!console.isEmpty()) {
                System.out.println(console);
            }
            console.clear();
            this.errored = false;
            return;
        }
        if (this.engine == null) {
            return;
        }
        synchronized ("lock") {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            this.engine.getContext().setWriter(pw);
            this.engine.getContext().setErrorWriter(pw);
            try {
                if (!this.init) {
                    this.engine.eval(this.getFullCode());
                    this.init = true;
                }
                this.engine.eval(this.getFullCode());
                ((Invocable) this.engine).invokeFunction(type, event);

            } catch (NoSuchMethodException e2) {
                this.unknownFunctions.add(type);
            } catch (Exception e) {
                this.errored = true;
                DebugManager.error(e);
            } finally {
                this.appandConsole(sw.getBuffer().toString().trim());
                pw.close();
            }
        }
    }

    public void appandConsole(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        long time = System.currentTimeMillis();
        if (this.console.containsKey(time)) {
            message = this.console.get(time) + "\n" + message;
        }
        this.console.put(time, message);
        while (this.console.size() > 40) {
            this.console.remove(this.console.firstKey());
        }
    }

    private String getFullCode() {
        if (!this.init) {
            this.fullscript = this.script;
            if (!this.fullscript.isEmpty()) {
                this.fullscript += "\n";
            }
        }
        return this.script;
    }

    public boolean hasCode() {
        return !this.getFullCode().isEmpty();
    }

    public boolean isValid() {
        return this.init && !this.errored;
    }

    public void setEngine(String scriptLanguage) {
        if (this.currentScriptLanguage != null && this.currentScriptLanguage.equals(scriptLanguage)) {
            return;
        }

        this.engine = ScriptController.instance.getEngineByName(scriptLanguage);
        if (this.engine == null) {
            this.errored = true;
            return;
        }

        this.engine.put("dump", new Dump());
        this.engine.put("log", new Log());
        this.currentScriptLanguage = scriptLanguage;
        this.init = false;
    }

    private static class Dump implements Function<Object, String> {
        @Override
        public String apply(Object o) {
            if (o == null) {
                return "null";
            }
            StringBuilder builder = new StringBuilder();
            builder.append(o + ":" + System.getProperty("line.separator"));
            for (Field field : o.getClass().getFields()) {
                try {
                    builder.append(field.getName() + " - " + field.getType().getSimpleName() + ", ");
                } catch (IllegalArgumentException ex) {
                }
            }
            for (Method method : o.getClass().getMethods()) {
                try {
                    String s = method.getName() + "(";
                    for (Class c : method.getParameterTypes()) {
                        s = s + c.getSimpleName() + ", ";
                    }
                    if (s.endsWith(", ")) {
                        s = s.substring(0, s.length() - 2);
                    }
                    builder.append(s + "), ");
                } catch (IllegalArgumentException ex2) {
                }
            }
            return builder.toString();
        }
    }

    private class Log implements Function<Object, Void> {
        @Override
        public Void apply(Object o) {
            ScriptContainer.this.appandConsole(o + "");
            System.out.println(o + "");
            return null;
        }
    }

}
