package jne.engine.scripts;

import jne.engine.errors.DebugManager;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

public class ScriptController {

    public static ScriptController instance;
    public ScriptEngineManager manager;

    public Map<String, String> languages = new HashMap<>();
    public Map<String, ScriptEngineFactory> factories = new HashMap<>();

    public ScriptController() {
        ScriptController.instance = this;
        System.setProperty("nashorn.args", "-strict");

        this.manager = new ScriptEngineManager();
        try {
            if (this.manager.getEngineByName("ecmascript") == null) {
                Class c = Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory");
                ScriptEngineFactory factory = (ScriptEngineFactory) c.newInstance();
                factory.getScriptEngine();
                this.manager.registerEngineName("ecmascript", factory);
                this.manager.registerEngineExtension("js", factory);
                this.manager.registerEngineMimeType("application/ecmascript", factory);
                this.languages.put(factory.getLanguageName(), ".js");
                this.factories.put(factory.getLanguageName().toLowerCase(), factory);
            } else {
                ScriptEngine ecmascript = manager.getEngineByName("ecmascript");
                ScriptEngineFactory factory = ecmascript.getFactory();
                this.languages.put(factory.getLanguageName(), ".js");
                this.factories.put(factory.getLanguageName().toLowerCase(), factory);
            }
        } catch (Exception e) {
            DebugManager.error(e);
        }
    }

    public ScriptEngine getEngineByName(String language) {
        ScriptEngineFactory fac = this.factories.get(language.toLowerCase());
        if (fac == null) {
            return null;
        }
        return fac.getScriptEngine();
    }

}
