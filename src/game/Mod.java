package game;

import acm.graphics.GLabel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import static acm.util.JTFTools.pause;

public abstract class Mod {
    static ArrayList<ModRegisterer> r = new ArrayList<>();
    public static void initMods() {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("mods".replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Set<Class> classes = reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line,"mods"))
                .collect(Collectors.toSet());
        ArrayList<String> done = new ArrayList<>();
        for (Class s: classes) {
            if (!s.getName().contains("$")){
                r.add(new ModRegisterer(s));
                done.add(s.getName());
            }
        }
        GlobalVariables.initSynergies();
    }

    public static void tickAll(){
        for (ModRegisterer m: r) {
            m.tick();
        }
    }
    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
    protected abstract void run();
    protected static int registerNewTower(TileCode tileCode, BulletCode bulletCode, DesignCode design){
        GlobalVariables.names.add(tileCode.name());
        GlobalVariables.colors.add(design);
        GTile.code.add(tileCode);
        GProjectile.code.add(bulletCode);
        return GlobalVariables.names.size();
    }
    protected static void registerLevel(LevelCode level){
        GlobalVariables.levels.add(level);
    }
    protected static void registerSynergy(SynergyCode synergy){
        GlobalVariables.synergies.add(synergy);
        GlobalVariables.colors.add(synergy);
    }
    protected static void startDetecting(EventListener listener){
        GlobalVariables.listeners.add(listener);
    }
    public abstract void tick();
}
