package game;

import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.graphics.GPolygon;

import java.awt.*;

public interface MapCode {
    GPoint getPositionOf(int x, int y);
    GPolygon configureShape(int x, int y, int size);
    GPolygon[] configureSynergy(int x, int y, int size, Color[] colors);
    boolean isNextTo(int x1, int y1, int x2, int y2);
    default void configureDots(GTile tile){
        if (tile.diceAmount%2==1){
            tile.o.add(new GOval((double)tile.size/2-(double)tile.size/14,(double)tile.size/2-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
        }
        switch(tile.diceAmount/2){
            case 3:
                tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size/2-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                tile.o.add(new GOval((double)tile.size*3/4-(double)tile.size/14,(double)tile.size/2-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
            case 2:
                tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                tile.o.add(new GOval((double)tile.size*3/4-(double)tile.size/14,(double)tile.size*3/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
            case 1:
                tile.o.add(new GOval((double)tile.size/4-(double)tile.size/14,(double)tile.size*3/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
                tile.o.add(new GOval((double)tile.size*3/4-(double)tile.size/14,(double)tile.size/4-(double)tile.size/14,(double)tile.size/7,(double)tile.size/7));
        }
    }
    default GPoint getCenter(int x, int  y, int size){
        return new GPoint(0,0);
    }
    GPoint[] polygonPoints(int x, int y, int size);
}
