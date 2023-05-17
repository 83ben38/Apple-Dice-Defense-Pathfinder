package game;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import svu.csc213.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ADD extends GraphicsProgram {
    JButton waveStarter;
    JButton fastForward;
    public static void main(String[] args) {
        Mod.initMods();
        String location = URLDecoder.decode(ADD.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8);
        location = location.substring(0,location.lastIndexOf("/")+1);
        GlobalVariables.location = location;
        try {
            if (new File(location + "/saveFile.txt").createNewFile()){
                FileWriter w = new FileWriter(location + "/saveFile.txt");
                String s = "";
                for (String st: GlobalVariables.names) {
                    s = s + "0";
                }
                String x = "";
                for (int i = 0; i < GlobalVariables.synergies.size(); i++) {
                    x = x + "0";
                }
                w.write(s + "\n" + s + "\n" + x);
                w.close();
                for (int i = 0; i < 5; i++) {
                    GlobalVariables.win();
                }
            }
        } catch (IOException ignored) {
        }
        ADD s = new ADD();
        s.start();
        GlobalVariables.screen = s;
        new Thread(GlobalVariables::displayTitleScreen).start();
    }
    public void run1(){
        GlobalVariables.screen = this;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                GTile a = new GTile(50,i,j);
                GlobalVariables.tiles[i][j] = a;
                addToScreen(a);
            }
        }
        new Thread(GlobalVariables::addButton).start();
    }
    public void addToScreen(GTile t){
        add(t, GlobalVariables.getPositionOf(t.x,t.y));
    }
    public void addToScreen(GApple g){
        int position = g.reverse ? GlobalVariables.path.size()-1 : 0;
        GPoint x = GlobalVariables.getPositionOf(GlobalVariables.path.get(position).x, GlobalVariables.path.get(position).y);
        add(g,x.getX()-g.getWidth()/2+ GlobalVariables.path.get(position).getWidth()/2,x.getY()-g.getHeight()/2+ GlobalVariables.path.get(position).getHeight()/2);
        g.sendToFront();
        repaint();
    }
    public void readyToGo(){
        waveStarter.setText("Start wave");
        waveStarter.setVisible(true);
        waveStarter.removeMouseListener(waveStarter.getMouseListeners()[0]);
        waveStarter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!(GlobalVariables.wave || GlobalVariables.enemiesLeft )){
                    if (GlobalVariables.waveN == GlobalVariables.waveData.length && !GlobalVariables.diceGotten){
                        Dialog.showMessage("You win!");
                        if (GlobalVariables.levels.get(GlobalVariables.levelNumber).rewardDice()) {
                            GlobalVariables.win();
                        }
                        if (GlobalVariables.levels.get(GlobalVariables.levelNumber).rewardExtras()){
                            GlobalVariables.synergyWin();
                        }
                        GlobalVariables.diceGotten = true;
                        if (!Dialog.getYesOrNo("Would you like to continue playing?")) {
                            exit();
                        }
                        else{
                            new Thread(GlobalVariables::startWave).start();
                        }
                    }
                    else {
                        new Thread(GlobalVariables::startWave).start();
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
        add(waveStarter,SOUTH);
        getADice();
        fastForward.removeMouseListener(fastForward.getMouseListeners()[0]);
        fastForward.setText("Fast forward");
        fastForward.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlobalVariables.FF = !GlobalVariables.FF;
                if (GlobalVariables.FF){
                    fastForward.setText("Un-Fast forward");
                }
                else{
                    fastForward.setText("Fast forward");
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
        add(fastForward,SOUTH);
    }
    public void getADice(){
        for (GTile[] someTiles: GlobalVariables.tiles) {
            for (GTile t: someTiles) {
                if (t.dice){
                    t.turnPassed();
                }
            }
        }
        int diceLvl = (int) Math.sqrt(GlobalVariables.randomInt(1, GlobalVariables.waveN));
        while (diceLvl > GlobalVariables.levels.get(GlobalVariables.levelNumber).maxDiceLevel()){
            diceLvl--;
        }
        GlobalVariables.pickedUp = new GTile(diceLvl);

    }
}
