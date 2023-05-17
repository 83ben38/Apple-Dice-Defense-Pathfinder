package game;

import acm.graphics.*;
import acm.program.Program;
import svu.csc213.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

import static acm.util.JTFTools.pause;
import static acm.util.JTFTools.terminateAppletThreads;

public abstract class GlobalVariables {
    static ArrayList<SynergyCode> synergies = new ArrayList<>();
    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<String> synergyNames = new ArrayList<>();
    static ArrayList<EventListener> listeners = new ArrayList<>();
    static ArrayList<DesignCode> colors = new ArrayList<>();
    static ArrayList<LevelCode> levels = new ArrayList<>();
    static ArrayList<Integer> flyingWaves = new ArrayList<>();
    static ArrayList<Integer> flyingAmounts = new ArrayList<>();
    static ArrayList<Integer> splitting = new ArrayList<>();
    static ArrayList<Double> camoWaves = new ArrayList<>();
    static ArrayList<Double> camoAmounts = new ArrayList<>();
    static ArrayList<Integer> reverse = new ArrayList<>();
    static ArrayList<Integer> healingWaves = new ArrayList<>();
    static ArrayList<Integer> healingAmounts = new ArrayList<>();
    static ArrayList<Integer> summons = new ArrayList<>();
    static ArrayList<MapCode> maps = new ArrayList<>();
    static{
        colors.add(new DesignCode() {
            @Override
            public Color getColor(GTile tile) {
                return null;
            }
            @Override
            public Color getColor(GProjectile bullet) {
                return Color.black;
            }
        });
        colors.add(tile -> Color.green);
        colors.add(tile -> Color.red);
        colors.add(tile -> Color.blue);
        colors.add(tile -> Color.yellow);
        colors.add(tile -> Color.cyan);
        colors.add(tile -> new Color(150, 75, 255));
        colors.add(tile -> new Color(50, 0, 125));
        colors.add(tile -> new Color(75, 30, 25));
        colors.add(tile -> Color.orange);
        colors.add(tile -> new Color(150, 255, 150));
        colors.add(tile -> new Color(125, 150, 75));
        colors.add(tile -> Color.black);
        colors.add(tile -> new Color(125,0,0));
        colors.add(tile -> Color.pink);
        colors.add(tile -> Color.magenta);
        colors.add(new DesignCode() {
            @Override
            public Color getColor(GTile tile) {
                return Color.white;
            }
            public void configureDice(GTile tile){
                DesignCode.super.configureDice(tile);
                while (tile.stuff.size() > 0){
                    tile.remove(tile.stuff.get(0));
                    tile.stuff.remove(0);
                }
                GPoint[] i = map.polygonPoints(tile.x, tile.y, tile.size);
                Color[] c = new Color[]{Color.red,Color.orange,Color.yellow,Color.green,Color.blue,Color.magenta};
                for (int j = 0; j < i.length-1; j++) {
                    GPolygon p = new GPolygon();
                    p.addVertex(i[j].getX(),i[j].getY());
                    p.addVertex(i[j+1].getX(),i[j+1].getY());
                    p.addVertex(((i[j+1].getX()-25)*0.8)+25,((i[j+1].getY()-25)*0.8)+25);
                    p.addVertex(((i[j].getX()-25)*0.8)+25,((i[j].getY()-25)*0.8)+25);
                    if (tile.getThings()[j]){
                        p.setFilled(true);
                        p.setFillColor(c[j]);
                    }
                    p.setColor(c[j]);
                    tile.add(p);
                    tile.stuff.add(p);
                    p.sendToBack();
                }
                for (int j = i.length-1; j < i.length; j++) {
                    GPolygon p = new GPolygon();
                    p.addVertex(i[j].getX(),i[j].getY());
                    p.addVertex(i[0].getX(),i[0].getY());
                    p.addVertex(((i[0].getX()-25)*0.8)+25,((i[0].getY()-25)*0.8)+25);
                    p.addVertex(((i[j].getX()-25)*0.8)+25,((i[j].getY()-25)*0.8)+25);
                    if (tile.getThings()[j]){
                        p.setFilled(true);
                        p.setFillColor(c[j]);
                    }
                    p.setColor(c[j]);
                    tile.add(p);
                    tile.stuff.add(p);
                    p.sendToBack();
                }
                tile.g.sendToBack();
            }
        });
        colors.add(new DesignCode() {
            @Override
            public Color getColor(GTile tile) {
                switch (tile.phase) {
                    case 1 -> {
                        return new Color(250, 250, 125);
                    }
                    case 2 -> {
                        return new Color(175, 175, 100);
                    }
                    default -> {
                        return new Color(150, 150, 50);
                    }
                }
            }
            @Override
            public Color getColor(GProjectile bullet){
                if (bullet.out){
                    return new Color(150, 150, 50);
                }
                else{
                    return new Color(250, 250, 125);
                }
            }
        });
        colors.add(tile -> new Color(100,0,100));
        colors.add(tile -> new Color(0,178,255));
        colors.add(tile -> GConnectile.LASER_COLOR);
        colors.addAll(GlobalVariables.synergies);
        String[] namess = new String[]{"Green dice", "Fire dice", "Water dice", "Electric dice", "Ice dice", "Growth dice", "Orbit dice", "Multi-projectile dice", "Buff dice", "Trapper dice", "Zombie dice", "Bomb dice", "Flamethrower dice", "Anti-pierce dice", "Effect amplifier dice", "Rainbow dice", "Storm dice", "Randomizer dice", "Sniper dice", "Laser dice"};
        names.addAll(Arrays.asList(namess));
        listeners.add(new EventListener() {
            @Override
            public void placed(GTile placed) {
                for (GTile[] someTiles : GlobalVariables.tiles) {
                    for (GTile tile : someTiles) {
                        if (tile.dice && tile.diceType == 16) {
                            tile.configureDice();
                        }
                    }
                }
            }

            @Override
            public void pickedUp(GTile pickedUp) {
                for (GTile[] someTiles : GlobalVariables.tiles) {
                    for (GTile tile : someTiles) {
                        if (tile.dice && tile.diceType == 16) {
                            tile.configureDice();
                        }
                    }
                }
            }

            @Override
            public void diceGrown(GTile grown) {
                for (GTile[] someTiles : GlobalVariables.tiles) {
                    for (GTile tile : someTiles) {
                        if (tile.dice && tile.diceType == 16) {
                            tile.configureDice();
                        }
                    }
                }
            }
        });
    }
    static{
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{5,6,1,6},{15,1,1,1},{5,3,1,30},{100,3,0,1},{20,5,3,3},{20,3,1,50},{50,2,3,1},{250,4,0,1},{50,7,3,5},{100,4,3,1},{100,3,1,50},{250,3,0,1},{100,25,1,10},{10,3,0.1,300},{500,3,2,20},{1500,4,0,1}};
            }

            @Override
            public Integer[][] healingWaves() {
                return new Integer[][]{{15,20,24},{1,2,3}};
            }

            @Override
            public Double[][] camoWaves() {
                return new Double[][]{{9.0,22.0},{1.0,0.5}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public ArrayList<Integer> reverseWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(17);
                returner.add(4);
                returner.add(13);
                return returner;
            }

            @Override
            public ArrayList<Integer> splittingWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(10);
                return returner;
            }

            @Override
            public Integer[][] flyingWaves() {
                return new Integer[][]{{13,17,22},{1,1,1}};
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                if (randomInt(1,3) < 3) {
                    return new double[]{wave * wave * wave * wave / 500, wave - 22, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
                else if (randomInt(1,2) == 1){
                    return new double[]{wave * wave * wave * wave / 50, wave - 22, 0, 1};
                }
                else{
                    return new double[]{wave * wave * wave * wave / 1500, (wave-22)*3, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public String name() {
                return "Normal";
            }

            @Override
            public Color difficultyColor() {
                return new Color(250,250,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{6,6,2,6},{20,1,1,5},{5,3,1,30},{100,3,0,1},{20,10,3,3},{20,3,1,50},{100,3,3,5},{250,4,0,1},{50,20,3,5},{250,4,3,5},{100,3,1,50},{500,5,0,1},{100,25,1,10},{10,4,0.1,300},{500,3,2,20},{2500,4,0,1}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                return new double[]{wave*wave*wave*wave/500,wave-22,25 * Math.pow(0.75,(wave-22)),((waveN-22)*10)};
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public String name() {
                return "Original";
            }

            @Override
            public Color difficultyColor() {
                return new Color(100,150,250);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public boolean rewardDice() {
                return true;
            }
            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,3,2,1},{2,4,0,1},{2,2,1,3},{4,3,0,1},{1,8,1,4},{2,8,0,1},{4,4,2,3},{3,5,1,5},{10,8,0,1},{4,12,2,4},{10,8,1,4},{5,6,1,20},{50,16,0,1},{10,25,3,3},{15,6,1,25},{50,6,2,5},{100,30,0,1},{25,40,3,5},{150,8,3,4},{80,6,1,40},{250,15,0,1},{50,50,1,5},{10,16,0.2,100},{250,10,2,20},{1000,16,0,1}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                ArrayList<Integer> returner  = new ArrayList<>();
                returner.add(5);
                return returner;
            }

            @Override
            public String name() {
                return "Speedy";
            }
            @Override
            public double[] generateFreeplayWave(int wave) {
                return new double[]{wave*wave*wave/50,(wave-22)*2,15 * Math.pow(0.75,(wave-22)),((waveN-22)*6)};
            }
            @Override
            public Color difficultyColor() {
                return new Color(250,125,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public boolean rewardDice() {
                return true;
            }
            @Override
            public double[][] getWaveData() {
                return new double[][] {{3,1,2,1},{5,2,0,1},{3,1,1,3},{15,1,0,1},{3,1,1,5},{6,3,0,1},{15,1,2,3},{7,2,1,7},{50,1,0,1},{25,4,2,4},{50,1,1,3},{20,2,1,10},{250,1,0,1},{50,5,3,3},{50,2,1,25},{250,1,3,4},{1000,1,0,1},{250,10,5,3},{1000,1,4,4},{250,1,1,50},{2500,1,0,1},{500,10,1,7},{100,1,0.3,200},{2500,1,3,10},{15000,1,0,1}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                ArrayList<Integer> returner  = new ArrayList<>();
                returner.add(19);
                return returner;
            }
            @Override
            public double[] generateFreeplayWave(int wave) {
                if (randomInt(1,3) < 3) {
                    return new double[]{wave * wave * wave * wave * wave / 1500,((wave-22)/5)+1,25 * Math.pow(0.75,(wave-22)),((waveN-22)*4)};
                }
                else if (randomInt(1,2) == 1){
                    return new double[]{wave * wave * wave * wave / 150, ((wave-22)/5)+1, 0, 1};
                }
                else{
                    return new double[]{wave * wave * wave * wave * wave / 4500, (wave-22), 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 4)};
                }
            }
            @Override
            public String name() {
                return "Tanky";
            }

            @Override
            public Color difficultyColor() {
                return new Color(250,125,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardDice() {
                return true;
            }
            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,0.5,3},{1,1,0.1,8},{2,1,0.5,5},{3,1,0.1,5},{1,1,0.2,10},{2,2,0.3,5},{3,2,0.5,8},{3,1,0.3,20},{10,2,0.3,2},{4,4,0.5,16},{20,1,0.1,5},{5,3,0.1,25},{10,3,0.3,10},{20,3,1,10},{20,3,0.3,50},{20,3,1,25},{100,4,0.3,3},{25,10,1,25},{100,4,1,15},{50,3,0.5,100},{100,5,0.5,5},{50,15,0.7,25},{7,4,0.05,500},{200,3,0.5,40},{500,3,1,10}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public String name() {
                return "Crowded";
            }
            @Override
            public double[] generateFreeplayWave(int wave) {
                return new double[]{wave*wave*wave/50,((wave-22)/2)+1,25 * Math.pow(0.5,(wave-22)),((waveN-22)*25)};
            }
            @Override
            public Color difficultyColor() {
                return new Color(150,250,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardDice() {
                return false;
            }
            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,2},{1,2,0,2},{2,1,1,3},{4,2,0,1},{1,1,1,5},{3,2,0,1},{4,2,2,3},{3,2,1,5},{10,1,0,1},{3,3,1,3},{10,1,1,4},{5,3,1,10},{25,3,0,1},{10,5,3,3},{15,3,1,25},{50,3,4,4},{100,4,0,1},{50,10,3,5},{100,4,3,5},{100,3,1,15},{250,3,0,1},{100,10,1,10},{10,4,0.3,100},{500,3,2,5},{1000,4,0,1}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public String name() {
                return "Easy";
            }
            @Override
            public double[] generateFreeplayWave(int wave) {
                return new double[]{wave*wave*wave/50,wave-22,25 * Math.pow(0.75,(wave-22)),((waveN-22)*5)};
            }
            @Override
            public Color difficultyColor() {
                return new Color(0,250,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public int[] dicePicked() {
                return new int[]{1,1,1,1,1};
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{6,6,2,6},{20,1,1,5},{5,3,1,30},{100,3,0,1},{20,10,3,3},{20,3,1,50},{100,3,3,5},{250,4,0,1},{50,20,3,5},{250,4,3,5},{100,3,1,50},{500,5,0,1},{100,25,1,10},{10,4,0.1,300},{500,3,2,20},{2500,4,0,1}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                ArrayList<Integer> returner  = new ArrayList<>();
                for (int i = 6; i < 21; i++) {
                    returner.add(i);
                }
                return returner;
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                return new double[]{wave*wave*wave/100,wave-24,25 * Math.pow(0.75,(wave-22)),((waveN-22)*10)};
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public String name() {
                return "Scratch";
            }

            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public Color difficultyColor() {
                return new Color(250,125,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardExtras() {
                return true;
            }
            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{3,4,0,1},{3,2,1,10},{6,6,2,6},{5,3,1,30},{20,10,3,3},{100,3,3,5},{50,20,3,5},{250,4,3,5},{100,3,1,50},{500,5,0,1},{100,25,1,10},{10,4,0.1,300},{500,3,2,20},{2500,4,0,1}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                ArrayList<Integer> returner  = new ArrayList<>();
                returner.add(5);
                returner.add(6);
                returner.add(9);
                return returner;
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                return new double[]{wave*wave*wave*wave*wave/500,wave-17,25 * Math.pow(0.75,(wave-22)),((waveN-22)*10)};
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public String name() {
                return "Hard";
            }

            @Override
            public Color difficultyColor() {
                return new Color(250,0,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{6,6,2,6},{20,1,1,5},{5,3,1,30},{100,3,0,1},{20,5,3,3},{20,3,1,50},{100,3,3,5},{250,4,0,1},{50,7,3,5},{250,4,3,5},{100,3,1,50},{500,5,0,1},{100,25,1,10},{10,3,0.1,300},{500,3,2,20},{2500,4,0,1}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                if (randomInt(1,3) < 3) {
                    return new double[]{wave * wave * wave * wave / 500, wave - 22, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
                else if (randomInt(1,2) == 1){
                    return new double[]{wave * wave * wave * wave / 50, wave - 22, 0, 1};
                }
                else{
                    return new double[]{wave * wave * wave * wave / 1500, (wave-22)*3, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public Integer[][] flyingWaves() {
                return new Integer[][]{{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24},{1,1,1,2,1,2,1,1,2,1,1,1,2,1,1,1,2,1,1,1,2,1,1,1,2}};
            }

            @Override
            public int freeplayIsFlying() {
                return 1;
            }

            @Override
            public String name() {
                return "Flying";
            }

            @Override
            public Color difficultyColor() {
                return new Color(250,50,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return true;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{5,6,1,6},{15,1,1,1},{5,3,1,30},{100,3,0,1},{20,5,3,3},{20,3,1,50},{100,3,3,5},{250,4,0,1},{50,7,3,5},{100,4,3,1},{100,3,1,50},{500,5,0,1},{100,25,1,10},{10,3,0.1,300},{500,3,2,20},{2500,4,0,1}};
            }

            @Override
            public Double[][] camoWaves() {
                return new Double[][]{{9.0,22.0},{1.0,0.5}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public ArrayList<Integer> reverseWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                for (int i = 0; i < 25; i++) {
                    if (i != 17 && i != 4 && i != 13){
                        returner.add(i);
                    }
                }
                return returner;
            }

            @Override
            public ArrayList<Integer> splittingWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(10);
                return returner;
            }

            @Override
            public Integer[][] flyingWaves() {
                return new Integer[][]{{13,17,22},{1,1,1}};
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                if (randomInt(1,3) < 3) {
                    return new double[]{wave * wave * wave * wave / 500, wave - 22, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
                else if (randomInt(1,2) == 1){
                    return new double[]{wave * wave * wave * wave / 50, wave - 22, 0, 1};
                }
                else{
                    return new double[]{wave * wave * wave * wave / 1500, (wave-22)*3, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public boolean freeplayIsReverse() {
                return true;
            }

            @Override
            public String name() {
                return "Reverse";
            }

            @Override
            public Color difficultyColor() {
                return new Color(250,250,0);
            }
        });
        levels.add(new LevelCode() {

            @Override
            public int maxDiceLevel() {
                return 7;
            }

            @Override
            public int[] dicePicked() {
                return new int[]{2,1,1,1,1,1,1};
            }

            @Override
            public boolean rewardDice() {
                return false;
            }
            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,0.5,3},{1,1,0.1,8},{2,1,0.5,5},{3,1,0.1,5},{1,1,0.2,10},{2,2,0.3,5},{3,2,0.5,8},{3,1,0.3,20},{10,2,0.3,2},{4,4,0.5,16},{20,1,0.1,5},{5,3,0.1,25},{10,3,0.3,10},{20,3,1,10},{20,3,0.3,50},{20,3,1,25},{100,4,0.3,3},{25,10,1,25},{100,4,1,15},{50,3,0.5,100},{100,5,0.5,5},{50,15,0.7,25},{7,4,0.05,500},{200,3,0.5,40},{500,3,1,10}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(2);
                returner.add(4);
                returner.add(5);
                returner.add(6);
                returner.add(9);
                returner.add(10);
                for (int i = 13; i < 20; i++) {
                    returner.add(i);
                }
                return returner;
            }

            @Override
            public String name() {
                return "Group Damage";
            }
            @Override
            public double[] generateFreeplayWave(int wave) {
                return new double[]{wave*wave*wave/50,((wave-22)/2)+1,25 * Math.pow(0.5,(wave-22)),((waveN-22)*25)};
            }
            @Override
            public Color difficultyColor() {
                return new Color(196,250,0);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return false;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{5,6,1,6},{15,1,1,1},{5,3,1,30},{100,3,0,1},{20,5,3,3},{20,3,1,50},{50,2,3,1},{250,4,0,1},{50,7,3,5},{100,4,3,1},{100,3,1,50},{250,3,0,1},{100,25,1,10},{10,3,0.1,300},{500,3,2,20},{1500,4,0,1}};
            }

            @Override
            public Integer[][] healingWaves() {
                return new Integer[][]{{15,20,24},{1,2,3}};
            }

            @Override
            public Double[][] camoWaves() {
                return new Double[][]{{9.0,22.0},{1.0,0.5}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public ArrayList<Integer> reverseWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(17);
                returner.add(4);
                returner.add(13);
                return returner;
            }

            @Override
            public ArrayList<Integer> splittingWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(10);
                return returner;
            }

            @Override
            public Integer[][] flyingWaves() {
                return new Integer[][]{{13,17,22},{1,1,1}};
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                if (randomInt(1,3) < 3) {
                    return new double[]{wave * wave * wave * wave / 500, wave - 22, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
                else if (randomInt(1,2) == 1){
                    return new double[]{wave * wave * wave * wave / 50, wave - 22, 0, 1};
                }
                else{
                    return new double[]{wave * wave * wave * wave / 1500, (wave-22)*3, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
            }

            @Override
            public int maxDiceLevel() {
                return 8;
            }

            @Override
            public int[] dicePicked() {
                return new int[]{1,1};
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public String name() {
                return "Duo";
            }

            @Override
            public Color difficultyColor() {
                return new Color(250,125,250);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return false;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{5,6,1,6},{15,1,1,1},{5,3,1,30},{100,3,0,1},{20,5,3,3},{20,3,1,50},{50,2,3,1},{250,4,0,1},{50,7,3,5},{100,4,3,1},{100,3,1,50},{250,3,0,1},{100,25,1,10},{10,3,0.1,300},{500,3,2,20},{1500,4,0,1}};
            }

            @Override
            public Integer[][] healingWaves() {
                return new Integer[][]{{15,20,24},{1,2,3}};
            }

            @Override
            public Double[][] camoWaves() {
                return new Double[][]{{9.0,22.0},{1.0,0.5}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public ArrayList<Integer> reverseWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(17);
                returner.add(4);
                returner.add(13);
                return returner;
            }

            @Override
            public ArrayList<Integer> splittingWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(10);
                return returner;
            }

            @Override
            public Integer[][] flyingWaves() {
                return new Integer[][]{{13,17,22},{1,1,1}};
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                if (randomInt(1,3) < 3) {
                    return new double[]{wave * wave * wave * wave / 500, wave - 22, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
                else if (randomInt(1,2) == 1){
                    return new double[]{wave * wave * wave * wave / 50, wave - 22, 0, 1};
                }
                else{
                    return new double[]{wave * wave * wave * wave / 1500, (wave-22)*3, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
            }

            @Override
            public int maxDiceLevel() {
                return 1;
            }

            @Override
            public int[] dicePicked() {
                return new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
            }

            @Override
            public boolean rewardDice() {
                return false;
            }

            @Override
            public String name() {
                return "CHAOS";
            }

            @Override
            public Color difficultyColor() {
                return new Color(125,125,125);
            }
        });
        levels.add(new LevelCode() {
            @Override
            public boolean rewardUpgrades() {
                return false;
            }

            @Override
            public double[][] getWaveData() {
                return new double[][] {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{5,6,1,6},{15,1,1,1},{5,3,1,30},{100,3,0,1},{20,5,3,3},{20,3,1,50},{50,2,3,1},{250,4,0,1},{50,7,3,5},{100,4,3,1},{100,3,1,50},{250,3,0,1},{100,25,1,10},{10,3,0.1,300},{500,3,2,20},{1500,4,0,1}};
            }

            @Override
            public Integer[][] healingWaves() {
                return new Integer[][]{{15,20,24},{1,2,3}};
            }

            @Override
            public Double[][] camoWaves() {
                return new Double[][]{{9.0,22.0},{1.0,0.5}};
            }

            @Override
            public ArrayList<Integer> bannedTowers() {
                return new ArrayList<>();
            }

            @Override
            public ArrayList<Integer> reverseWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(17);
                returner.add(4);
                returner.add(13);
                return returner;
            }

            @Override
            public ArrayList<Integer> splittingWaves() {
                ArrayList<Integer> returner = new ArrayList<>();
                returner.add(10);
                return returner;
            }

            @Override
            public Integer[][] flyingWaves() {
                return new Integer[][]{{13,17,22},{1,1,1}};
            }

            @Override
            public double[] generateFreeplayWave(int wave) {
                if (randomInt(1,3) < 3) {
                    return new double[]{wave * wave * wave * wave / 500, wave - 22, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
                else if (randomInt(1,2) == 1){
                    return new double[]{wave * wave * wave * wave / 50, wave - 22, 0, 1};
                }
                else{
                    return new double[]{wave * wave * wave * wave / 1500, (wave-22)*3, 25 * Math.pow(0.75, (wave - 22)), ((waveN - 22) * 10)};
                }
            }

            @Override
            public int maxDiceLevel() {
                return 8;
            }

            @Override
            public int[] dicePicked() {
                return new int[]{1};
            }

            @Override
            public boolean rewardDice() {
                return true;
            }

            @Override
            public String name() {
                return "Solo";
            }

            @Override
            public Color difficultyColor() {
                return new Color(125,125,250);
            }
        });
    }
    static {
        maps.add(new MapCode() {
            @Override
            public GPoint getPositionOf(int x, int y) {
                GPoint p = new GPoint();
                p.setLocation(x*38,(y+((double)x%2/2))*50);
                return p;
            }

            @Override
            public GPolygon configureShape(int x, int y, int size) {
               GPolygon g = new GPolygon();
                g.addVertex((double)size*1/4,0);
                g.addVertex((double)size*3/4,0);
                g.addVertex(size,(double)size/2);
                g.addVertex((double)size*3/4,size);
                g.addVertex((double)size*1/4,size);
                g.addVertex(0,(double)size/2);
                return g;
            }

            @Override
            public GPoint[] polygonPoints(int x, int y, int size) {
                GPoint[] g = new GPoint[6];
                g[0] = new GPoint((double)size*1/4,0);
                g[1] = new GPoint((double)size*3/4,0);
                g[2] = new GPoint(size,(double)size/2);
                g[3] = new GPoint((double)size*3/4,size);
                g[4] = new GPoint((double)size*1/4,size);
                g[5] = new GPoint(0,(double)size/2);
                return g;
            }

            @Override
            public GPolygon[] configureSynergy(int x, int y, int size, Color[] colors) {
                GPolygon[] returner = new GPolygon[2];
                for (int i = 0; i < 2; i++) {
                    GPolygon p = new GPolygon();
                    p.setFillColor(new Color(0,125,0));
                    p.setColor(i == 0 ? colors[0] : colors[1]);
                    p.setFilled(true);
                    p.addVertex(size/2,0);
                    p.addVertex(size/4 + (i*size/2),0);
                    p.addVertex(size*i,size/2);
                    p.addVertex(size/4 + (i*size/2),size);
                    p.addVertex(size/2,size);
                    returner[i] = p;
                }
                return returner;
            }

            @Override
            public boolean isNextTo(int x1, int y1, int x2, int y2) {
                if (x2%2==1){
                    if (y2 == y1-1){
                        return Math.abs(x1 - x2) < 2;
                    }
                    else if (y2 == y1){
                        return Math.abs(x1 - x2) == 1;
                    }
                    else if (y2-1 == y1){
                        return x1 == x2;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    if (y2-1 == y1){
                        return Math.abs(x1 - x2) < 2;
                    }
                    else if (y2 == y1){
                        return Math.abs(x1 - x2) == 1;
                    }
                    else if (y2 == y1-1){
                        return x1 == x2;
                    }
                    else{
                        return false;
                    }
                }
            }
        });
        maps.add(new MapCode() {
            @Override
            public GPoint getPositionOf(int x, int y) {
                GPoint p = new GPoint();
                p.setLocation(x*50,y*50);
                return p;
            }

            @Override
            public GPolygon configureShape(int x, int y, int size) {
                GPolygon g = new GPolygon();
                g.addVertex(0,0);
                g.addVertex(size,0);
                g.addVertex(size,size);
                g.addVertex(0,size);
                return g;
            }
            @Override
            public GPolygon[] configureSynergy(int x, int y, int size, Color[] colors) {
                GPolygon[] returner = new GPolygon[2];
                for (int i = 0; i < 2; i++) {
                    GPolygon p = new GPolygon();
                    p.setFillColor(new Color(0,125,0));
                    p.setColor(i == 0 ? colors[0] : colors[1]);
                    p.setFilled(true);
                    p.addVertex(size/2,0);
                    p.addVertex(i*size,0);
                    p.addVertex(size*i,size);
                    p.addVertex(size/2,size);
                    returner[i] = p;
                }
                return returner;
            }
            @Override
            public GPoint[] polygonPoints(int x, int y, int size) {
                GPoint[] g = new GPoint[4];
                g[0] = new GPoint(0,0);
                g[1] = new GPoint( size,0);
                g[2] = new GPoint(size,size);
                g[3] = new GPoint(0,size);
                return g;
            }
            @Override
            public boolean isNextTo(int x1, int y1, int x2, int y2) {
                if (x1 == x2){
                    if (y1 == y2){
                        return false;
                    }
                    return Math.abs(y1 - y2) < 2;
                }
                else if (y1 == y2){
                    return Math.abs(x1 - x2) < 2;
                }
                return false;
            }
        });
        maps.add(new MapCode() {
            @Override
            public GPoint getPositionOf(int x, int y) {
                GPoint p = new GPoint();
                p.setLocation(x*25,y*50);
                return p;
            }

            @Override
            public GPolygon configureShape(int x, int y, int size) {
                GPolygon p = new GPolygon();
                if ((y+x)%2 == 0){
                    p.addVertex(size/2,0);
                    p.addVertex(0,size);
                    p.addVertex(size,size);
                }
                else{
                    p.addVertex(0,0);
                    p.addVertex(size,0);
                    p.addVertex(size/2,size);
                }
                return p;
            }
            @Override
            public GPoint[] polygonPoints(int x, int y, int size) {
                GPoint[] g = new GPoint[3];
                if ((y+x)%2 == 0){
                    g[0] = new GPoint(size/2,0);
                    g[1] = new GPoint(0,size);
                    g[2] = new GPoint(size,size);
                }
                else{
                    g[0] = new GPoint(0,0);
                    g[1] = new GPoint( size,0);
                    g[2] = new GPoint(size/2,size);
                }
                return g;
            }
            @Override
            public GPolygon[] configureSynergy(int x, int y, int size, Color[] colors) {
                GPolygon[] p = new GPolygon[2];
                for (int i = 0; i < 2; i++) {
                    p[i] = new GPolygon();
                    if ((y+x)%2 == 0){
                        p[i].addVertex(size/2,0);
                        p[i].addVertex(size/2,size);
                        p[i].addVertex(size*i,size);
                    }
                    else{
                        p[i].addVertex(size*i,0);
                        p[i].addVertex(size/2,0);
                        p[i].addVertex(size/2,size);
                    }
                    p[i].setFillColor(new Color(0,125,0));
                    p[i].setColor(i == 0 ? colors[0] : colors[1]);
                    p[i].setFilled(true);
                }
                return p;
            }

            @Override
            public boolean isNextTo(int x1, int y1, int x2, int y2) {
                if (y1 == y2){
                    if (x1 == x2){
                        return false;
                    }
                    if (Math.abs(x1-x2) < 2){
                        return true;
                    }
                }
                if (x1 == x2) {
                    if ((y1 + x1) % 2 == 1) {
                        if (y1 - y2 == 1){
                            return true;
                        }
                    } else {
                        if (y2 - y1 == 1){
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void configureDots(GTile tile) {
                if (tile.diceAmount%2==1){
                    tile.o.add(new GOval((double)tile.size/2-(double)tile.size/14,(double)tile.size/2-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                }
                switch(tile.diceAmount/2) {
                    case 3:
                            tile.o.add(new GOval((double)tile.size/3-(double)tile.size/14,(double)tile.size/2-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                            tile.o.add(new GOval((double)tile.size*2/3-(double)tile.size/14,(double)tile.size/2-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                        case 2:
                        tile.o.add(new GOval((double)tile.size/2-(double)tile.size/14,(double)tile.size/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                        tile.o.add(new GOval((double)tile.size/2-(double)tile.size/14,(double)tile.size*3/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                    case 1:
                        switch ((tile.x+tile.y)%2){
                            case 0 ->
                                    {
                                        tile.o.add(new GOval((double)tile.size*3/4-(double)tile.size/14,(double)tile.size*3/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                                        tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size*3/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                                    }
                            case 1 ->
                                    {
                                        tile.o.add(new GOval((double)tile.size*3/4-(double)tile.size/14,(double)tile.size/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                                        tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                                    }
                        }

                }
            }
        });
        maps.add(new MapCode() {
            @Override
            public GPoint getPositionOf(int x, int y) {
                return new GPoint((((x/4)+((double)y%2/2))*150)+75,(y+1)*75);
            }

            @Override
            public GPoint[] polygonPoints(int x, int y, int size) {
                GPoint[] returner = new GPoint[5];
                switch(x%4){
                    case 0 -> {
                        returner[0] = new GPoint(-size/2,0);
                        returner[1] = new GPoint(-3*size/4,-size*3/4);
                        returner[2] = new GPoint(-size*3/2,-size/2);
                        returner[3] = new GPoint(-size*3/2,size/2);
                        returner[4] = new GPoint(-3*size/4,size*3/4);
                    }
                    case 1->{
                        returner[0] = new GPoint(size/2,0);
                        returner[1] = new GPoint(-size/2,0);
                        returner[2] = new GPoint(-3*size/4,-size*3/4);
                        returner[3] = new GPoint(0,-size);
                        returner[4] = new GPoint(3*size/4,-size*3/4);
                    }



                    case 2 ->{
                        returner[0] = new GPoint(size/2,0);
                        returner[1] = new GPoint(-size/2,0);
                        returner[2] = new GPoint(-3*size/4,size*3/4);
                        returner[3] = new GPoint(0,size);
                        returner[4] = new GPoint(3*size/4,size*3/4);

                    }
                    case 3->{
                        returner[0] = new GPoint(size/2,0);
                        returner[1] = new GPoint(3*size/4,-size*3/4);
                        returner[2] = new GPoint(size*3/2,-size/2);
                        returner[3] = new GPoint(size*3/2,size/2);
                        returner[4] = new GPoint(3*size/4,size*3/4);
                    }

                }
                return returner;
            }

            @Override
            public GPolygon configureShape(int x, int y, int size) {
                GPolygon p = new GPolygon();
                switch(x%4){
                    case 0 -> {
                        p.addVertex(-size/2,0);
                        p.addVertex(-3*size/4,-size*3/4);
                        p.addVertex(-size*3/2,-size/2);
                        p.addVertex(-size*3/2,size/2);
                        p.addVertex(-3*size/4,size*3/4);
                            }
                            case 1->{
                                p.addVertex(size/2,0);
                                p.addVertex(-size/2,0);
                                p.addVertex(-3*size/4,-size*3/4);
                                p.addVertex(0,-size);
                                p.addVertex(3*size/4,-size*3/4);
                            }



                    case 2 ->{
                        p.addVertex(size/2,0);
                        p.addVertex(-size/2,0);
                        p.addVertex(-3*size/4,size*3/4);
                        p.addVertex(0,size);
                        p.addVertex(3*size/4,size*3/4);

                    }
                            case 3->{
                                p.addVertex(size/2,0);
                                p.addVertex(3*size/4,-size*3/4);
                                p.addVertex(size*3/2,-size/2);
                                p.addVertex(size*3/2,size/2);
                                p.addVertex(3*size/4,size*3/4);
                            }

                }
                return p;
            }

            @Override
            public GPoint getCenter(int x, int y, int size) {
                switch(x%4){
                    case 0 -> {
                        return new GPoint(-size,0);
                    }
                    case 1->{
                        return new GPoint(0,-size/2);
                    }
                    case 2 ->{
                        return new GPoint(0,size/2);
                    }
                    case 3->{
                        return new GPoint(size,0);
                    }

                }
                return new GPoint(0,0);
            }

            @Override
            public GPolygon[] configureSynergy(int x, int y, int size, Color[] colors) {
                return new GPolygon[0];
            }

            @Override
            public boolean isNextTo(int x1, int y1, int x2, int y2) {
                if (y1==y2) {
                    if (Math.abs(x1 - x2) == 1) {
                        return true;
                    }
                    if (x1 % 4 > 1) {
                        if (x1 - x2 == 2){
                            return true;
                        }
                    } else {
                        if (x2-x1 == 2){
                            return true;
                        }
                    }
                }
                if (y1%2==1){
                    if (y1-y2==1){
                        if (switch (x1%4){
                            case 3 -> x2-x1==3;
                            case 0 -> x2-x1==2;
                            case 1 -> x2-x1==3 || x2-x1==2;
                            default -> false;
                        }){
                            return true;
                        }
                    }
                    if (y2-y1==1){
                        if (switch (x1%4){
                            case 3 -> x2-x1==2;
                            case 0 -> x2-x1==1;
                            case 2 -> x2-x1==1 || x2-x1==2;
                            default -> false;
                        }){
                            return true;
                        }
                    }
                }
                else{
                    if (y1-y2==1){
                        if (switch (x1%4){
                            case 3 -> x1-x2==1;
                            case 0 -> x1-x2==2;
                            case 1 -> x1-x2==1 || x1-x2==2;
                            default -> false;
                        }){
                            return true;
                        }
                    }
                    if (y2-y1==1){
                        if (switch (x1%4){
                            case 3 -> x1-x2==2;
                            case 0 -> x1-x2==3;
                            case 2 -> x1-x2==3 || x1-x2==2;
                            default -> false;
                        }){
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void configureDots(GTile tile) {
                    if (tile.diceAmount%2==1){
                        tile.o.add(new GOval((double)tile.size/-14,(double)tile.size/-14,(double)tile.size/7,(double)tile.size/7));
                    }
                    switch(tile.diceAmount/2){
                        case 3:
                            tile.o.add(new GOval((double)tile.size/-4-(double)tile.size/14,(double)tile.size/-14,(double)tile.size/7,(double)tile.size/7));
                            tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size/-14,(double)tile.size/7,(double)tile.size/7));
                        case 2:
                            tile.o.add(new GOval((double)tile.size/-4-(double)tile.size/14,(double)tile.size/-4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                            tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                        case 1:
                            tile.o.add(new GOval((double)tile.size/-4-(double)tile.size/14,(double)tile.size/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                            tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size/-4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                    }
            }
        });

    }
    static volatile String location;
    static ArrayList<Integer> v = new ArrayList<>();
    static File f;
    static boolean wave = false;
    static boolean FF = false;
    static volatile boolean enemiesLeft = false;
    static int ticksTaken = 0;
    static int waveN = 0;
    static int lives = 100;
    static int levelNumber = 3;
    static GTile pickedUp;
    static MapCode map = maps.get(0);
    public static ArrayList<GApple> enemies = new ArrayList<>();
    public static ArrayList<GApple> targets = new ArrayList<>();
    static ArrayList<GApple> zombies = new ArrayList<>();
    //life,speed,spacing,amount
    static ArrayList<Integer> bannedTowers = new ArrayList<>();
    static double[][] waveData = {{1,1,2,3},{2,2,0,2},{2,1,1,5},{5,2,0,1},{1,1,1,10},{3,4,0,1},{5,2,2,4},{3,2,1,10},{20,2,0,1},{6,6,2,6},{20,1,1,5},{5,3,1,30},{100,3,0,1},{20,10,3,3},{20,3,1,50},{100,3,3,5},{250,4,0,1},{50,20,3,5},{250,4,3,5},{100,3,1,50},{500,5,0,1},{100,25,1,10},{10,4,0.1,300},{500,3,2,20},{2500,4,0,1}};
    public static GTile[][] tiles = new GTile[20][10];
    static ArrayList<GProjectile> projectiles = new ArrayList<>();
    public static ADD screen;
    static volatile boolean diceGotten = false;
    static volatile boolean already = false;
    static volatile boolean starting = false;
    static ArrayList<GTile> path = new ArrayList<>();
    static ArrayList<GTile> truePath = new ArrayList<>();
    public static void initSynergies(){
            synergies.add(new SynergyCode() {
                @Override
                public Color getColor2(GTile tile) {
                    return Color.yellow;
                }

                @Override
                public int[] synergyNumbers() {
                    return new int[]{4,14};
                }

                @Override
                public void run(GProjectile bullet) {
                    if (bullet.target != null){
                        bullet.moveTowards(bullet.target.getX(), bullet.target.getY(),21);
                        for (GApple enemy: bullet.getTouching()) {
                            enemy.damage(Math.pow(1.5, bullet.pierceLeft));
                            bullet.hit.add(enemy);
                            if (enemy.pierce) {
                                bullet.pierceLeft--;
                                enemy.pierce=false;
                            }
                            bullet.target = null;
                            break;
                        }

                    }
                    else {
                        GApple target = null;
                        for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
                            if (!bullet.hit.contains(GlobalVariables.enemies.get(i))) {
                                target = GlobalVariables.enemies.get(i);
                                break;
                            }
                        }
                        if (target == null) {
                            bullet.remove();
                        } else {
                            if (bullet.pierceLeft < 1) {
                                bullet.pierceLeft--;
                                if (bullet.pierceLeft < -10) {
                                    bullet.remove();
                                }
                            } else {
                                GLine l = new GLine(bullet.hit.get(bullet.hit.size() - 1).getX() - bullet.getX(), bullet.hit.get(bullet.hit.size() - 1).getY() - bullet.getY(), target.getX() - bullet.getX(), target.getY() - bullet.getY());
                                l.setColor(Color.pink);
                                bullet.add(l);
                                bullet.hit.add(target.damage(Math.pow(2, bullet.pierceLeft)));
                                bullet.hit.add(target);
                                if (target.pierce) {
                                    bullet.pierceLeft--;
                                    target.pierce = false;
                                }
                            }
                        }
                    }
                }

                @Override
                public Color getColor(GTile tile) {
                    return Color.pink;
                }

                @Override
                public String description() {
                    return "Lighting chains grow longer and longer with this anti-pierce lighting synergy";
                }

                @Override
                public String name() {
                    return "Anti-apple lighting";
                }
                @Override
                public int pierce(GProjectile bullet){
                    return 9;
                }

                @Override
                public int getRange(GTile tile) {
                    return 4;
                }

                @Override
                public int getAttackSpeed(GTile tile) {
                    return SynergyCode.super.getAttackSpeed(tile)*2;
                }
            });
            synergies.add(new SynergyCode() {
                @Override
                public Color getColor2(GTile tile) {
                    return Color.red;
                }

                @Override
                public int[] synergyNumbers() {
                    return new int[]{2,15};
                }

                @Override
                public int getRange(GTile tile) {
                    return 3;
                }

                @Override
                public int pierce(GProjectile bullet) {
                    return 20;
                }

                @Override
                public void run(GProjectile bullet) {
                    bullet.moveToTargetIfExists(14);
                    for (GApple enemy : bullet.getTouching()) {
                        if (bullet.pierceLeft > 0) {
                            enemy.fire += 25;
                            if (enemy.fireDmg < 10) {
                                enemy.fireDmg = 10;
                            }
                            else if ((long)enemy.fireDmg*2 > Integer.MAX_VALUE){
                                enemy.fireDmg*=2;
                            }
                        }
                        bullet.pierceStuff(enemy);
                    }
                }

                @Override
                public Color getColor(GTile tile) {
                    return Color.magenta;
                }

                @Override
                public String description() {
                    return "Fire grows stronger and stronger with this effect amplifier fire combo";
                }

                @Override
                public String name() {
                    return "Amplified fire";
                }
            });
            synergies.add(new SynergyCode() {
                @Override
                public Color getColor2(GTile tile) {
                    return new Color(0,178,255);
                }

                @Override
                public int pierce(GProjectile bullet) {
                    return 7;
                }

                @Override
                public int getRange(GTile tile) {
                    return 2;
                }

                @Override
                public int[] synergyNumbers() {
                    return new int[]{13,19};
                }

                @Override
                public void run(GProjectile bullet) {
                    bullet.moveToTargetIfExists(21);
                    if (bullet.calculateDistance(bullet.owner.getX()-bullet.owner.getWidth()/2,bullet.owner.getY()-bullet.owner.getHeight()/2) > (bullet.owner.getRange()*50)+50){
                        bullet.remove();
                    }
                    for (GApple enemy: bullet.getTouching()) {
                        if (bullet.pierceLeft > 0){
                            enemy.damage(enemy.life);
                        }
                        bullet.pierceStuff(enemy);
                    }
                }

                @Override
                public Color getColor(GTile tile) {
                    return new Color(125,0,0);
                }

                @Override
                public String description() {
                    return "This flamethrower sniper synergy allows the sniper to shoot faster at the cost of range.";
                }

                @Override
                public String name() {
                    return "Snipe thrower";
                }
            });
            synergies.add(new SynergyCode() {
                @Override
                public Color getColor2(GTile tile) {
                    return Color.orange;
                }

                @Override
                public int[] synergyNumbers() {
                    return new int[]{9,6};
                }

                @Override
                public void run(GProjectile bullet) {

                }

                @Override
                public int getRange(GTile tile) {
                    return 1;
                }

                @Override
                public void tick(GTile tile) {
                    tile.nextTo = tile.getNextTo();
                    for (GTile t: tile.getNextTo()) {
                        if (t.dice && t.diceType != 9 && t.diceType != tile.diceType){
                            for (int i = 0; i < (tile.ticksLeft/4); i++) {
                                t.tick();
                            }
                        }
                    }
                }

                @Override
                public int getAttackSpeed(GTile tile) {
                    return 4;
                }

                @Override
                public void turnPassed(GTile tile) {
                    tile.ticksLeft++;
                }

                @Override
                public Color getColor(GTile tile) {
                    return new Color(150, 75, 255);
                }

                @Override
                public String description() {
                    return "This growth buff synergy allows the buff to get stronger every 4 turns. It also acts as a buff distributer and sends any buffs given to it to the tiles next to it.";
                }

                @Override
                public String name() {
                    return "Growing faster";
                }
            });
            synergies.add(new SynergyCode() {
                @Override
                public Color getColor2(GTile tile) {
                    return Color.cyan;
                }

                @Override
                public int getRange(GTile tile) {
                    return 2;
                }

                @Override
                public int[] synergyNumbers() {
                    return new int[]{5,10};
                }

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
                                enemy.frozen += bullet.pierceLeft;
                                double addition =(double)bullet.pierceLeft/10;
                                enemy.damage(bullet.pierceLeft);
                                enemy.vunrablility += addition;
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
                public void fire(GTile tile) {
                    tile.nextTo = tile.getPathInRange();
                    if (tile.nextTo.size() > 0) {
                        tile.projectiles.add(new GProjectile(tile.diceAmount, tile.nextTo.get(GlobalVariables.randomInt(0, tile.nextTo.size() - 1)), tile));
                        tile.ticksLeft = tile.getAttackSpeed();
                    }
                }

                @Override
                public int pierce(GProjectile bullet) {
                    return 42;
                }

                @Override
                public Color getColor(GProjectile bullet) {
                    return Color.cyan;
                }

                @Override
                public Color getColor(GTile tile) {
                    return new Color(150, 255, 150);
                }

                @Override
                public String description() {
                    return "These traps freeze enemies, look out!";
                }

                @Override
                public String name() {
                    return "Icy traps";
                }
            });
            synergies.add(new SynergyCode() {
                @Override
                public Color getColor2(GTile tile) {
                    return Color.BLUE;
                }

                @Override
                public int getRange(GTile tile) {
                    return 4;
                }

                @Override
                public int[] synergyNumbers() {
                    return new int[]{8,3};
                }

                @Override
                public void run(GProjectile bullet) {
                    if (!bullet.out){
                        bullet.moveToTargetIfExists(15 - bullet.pierceLeft);
                        if (bullet.target == null){
                            bullet.out = true;
                        }
                        else if (bullet.isTouching(bullet.target)){
                            bullet.out = true;
                            bullet.target.damage(bullet.pierceLeft*bullet.pierceLeft*bullet.pierceLeft*bullet.pierceLeft*bullet.target.vunrablility + 3);
                            bullet.hit.add(bullet.target);

                        }
                        if (bullet.out){
                            int i = 0;
                            for (GApple enemy: enemies) {
                                if (i >= bullet.pierceLeft*3){
                                    return;
                                }
                                if (!bullet.hit.contains(enemy)){
                                    if (bullet.calculateDistance(enemy.getX(),enemy.getY()) > 175 - (bullet.pierceLeft*50)){
                                        GProjectile newProjectile = new GProjectile(bullet.type,bullet.lvl,enemy,bullet);
                                        newProjectile.pierceLeft = bullet.pierceLeft-1;
                                        projectiles.add(newProjectile);
                                        i++;
                                    }
                                }
                            }

                        }
                    }
                    else{
                        bullet.setLocation(bullet.getX() - bullet.getWidth() * 0.02, bullet.getY() - bullet.getHeight() * 0.02);
                        bullet.scale(1.04);
                        for (GApple enemy: enemies) {
                            if (bullet.isTouching(enemy)){
                                enemy.damage(bullet.pierceLeft*bullet.pierceLeft*bullet.pierceLeft*bullet.pierceLeft*enemy.vunrablility + 3);
                                bullet.hit.add(enemy);
                                break;
                            }
                        }
                        if (bullet.getWidth() > 29 + (20 * bullet.pierceLeft)) {
                            bullet.remove();
                        }
                    }
                }

                @Override
                public int pierce(GProjectile bullet) {
                    return 3;
                }

                @Override
                public Color getColor(GProjectile bullet) {
                    return new Color(75,30,25);
                }

                @Override
                public Color getColor(GTile tile) {
                    return new Color(75,30,25);
                }

                @Override
                public String description() {
                    return "Multi water bombs explode a bunch of times.";
                }

                @Override
                public String name() {
                    return "Multi Water Bombs";
                }
            });
            for (SynergyCode synergy: synergies) {
                synergyNames.add(synergy.name());
                colors.add(synergy);
                GTile.code.add(synergy);
                GProjectile.code.add(synergy);
            }

    }
    public static void tick() {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).tick();
        }
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).tick();
        }
        for (GTile[] someTiles : tiles) {
            for (GTile tile : someTiles) {
                if (tile.dice) {
                    tile.tick();
                }
            }
        }
        for (int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).tick();
        }
        Mod.tickAll();
        try {
            enemies.sort((o1, o2) -> {
                if (o1.pathAmount > o2.pathAmount) {
                    return -1;
                } else if (o2.pathAmount > o1.pathAmount) {
                    return 1;
                } else {
                    if (o1.pathLeft < o2.pathLeft) {
                        return -1;
                    } else if (o2.pathLeft < o1.pathLeft) {
                        return 1;
                    }
                    return 0;
                }
            });
            targets.sort((o1, o2) -> {
                if (o1.pathAmount > o2.pathAmount) {
                    return -1;
                } else if (o2.pathAmount > o1.pathAmount) {
                    return 1;
                } else {
                    if (o1.pathLeft < o2.pathLeft) {
                        return -1;
                    } else if (o2.pathLeft < o1.pathLeft) {
                        return 1;
                    }
                    return 0;
                }
            });
        } catch (ConcurrentModificationException ignored) {

        }
        if (lives <= 0) {
            Dialog.showMessage("You lose!");
            screen.exit();
        }
        ticksTaken++;
    }
    public static boolean isNextTo(int x1, int y1, int x2, int y2){
        return map.isNextTo(x1, y1, x2, y2);
    }
    public static GPoint getPositionOf(int x, int y){
        return map.getPositionOf(x,y);
    }
    public static GTile getGTileAt(int x, int y){
        if (x/38%2==0){
            return tiles[x/38][(y/50)];
        }
        else{
            return tiles[x/38][((y-25)/50)];
        }
    }
    public static int randomInt(int from, int to){
        return (int)(Math.random()*(to-from+1)) + from;
    }
    public static boolean pathFind(boolean enemyFind){
        int skips = 0;
        if (waveN < waveData.length) {
            if (flyingWaves.contains(waveN)){
                skips = flyingAmounts.get(flyingWaves.indexOf(waveN));
            }
        }
        else{
            skips = levels.get(levelNumber).freeplayIsFlying();
        }
        skips = enemyFind ? skips : 0;
        ArrayList<GTile> possible = null;
        for (GTile t : tiles[0]) {
            for (GTile[] someTiles : tiles) {
                for (GTile s : someTiles) {
                    s.z = 300;
                }
            }
            ArrayList<GTile> s = t.pathFind(new ArrayList<>(),skips,0);
            if (possible != null){
                if (s != null) {
                    if (s.size() < possible.size()) {
                        possible = s;
                    }
                }
            }
            else{
                possible = s;
            }
        }
        if(possible == null){
            return false;
        }
        truePath = possible;
        if (enemyFind) {
            path = possible;
        }
        for (GTile[] sometile:
             tiles) {
            for (GTile tile:
                 sometile) {
                tile.tick();
            }
        }
        return true;
    }
    public static void startWave()  {
        if (already){
            return;
        }
        already = true;
        for (EventListener listener: listeners) {
            listener.roundStarted(waveN);
        }
        pathFind(true);
        wave = true;
        enemiesLeft = true;
        ticksTaken = 0;

        // make enemies properly spaced and stuff
        if (waveN < waveData.length) {
            for (int i = 0; i < waveData[waveN][3]; i++) {
                boolean camo = camoWaves.contains((double)waveN);
                if (camo){
                    double camoAmount = camoAmounts.get(camoWaves.indexOf((double)waveN));
                    if (camoAmount >= 1){
                        if (i%(camoAmount+1) >= camoAmount){
                            camo = false;
                        }
                    }
                    else{
                        camoAmount = 1/camoAmount;
                        if (i%(camoAmount+1) < camoAmount){
                            camo = false;
                        }
                    }
                }
                GApple p = new GApple((int)waveData[waveN][0], (int)waveData[waveN][1],flyingWaves.contains(waveN), splitting.contains(waveN),camo,reverse.contains(waveN),healingWaves.contains(waveN) ? healingAmounts.get(healingWaves.indexOf(waveN)) : 0, summons.get(waveN) == null ? 0 : summons.get(waveN));
                screen.addToScreen(p);
                enemiesLeft = true;
                for (int j = 0; j < 10 * GlobalVariables.waveData[waveN][2]; j++) {
                    tick();
                    if (FF) {
                        //fast forward is 100 fps
                        pause(10);
                    } else {
                        //slower is 40 fps
                        pause(25);
                    }
                }
            }
        }
        else{
            double[] wave = levels.get(levelNumber).generateFreeplayWave(waveN);
            for (int i = 0; i < wave[3]; i++) {
                boolean camo = levels.get(levelNumber).freeplayIsCamo() > 0;
                if (camo){
                    double camoAmount = levels.get(levelNumber).freeplayIsCamo();
                    if (camoAmount >= 1){
                        if (i%(camoAmount+1) >= camoAmount){
                            camo = false;
                        }
                    }
                    else{
                        camoAmount = 1/camoAmount;
                        if (i%(camoAmount+1) < camoAmount){
                            camo = false;
                        }
                    }
                }
                GApple p = new GApple((int)wave[0],(int)wave[1],levels.get(levelNumber).freeplayIsFlying() > 0, levels.get(levelNumber).freeplayIsSplitting(), camo, levels.get(levelNumber).freeplayIsReverse(), levels.get(levelNumber).freeplayHealing(waveN), levels.get(levelNumber).freeplaySummoning(waveN));
                screen.addToScreen(p);
                enemiesLeft = true;
                for (int j = 0; j < wave[2]; j++) {
                    tick();
                    if (FF) {
                        //freeplay fast forward is 250 fps
                        pause(4);
                    } else {
                        pause(10);
                    }
                }
            }
        }

        wave = false;
        // continue ticking while there are enemies left
        while(enemiesLeft){
            tick();
            if (waveN < waveData.length){
                if (FF) {
                    pause(10);
                } else {
                    pause(25);
                }
            }
            else {
                if (FF) {
                    pause(4);
                } else {
                    pause(10);
                }
            }
        }
        //remove all the projectiles, zombies
        while(projectiles.size() > 0){
            projectiles.get(0).remove();
        }
        while(zombies.size() > 0){
            zombies.get(0).remove();
        }
        //if the wave was a freeplay wave, have a chance to get a freeplay dice
        if (randomInt(1,4) == 1 && levels.get(levelNumber).rewardUpgrades() && waveN >= waveData.length){
            getFreeplayDice();
        }
        waveN++;
        //get a dice at the end of the wave
        screen.getADice();
        already = false;
    }
    public static void run2(){
        pickedUp = null;
        screen.removeAll();
        waveData = levels.get(levelNumber).getWaveData();
        bannedTowers = levels.get(levelNumber).bannedTowers();
        Collections.addAll(flyingWaves, levels.get(levelNumber).flyingWaves()[0]);
        Collections.addAll(flyingAmounts, levels.get(levelNumber).flyingWaves()[1]);
        Collections.addAll(camoWaves, levels.get(levelNumber).camoWaves()[0]);
        Collections.addAll(camoAmounts, levels.get(levelNumber).camoWaves()[1]);
        Collections.addAll(healingWaves,levels.get(levelNumber).healingWaves()[0]);
        Collections.addAll(healingAmounts,levels.get(levelNumber).healingWaves()[1]);
        Collections.addAll(summons,levels.get(levelNumber).summonWaves());
        splitting = levels.get(levelNumber).splittingWaves();
        reverse = levels.get(levelNumber).reverseWaves();
        String s = null;
        try {
            s = new Scanner(f).next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < s.length()+1; i++) {
            if ((s.charAt(i-1) == '1' || s.charAt(i-1) == '2') && !bannedTowers.contains(i)) {
                GTile t = new GTile(50, ((i - 1) % 5) + 2, 4 + ((i - 1) / 5), i);
                screen.addToScreen(t);
            }
        }
        GTile[] z = new GTile[levels.get(levelNumber).dicePicked().length];
        for (int i = 1; i < z.length+1; i++) {
            GTile t = new GTile(50,i+1,2);
            screen.addToScreen(t);
            z[i-1] = t;
        }
        JButton j = new JButton("GO!");
        screen.add(j, Program.SOUTH);

        j.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean exit = true;
                for (int i = 0; i < levels.get(levelNumber).dicePicked().length; i++) {
                    if (!z[i].dice) {
                        exit = false;
                        break;
                    }
                }
                if (exit) {
                    starting = true;
                    j.setVisible(false);
                    for (int i = 0; i < levels.get(levelNumber).dicePicked().length; i++) {
                        for (int k = 0; k < levels.get(levelNumber).dicePicked()[i]; k++) {
                            v.add(z[i].diceType);
                        }
                    }
                    screen.removeAll();
                    screen.waveStarter = j;
                    j.removeMouseListener(this);
                    screen.run1();
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

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    public static void exsist(){
        run2();
    }
    public static void getDice(int i, boolean upgrade) throws IOException {
        File f = new File(location + "saveFile.txt");
        String s = null;
        String v = null;
        String x = null;
        try {
            Scanner c = new Scanner(f);
            s = c.nextLine();
            v = c.nextLine();
            x = c.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (upgrade){
            s = s.substring(0, i) + "2" + s.substring(i + 1);
        }
        else {
            s = s.substring(0, i) + "1" + s.substring(i + 1);
            v = v.substring(0,i) + "1" + v.substring(i+1);
        }
        FileWriter w = new FileWriter(location + "saveFile.txt");
        w.append(s + "\n" + v + "\n" + x);
        w.flush();
    }
    public static void getSynergy(int i)throws IOException{
        File f = new File(location + "saveFile.txt");
        String s = null;
        String v = null;
        String x = null;
        try {
            Scanner c = new Scanner(f);
            s = c.nextLine();
            v = c.nextLine();
            x = c.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        x = x.substring(0, i) + "1" + x.substring(i + 1);
        FileWriter w = new FileWriter(location + "saveFile.txt");
        w.append(s + "\n" + v + "\n" + x);
        w.flush();
    }
    public static void win(){
        String s = "";
        try {
            s = new Scanner(new File(location + "saveFile.txt")).next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (s.contains("0")){
            int o = s.indexOf("0");
            String v = s.substring(0,o) + "1" + s.substring(o+1);
            if (v.contains("0")){
                int r = randomInt(0,s.length()-1);
                while (s.charAt(r) != '0'){
                    r = randomInt(0,s.length()-1);
                }
                int k = randomInt(0,s.length()-1);
                while (s.charAt(k) != '0' || k == r){
                    k = randomInt(0,s.length()-1);
                }
                String[] names = GlobalVariables.names.toArray(new String[0]);
                try {
                    if (Dialog.getYesOrNo("Would you like " + names[r] + "(Yes), or " + names[k] + "(No)?")) {
                        getDice(r,false);
                    } else {
                        getDice(k,false);
                    }
                }
                catch(IOException ignored){

                }
            }
            else{
                Dialog.showMessage("You have unlocked all the dice.");
                try {
                    getDice(o,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void synergyWin(){
        String s = "";
        try {
            Scanner v = new Scanner(new File(location + "saveFile.txt"));
            v.nextLine();
            v.nextLine();
            s = v.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (s.contains("0")){
            int o = s.indexOf("0");
            String v = s.substring(0,o) + "1" + s.substring(o+1);
            if (v.contains("0")){
                int r = randomInt(0,s.length()-1);
                while (s.charAt(r) != '0'){
                    r = randomInt(0,s.length()-1);
                }
                int k = randomInt(0,s.length()-1);
                while (s.charAt(k) != '0' || k == r){
                    k = randomInt(0,s.length()-1);
                }
                String[] names = GlobalVariables.synergyNames.toArray(new String[0]);
                try {
                    if (Dialog.getYesOrNo("Would you like " + names[r] + "(Yes), or " + names[k] + "(No)?")) {
                        getSynergy(r);
                    } else {
                        getSynergy(k);
                    }
                }
                catch(IOException ignored){

                }
            }
            else{
                Dialog.showMessage("You have unlocked all the synergies.");
                try {
                    getSynergy(o);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void addButton(){
        JButton b = new JButton("Done");
        screen.fastForward = b;
        screen.add(b,Program.SOUTH);
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starting = false;
                screen.readyToGo();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        screen.waveStarter.setText("Random map");
        screen.waveStarter.removeMouseListener(screen.waveStarter.getMouseListeners()[0]);
        screen.waveStarter.setVisible(true);
        screen.waveStarter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (GTile[] someTiles: tiles){
                    for (GTile t : someTiles) {
                        if (randomInt(1,3) == 1){
                            t.setPath();
                        }
                    }
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

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    public static void pickedUp(GTile pickedUp){
        if (tiles[0][0] != null) {
            for (EventListener listener : listeners) {
                listener.pickedUp(pickedUp);
            }
        }
    }
    public static void placed(GTile placed){
        if (tiles[0][0] != null) {
            for (EventListener listener : listeners) {
                listener.placed(placed);
            }
        }
    }
    public static void merged(GTile merged1, GTile merged2, GTile result){
        for (EventListener list: listeners) {
            list.merged(merged1,merged2,result);
        }
    }
    public static void getFreeplayDice(){
        String s = "";
        try {
            s = new Scanner(new File(location + "saveFile.txt")).next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (s.contains("1")){
            int o = s.indexOf("1");
            String v = s.substring(0,o) + "2" + s.substring(o+1);
            if (v.contains("1")){
                int r = randomInt(0,s.length()-1);
                while (s.charAt(r) != '1'){
                    r = randomInt(0,s.length()-1);
                }
                int k = randomInt(0,s.length()-1);
                while (s.charAt(k) != '1' || k == r){
                    k = randomInt(0,s.length()-1);
                }
                String[] names = GlobalVariables.names.toArray(new String[0]);
                try {
                    if (Dialog.getYesOrNo("Would you like to upgrade " + names[r] + "(Yes), or " + names[k] + "(No)?")) {
                        getDice(r,true);
                    } else {
                        getDice(k,true);
                    }
                }
                catch(IOException ignored){

                }
            }
            else{
                try {
                    getDice(o,true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void diceGrown(GTile grown){
        for (EventListener list: listeners) {
            list.diceGrown(grown);
        }
    }
    public static void displayTitleScreen(){
        f = new File(location + "/saveFile.txt");
        GTile.resetUpgrades();
        screen.removeAll();
        GLabel title1 = new GLabel("Apple");
        GLabel title2 = new GLabel("Dice");
        GLabel title3 = new GLabel("Defense");
        GLabel[] titles = new GLabel[]{title1,title2,title3};
        for (GLabel title: titles) {
            title.setFont(new Font(title.getFont().getFontName(),Font.PLAIN,32));
        }
        GCompound startButton = new GCompound();
        startButton.add(new GRect(50,20));
        startButton.add(new GLabel("Play"),0,startButton.getHeight());
        startButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayMapSelect();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        GCompound dictionaryButton = new GCompound();
        dictionaryButton.add(new GRect(50,20));
        dictionaryButton.add(new GLabel("Dictionary"),0, dictionaryButton.getHeight());
        dictionaryButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayDictionary();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        screen.add(title1,screen.getWidth()/2-title1.getWidth()/2,screen.getHeight()/5);
        screen.add(title2,screen.getWidth()/2-title1.getWidth()/2,screen.getHeight()*2/5);
        screen.add(title3,screen.getWidth()/2-title1.getWidth()/2,screen.getHeight()*3/5);
        screen.add(startButton,screen.getWidth()/2-title1.getWidth()/2,screen.getHeight()*4/5);
        screen.add(dictionaryButton,screen.getWidth()/2-title1.getWidth()/2,screen.getHeight()*9/10);
    }
    public static void displayLevelSelect(){
        screen.removeAll();
        GCompound backButton = new GCompound();
        backButton.add(new GRect(50,20));
        backButton.add(new GLabel("Back"),0, backButton.getHeight());
        backButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayMapSelect();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        screen.add(backButton,10,10);
        for (int i = 0; i < levels.size(); i++) {
            screen.add(makeButton(levels.get(i)),((i%5)*60)+15,((i/5)*25)+40);
        }
    }
    public static void displayMapSelect(){
        screen.removeAll();
        for (int i = 0; i < maps.size(); i++) {
            final int z = i;
            GPolygon p = maps.get(i).configureShape(0,0,50);
            screen.add(p,((i%5)*60)+15,((i/5)*60)+40);
            p.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    map = maps.get(z);
                    displayLevelSelect();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
        displayBackButton();
    }
    public static void displayDictionary(){
        screen.removeAll();
        displayBackButton();
        File f = new File(location + "/saveFile.txt");
        String s = null;
        String x = null;
        try {
            Scanner z = new Scanner(f);
            s = z.nextLine();
            z.nextLine();
            x = z.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < s.length()+1; i++) {
            final int z = i;
            if ((s.charAt(i-1) == '1' || s.charAt(i-1) == '2')) {
                GTile t = new GTile(50, ((i - 1) % 5) + 2, 4 + ((i - 1) / 5), i);
                screen.addToScreen(t);
                t.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        displayPage(z);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
        }
        for (int i = 1; i < x.length()+1; i++) {
            final int z = i;
            if (x.charAt(i-1) == '1') {
                GTile t = new GTile(50, (((i+s.length()) - 1) % 5) + 2, 4 + (((i+s.length()) - 1) / 5), i+s.length());
                screen.addToScreen(t);
                t.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        displaySynergyPage(z);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
        }
    }
    public static void displayPage(int diceNumber){
        screen.removeAll();
        GCompound backButton = new GCompound();
        backButton.add(new GRect(50,20));
        backButton.add(new GLabel("Back"),0, backButton.getHeight());
        backButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayDictionary();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        screen.add(backButton,10,10);
        GLabel name = new GLabel(GTile.code.get(diceNumber).name());
        name.setFont(new Font(name.getFont().getFontName(),Font.PLAIN,32));
        screen.add(name,30,20+name.getHeight());
        int sections = ((GTile.code.get(diceNumber).description().length()-1)/30)+1;
        GLabel[] labels = new GLabel[sections];
        for (int i = 0; i < sections; i++) {
            if (30*(i+1) < GTile.code.get(diceNumber).description().length()) {
                labels[i] = new GLabel(GTile.code.get(diceNumber).description().substring(30 * i, 30 * (i + 1)));
            }
            else{
                labels[i] = new GLabel(GTile.code.get(diceNumber).description().substring(30 * i));
            }
            screen.add(labels[i],20,i*15+ (75));
        }
        int unlocked = 0;
        int selected = 0;
        try {
            Scanner s = new Scanner(f);
           unlocked = Integer.parseInt(s.next().charAt(diceNumber-1)+ "");
           selected = Integer.parseInt(s.next().charAt(diceNumber-1)+ "");
        }
        catch (Exception ignored){}
        final int unlock = unlocked;
        GCompound normalButton = new GCompound();
        GRect rect1 = new GRect(100,40);
        GRect rect2 = new GRect(100,40);
        normalButton.add(rect1);
        GLabel label = new GLabel("Normal");
        normalButton.add(label,0, label.getHeight());
        normalButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(diceNumber,1);
                rect1.setFillColor(Color.green);
                if (unlock > 1) {
                    rect2.setFillColor(Color.white);
                }
                else{
                    rect2.setFillColor(Color.red);
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

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        screen.add(normalButton,250,30);
        GCompound other = new GCompound();
        other.add(rect2);
        if (unlocked > 1) {
            label = new GLabel(GTile.code.get(diceNumber).secondaryName());
            other.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setSelected(diceNumber,2);
                    rect2.setFillColor(Color.green);
                    rect1.setFillColor(Color.white);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            sections = ((GTile.code.get(diceNumber).secondaryDescription().length()-1)/30)+1;
            labels = new GLabel[sections];
            for (int i = 0; i < sections; i++) {
                if (30*(i+1) < GTile.code.get(diceNumber).secondaryDescription().length()) {
                    labels[i] = new GLabel(GTile.code.get(diceNumber).secondaryDescription().substring(30 * i, 30 * (i + 1)));
                }
                else{
                    labels[i] = new GLabel(GTile.code.get(diceNumber).secondaryDescription().substring(30 * i));
                }
                labels[i].setFont(new Font(labels[i].getFont().getFontName(),Font.PLAIN,6));
                other.add(labels[i],0,labels[i].getHeight()*(i+2));
            }
        }
        else{
            label = new GLabel("Locked");
        }
        other.add(label,0, label.getHeight()*3/5);
        screen.add(other,250,75);
        rect1.setFilled(true);
        rect2.setFilled(true);
        if (selected == 1){
            rect1.setFillColor(Color.GREEN);
            if (unlocked > 1){
                rect2.setFillColor(Color.white);
            }
            else{
                rect2.setFillColor(Color.red);
            }
        }
        else if (selected == 2){
            rect1.setFillColor(Color.white);
            rect2.setFillColor(Color.green);
        }
    }
    public static void displaySynergyPage(int synergyNumber){
        screen.removeAll();
        GCompound backButton = new GCompound();
        backButton.add(new GRect(50,20));
        backButton.add(new GLabel("Back"),0, backButton.getHeight());
        backButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayDictionary();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        screen.add(backButton,10,10);
        SynergyCode synergy = synergies.get(synergyNumber-1);
        GLabel name = new GLabel(synergy.name());
        name.setFont(new Font(name.getFont().getFontName(),Font.PLAIN,32));
        screen.add(name,30,20+name.getHeight());
        int sections = ((synergy.description().length()-1)/30)+1;
        GLabel[] labels = new GLabel[sections];
        for (int i = 0; i < sections; i++) {
            if (30*(i+1) < synergy.description().length()) {
                labels[i] = new GLabel(synergy.description().substring(30 * i, 30 * (i + 1)));
            }
            else{
                labels[i] = new GLabel(synergy.description().substring(30 * i));
            }
            screen.add(labels[i],20,i*15+ (75));
        }
    }
    public static void setSelected(int number, int unlock){
        String s = null;
        String v = null;
        String x = null;
        FileWriter w = null;
        try {
            Scanner c = new Scanner(f);
            s = c.nextLine();
            v = c.nextLine();
            x = c.nextLine();
            w = new FileWriter(location + "saveFile.txt");
        } catch (Exception ignored) {

        }
        v = v.substring(0,number-1) + unlock + v.substring(number);
        try {
            w.append(s + "\n" + v + "\n" + x);
            w.flush();
        } catch (IOException ignored) {
        }
        GTile.resetUpgrades();
    }
    public static void displayBackButton(){
        GCompound backButton = new GCompound();
        backButton.add(new GRect(50,20));
        backButton.add(new GLabel("Back"),0, backButton.getHeight());
        backButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayTitleScreen();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        screen.add(backButton,10,10);
    }
    public static GCompound makeButton(LevelCode level){
        GCompound button = new GCompound();
        GRect rect = new GRect(50,20);
        rect.setFilled(true);
        rect.setFillColor(level.difficultyColor());
        button.add(rect);
        button.add(new GLabel(level.name()),0,button.getHeight());
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                levelNumber =levels.indexOf(level);
                exsist();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        return button;
    }
}
