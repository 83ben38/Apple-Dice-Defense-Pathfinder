package game;

import java.awt.*;
import java.util.ArrayList;

public interface LevelCode {
    double[][] getWaveData();
    ArrayList<Integer> bannedTowers();
    double[] generateFreeplayWave(int wave);
    boolean rewardDice();
    default boolean rewardExtras(){
        return false;
    }
    String name();
    Color difficultyColor();
    default boolean rewardUpgrades(){
        return true;
    }
    default Integer[][] flyingWaves(){
        return new Integer[][]{{},{}};
    }
    default int freeplayIsFlying(){
        return 0;
    }
    default ArrayList<Integer> splittingWaves(){
        return new ArrayList<>();
    }
    default boolean freeplayIsSplitting(){
        return false;
    }
    default Double[][] camoWaves(){
        return new Double[][]{{},{}};
    }
    default double freeplayIsCamo(){
        return 0;
    }
    default ArrayList<Integer> reverseWaves(){
        return new ArrayList<>();
    }
    default boolean freeplayIsReverse(){
        return false;
    }
    default Integer[][] healingWaves(){
        return  new Integer[][]{{},{}};
    }
    default int freeplayHealing(int waveN){return 0;}
    default Integer[] summonWaves(){
        return new Integer[getWaveData().length];
    }
    default int freeplaySummoning(int waveN){
        return 0;
    }

    default int maxDiceLevel(){
        return 6;
    }

    default int[] dicePicked(){
        return new int[]{3,2,2,2,1};
    }
}
