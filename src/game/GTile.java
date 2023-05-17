package game;

import acm.graphics.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GTile extends GCompound {
    static ArrayList<TileCode> code = new ArrayList<>();
    public static ArrayList<Boolean> upgraded = new ArrayList<>();
    boolean isUpgraded;
    boolean isSynergy = false;
    public static void resetUpgrades(){
        upgraded = new ArrayList<>();
        try {
            Scanner s = new Scanner(GlobalVariables.f);
            s.nextLine();
            String up =s.nextLine();
            upgraded.add(false);
            for (int i = 1; i <= up.length(); i++) {
                upgraded.add(up.charAt(i-1) == '2');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    static{
        //0
        code.add(new TileCode() {
            @Override
            public String description() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }

            @Override
            public String secondaryName() {
                return null;
            }

            @Override
            public String secondaryDescription() {
                return null;
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return 0;
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return 192;
            }

            @Override
            public void fire(GTile tile) {
                if (tile.isUpgraded){
                    for (int i = 7; i < 15; i++) {
                        tile.ticksLeft = tile.getAttackSpeed();
                        tile.projectiles.add(new GProjectile(tile.diceAmount, i, tile));
                    }
                }
                else {
                    for (int i = 0; i < 6; i++) {
                        tile.ticksLeft = tile.getAttackSpeed();
                        tile.projectiles.add(new GProjectile(tile.diceAmount, i, tile));
                    }
                }
            }

            @Override
            public String description() {
                return "Shoots highly piercing projectiles in six directions";
            }

            @Override
            public String name() {
                return "Green dice";
            }

            @Override
            public String secondaryName() {
                return "Burst";
            }

            @Override
            public String secondaryDescription() {
                return "Shoots in 8 directions";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+1;
            }

            @Override
            public String description() {
                return "Burns enemies on fire.";
            }

            @Override
            public String name() {
                return "Fire dice";
            }

            @Override
            public String secondaryName() {
                return "Intense fire";
            }

            @Override
            public String secondaryDescription() {
                return "More damage, but less burn time";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+1;
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return (int) (192 * (tile.isUpgraded ? 1.5 : 1));
            }

            @Override
            public String description() {
                return "Shoots exploding projectiles, dealing damage in an area.";
            }

            @Override
            public String name() {
                return "Water dice";
            }

            @Override
            public String secondaryName() {
                return "Recursive";
            }

            @Override
            public String secondaryDescription() {
                return "Explosions shoot out more exploding projectiles";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+2;
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return (int) (192 * (tile.isUpgraded ? 0.5 : 1));
            }

            @Override
            public String description() {
                return "Shoots chaining lighting.";
            }

            @Override
            public String name() {
                return "Lightning dice";
            }

            @Override
            public String secondaryName() {
                return "Lightning storm";
            }

            @Override
            public String secondaryDescription() {
                return "Shoots faster, but has less chain length";
            }
        });
        //5
        code.add(new TileCode() {
            @Override
            public String description() {
                return "Freezes enemies and makes them more vulnerable";
            }

            @Override
            public String name() {
                return "Ice Dice";
            }

            @Override
            public String secondaryName() {
                return "Cold freeze";
            }

            @Override
            public String secondaryDescription() {
                return "Enemies become more vunrable, but thaw faster";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return 0;
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                if (tile.isUpgraded) {
                    return 7;
                }
                else{
                    return 3;
                }
            }

            @Override
            public void tick(GTile tile) {

            }

            @Override
            public void turnPassed(GTile tile) {
                tile.ticksLeft--;
                if (tile.ticksLeft == 0){
                    tile.setEmpty();
                    tile.setDice(GlobalVariables.v.get(GlobalVariables.randomInt(0,GlobalVariables.v.size()-1)),tile.diceAmount + (tile.isUpgraded ? 2:1));
                    GlobalVariables.diceGrown(tile);
                }
            }

            @Override
            public String description() {
                return "Grows in 3 turns";
            }

            @Override
            public String name() {
                return "Growth dice";
            }

            @Override
            public String secondaryName() {
                return "Mega growth";
            }

            @Override
            public String secondaryDescription() {
                return "Grows twice in 7 turns";
            }
        });
        code.add(new TileCode() {
            @Override
            public void tick(GTile tile) {
                TileCode.super.tick(tile);
                for (GProjectile p: tile.projectiles) {
                    p.tick();
                }
            }
            @Override
            public void fire(GTile tile) {
                if (tile.isUpgraded){
                    if (tile.projectiles.size() < 2) {
                        tile.projectiles.add(new GProjectile(tile.diceAmount, tile, false));
                    }
                    else if (tile.projectiles.size() < 4 && tile.diceAmount > 4) {
                        tile.projectiles.add(new GProjectile(tile.diceAmount, tile, true));
                    }
                }
                else {
                    if (tile.projectiles.size() < 1) {
                        tile.projectiles.add(new GProjectile(tile.diceAmount, tile, false));
                    }
                    if (tile.projectiles.size() < 2 && tile.diceAmount > 4) {
                        tile.projectiles.add(new GProjectile(tile.diceAmount, tile, true));
                    }
                }
            }

            @Override
            public String description() {
                return "A projectile rotates around it.";
            }

            @Override
            public String name() {
                return "Atom dice";
            }

            @Override
            public String secondaryName() {
                return "Dual electron";
            }

            @Override
            public String secondaryDescription() {
                return "Two atoms that deal less damage rotate around it";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+2;
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return 192 * (tile.isUpgraded ? 2 : 1);
            }

            @Override
            public void fire(GTile tile) {
                int number = tile.isUpgraded ? 100000 : tile.diceAmount;
                for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
                    if (tile.calculateDistance(GlobalVariables.enemies.get(i).getX(), GlobalVariables.enemies.get(i).getY()) < (tile.getRange()+0.5) * 50) {
                        number--;
                        tile.projectiles.add(new GProjectile(tile.diceType, tile.diceAmount, GlobalVariables.enemies.get(i), tile));
                        if (number < 1){
                            break;
                        }
                    }
                }
                if (number < tile.diceAmount || tile.isUpgraded) {
                    tile.ticksLeft = tile.getAttackSpeed();
                }
            }

            @Override
            public String description() {
                return "Shoots multiple projectiles.";
            }

            @Override
            public String name() {
                return "Multi-projectile Dice";
            }

            @Override
            public String secondaryName() {
                return "Projectile storm";
            }

            @Override
            public String secondaryDescription() {
                return "Can target infinite enemies, but reduces other stats";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile) + (tile.isUpgraded ? 1 : 0);
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return 6 * (tile.isUpgraded ? 3 : 1);
            }

            @Override
            public void fire(GTile tile) {
                tile.nextTo = tile.getNextTo();
                for (GTile t: tile.getNextTo()) {
                    if (t.dice && t.diceType != 9){
                        t.tick();
                    }
                }
                tile.ticksLeft = tile.getAttackSpeed();
            }

            @Override
            public String description() {
                return "Increases attack speed of nearby towers.";
            }

            @Override
            public String name() {
                return "Buff dice";
            }

            @Override
            public String secondaryName() {
                return "Long range";
            }

            @Override
            public String secondaryDescription() {
                return "Increases range and reduces buff amount";
            }
        });
        //10
        code.add(new TileCode() {
            @Override
            public int getAttackSpeed(GTile tile) {
                return TileCode.super.getAttackSpeed(tile) * (tile.isUpgraded ? 16 : 1);
            }

            @Override
            public int getRange(GTile tile) {
                return tile.isUpgraded ? 0:TileCode.super.getRange(tile)+1;
            }

            @Override
            public void fire(GTile tile) {
                if (!tile.isUpgraded) {
                    tile.nextTo = tile.getPathInRange();
                    if (tile.nextTo.size() > 0) {
                        tile.projectiles.add(new GProjectile(tile.diceAmount, tile.nextTo.get(GlobalVariables.randomInt(0, tile.nextTo.size() - 1)), tile));
                        tile.ticksLeft = tile.getAttackSpeed();
                    }
                }
                else{
                    for (GTile path: GlobalVariables.path) {
                        tile.projectiles.add(new GProjectile(tile.diceAmount,path,tile));
                    }
                    tile.ticksLeft = tile.getAttackSpeed();
                }
            }

            @Override
            public String description() {
                return "Shoots projectiles on to the track";
            }

            @Override
            public String name() {
                return "Trapper dice";
            }

            @Override
            public String secondaryName() {
                return "Track coverer";
            }

            @Override
            public String secondaryDescription() {
                return "Occasionally covers the track instead of shooting normally";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+(tile.isUpgraded ? 1:2);
            }

            @Override
            public void tick(GTile tile) {
            }

            @Override
            public String description() {
                return "Summons zombies from dead apples";
            }

            @Override
            public String name() {
                return "Zombie dice";
            }

            @Override
            public String secondaryName() {
                return "Full necromancy";
            }

            @Override
            public String secondaryDescription() {
                return "Stronger zombies, but less range";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+1;
            }

            @Override
            public void fire(GTile tile) {
                int number = -1;
                for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
                    if (tile.calculateDistance(GlobalVariables.enemies.get(i).getX(), GlobalVariables.enemies.get(i).getY()) < (tile.getRange()+0.5) * 50 && !GlobalVariables.enemies.get(i).bombed) {
                        number = i;
                        break;
                    }
                }
                if (number > -1) {
                    tile.ticksLeft = tile.getAttackSpeed();
                    tile.projectiles.add(new GProjectile(tile.diceType, tile.diceAmount, GlobalVariables.enemies.get(number), tile));
                }
            }

            @Override
            public String description() {
                return "Bombs enemies so they explode when they die";
            }

            @Override
            public String name() {
                return "Bomb dice";
            }

            @Override
            public String secondaryName() {
                return "Timed bombs";
            }

            @Override
            public String secondaryDescription() {
                return "Bombs explode after a certain duration";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getAttackSpeed(GTile tile) {
                return 24 * (tile.isUpgraded ? 2 : 1);
            }

            @Override
            public String description() {
                return "Shoots really fast";
            }

            @Override
            public String name() {
                return "Flamethrower dice";
            }

            @Override
            public String secondaryName() {
                return "Firey fire";
            }

            @Override
            public String secondaryDescription() {
                return "Now does fire damage, but shoots slower";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+2;
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return 192;
            }

            @Override
            public String description() {
                return "Anti-pierces enemies with high damaging bullets";
            }

            @Override
            public String name() {
                return "Antipierce dice";
            }

            @Override
            public String secondaryName() {
                return "Antidamage";
            }

            @Override
            public String secondaryDescription() {
                return "Doesn't deal damage, but has more pierce";
            }
        });
        //15
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+1;
            }

            @Override
            public String description() {
                return "Amplifies bomb damage, fire damage, and ice vulnerability.";
            }

            @Override
            public String name() {
                return "Amplifier dice";
            }

            @Override
            public String secondaryName() {
                return "Reverse effects";
            }

            @Override
            public String secondaryDescription() {
                return "Instead of increasing fire damage and ice vulnerability, increases duration";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                if (tile.getThings()[2]) {
                    return TileCode.super.getRange(tile)+(tile.isUpgraded ? 3 : 2);
                }
                else{
                    return TileCode.super.getRange(tile)+1;
                }
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                if (tile.getThings()[0]&&tile.getThings()[3]){
                    return 48 / (tile.isUpgraded ? 4 : 1);
                }
                else if (tile.getThings()[0]||tile.getThings()[3]){
                    return 96 / (tile.isUpgraded ? 2 : 1);
                }
                return 192;
            }

            @Override
            public String description() {
                return "Gets stronger for each adjacent rainbow dice";
            }

            @Override
            public String name() {
                return "Rainbow dice";
            }

            @Override
            public String secondaryName() {
                return "Color creation";
            }

            @Override
            public String secondaryDescription() {
                return "Stronger buffs, for a maximum of 3";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getAttackSpeed(GTile tile) {
                if (tile.phase > 1){
                    return tile.isUpgraded ? 36 : 12;
                }
                else{
                    return 96;
                }
            }
            @Override
            public int getRange(GTile tile){
                return TileCode.super.getRange(tile)+1;
            }
            @Override
            public void tick(GTile tile) {
                if (tile.isUpgraded){
                    if (GlobalVariables.ticksTaken%150 < 50){
                        if (tile.phase != 3) {
                            tile.phase = 3;
                            tile.configureDice();
                            tile.ticksLeft = tile.getAttackSpeed();
                        }
                    }
                    else{
                        if (tile.phase != 2){
                            tile.phase = 2;
                            tile.configureDice();
                            tile.ticksLeft = tile.getAttackSpeed();
                        }
                    }
                }
                else {
                    if (GlobalVariables.ticksTaken % 350 < 200) {
                        if (tile.phase != 1) {
                            tile.phase = 1;
                            tile.configureDice();
                            tile.ticksLeft = tile.getAttackSpeed();
                        }
                    } else if (GlobalVariables.ticksTaken % 350 < 300) {
                        if (tile.phase != 2) {
                            tile.phase = 2;
                            tile.configureDice();
                            tile.ticksLeft = tile.getAttackSpeed();
                        }
                    } else {
                        if (tile.phase != 3) {
                            tile.phase = 3;
                            tile.configureDice();
                            tile.ticksLeft = tile.getAttackSpeed();
                        }
                    }
                }
                TileCode.super.tick(tile);
            }

            @Override
            public void fire(GTile tile) {
                if (tile.phase == 3){
                    int number = -1;
                    for (int i = 0; i < GlobalVariables.targets.size(); i++) {
                        if (tile.calculateDistance(GlobalVariables.targets.get(i).getX(), GlobalVariables.targets.get(i).getY()) < (tile.getRange()+0.5) * 50) {
                            number = i;
                            break;
                        }
                    }
                    if (number > -1) {
                        tile.ticksLeft = tile.getAttackSpeed();
                        tile.projectiles.add(new GProjectile(tile.diceAmount, tile,GlobalVariables.targets.get(number)));
                    }
                }
                else {
                    TileCode.super.fire(tile);
                }
            }

            @Override
            public String description() {
                return "Shoots really fast for a short duration";
            }

            @Override
            public String name() {
                return "Storm dice";
            }

            @Override
            public String secondaryName() {
                return "Permastorm";
            }

            @Override
            public String secondaryDescription() {
                return "Always activated, but reduced other stats";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return 0;
            }

            @Override
            public void tick(GTile tile) {

            }

            @Override
            public String description() {
                return "Randomizes the dice it merges with.";
            }

            @Override
            public String name() {
                return "Randomizer dice";
            }

            @Override
            public String secondaryName() {
                return "Increase";
            }

            @Override
            public String secondaryDescription() {
                return "No longer randomizes";
            }
        });
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return TileCode.super.getRange(tile)+4;
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return 192* (tile.isUpgraded ? 2:4);
            }

            @Override
            public String description() {
                return "One shots enemies";
            }

            @Override
            public String name() {
                return "Sniper dice";
            }

            @Override
            public String secondaryName() {
                return "Speedy sniper";
            }

            @Override
            public String secondaryDescription() {
                return "Shoots faster but doesn't one shot enemies";
            }
        });
        //20
        code.add(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return (tile.diceAmount * (tile.isUpgraded ? 2:1))+2;
            }

            @Override
            public void tick(GTile tile) {
                for (GTile t: tile.getNextToConnect()) {
                    boolean added = false;
                    for (GProjectile c: tile.projectiles) {
                        if ((((GConnectile) c).connector1 == t || ((GConnectile) c).connector2 == t)) {
                            added = true;
                            break;
                        }
                    }
                    if (!added){
                        tile.projectiles.add(new GConnectile(tile.diceAmount,tile,t));
                        t.projectiles.add(tile.projectiles.get(tile.projectiles.size()-1));
                    }
                }
                for (GProjectile p: tile.projectiles) {
                    p.tick();
                }
            }

            @Override
            public String description() {
                return "Connects with other Laser dice in range, creating a laser";
            }

            @Override
            public String name() {
                return "Laser dice";
            }

            @Override
            public String secondaryName() {
                return "Longer lasers";
            }

            @Override
            public String secondaryDescription() {
                return "Increased range, but decreased intensity";
            }
        });
        code.addAll(GlobalVariables.synergies);
    }
    public ArrayList<GProjectile> projectiles = new ArrayList<>();
    ArrayList<GTile> nextTo;
    public ArrayList<GObject> extras = new ArrayList<>();
    GOval range;
    GPolygon g;
    ArrayList<GOval> o =  new ArrayList<>();
    ArrayList<GPolygon> stuff = new ArrayList<>();
    public boolean path = false;
    public boolean dice = false;
    public int diceType;
    public int diceAmount;
    int size;
    public int x;
    public int y;
    public int ticksLeft;
    int phase = 1;
    int z;
    public GTile(int size, int x, int y){
        this.size = size;
        this.x = x;
        this.y = y;
        g = GlobalVariables.map.configureShape(x,y,size);
        add(g);
        g.setFillColor(new Color(0,125,0));
        g.setFilled(true);
        MouseListener m = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    GTile.this.mouseClicked();
                } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (range != null) {
                    range.setVisible(true);
                    sendToFront();
                    if (getCenter().getX() == 0 && getCenter().getY() == 0) {
                        range.setLocation(g.getWidth() / 2 - range.getWidth() / 2 + getX(), g.getHeight() / 2 - range.getHeight() / 2 + getY());
                    }
                    else {
                        range.setLocation(getTrueCenter().getX() - range.getWidth() / 2, getTrueCenter().getY() - range.getHeight() / 2);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (range != null) {
                    range.setVisible(false);
                    sendToBack();
                }
            }
        };
        this.addMouseListener(m);
    }
    public GTile(int lvl){
        diceType = GlobalVariables.v.get(GlobalVariables.randomInt(0,GlobalVariables.v.size()-1));
        diceAmount = lvl;
    }
    public GTile(int size, int x, int y, int diceType){
        this(size,x,y);
        setDice(diceType,1);
    }
    public void setPath(){
        path = true;
        if (GlobalVariables.pathFind(false)){
            g.setFillColor(Color.MAGENTA);
            range = null;
        }
        else{
            path = false;
        }
    }
    public GPoint getCenter(){
        return GlobalVariables.map.getCenter(x,y,size);
    }
    public GPoint getTrueCenter(){
        return new GPoint(getX() + getCenter().getX(), getY() + getCenter().getY());
    }
    public void setEmpty(){
        if (range != null) {
            GlobalVariables.screen.remove(range);
        }
        range = null;
        dice = false;
        path = false;
        diceType = 0;
        while (stuff.size() > 0){
            remove(stuff.get(0));
            stuff.remove(0);
        }
        while(o.size() > 0){
            remove(o.get(0));
            o.remove(0);
        }
        while (extras.size() > 0){
            remove(extras.get(0));
            extras.remove(0);
        }
        g.setFillColor(new Color(0,125,0));
        g.setColor(Color.black);
        add(g);
        while (projectiles.size() > 0){
            projectiles.get(0).remove();
        }
    }
    public int getAttackSpeed(){
        return code.get(diceType).getAttackSpeed(this);
    }
    public void setDice(int type, int amount){
        isSynergy = code.get(type) instanceof SynergyCode;
        isUpgraded = isSynergy ? false:upgraded.get(type);
        dice = true;
        diceType = type;
        diceAmount = amount;
        configureDice();
    }
    public void configureDice(){
        GlobalVariables.colors.get(diceType).configureDice(this);
    }
    public boolean[] getThings(){
        boolean[] f = new boolean[]{false,false,false,false,false,false};
        if (GlobalVariables.tiles[0][0] != null) {
            for (int i = 0; i < getAround().size(); i++) {
                if (getAround().get(i).diceType == diceType){
                    f[i] = true;
                }
            }
        }
        if (isUpgraded) {
            int total = 0;
            for (int i = 0; i < 6; i++) {
                if (f[i]){
                    total++;
                    if (total >3){
                        f[i] = false;
                        total--;
                    }
                }
            }
        }
        return f;
    }
    private void mouseClicked() throws CloneNotSupportedException   {
        if(GlobalVariables.starting){
            if (path){
                setEmpty();
            }
            else {
                setPath();
            }
        }
        else if (!(GlobalVariables.wave || GlobalVariables.enemiesLeft)){
            if (GlobalVariables.pickedUp == null && dice){
                GlobalVariables.pickedUp(this);
                GlobalVariables.pickedUp = (GTile)this.clone();
                setEmpty();
            }
            else if (GlobalVariables.pickedUp == null){
                /*nothing*/
            }
            else if (!dice && !path){
                setDice(GlobalVariables.pickedUp.diceType, GlobalVariables.pickedUp.diceAmount);
                if (GlobalVariables.tiles[0][0] != null) {
                    if (GlobalVariables.pathFind(false)) {
                        GlobalVariables.placed(this);
                        GlobalVariables.pickedUp = null;
                        if (diceType == 9) {
                            nextTo = getNextTo();
                        }
                    } else {
                        setEmpty();
                    }
                }
                else{
                    GlobalVariables.pickedUp = null;
                    if (diceType == 9) {
                        nextTo = getNextTo();
                    }
                }
            }
            else if (GlobalVariables.tiles[0][0] != null) {
                if (dice && GlobalVariables.pickedUp.diceType == diceType && GlobalVariables.pickedUp.diceAmount == diceAmount && diceAmount < GlobalVariables.levels.get(GlobalVariables.levelNumber).maxDiceLevel()) {
                    GTile starter = (GTile) this.clone();
                    diceAmount++;
                    int type = diceType;
                    setEmpty();
                    setDice(type, diceAmount);
                    GlobalVariables.merged(starter, GlobalVariables.pickedUp, this);
                    GlobalVariables.pickedUp = null;
                } else if (dice && GlobalVariables.pickedUp.diceType == 18 && GlobalVariables.pickedUp.diceAmount == diceAmount && !isSynergy) {
                    GTile starter = (GTile) this.clone();
                    diceAmount++;
                    int type = GlobalVariables.pickedUp.isUpgraded ? diceType : GlobalVariables.v.get(GlobalVariables.randomInt(0, GlobalVariables.v.size() - 1));
                    setEmpty();
                    setDice(type, diceAmount);
                    GlobalVariables.merged(starter, GlobalVariables.pickedUp, this);
                    GlobalVariables.pickedUp = null;
                } else if (dice && GlobalVariables.pickedUp.diceType != diceType && GlobalVariables.pickedUp.diceAmount + diceAmount > 10 && !isSynergy) {
                    //check if we have a synergy
                    SynergyCode ourSynergy = null;
                    for (SynergyCode synergy : GlobalVariables.synergies) {
                        if ((synergy.synergyNumbers()[0] == diceType || synergy.synergyNumbers()[1] == diceType) && (synergy.synergyNumbers()[0] == GlobalVariables.pickedUp.diceType || synergy.synergyNumbers()[1] == GlobalVariables.pickedUp.diceType)) {
                            ourSynergy = synergy;
                        }
                    }
                    if (ourSynergy != null) {
                        GlobalVariables.pickedUp = null;
                        setEmpty();
                        setDice(code.indexOf(ourSynergy), 6);
                    }
                }
            }
        }
    }
    public void tick(){
        code.get(diceType).tick(this);
        if(GlobalVariables.truePath.contains(this)){
            g.setFillColor(Color.orange);
        }
        else if (!path){
            g.setFillColor(new Color(0,125,0));
        }
        else{
            g.setFillColor(Color.magenta);
        }
    }
    public double calculateDistance(double x, double y){
        return Math.sqrt(((x-getTrueCenter().getX())*(x-getTrueCenter().getX()))+((y-getTrueCenter().getY())*(y-getTrueCenter().getY())));
    }
    public int getRange(){
        return code.get(diceType).getRange(this);
    }
    public void fire(){
        code.get(diceType).fire(this);
    }
    public void turnPassed(){
        code.get(diceType).turnPassed(this);
    }
    @Override
    public double getWidth(){
        return g.getWidth();
    }
    public double getHeight(){
        return g.getHeight();
    }
    ArrayList<GTile> getNextTo(){
        ArrayList<GTile> x = new ArrayList<>();
        for (GTile[] t : GlobalVariables.tiles) {
            for (GTile c : t) {
                if (c != null) {
                    if (calculateDistance(c.getX(), c.getY()) < (getRange()*50)+25 && c.dice) {
                        x.add(c);
                    }
                }
            }
        }
        return x;
    }
    private ArrayList<GTile> getNextToConnect(){
        ArrayList<GTile> x = new ArrayList<>();
        for (GTile[] t : GlobalVariables.tiles) {
            for (GTile c : t) {
                if (c != null) {
                    if (calculateDistance(c.getX(), c.getY()) < (getRange()*50)+25 && c.dice && c.diceType == 20 && c.diceAmount == diceAmount && c != this) {
                        x.add(c);
                    }
                }
            }
        }
        return x;
    }
    public ArrayList<GTile> getAround(){
        ArrayList<GTile> x = new ArrayList<>();
        for (GTile[] t : GlobalVariables.tiles) {
            for (GTile c : t) {
                if (c != null) {
                    if (GlobalVariables.isNextTo(this.x,y,c.x,c.y)) {
                        x.add(c);
                    }
                }
            }
        }
        return x;
    }
    ArrayList<GTile> getPathInRange(){
        ArrayList<GTile> x = new ArrayList<>();
        for (GTile[] t : GlobalVariables.tiles) {
            for (GTile c : t) {
                if (c != null) {
                    if (calculateDistance(c.getX(), c.getY()) < (getRange()*50)+25 && GlobalVariables.path.contains(c)) {
                        x.add(c);
                    }
                }
            }
        }
        return x;
    }
    public int zombieStuff(GApple p){
        if (calculateDistance(p.getX(), p.getY()) < (getRange()*50)+25) {
            return diceAmount*diceAmount*(isUpgraded ? 9:3);
        }
        return 0;
    }
    public ArrayList<GTile> pathFind(ArrayList<GTile> i, int flyMax, int flyTotal){
        i.add(this);
        if (path || dice){
            if (flyMax > flyTotal){
                flyTotal++;
            }
            else {
                return null;
            }
        }
        if (x == 19){
           return i;
        }
        else if (i.size() < z) {
            z = i.size();
            ArrayList<GTile> pathFind = null;
            for (GTile t : getAround()) {
                ArrayList<GTile> r = t.pathFind((ArrayList<GTile>) i.clone(),flyMax,flyTotal);
                if (pathFind != null) {
                    if (r != null) {
                        if (r.size() < pathFind.size()) {
                            pathFind = r;
                        }
                    }
                }
                else{
                    pathFind = r;
                }
            }
            return pathFind;
        }
        return null;
    }
}
