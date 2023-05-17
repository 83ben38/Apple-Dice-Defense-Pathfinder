package game;

import acm.graphics.GOval;
import acm.graphics.GPolygon;

import java.awt.*;

public interface SynergyCode extends TileCode, DesignCode, BulletCode {
    default String secondaryName(){
        return null;
    }
    default String secondaryDescription(){
        return null;
    }
    Color getColor2(GTile tile);
    default void configureDice(GTile tile){
        if (tile.range != null) {
            GlobalVariables.screen.remove(tile.range);
        }
        tile.ticksLeft = tile.getAttackSpeed();
        Color v = getColor(tile);
        Color v2 = getColor2(tile);
        tile.diceAmount = 6;
        GlobalVariables.map.configureDots(tile);
        for (GOval z: tile.o) {
            z.setFillColor(z.getX() == tile.size/2 ? GlobalVariables.randomInt(0,1) == 1 ? v:v2 : z.getX() > tile.size/2 ? v:v2);
            z.setFilled(true);
            tile.add(z);
        }
        tile.remove(tile.g);
        GPolygon[] polygons = GlobalVariables.map.configureSynergy(tile.x,tile.y,tile.size,new Color[]{v,v2});
        for (int i = 0; i < 2; i++) {
            GPolygon p = polygons[i];
            tile.add(p);
            p.sendToBack();
        }
        int rangeAmt = tile.getRange();
        tile.range = new GOval((rangeAmt*100)+50,(rangeAmt*100)+50);
        tile.range.setFillColor(new Color(0, 250, 200, 75));
        tile.range.setFilled(true);
        GlobalVariables.screen.add(tile.range,tile.g.getWidth()/2-tile.range.getWidth()/2+GlobalVariables.getPositionOf(tile.x,tile.y).getX(),tile.g.getHeight()/2-tile.range.getHeight()/2+GlobalVariables.getPositionOf(tile.x,tile.y).getY());
        tile.range.setVisible(false);
    }
    int[] synergyNumbers();
}
