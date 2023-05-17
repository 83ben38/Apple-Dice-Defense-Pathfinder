package modExamples;

import acm.graphics.GLabel;
import game.*;

import java.awt.*;
/**This tower is a bit more complicated.
 * This towers shoots in its entire range which is always 1.
 * It also vunrablizes every enemy in its range constantly.
 * Every tower level is equal in strength to all others.
 * Every 4 merges, all towers of this type gain 1 power which increases its damage and how much it vunrablizes enemies.
 * At 0 power, it will not be able to shoot.*/
public class ModExample2 extends game.Mod{
    //the dice number of the added dice
    public static int diceNumber;
    //the number of merges since the last power up
    static int merges = 0;
    //the amount of power
    static int power = 0;
    @Override
    protected void run() {
        diceNumber = registerNewTower(new TileCode() {
            @Override
            public void tick(GTile tile) {
                //every GApple in range gets vulnerable
                for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
                    if (tile.calculateDistance(GlobalVariables.enemies.get(i).getX(), GlobalVariables.enemies.get(i).getY()) < (tile.getRange()+0.5) * 50) {
                        if (GlobalVariables.enemies.get(i).vunrablility < power + 1){
                            GlobalVariables.enemies.get(i).vunrablility = power + 1;
                        }
                    }
                }
                //once the power is one or more, the tower will shoot normally
                if (power > 0) {
                    int realDiceAmount = tile.diceAmount;
                    tile.diceAmount = power;
                    TileCode.super.tick(tile);
                    tile.diceAmount = realDiceAmount;
                }
            }

            @Override
            public void fire(GTile tile) {
                //make sure that you have power before you shoot
                if (power > 0) {
                    TileCode.super.fire(tile);
                }
            }

                                          @Override
                                          public String description() {
                                              return "Gains power every 4 merges";
                                          }

                                          @Override
                                          public String name() {
                                              return  "Combo dice";
                                          }

                                          @Override
                                          public String secondaryName() {
                                              return null;
                                          }

                                          @Override
                                          public String secondaryDescription() {
                                              return null;
                                          }

                                          @Override
            public int getRange(GTile tile){
                //range is always one even at lvls 5 and 6
                return 1;
            }
        },
                new BulletCode() {
            @Override
            public void run(GProjectile bullet) {
                //make sure the bullet is the size of the range when starting
                if (bullet.pierceLeft == bullet.pierce()){
                    bullet.v.setSize(150, 150);
                    bullet.setLocation(bullet.getX()- bullet.getWidth()/2,bullet.getY()-bullet.getHeight()/2);
                }
                //if damage has already been dealt, the saturation decreases
                if (bullet.pierceLeft < bullet.pierce()/2) {
                    bullet.v.setFillColor(new Color(0, 255, 255,(bullet.pierceLeft * 10)));
                }
                //otherwise, it increases
                else{
                    bullet.v.setFillColor(new Color(0, 255, 255,((25-bullet.pierceLeft) * 10)));
                }
                //once it hits 50% saturation, deal damage to all enemies it is touching
                if (bullet.pierceLeft == bullet.pierce()/2){
                    for (GApple enemy: bullet.getTouching()) {
                        enemy.damage(power*power);
                    }
                }
                if (bullet.pierceLeft == 0){
                    bullet.remove();
                }
                bullet.pierceLeft--;
            }
            @Override
            public int pierce(GProjectile bullet){
                //the lifespan of a bullet is 25/power ticks
                return 25/power;
            }
        },
                new DesignCode() {
            @Override
            public Color getColor(GTile tile) {
                //the color changes based on how many merges there are
                return new Color(merges*85,0,0);
            }
            //use this to change how the dice looks
            @Override
            public void configureDice(GTile tile){
                // make sure to add the number at the top showing the power
                game.DesignCode.super.configureDice(tile);
                while(tile.extras.size() > 0){
                    tile.remove(tile.extras.get(0));
                    tile.extras.remove(0);
                }
                tile.extras.add(new GLabel(power +""));
                tile.extras.get(0).setColor(getColor(tile));
                tile.add(tile.extras.get(0),tile.getWidth()/2-tile.extras.get(0).getWidth()/2,tile.extras.get(0).getHeight());
            }
        });
        //you can detect for merges as well
        startDetecting(new EventListener() {
            @Override
            public void merged(GTile merger1, GTile merger2, GTile result) {
                //when merged, increased the merges by 1
                if (merger1.diceType == diceNumber || merger2.diceType == diceNumber){
                    merges++;
                    checkMerges();
                }
            }
        });
    }

    @Override
    public void tick() {

    }

    private void checkMerges(){
        //when hitting 4 merges, increase your power
        if (merges > 3){
            merges = 0;
            power++;
        }
        for (GTile[] someTiles: GlobalVariables.tiles) {
            for (GTile tile: someTiles) {
                if (tile.dice && tile.diceType == diceNumber){
                    tile.configureDice();
                }
            }
        }
    }
}
