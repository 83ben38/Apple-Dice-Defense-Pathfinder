package game;

public interface TileCode {
    default int getRange(GTile tile){
        if (tile.diceAmount > 4){
            return 2;
        }
        return 1;
    }
    default int getAttackSpeed(GTile tile){
        return 96;
    }
    default void tick(GTile tile){
        tile.ticksLeft-= tile.diceAmount;
        if (tile.ticksLeft < 1){
            tile.fire();
        }
    }
    default void fire(GTile tile){
        int number = -1;
        for (int i = 0; i < GlobalVariables.targets.size(); i++) {
            if (tile.calculateDistance(GlobalVariables.targets.get(i).getX(), GlobalVariables.targets.get(i).getY()) < (tile.getRange()+0.5) * 50) {
                number = i;
                break;
            }
        }
        if (number > -1) {
            tile.ticksLeft = tile.getAttackSpeed();
            tile.projectiles.add(new GProjectile(tile.diceType, tile.diceAmount, GlobalVariables.targets.get(number), tile));
        }
    }
    default void turnPassed(GTile tile){}
    String description();
    String name();
    String secondaryName();
    String secondaryDescription();
}
