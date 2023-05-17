package modExamples;

import acm.graphics.GCompound;
import acm.graphics.GLine;
import acm.graphics.GRect;
import game.*;

import java.awt.*;
import java.util.ArrayList;

public class MultiplierMod extends game.Mod{
    static Color c = new Color(18,222, 144);
    static ArrayList<GApple> targeted = new ArrayList<>();
    static ArrayList<MiniTower> m = new ArrayList<>();

    static ArrayList<GTile> z = new ArrayList<>();
    static int bonuses = 0;
    static int diceNumber;
    static boolean upgraded;
    @Override
    protected void run() {

        diceNumber = registerNewTower(new TileCode() {
                                          @Override
                                          public void tick(GTile tile) {
                                              if (z.contains(tile)){
                                                  bonuses++;
                                              }
                                              else {
                                                  z.add(tile);
                                              }
                                              TileCode.super.tick(tile);
                                          }

                                          @Override
                                          public void fire(GTile tile) {
                                              int number = -1;
                                              for (int i = 0; i < GlobalVariables.targets.size(); i++) {
                                                  if (tile.calculateDistance(GlobalVariables.targets.get(i).getX(), GlobalVariables.targets.get(i).getY()) < (tile.getRange()+0.5) * 50 && !targeted.contains(GlobalVariables.targets.get(i))) {
                                                      number = i;
                                                      break;
                                                  }
                                              }
                                              if (number > -1) {
                                                  tile.ticksLeft = tile.getAttackSpeed();
                                                  tile.projectiles.add(new GProjectile(tile.diceType, tile.diceAmount, GlobalVariables.targets.get(number), tile));
                                                  tile.projectiles.get(tile.projectiles.size()-1).out = false;
                                              }
                                          }

                                          @Override
                                          public int getAttackSpeed(GTile tile) {
                                              return TileCode.super.getAttackSpeed(tile)*2;
                                          }

                                          @Override
                                          public int getRange(GTile tile) {
                                              return TileCode.super.getRange(tile)+1;
                                          }

                                          @Override
                                          public String description() {
                                              return "Throws towers on apples.";
                                          }

                                          @Override
                                          public String name() {
                                              return "Multiplier dice";
                                          }

                                          @Override
                                          public String secondaryName() {
                                              return "Lightning";
                                          }

                                          @Override
                                          public String secondaryDescription() {
                                              return "Thrown towers shoot lightning.";
                                          }
                                      },
                new BulletCode() {
                    @Override
                    public void run(GProjectile bullet) {
                        if (bullet.out && !upgraded) {
                            bullet.moveToTargetIfExists(bullet.lvl*4);
                            for (GApple enemy : bullet.getTouching()) {
                                if (bullet.pierceLeft > 0) {
                                    enemy.damage(bullet.lvl*bullet.lvl);
                                }
                                bullet.pierceStuff(enemy);
                            }
                        }
                        else if (bullet.out){
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
                                        l.setColor(c);
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
                        else {
                            bullet.moveToTargetIfExists(bullet.lvl*4);
                            for (GApple enemy : bullet.getTouching()) {
                                if (!targeted.contains(enemy)) {
                                    targeted.add(enemy);
                                    m.add(new MiniTower(enemy, bullet.lvl));
                                    bullet.pierceStuff(enemy);
                                    if (bullet.pierceLeft < 1){
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public int pierce(GProjectile bullet) {
                        if (bullet.out) {
                            return bullet.lvl * 2;
                        }
                        else{
                            return bullet.lvl > 3 ? 2 : 1;
                        }
                    }
                },
                new DesignCode() {
                    @Override
                    public Color getColor(GProjectile bullet) {
                        if (bullet.out){
                            return Color.yellow;
                        }
                        return DesignCode.super.getColor(bullet);
                    }

                    @Override
                    public Color getColor(GTile tile) {
                        return c;
                    }
                });
    }
    @Override
    public void tick(){
        z = new ArrayList<>();
        upgraded = GTile.upgraded.get(diceNumber);
        for (; bonuses >= 0; bonuses--) {
            for (int i = 0; i < m.size(); i++) {
                m.get(i).tick();
            }
        }
        bonuses = 0;
    }
    public static class MiniTower{
        int ticksleft;
        GCompound thing;
        GApple target;
        int level;
        public MiniTower(GApple enemy, int level){
            target = enemy;
            this.level = level;
            ticksleft = 96;
            thing = new GCompound();
            GRect r = new GRect(10,10);
            r.setFillColor(Color.yellow);
            r.setFilled(true);
            thing.add(r);

        }
        public void tick(){
            if (!GlobalVariables.targets.contains(target)){
                target = null;
                m.remove(this);
                GlobalVariables.screen.remove(thing);
                return;
            }
            GlobalVariables.screen.add(thing,target.getLocation());
            ticksleft -= level;
            if (ticksleft < 1){
                    int number = -1;
                    for (int i = 0; i < GlobalVariables.targets.size(); i++) {
                        if (calculateDistance(GlobalVariables.targets.get(i).getX(), GlobalVariables.targets.get(i).getY()) < (level+0.5) * 50 && GlobalVariables.targets.get(i) != target) {
                            number = i;
                            break;
                        }
                    }
                    ticksleft = 96;
                    if (number > -1) {
                        GProjectile p = new GProjectile(diceNumber, level, GlobalVariables.targets.get(number), thing);
                        p.out = true;
                        p.hit.add(target);
                        p.setFillColor();
                    }
                    else{
                        target.damage(level);
                        if (!GlobalVariables.targets.contains(target)){
                            target = null;
                            m.remove(this);
                            GlobalVariables.screen.remove(thing);
                            return;
                        }
                    }
            }
        }
        public double calculateDistance(double x, double y){
            return Math.sqrt(((x-target.getX())*(x-target.getX()))+((y-target.getY())*(y-target.getY())));
        }
    }
}
