package workexpIT.merlin.listeners;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.graphics.JavaDrawer;

import java.awt.event.MouseEvent;

/**
 * Created by ict11 on 2016-02-24.
 */
public class MouseListener implements java.awt.event.MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            int editX;
            int editY;
            if (x > JavaDrawer.editorMenuSize) {
                Output.write((x - JavaDrawer.frame.getWidth() + JavaDrawer.editorMenuSize - 10)/(JavaDrawer.imageSize+10)+"");
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
}
