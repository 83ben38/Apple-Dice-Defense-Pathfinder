package game;

import acm.graphics.GLine;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GPoint;

import java.awt.*;
import java.util.ArrayList;

public class GProjectile extends GMovable {
    static ArrayList<BulletCode> code = new ArrayList<>();
    static {
        //0
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                bullet.setLocation(bullet.getX() - bullet.getWidth() * 0.02, bullet.getY() - bullet.getHeight() * 0.02);
                bullet.scale(1.04);
                for (GApple enemy : bullet.getTouching()) {
                    bullet.hit.add(enemy.damage(bullet.lvl));
                    bullet.hit.add(enemy);
                }
                if (bullet.getWidth() > 100) {
                    bullet.remove();
                }
            }

            @Override
            public int pierce(GProjectile bullet) {
                return 0;
            }

        });
        code.add(bullet -> {
            bullet.moveSteps(bullet.lvl);
            for (GApple enemy : bullet.getTouching()) {
                if (bullet.pierceLeft > 0) {
                    enemy.damage(bullet.lvl + (bullet.lvl / 2) + (bullet.lvl / 3) + (bullet.lvl / 5));
                }
                bullet.pierceStuff(enemy);
            }
        });
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                bullet.moveToTargetIfExists(bullet.lvl * 2);
                for (GApple enemy : bullet.getTouching()) {
                    if (bullet.pierceLeft > 0) {
                        if (bullet.lvl > 2) {
                            enemy.damage(Math.pow(bullet.lvl - 2, 2));
                        }
                        if (bullet.isUpgraded){
                            enemy.fire += bullet.lvl;
                            if (enemy.fireDmg < bullet.lvl*2) {
                                enemy.fireDmg = bullet.lvl*2;
                            }
                        }
                        else {
                            enemy.fire += bullet.lvl * Math.sqrt(bullet.lvl);
                            if (enemy.fireDmg < bullet.lvl) {
                                enemy.fireDmg = bullet.lvl;
                            }
                        }
                    }
                    bullet.pierceStuff(enemy);
                }
            }
            @Override
            public int pierce(GProjectile bullet){
                return bullet.lvl*2;
            }
        });
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                if (!bullet.out) {
                    if (bullet.pierceLeft > 0) {
                        bullet.moveTowards(bullet.target.getX(), bullet.target.getY(), bullet.lvl * 2);
                        for (GApple enemy : bullet.getTouching()) {
                            enemy.damage(bullet.lvl * ((bullet.lvl / 2) + 1));
                            bullet.pierceLeft = 0;
                            bullet.hit.add(enemy);

                        }
                        if (bullet.pierceLeft == 0 && bullet.isUpgraded) {
                            for (int direction = 0; direction < 6; direction++) {
                                GProjectile spawn = new GProjectile(bullet.type, bullet.lvl, bullet.target, bullet.owner);
                                spawn.setLocation(bullet.getLocation());
                                if (direction == 1) {
                                    spawn.xAmount = 0;
                                    spawn.yAmount = 1;
                                } else if (direction == 2) {
                                    spawn.yAmount = 0.4;
                                    spawn.xAmount = 0.6;
                                } else if (direction == 3) {
                                    spawn.yAmount = -0.4;
                                    spawn.xAmount = 0.6;
                                } else if (direction == 4) {
                                    spawn.yAmount = -1;
                                    spawn.xAmount = 0;
                                } else if (direction == 5) {
                                    spawn.yAmount = -0.4;
                                    spawn.xAmount = -0.6;
                                } else if (direction == 0) {
                                    spawn.xAmount = -0.6;
                                    spawn.yAmount = 0.4;
                                }
                                spawn.out = true;
                                spawn.pierceLeft = spawn.pierce();
                            }

                        }
                    }
                    else {
                        bullet.setLocation(bullet.getX() - bullet.getWidth() * 0.02, bullet.getY() - bullet.getHeight() * 0.02);
                        bullet.scale(1.04);
                        for (GApple enemy : bullet.getTouching()) {
                            if (bullet.isUpgraded) {
                                bullet.hit.add(enemy.damage(bullet.lvl));
                            } else {
                                bullet.hit.add(enemy.damage(bullet.lvl * ((bullet.lvl / 2) + 1)));
                            }
                            bullet.hit.add(enemy);
                        }
                        if (bullet.getWidth() > 24 + (25 * bullet.lvl)) {
                            bullet.remove();
                        }
                    }
                }
                else{

                    if (bullet.pierceLeft > 0){
                        bullet.moveSteps(bullet.lvl);
                        bullet.pierceLeft-=bullet.lvl;
                        if ((bullet.lvl == 6 || bullet.lvl == 5) && bullet.pierceLeft < 1){
                            for (int direction = 0; direction < 6; direction++) {
                                GProjectile spawn = new GProjectile(bullet.type, bullet.lvl-4, bullet.target, bullet.owner);
                                spawn.setLocation(bullet.getLocation());
                                if (direction == 1) {
                                    spawn.xAmount = 0;
                                    spawn.yAmount = 1;
                                } else if (direction == 2) {
                                    spawn.yAmount = 0.4;
                                    spawn.xAmount = 0.6;
                                } else if (direction == 3) {
                                    spawn.yAmount = -0.4;
                                    spawn.xAmount = 0.6;
                                } else if (direction == 4) {
                                    spawn.yAmount = -1;
                                    spawn.xAmount = 0;
                                } else if (direction == 5) {
                                    spawn.yAmount = -0.4;
                                    spawn.xAmount = -0.6;
                                } else {
                                    spawn.xAmount = -0.6;
                                    spawn.yAmount = 0.4;
                                }
                                spawn.out = true;
                                spawn.pierceLeft = spawn.pierce()/2;
                            }
                        }
                    }
                    else{
                        bullet.setLocation(bullet.getX() - bullet.getWidth() * 0.02, bullet.getY() - bullet.getHeight() * 0.02);
                        bullet.scale(1.04);
                        for (GApple enemy : bullet.getTouching()) {
                            enemy.damage((bullet.lvl/3)+1);
                            bullet.hit.add(enemy);
                        }
                        if (bullet.getWidth() > 9 + (10 * bullet.lvl)) {
                            bullet.remove();
                        }
                    }
                }
            }

            @Override
            public int pierce(GProjectile bullet) {
                if (!bullet.out) {
                    return BulletCode.super.pierce(bullet);
                }
                else{
                    return 50;
                }
            }
        });
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                if (bullet.target != null){
                    bullet.moveTowards(bullet.target.getX(), bullet.target.getY(),bullet.lvl*3);
                    for (GApple enemy: bullet.getTouching()) {
                        enemy.damage(Math.pow(1.5,bullet.pierceLeft));
                        bullet.hit.add(enemy);
                        if (enemy.pierce) {
                            bullet.pierceLeft--;
                        }
                        bullet.target = null;
                        break;
                    }

                }
                else{
                    GApple target = null;
                    for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
                        if (!bullet.hit.contains(GlobalVariables.enemies.get(i))){
                            target = GlobalVariables.enemies.get(i);
                            break;
                        }
                    }
                    if (target == null){
                        bullet.remove();
                    }
                    else{
                        if (bullet.pierceLeft < 1){
                            bullet.pierceLeft--;
                            if (bullet.pierceLeft < -10){
                                bullet.remove();
                            }
                        }
                        else {
                            GLine l = new GLine(bullet.hit.get(bullet.hit.size() - 1).getX() - bullet.getX(), bullet.hit.get(bullet.hit.size() - 1).getY() - bullet.getY(), target.getX() - bullet.getX(), target.getY() - bullet.getY());
                            l.setColor(Color.yellow);
                            bullet.add(l);
                            target.damage(Math.pow(1.5,bullet.pierceLeft));
                            bullet.hit.add(target);
                            if (target.pierce) {
                                bullet.pierceLeft--;
                            }
                        }
                    }
                }
            }
            @Override
            public int pierce(GProjectile bullet){
                return 2+ bullet.lvl;
            }
        });
        //5
        code.add(bullet -> {
            bullet.moveToTargetIfExists(bullet.lvl);
            for (GApple enemy: bullet.getTouching()){
                if (bullet.pierceLeft > 0) {
                    if (bullet.lvl > 2) {
                        enemy.damage(Math.pow(bullet.lvl - 2, 2));
                    }
                    else{
                        enemy.damage(1);
                    }
                    if (bullet.isUpgraded){
                        enemy.frozen += 15 * bullet.lvl;
                        if (enemy.vunrablility < 1 + (0.5 * bullet.lvl)) {
                            enemy.vunrablility = 1 + (0.5 * bullet.lvl);
                        }
                    }
                    else {
                        enemy.frozen += 25 * bullet.lvl;
                        if (enemy.vunrablility < 1 + (0.2 * bullet.lvl)) {
                            enemy.vunrablility = 1 + (0.2 * bullet.lvl);
                        }
                    }
                }
                bullet.pierceStuff(enemy);
            }
        });
        code.add(bullet -> {});
        code.add(bullet -> {
            double x = bullet.owner.getX()+bullet.owner.getWidth()/2-bullet.getWidth()/2;
            double y = bullet.owner.getY()+bullet.owner.getHeight()/2-bullet.getHeight()/2;
            if (bullet.out){
                if (bullet.pierceLeft < 0) {
                    bullet.pierceLeft = 300;
                    bullet.hit.clear();
                }
                if (bullet.pierceLeft < 25){
                    bullet.moveTowards(x,y+100,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 50){
                    bullet.moveTowards(x-38,y+75,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 75){
                    bullet.moveTowards(x-76,y+50,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 100){
                    bullet.moveTowards(x-76,y,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 125){
                    bullet.moveTowards(x-76,y-50,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 150){
                    bullet.moveTowards(x-38,y-75,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 175){
                    bullet.moveTowards(x,y-100,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 200){
                    bullet.moveTowards(x+38,y-75,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 225){
                    bullet.moveTowards(x+76,y-50,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 250){
                    bullet.moveTowards(x+76,y,bullet.lvl*2);
                }
                else if (bullet.pierceLeft < 275){
                    bullet.moveTowards(x+76,y+50,bullet.lvl*2);
                }
                else{
                    bullet.moveTowards(x+38,y+75,bullet.lvl*2);
                }
            }
            else {
                if (bullet.pierceLeft < 0) {
                    bullet.pierceLeft = 300;
                    bullet.hit.clear();
                }
                if (bullet.pierceLeft < 50) {
                    bullet.moveTowards(x, y + 50, bullet.lvl);
                }
                else if (bullet.pierceLeft < 100) {
                    bullet.moveTowards(x + 38, y + 25, bullet.lvl);
                }
                else if (bullet.pierceLeft < 150) {
                    bullet.moveTowards(x + 38, y - 25, bullet.lvl);
                }
                else if (bullet.pierceLeft < 200) {
                    bullet.moveTowards(x, y - 50, bullet.lvl);
                }
                else if (bullet.pierceLeft < 250) {
                    bullet.moveTowards(x - 38, y - 25, bullet.lvl);
                }
                else {
                    bullet.moveTowards(x - 38, y + 25, bullet.lvl);
                }
            }
            for (GApple enemy: bullet.getTouching()) {
                bullet.hit.add(enemy.damage(bullet.lvl*2*((bullet.lvl/3)+1)));
                bullet.hit.add(enemy);
            }
            bullet.pierceLeft-=bullet.lvl;
        });
        code.add(bullet -> {
            bullet.moveToTargetIfExists(bullet.lvl*2);
            for (GApple enemy: bullet.getTouching()) {
                if (bullet.pierceLeft > 0){
                    enemy.damage(bullet.isUpgraded ? (bullet.lvl/2)+1:(bullet.lvl + (bullet.lvl/2) + (bullet.lvl/3)));
                }
                bullet.pierceStuff(enemy);
            }
        });
        code.add(bullet -> {});
        //10
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                if (bullet.targets == null){
                    for (GApple enemy: bullet.getTouching()) {
                        if (bullet.pierceLeft*enemy.vunrablility > enemy.life) {
                            bullet.pierceLeft-=enemy.life/enemy.vunrablility;
                            enemy.damage(enemy.life);
                            enemy.remove();
                        }
                        else{
                            enemy.damage(bullet.pierceLeft);
                            bullet.remove();
                            break;
                        }
                    }
                }
                else {
                    bullet.moveTowards(bullet.targets.getX(), bullet.targets.getY(), bullet.lvl);
                    if (bullet.calculateDistance(bullet.targets.getX(), bullet.targets.getY()) < bullet.lvl) {
                        bullet.setLocation(bullet.targets);
                        bullet.targets = null;
                    }
                }
            }
            @Override
            public int pierce(GProjectile bullet){
                return 6*bullet.lvl;
            }
        });
        code.add(bullet -> {});
        code.add(bullet -> {
            bullet.moveTowards(bullet.target.getX(), bullet.target.getY(),bullet.lvl*2);
            if (bullet.isTouching(bullet.target)){
                bullet.target.bombed = true;
                bullet.target.bombDmg = (bullet.isUpgraded ? 3:5)*bullet.lvl;
                bullet.remove();
                if (bullet.isUpgraded){
                    new Thread(() -> bullet.target.getBombed(bullet.lvl)).start();
                }
            }
        });
        code.add(bullet -> {
            bullet.moveToTargetIfExists(bullet.lvl*3);
            if (bullet.calculateDistance(bullet.owner.getX()-bullet.owner.getWidth()/2,bullet.owner.getY()-bullet.owner.getHeight()/2) > (bullet.owner.getRange()*50)+50){
                bullet.remove();
            }
            for (GApple enemy: bullet.getTouching()) {
                if (bullet.pierceLeft > 0){
                    enemy.damage(bullet.lvl);
                    if (bullet.isUpgraded){
                        enemy.fire += (bullet.lvl/2)+1;
                        enemy.fireDmg += 1;
                    }
                }
                bullet.pierceStuff(enemy);
            }
        });
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                bullet.moveToTargetIfExists(bullet.lvl*3);
                for (GApple enemy: bullet.getTouching()) {
                    if (bullet.pierceLeft > 0 && !bullet.isUpgraded) {
                        enemy.damage((bullet.lvl * bullet.lvl) + 4);
                    }
                    bullet.pierceStuff(enemy);
                    enemy.pierce = false;
                }
            }
            @Override
            public int pierce(GProjectile bullet){
                return bullet.isUpgraded ? 3:1;
            }
        });
        //15
        code.add(bullet -> {
            bullet.moveToTargetIfExists(bullet.lvl*3);
            for (GApple enemy: bullet.getTouching()) {
                if (bullet.pierceLeft > 0) {
                    if (bullet.isUpgraded){
                        if (enemy.fire > 0) {
                            if (enemy.fire * (1 + (0.25 * bullet.lvl)) == enemy.fire) {
                                enemy.fire++;
                            } else {
                                enemy.fire *= (1 + (0.25 * bullet.lvl));
                            }
                        }
                        if (enemy.frozen > 0) {
                            if (enemy.frozen * (1 + (0.25 * bullet.lvl)) == enemy.frozen) {
                                enemy.frozen++;
                            } else {
                                enemy.frozen *= (1 + (0.25 * bullet.lvl));
                            }
                        }
                    }
                    else {
                        if (enemy.fireDmg > 0) {
                            if (enemy.fireDmg * (1 + (0.05 * bullet.lvl)) == enemy.fireDmg) {
                                enemy.fireDmg++;
                            } else {
                                enemy.fireDmg *= (1 + (0.05 * bullet.lvl));
                            }
                        }
                        if (enemy.vunrablility > 1) {
                            enemy.vunrablility *= (1 + (0.05 * bullet.lvl));
                        }
                    }
                    if (enemy.bombDmg > 0) {
                        if (enemy.bombDmg * (1 + (0.05 * bullet.lvl)) == enemy.bombDmg) {
                            enemy.bombDmg++;
                        } else {
                            enemy.bombDmg *= (1 + (0.05 * bullet.lvl));
                        }
                    }
                }
                bullet.pierceStuff(enemy);
            }
        });
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                if (bullet.owner.getThings()[0]){
                    bullet.moveToTargetIfExists(bullet.lvl*(bullet.isUpgraded ? 11 : 8));
                }
                else{
                    bullet.moveToTargetIfExists(bullet.lvl*3);
                }
                for (GApple enemy: bullet.getTouching()) {
                    if (bullet.pierceLeft > 0) {
                        if (bullet.owner.getThings()[1] && bullet.owner.getThings()[4]) {
                            enemy.damage((bullet.lvl * bullet.lvl * (bullet.isUpgraded ? bullet.lvl : 1)) + (bullet.isUpgraded ? 7 : 3));
                        } else if (bullet.owner.getThings()[1] || bullet.owner.getThings()[4]) {
                            enemy.damage((bullet.lvl + (bullet.lvl / 2) + (bullet.lvl / 3) + (bullet.lvl / 5)) * (bullet.isUpgraded ? bullet.lvl : 1));
                        } else {
                            enemy.damage(bullet.lvl);
                        }
                    }
                    bullet.pierceStuff(enemy);
                }
            }
            @Override
            public int pierce(GProjectile bullet){
                if (bullet.owner.getThings()[5]){
                    return (bullet.isUpgraded ? 5 : 2)* bullet.lvl;
                }
                else{
                    return 2 + (bullet.lvl/2);
                }
            }
        });
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                if (bullet.out){
                    bullet.moveToTargetIfExists(10* bullet.lvl);
                }
                else{
                    bullet.moveToTargetIfExists(5* bullet.lvl);
                }
                for (GApple enemy: bullet.getTouching()) {
                    if (bullet.pierceLeft > 0) {
                        if (bullet.out) {
                            enemy.damage(6 * bullet.lvl);
                        } else {
                            enemy.damage((bullet.isUpgraded ? 1:2) * bullet.lvl);
                        }
                    }
                    bullet.pierceStuff(enemy);
                }
            }
            @Override
            public int pierce(GProjectile bullet){
                if (bullet.out){
                    return 2*bullet.lvl;
                }
                else{
                    return 2 + (bullet.lvl/2);
                }
            }
        });
        code.add(bullet -> {});
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                bullet.moveToTargetIfExists(7* bullet.lvl);
                for (GApple enemy: bullet.getTouching()) {
                    if (bullet.pierceLeft > 0){
                        if (bullet.isUpgraded){
                            enemy.damage(100 * bullet.lvl * bullet.lvl);
                        }
                        else {
                            enemy.damage(enemy.life);
                        }
                    }
                    bullet.pierceStuff(enemy);
                }
            }
            @Override
            public int pierce(GProjectile bullet){
                return bullet.lvl;
            }
        });
        //20
        code.add(new BulletCode() {
            @Override
            public void run(GProjectile bullet) {

            }
            @Override
            public int pierce(GProjectile bullet){
                return 6*bullet.lvl;
            }
        });
        code.addAll(GlobalVariables.synergies);
    }
    GTile owner;
    boolean isUpgraded;
    public ArrayList<GApple> hit = new ArrayList<>();
    public GOval v = new GOval(7,7);
    public GApple target;
    GPoint targets;
    int type;
    public int lvl;
    public int pierceLeft;
    public boolean out = false;
    public void tick(){
        if (owner != null) {
            isUpgraded = owner.isUpgraded;
        }
        code.get(type).run(this);
        if (target != null) {
            if (target.life < 1) {
                if (pierceLeft == pierce()) {
                    remove();
                }
            }
        }
        if (this.getX() < -50 || this.getX() > 2050 || this.getY() < -50 || this.getY() > 1550){
            remove();
        }
    }
    public GProjectile(int lvl){
        type = 20;
        this.lvl = lvl;
        pierceLeft = pierce();
    }
    public GProjectile(int lvl, GTile starter, GApple target){
        this(17,lvl,target,starter);
        out = true;
        setFillColor();
    }
    public GProjectile(int type, int lvl, GApple target, GObject starter){
        if (starter instanceof GTile) {
            owner = (GTile) starter;
        }
        GlobalVariables.projectiles.add(this);
        this.lvl = lvl;
        this.type = type;
        this.target = target;
        pierceLeft = pierce();
        v.setFilled(true);
        setFillColor();
        add(v);
        addToScreen(starter);
    }
    public GProjectile(int lvl, int direction, GTile starter){
        owner = starter;
        GlobalVariables.projectiles.add(this);
        type = 1;
        this.lvl = lvl;
        final double sqrtTwo = Math.sqrt(2);
        if (direction == 1 || direction == 7){
            xAmount = 0;
            yAmount = 1;
        }
        else if (direction == 2){
            yAmount = 0.4;
            xAmount = 0.6;
        }
        else if (direction == 3){
            yAmount = -0.4;
            xAmount = 0.6;
        }
        else if (direction == 4 || direction == 11){
            yAmount = -1;
            xAmount = 0;
        }
        else if (direction == 5){
            yAmount = -0.4;
            xAmount = -0.6;
        }
        else if (direction == 0){
            xAmount = -0.6;
            yAmount = 0.4;
        }
        else if (direction == 8){
            xAmount = sqrtTwo;
            yAmount = sqrtTwo;
        }
        else if (direction == 9){
            xAmount = 1;
            yAmount = 0;
        }
        else if (direction == 10){
            xAmount = sqrtTwo;
            yAmount = -sqrtTwo;
        }
        else if (direction == 12){
            xAmount = -sqrtTwo;
            yAmount = -sqrtTwo;
        }
        else if (direction == 13){
            xAmount = -1;
            yAmount = 0;
        }
        else if (direction == 14){
            xAmount = -sqrtTwo;
            yAmount = sqrtTwo;
        }
        pierceLeft = 10*lvl;
        v.setFilled(true);
        setFillColor();
        add(v);
        addToScreen(starter);
    }
    public GProjectile(int lvl, GTile starter, boolean outside){
        setFillColor();
        v.setFilled(true);
        add(v);
        type = 7;
        this.lvl = lvl;
        owner = starter;
        addToScreen(starter);
        out = outside;
        pierceLeft = 300;
    }
    public GProjectile(int lvl, GTile target, GTile starter){
        owner = starter;
        GlobalVariables.projectiles.add(this);
        this.lvl = lvl;
        type  = starter.diceType;
        v.setSize(6+lvl,6+lvl);
        targets = new GPoint(target.getX()+target.getWidth()/2+GlobalVariables.randomInt(-15,15),target.getY()+target.getHeight()/2+GlobalVariables.randomInt(-15,15));
        pierceLeft = pierce();
        v.setFilled(true);
        setFillColor();
        add(v);
        addToScreen(starter);
    }
    public GProjectile(int dmg, GApple starter){
        this.lvl = dmg;
        type = 0;
        v.setFilled(true);
        setFillColor();
        add(v);
        addToScreen(starter);
    }
    public boolean isTouching(GObject enemy){
        return (calculateDistance(enemy.getX(),enemy.getY()) < (v.getWidth()+v.getHeight()+enemy.getWidth()+enemy.getHeight())/2 && !hit.contains(enemy));
    }
    public int pierce(){
        return code.get(type).pierce(this);
    }
    public double calculateDistance(double x, double y){
        return Math.sqrt(Math.pow(x-getX(),2)+Math.pow(y-getY(),2));
    }
    public void addToScreen(GObject s){
        GlobalVariables.screen.add(this,s.getX()+s.getWidth()/2-this.getWidth()/2,s.getY()+s.getHeight()/2-this.getHeight()/2);
        sendToFront();
    }
    public void addToScreen(GTile t){
        if (t.getCenter().getX() == 0 && t.getCenter().getY() == 0){
            addToScreen((GObject) t);
        }
        else{
            GlobalVariables.screen.add(this,t.getTrueCenter().getX()-this.getWidth()/2,t.getTrueCenter().getY()-this.getHeight()/2);
            sendToFront();
        }
    }
    public void remove(){
        GlobalVariables.projectiles.remove(this);
        GlobalVariables.screen.remove(this);
        if (owner != null) {
            owner.projectiles.remove(this);
        }
    }
    public ArrayList<GApple> getTouching(){
        ArrayList<GApple> touching = new ArrayList<>();
        for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
            GApple enemy = GlobalVariables.enemies.get(i);
            if (isTouching(enemy)) {
                touching.add(enemy);
            }
        }
        return touching;
    }
    public void pierceStuff(GApple enemy){
        hit.add(enemy);
        if (enemy.life < 1) {
            enemy.remove();
        }
        if (enemy.pierce) {
            pierceLeft--;
        }
        target = null;
        if (pierceLeft < 1){
            this.remove();
        }
    }
    public void moveToTargetIfExists(int speed){
        if (target != null){
            moveTowards(target.getX(), target.getY(),speed);
        }
        else{
            moveSteps(speed);
        }
    }
    public void setFillColor(){
        v.setFillColor(GlobalVariables.colors.get(type).getColor(this));
    }
}
