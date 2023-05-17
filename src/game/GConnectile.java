package game;

import acm.graphics.GLine;

import java.awt.*;
import java.util.ArrayList;

public class GConnectile extends GProjectile{
    public static final Color LASER_COLOR = new Color(255,142,0);
    public int ticksLeft;
    public GTile connector1;
    public boolean ticked = false;
    public GTile connector2;
    public ArrayList<GTile> t;
    public GConnectile(int lvl, GTile starter, GTile ender){
        super(lvl);
        connector1 = starter;
        isUpgraded = connector1.isUpgraded;
        connector2 = ender;
        v = null;
        reset(false);
        GlobalVariables.screen.add(this,connector1.getX()+connector1.getWidth()/2,connector1.getY()+connector1.getWidth()/2);
        ticksLeft = isUpgraded ? 12:6;
    }
    public void reset(boolean check){
        removeAll();
        if (check) {
            t = new ArrayList<>();
        }
        final double endingX = connector2.getX()-(connector1.getX());
        final double endingY = connector2.getY()-(connector1.getY());
        double x = 0;
        double y = 0;
        for (int i = 0; i < 10; i++) {
            double xx = (endingX/11)+x+GlobalVariables.randomInt(-3,3);
            double yy = (endingY/11)+y+GlobalVariables.randomInt(-3,3);
            if (check) {
                GTile b = GlobalVariables.getGTileAt((int) (xx+connector1.getX()), (int) (yy+connector1.getY()));
                if (!t.contains(b)) {
                    t.add(b);
                }
            }
            GLine l = new GLine(x,y,xx,yy);
            l.setColor(LASER_COLOR);
            add(l);
            x=xx;
            y=yy;

        }
        GLine l = new GLine(x,y,endingX,endingY);
        l.setColor(LASER_COLOR);
        add(l);
    }
    @Override
    public void tick(){
        if (ticked) {
            ticksLeft -= lvl;
            reset(ticksLeft<1);
        }
        ticked = !ticked;
        if(ticksLeft<1) {
            ticksLeft+=isUpgraded ? 12:6;
            for (int i = 0; i < GlobalVariables.enemies.size(); i++) {
                GApple p = GlobalVariables.enemies.get(i);
                if (t.contains(GlobalVariables.path.get(p.pathAmount))){
                    p.damage(lvl);
                }
            }
        }
    }
    @Override
    public void remove(){
        GlobalVariables.projectiles.remove(this);
        GlobalVariables.screen.remove(this);
        connector1.projectiles.remove(this);
        connector2.projectiles.remove(this);
    }
}
