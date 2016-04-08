package workexpIT.merlin.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ict11 on 2016-02-22.
 */
public class ImageReader {

    public static BufferedImage loadImage(String file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage cropImage(BufferedImage src, int x,int y, int w, int h) {
        BufferedImage dest = src.getSubimage(x, y, w, h);
        return dest;
    }

}
