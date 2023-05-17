package game;

import acm.graphics.GOval;

import java.awt.*;

public interface DesignCode {
    Color getColor(GTile tile);
    default Color getColor(GProjectile bullet){
        return getColor(bullet.owner);
    }
    default void configureDice(GTile tile){
        if (tile.range != null) {
            GlobalVariables.screen.remove(tile.range);
        }
        tile.ticksLeft = tile.getAttackSpeed();
        Color v = getColor(tile);
        GlobalVariables.map.configureDots(tile);
        for (GOval z: tile.o) {
            z.setFillColor(v);
            z.setFilled(true);
            z.setLocation(z.getX()+tile.getCenter().getX(),z.getY()+tile.getCenter().getY());
            tile.add(z);
        }
        tile.add(tile.g);
        tile.g.sendToBack();
        tile.g.setColor(v);
        int rangeAmt = tile.getRange();
        tile.range = new GOval((rangeAmt*100)+50,(rangeAmt*100)+50);
        tile.range.setFillColor(new Color(0, 250, 200, 75));
        tile.range.setFilled(true);
        if (tile.getCenter().getX() == 0 && tile.getCenter().getY() == 0) {
            GlobalVariables.screen.add(tile.range, tile.g.getWidth() / 2 - tile.range.getWidth() / 2 + tile.getX(), tile.g.getHeight() / 2 - tile.range.getHeight() / 2 + tile.getY());
        }
        else {
            GlobalVariables.screen.add(tile.range, tile.getTrueCenter().getX() - tile.range.getWidth() / 2, tile.getTrueCenter().getY() - tile.range.getHeight() / 2);
        }
        tile.range.setVisible(false);
    }
}
