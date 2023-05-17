package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ModRegisterer {
    Mod m;
    public ModRegisterer(Class<? extends Mod> mod){
        Constructor constructor = null;
        try {
            constructor = mod.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            m = ((Mod) constructor.newInstance());
            m.run();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void tick(){
        m.tick();
    }
}
