package workexpIT.merlin.graphics;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;

public class Animator implements Runnable {

    @Override
    public void run() {
        smoothOffset();
    }

    private void smoothOffset() {
        int newOffsetX = (-WorldData.getPlayer().getX()+Drawer.ww/2/Drawer.w)*Drawer.w;
        int newOffsetY = (-WorldData.getPlayer().getY()+Drawer.wh/2/Drawer.h)*Drawer.h;

        if(newOffsetX > Drawer.offsetX) {
            Drawer.offsetX = Drawer.offsetX+1;
        }
        if(newOffsetY > Drawer.offsetY) {
            Drawer.offsetY = Drawer.offsetY+1;
        }
        if(newOffsetX < Drawer.offsetX) {
            Drawer.offsetX = Drawer.offsetX-1;
        }
        if(newOffsetY < Drawer.offsetY) {
            Drawer.offsetY = Drawer.offsetY-1;
        }
    }
}
