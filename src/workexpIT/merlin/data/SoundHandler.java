package workexpIT.merlin.data;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by justi on 2016-05-05.
 */
public class SoundHandler {
    public static void playSound(String fileName)
    {
        File soundFile = new File(fileName);
        AudioInputStream audioInputStream = null;
        try
        {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        AudioFormat audioFormat = audioInputStream.getFormat();
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try
        {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[128000];
        while (nBytesRead != -1)
        {
            try
            {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (nBytesRead >= 0)
            {
                int nBytesWritten = line.write(abData, 0, nBytesRead);
            }
        }
        line.drain();
        line.close();
    }

    public static void playBackgroundMusic() {
        Thread bgMusic = new Thread () {
            public void run() {
                while (true) {
                    playSound("resources/audio/backgroundmusic/0.wav");
                }
            }
        };
        bgMusic.start();
    }
}
