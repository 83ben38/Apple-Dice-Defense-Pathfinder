package modExamples;

import game.*;

import java.awt.*;

public class SomeMod extends Mod {
    static int diceGrown = 0;
    @Override
    protected void run() {
        registerSynergy(new SynergyCode() {
            @Override
            public Color getColor2(GTile tile) {
                return new Color(75,30,25);
            }

            @Override
            public int[] synergyNumbers() {
                return new int[]{1,8};
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return 192;
            }

            @Override
            public void run(GProjectile bullet) {
                bullet.moveSteps(bullet.lvl);
                for (GApple enemy : bullet.getTouching()) {
                    if (bullet.pierceLeft > 0) {
                        enemy.damage(25);
                    }
                    bullet.pierceStuff(enemy);
                }
            }

            @Override
            public int getRange(GTile tile) {
                return 0;
            }

            @Override
            public void fire(GTile tile) {
                for (int i = 0; i <= diceGrown; i++) {
                    for (int j = 0; j < 4; j++) {
                        tile.projectiles.add(new GProjectile(8, 0, tile));
                        tile.projectiles.get(tile.projectiles.size()-1).xAmount = j%2==1 ? -1 : 1;
                        tile.projectiles.get(tile.projectiles.size()-1).yAmount = j/2==1 ? -1 : 1;
                        tile.projectiles.get(tile.projectiles.size()-1).xAmount*= (double) i /diceGrown;
                        tile.projectiles.get(tile.projectiles.size()-1).yAmount *= (double) (diceGrown - i) /diceGrown;
                    }
                }
            }

            @Override
            public Color getColor(GTile tile) {
                return Color.green;
            }

            @Override
            public String description() {
                return "Grow dice to grow this dice";
            }

            @Override
            public String name() {
                return "Multi directional green dice";
            }
        });
        startDetecting(new EventListener() {
            @Override
            public void diceGrown(GTile grown) {
                diceGrown++;
            }
        });
    }

    @Override
    public void tick() {

    }
}
