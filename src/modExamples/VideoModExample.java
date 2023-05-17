package modExamples;

import game.*;

import java.awt.*;
import java.util.ArrayList;

public class VideoModExample extends game.Mod{
    static int number = 0;
    static int diceNumber;
    @Override
    protected void run() {
        diceNumber = registerNewTower(new TileCode() {
            @Override
            public int getRange(GTile tile) {
                return 1 + (number/7);
            }

            @Override
            public int getAttackSpeed(GTile tile) {
                return 150 / (number+1);
            }

            @Override
            public void turnPassed(GTile tile) {
                if (tile.diceAmount == 6){
                    ArrayList<GTile> possibleTiles = new ArrayList<>();
                    for (GTile[] someTiles: GlobalVariables.tiles) {
                        for (GTile subTile: someTiles) {
                            if (GlobalVariables.isNextTo(tile.x,tile.y,subTile.x,subTile.y)){
                                if (!(subTile.dice || subTile.path)){
                                    subTile.setDice(diceNumber, 1);
                                    if (GlobalVariables.pathFind(false)) {
                                        possibleTiles.add(subTile);
                                    }
                                    subTile.setEmpty();
                                }
                            }
                        }
                    }
                    if (possibleTiles.size() > 0){
                        tile.setEmpty();
                        tile.setDice(diceNumber, 1);
                        GTile random = possibleTiles.get(GlobalVariables.randomInt(0,possibleTiles.size()-1));
                        random.setDice(diceNumber,1);
                        number++;
                    }
                }
                else {
                    tile.setEmpty();
                    tile.setDice(diceNumber, tile.diceAmount + 1);
                }
            }

            @Override
            public String description() {
                return "Clones itself at level 6";
            }

            @Override
            public String name() {
                return  "Clone dice";
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
            public void tick(GTile tile){
                int realDiceAmount = tile.diceAmount;
                tile.diceAmount = 1;
                game.TileCode.super.tick(tile);
                tile.diceAmount = realDiceAmount;
            }
        },
                bullet -> {
            bullet.moveToTargetIfExists(bullet.lvl*((number/3)+1));
            for (GApple enemy: bullet.getTouching()) {
                if (bullet.pierceLeft > 0){
                    enemy.damage(number);
                }
                bullet.pierceStuff(enemy);
            }
        },
                tile -> {
            if (number > 25) {
                return new Color(255,255,255);
            }
            return new Color(10*number,10*number,10*number);
        });
        startDetecting(new EventListener() {
            @Override
            public void merged(GTile merger1, GTile merger2, GTile result) {
                if (result.diceType != diceNumber && (merger1.diceType == diceNumber)){
                    number--;
                    checkStuff();
                }
                if (result.diceType == diceNumber && (merger1.diceType != diceNumber)){
                    number++;
                    checkStuff();
                }
            }

            @Override
            public void placed(GTile placed) {
                if (placed.diceType == diceNumber){
                    number++;
                    checkStuff();
                }
            }

            @Override
            public void pickedUp(GTile pickedUp) {
                if (pickedUp.diceType == diceNumber){
                    number--;
                    checkStuff();
                }
            }
        });
    }

    @Override
    public void tick() {

    }

    public void checkStuff(){
        for (GTile[] someTiles: GlobalVariables.tiles) {
            for (GTile tile: someTiles) {
                if (tile.dice && tile.diceType == diceNumber){
                    tile.configureDice();
                }
            }
        }
    }
}
