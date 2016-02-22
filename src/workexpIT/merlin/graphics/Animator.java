package workexpIT.merlin.graphics;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;

import static org.lwjgl.opengl.GL11.*;

public class Animator implements Runnable {

    @Override
    public void run() {
        smoothOffset();
        //drawEntities();
    }

    private void drawEntities() {
        for (int i = 0; i < WorldData.entities.size(); i++) {
            if (WorldData.entities.get(i).spriteId == -1) {
                //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Drawer.w, Drawer.h, 0, GL_RGBA, GL_UNSIGNED_BYTE, WorldData.entities.get(i).getSprites()[0]);
            } else {
                //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Drawer.w, Drawer.h, 0, GL_RGBA, GL_UNSIGNED_BYTE, WorldData.entities.get(i).getSprites()[WorldData.entities.get(i).spriteId]);
            }

            //Enable Alpha (Transparency)
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            //Start drawing
            glBegin(GL_QUADS);

            glTexCoord2f(0.0f, 0.0f);
            glVertex2f((WorldData.entities.get(i).getX()+WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0])* Drawer.w + Drawer.offsetX,(WorldData.entities.get(i).getY()+WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1]) * Drawer.h + Drawer.offsetY);

            glTexCoord2f(1.0f, 0.0f);
            glVertex2f((WorldData.entities.get(i).getX()+WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0]) * Drawer.w + Drawer.w + Drawer.offsetX, (WorldData.entities.get(i).getY()+WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1]) * Drawer.h + Drawer.offsetY);

            glTexCoord2f(1.0f, 1.0f);
            glVertex2f((WorldData.entities.get(i).getX()+WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0]) * Drawer.w + Drawer.w + Drawer.offsetX, (WorldData.entities.get(i).getY()+WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1]) * Drawer.h + Drawer.h + Drawer.offsetY);

            glTexCoord2f(0.0f, 1.0f);
            glVertex2f((WorldData.entities.get(i).getX()+WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0]) * Drawer.w + Drawer.offsetX, (WorldData.entities.get(i).getY()+WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1]) * Drawer.h + Drawer.h + Drawer.offsetY);

            //End Drawing
            glEnd();
        }
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
