package modExamples;

import game.*;

import java.awt.*;

//to create a mod, create a class in the mods package that extends mod and has no $ in the class name.
/**The concept of this tower is that every turn that there is 1,3,5, or 7 of them they gain damage.
 * Additionally, they have double attack speed and turn orange when they are activated*/
public class ModExample1 extends game.Mod {
    static boolean activated = false;
    static int number = 0;
    static int damage = 1;
    int diceNumber;
    //initializing the mod will be in the run method
    @Override
    protected void run(){
        //add a die with a TileCode, a BulletCode, and a DesignCode
        //the return number of this function is the type# of the dice
        diceNumber = registerNewTower(new TileCode() {
            //the default range for a tower is 1 at lvl 1-4 and 2 at 5 and 6
            @Override
            public int getRange(GTile tile) {
                //this tower gets 1 range more than default
                return TileCode.super.getRange(tile)+1;
            }
            //the default attack speed for a tower is 96
            @Override
            public int getAttackSpeed(GTile tile) {
                // when activated, it gets normal attack speed
                if (activated){
                    return TileCode.super.getAttackSpeed(tile);
                }
                //when not activated, its attack speed is halved
                return 192;
            }

                                          @Override
                                          public String description() {
                                              return "Gains damage when there are 1 3 5 or 7 of them.";
                                          }

                                          @Override
                                          public String name() {
                                              return "Solar dice";
                                          }

                                          @Override
                                          public String secondaryName() {
                                              return null;
                                          }

                                          @Override
                                          public String secondaryDescription() {
                                              return null;
                                          }
                                      },
                bullet -> {
            // the run method in bullet code is what to do every tick
                    //here we move to our target at a speed of bullet.lvl*2
                    bullet.moveToTargetIfExists(bullet.lvl*2);
                    //we check for enemies we are touching and deal damage to them if we have pierce left
                    for (GApple enemy: bullet.getTouching()) {
                        if (bullet.pierceLeft > 0) {
                            //this tower does the damage amount * the level
                            enemy.damage(damage*bullet.lvl);
                        }
                        //highly suggested to put this
                        bullet.pierceStuff(enemy);
                    }
                    },
                tile -> {
            //the color of the tile is orange when activated, gray when not
            if (activated) {
                return new Color(225, 100, 0);
            }
            return new Color(125, 125, 125);
        });
        //you can also detect for events to happen.
        startDetecting(new EventListener() {
            @Override
            public void merged(GTile merger1, GTile merger2, GTile result){
                if (result.diceType != diceNumber && (merger1.diceType == diceNumber)){
                    number--;
                    checkStuff();
                }
                if (result.diceType == diceNumber && (merger1.diceType != diceNumber)){
                    number++;
                    checkStuff();
                }
            }
            //everytime a round is started this will be called
            @Override
            public void roundStarted(int roundNumber) {
                //at the beggining of the round if they are activated their damage increases
                if (activated){
                    //damage increased by how many towers are activated divided by two (1 -> 1, 3 -> 2, 5 -> 3, 7 -> 4)
                    damage+= (number+1)/2;
                }
            }
            //you can detect when dice are placed
            @Override
            public void placed(GTile placed) {
                //anytime a dice is placed, if it is our dice, the amount of total dice increases
                if (placed.diceType == diceNumber){
                    number++;
                    //this checks if we have 1,3,5,or 7 dice and if so activates them
                    checkStuff();
                }
            }
            //also detect when dice are picked up
            @Override
            public void pickedUp(GTile pickedUp) {
                //same as placed but opposite
                if (pickedUp.diceType == diceNumber) {
                    number--;
                    checkStuff();
                }
            }
            //you can check when growth dice grow
            @Override
            public void diceGrown(GTile grown){
                //when a growth dice grows into our dice we have to make sure we mark it
                if (grown.diceType == diceNumber){
                    number++;
                    checkStuff();
                }
            }
        });
    }

    @Override
    public void tick() {

    }

    private void checkStuff(){
        activated = number > 0 && number < 8 && number%2==1;
        for (GTile[] someTiles: GlobalVariables.tiles) {
            for (GTile tile: someTiles) {
                if (tile.dice && tile.diceType == diceNumber){
                    tile.configureDice();
                }
            }
        }
    }
}
