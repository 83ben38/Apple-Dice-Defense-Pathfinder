package game;

import acm.graphics.*;

import java.awt.*;

public class GApple extends GMovable {
    public boolean pierce = true;
    boolean zombie = false;
    boolean bombed = false;
    int bombDmg;
    public double vunrablility = 1;
    int fireDmg = 0;
    int fire = 0;
    int frozen = 0;
    public int life;
    public int maxLife;
    int speed;
    int pathAmount = 0;
    int pathLeft = 50;
    boolean fly;
    boolean split;
    boolean camo;
    boolean reverse;
    int healing;
    int healingLeft = 100;
    boolean hit = false;
    int summonAmount;
    public GApple(int life, int speed, boolean fly, boolean split, boolean camo, boolean reverse, int healing, int summonAmount) {
        this.split = split;
        this.life=life;
        this.speed=speed;
        this.fly=fly;
        this.camo=camo;
        maxLife = life;
        this.reverse = reverse;
        this.healing = healing;
        this.summonAmount = summonAmount;
        configureImage();
        GlobalVariables.enemies.add(this);
        if (!camo){
            GlobalVariables.targets.add(this);
        }
    }
    public void configureImage(){
        removeAll();
        int width = split ? 25:40;
        if (zombie){
            GOval o = new GOval(width, 25);
            o.setFillColor(new Color(125,150,75));
            o.setFilled(true);
            GLine l = new GLine(0, 0, 0, -15);
            add(l);
            add(o, -width*0.5, -12.5);
        }
        else {
            int lifeDiv = life;
            double scale = 1;
            while (lifeDiv > 100) {
                scale *= 1.25;
                lifeDiv /= 100;
            }
            if (lifeDiv <= 10) {
                Color v;
                if (lifeDiv <= 1) {
                    v = Color.red;
                } else if (lifeDiv <= 2) {
                    v = Color.blue;
                } else if (lifeDiv <= 3) {
                    v = Color.green;
                } else if (lifeDiv <= 4) {
                    v = Color.yellow;
                } else if (lifeDiv <= 5) {
                    v = Color.magenta;
                } else if (lifeDiv <= 6) {
                    v = Color.black;
                } else if (lifeDiv <= 7) {
                    v = Color.white;
                } else if (lifeDiv <= 8) {
                    v = Color.orange;
                } else if (lifeDiv <= 9) {
                    v = new Color(95, 0, 255);
                } else {
                    v = Color.cyan;
                }
                if (camo){
                    v = new Color(v.getRed(),v.getGreen(),v.getBlue(),125);
                }
                GOval o = new GOval(width, 25);
                o.setFillColor(v);
                o.setFilled(true);
                GLine l = new GLine(0, 0, 0, -15);
                add(l);
                add(o, -width*0.5, -12.5);
            }
            else if (lifeDiv <= 30) {
                Color c = Color.white;
                Color v = Color.black;
                if (lifeDiv <= 20) {
                    c = Color.black;
                    v = Color.white;
                }
                if (camo){
                    v = new Color(v.getRed(),v.getGreen(),v.getBlue(),125);
                    c = new Color(c.getRed(),c.getGreen(),c.getBlue(),125);
                }
                GOval o = new GOval(width, 25);
                o.setFillColor(c);
                o.setFilled(true);
                GLine l = new GLine(0, 0, 0, -15);
                l.setColor(c);
                add(l);
                add(o, -width*0.5, -12.5);
                for (int i = 0; i < 4; i++) {
                    GLine r = new GLine(-15, i * 6.25 - 10, 15, i * 6.25 - 10);
                    r.setColor(v);
                    add(r);
                }
            }
            else if (lifeDiv <= 50) {
                GOval o = new GOval(width, 25);
                Color v = Color.gray;
                if (camo) {
                    v = new Color(v.getRed(), v.getGreen(), v.getBlue(), 125);
                }
                o.setFillColor(v);
                o.setFilled(true);
                GLine l = new GLine(0, 0, 0, -15);
                l.setColor(Color.gray);
                add(l);
                add(o, -width*0.5, -12.5);
                Color[] c = new Color[]{Color.red, Color.orange, Color.yellow, Color.green, Color.blue, new Color(95, 0, 255), Color.magenta};
                for (int i = 0; i < c.length; i++) {
                    GLine r = new GLine(-15, i * 3.125 - 10, 15, i * 3.125 - 10);
                    r.setColor(c[i]);
                    add(r);
                }
            }
            else {
                GOval o = new GOval(width, 25);
                Color v = Color.red;
                if (camo) {
                    v = new Color(v.getRed(), v.getGreen(), v.getBlue(), 125);
                }
                o.setFillColor(v);
                o.setFilled(true);
                add(o, -width*0.5, -12.5);
                GPolygon p = new GPolygon();
                p.addVertex(5, 0);
                p.addVertex(15, 0);
                p.addVertex(20, 10);
                p.addVertex(15, 20);
                p.addVertex(5, 20);
                p.addVertex(0, 10);
                add(p, p.getWidth() / -2, p.getHeight() / -2);
                Color[] c = new Color[]{Color.red, Color.yellow, Color.green, Color.blue, Color.cyan};
                for (int i = 0; i < c.length; i++) {
                    GOval g = new GOval(5, 5);
                    g.setFilled(true);
                    g.setFillColor(c[i]);
                    if (i == 2) {
                        add(g, g.getWidth() / -2, g.getHeight() / -2);
                    } else {
                        add(g, (i / 2) * 5 - g.getWidth(), (i % 2) * 5 - g.getHeight());
                    }
                }
            }
            if (frozen > 0) {
                GOval o = new GOval(width, 25);
                o.setFillColor(new Color(0, 255, 196, camo ? 46:92));
                o.setFilled(true);
                add(o, -width*0.5, -12.5);
            }
            if (fire > 0) {
                GOval o = new GOval(width, 25);
                o.setFillColor(new Color(255, 0, 0, camo ? 46:92));
                o.setFilled(true);
                add(o, -width*0.5, -12.5);
            }
            if (bombed){
                GOval o = new GOval(width, 25);
                o.setFillColor(new Color(0, 0, 0, camo ? 46:92));
                o.setFilled(true);
                add(o, -width*0.5, -12.5);
            }
            scale(scale);
            if (fly){
                GLine l = new GLine(-10, -15, 10, -15);
                add(l);
            }
            if (healing != 0){
                GOval o = new GOval(10,10);
                o.setFilled(true);
                o.setFillColor(healing > 0 ? Color.green : Color.red);
                add(o,-o.getWidth()/2,-o.getHeight()/2);
            }
            if (summonAmount > 0){
                GOval o = new GOval(10,10);
                o.setFilled(true);
                o.setFillColor(Color.yellow);
                add(o,-o.getWidth()/2,-o.getHeight()/2-15);
            }
        }
    }
    public void tick(){
        hit = false;
        configureImage();
        if (zombie){
            if (pathAmount == 0) {
                GlobalVariables.lives += life;
                remove();
                return;
            }
            GTile next = GlobalVariables.path.get(pathAmount - 1);
            moveTowards(next.getTrueCenter().getX() + next.getWidth() / 2, next.getTrueCenter().getY() + next.getHeight() / 2, speed);
            pathLeft -= speed;
            for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
                GApple enemy = GlobalVariables.enemies.get(i);
                if (Math.sqrt(Math.pow(enemy.getX()-getX(),2)+Math.pow(enemy.getY()-getY(),2)) < 25){
                    if (enemy.life > life){
                        enemy.life-=this.life;
                        remove();
                        break;
                    }
                    else{
                        this.life-= enemy.life;
                        enemy.life = 0;
                        enemy.remove();
                        i--;
                    }
                }
            }
            if (pathLeft < 1) {
                pathLeft = 50;
                pathAmount--;
                if (pathAmount == 0) {
                    GlobalVariables.lives += life;
                    remove();
                }
            }
        }
        else {
            if (!GlobalVariables.targets.contains(this) && !camo){
                GlobalVariables.targets.add(this);
            }
            if (frozen > 0) {
                frozen-= Math.sqrt((life/100)+1);

            } else {
                if (vunrablility > 1) {
                    vunrablility *= 0.995;
                }
                GTile next = GlobalVariables.path.get(reverse ? GlobalVariables.path.size() - 2 - pathAmount : pathAmount + 1);
                moveTowards(next.getX() + next.getWidth() / 2, next.getY() + next.getHeight() / 2, speed);
                pathLeft -= speed;
                healingLeft--;
                if (healingLeft < 1) {
                    healingLeft = 10;
                    if (life + healing > maxLife) {
                        life = maxLife;
                    }
                    else {
                        life += healing;
                    }
                }
                configureImage();
                if (pathLeft < 1) {
                    if (fire > 0) {
                        fire--;
                        life -= fireDmg * vunrablility;
                        if (fire < 1) {
                            fireDmg = 0;
                        }
                    }
                    if (life < 1) {
                        remove();
                    } else if (pathAmount == GlobalVariables.path.size() - 2) {
                        GlobalVariables.lives -= life;
                        remove();
                    }
                    else {
                        next = GlobalVariables.path.get(reverse ? GlobalVariables.path.size() - 2 - pathAmount : pathAmount + 1);
                        pathLeft = (int) next.calculateDistance(getX(), getY());
                        pathAmount++;
                    }
                    if (summonAmount > 0){
                        try {
                            GApple r = (GApple) clone();
                            r.move(GlobalVariables.randomInt(-10, 10), GlobalVariables.randomInt(-10, 10));
                            GlobalVariables.screen.add(r);
                            r.pathLeft = (int) GlobalVariables.path.get(reverse ? GlobalVariables.path.size() - 2 - pathAmount : pathAmount + 1).calculateDistance(getX(), getY());
                            GlobalVariables.enemies.add(r);
                            if (!r.camo) {
                                GlobalVariables.targets.add(r);
                            }
                            r.summonAmount = 0;
                            r.maxLife = summonAmount;
                            r.life = r.maxLife;
                            r.pierce = true;
                            r.hit = true;
                            r.configureImage();
                            sendToFront();
                        }
                        catch (Exception ignored){

                        }
                    }
                }
            }
        }
    }
    public void remove(){
        if (!zombie){
            GlobalVariables.enemies.remove(this);
            GlobalVariables.targets.remove(this);
            if (GlobalVariables.enemies.size() == 0){
                GlobalVariables.enemiesLeft = false;
                GlobalVariables.screen.remove(this);
                return;
            }
        }
        if (bombed){
            GlobalVariables.projectiles.add(new GProjectile(bombDmg,this));
        }
        if (zombie){
            GlobalVariables.zombies.remove(this);
        }
        int health = 0;
        if (!zombie){
            for (GTile[] someTiles: GlobalVariables.tiles) {
                for (GTile tile: someTiles) {
                    if (tile.dice && tile.diceType == 11){
                        if (tile.zombieStuff(this) > health){
                            health = tile.zombieStuff(this);
                        }
                    }
                }
            }
        }
        if (health != 0){
            zombie = true;
            GlobalVariables.zombies.add(this);
            life = health;
        }
        else{
            GlobalVariables.screen.remove(this);
        }
    }
    public void getBombed(int bombLvl){
        for (int i = 0; i < 200/bombLvl; i++) {
            while (!GlobalVariables.wave){
                pause(1);
            }
            pause(GlobalVariables.FF ? 10 : 25);
        }
        GlobalVariables.projectiles.add(new GProjectile(bombDmg,this));
        bombed = false;
    }
    public GApple damage(int amount){
        if (!hit) {
            life -= amount * vunrablility;
            if (life < 1) {
                remove();
            } else if (split && GlobalVariables.enemies.size() < 500) {
                try {
                    GApple r = (GApple) clone();
                    r.move(GlobalVariables.randomInt(-5, 5), GlobalVariables.randomInt(-5, 5));
                    GlobalVariables.screen.add(r);
                    r.pathLeft = (int) GlobalVariables.path.get(r.pathAmount + 2).calculateDistance(getX(), getY());
                    GlobalVariables.enemies.add(r);
                    if (!r.camo) {
                        GlobalVariables.targets.add(r);
                    }
                    r.pierce = true;
                    r.hit = true;
                    r.configureImage();
                    return r;
                } catch (Exception ignored) {

                }
            }
        }
            return null;

    }
    public GApple damage(double amount){
        return damage((int)amount);
    }
}
