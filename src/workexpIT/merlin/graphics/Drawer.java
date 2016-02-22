package workexpIT.merlin.graphics;

/**
 * Created by ict11 on 2016-02-03.
 */

import org.lwjgl.opengl.GL;
import org.lwjgl.system.libffi.Closure;
import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import static org.lwjgl.glfw.GLFW.*; // allows us to create windows
import static org.lwjgl.opengl.GL11.*; // gives us access to things like "GL_TRUE" which we'll need
import static org.lwjgl.system.MemoryUtil.*; // allows us to use 'NULL' in our code, note this is slightly different from java's 'null'

import java.awt.image.*;
import java.io.IOException;
import java.nio.ByteBuffer; // Used for getting the primary monitor later on.
import org.lwjgl.glfw.*;
import workexpIT.merlin.data.WorldData;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.stb.STBImage.*;
import static workexpIT.merlin.data.IOUtil.*;

import org.lwjgl.opengl.GLUtil;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.listeners.KeyListener;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.*;


public class Drawer implements Runnable {

    public double animationState = 0;
    public double maxAniState = 20;

    public static int offsetX = 0;
    public static int offsetY = 0;

    public static int w;
    public static int h;
    private int comp;

    private GLFWErrorCallback errorfun;
    private GLFWWindowSizeCallback windowSizefun;
    private GLFWFramebufferSizeCallback framebufferSizefun;
    private GLFWScrollCallback scrollfun;

    private long window;
    public static int ww = 800;
    public static int wh = 600;



    public int scale;

    private Closure debugProc;

    public void init() {

        loadScale();


        errorfun = GLFWErrorCallback.createPrint();

        windowSizefun = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Drawer.this.ww = width;
                Drawer.this.wh = height;

                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(0.0, ww, wh, 0.0, -1.0, 1.0);
                glMatrixMode(GL_MODELVIEW);
            }
        };

        framebufferSizefun = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
            }
        };



        //Scroll Listener
        scrollfun = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                //if (ctrlDown)
                    setScale(scale + (int) yoffset);
            }
        };

        errorfun.set();

        //Create & Configure Window
        createWindow();
        //Center Camera
        Drawer.setCamera((-WorldData.getPlayer().getX()+Drawer.ww/2/Drawer.w)*Drawer.w, (-WorldData.getPlayer().getY()+Drawer.wh/2/Drawer.h)*Drawer.h);


    }

    private void createWindow() {
        if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");



        //glfwDefaultWindowHints();
        //glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);


        Output.write("GO GO GO GO!");
        this.window = glfwCreateWindow(ww, wh, "STB Image Demo", 0, 0);
        Output.write("YA YA!");
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        windowSizefun.set(window);
        framebufferSizefun.set(window);
        //Merlin.keyListener.set(window);
        scrollfun.set(window);

        // Center window
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                window,
                (vidmode.width() - ww) / 2,
                (vidmode.height() - wh) / 2
        );

        // Create context
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback();

        glfwSwapInterval(1);
        glfwShowWindow(window);
        glfwInvoke(window, windowSizefun, framebufferSizefun);

    }

    private void loadScale() {
        String file = "resources/graphics/materials/"+Reference.tileIds[0]+".png";

        //LOADS material 0 as reference for scale;
        ByteBuffer imageBuffer;
        try {
            imageBuffer = ioResourceToByteBuffer(file, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        // Use info to read image metadata without decoding the entire image.
        // We don't need this for this demo, just testing the API.
        if (stbi_info_from_memory(imageBuffer, w, h, comp) == 0)
            throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
/*
        System.out.println("Image width: " + w.get(0));
        System.out.println("Image height: " + h.get(0));
        System.out.println("Image components: " + comp.get(0));
        System.out.println("Image HDR: " + (stbi_is_hdr_from_memory(imageBuffer) == 1));*/

        // Decode the image
        ByteBuffer img = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
        if (img == null)
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());



        this.w = w.get(0);
        this.h = h.get(0);
        this.comp = comp.get(0);
    }

    public static ByteBuffer loadTexture(String file) {
        ByteBuffer imageBuffer;
        try {
            imageBuffer = ioResourceToByteBuffer(file, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        // Use info to read image metadata without decoding the entire image.
        // We don't need this for this demo, just testing the API.
        if (stbi_info_from_memory(imageBuffer, w, h, comp) == 0)
            throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
/*
        System.out.println("Image width: " + w.get(0));
        System.out.println("Image height: " + h.get(0));
        System.out.println("Image components: " + comp.get(0));
        System.out.println("Image HDR: " + (stbi_is_hdr_from_memory(imageBuffer) == 1));*/

        // Decode the image
        ByteBuffer img = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
        if (img == null)
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        return img;
    }

    public void setScale(int scale) {
        this.scale = max(-3, scale);
    }

    private void loop() {

        //Configure Texture Stuff
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glEnable(GL_TEXTURE_2D);

        //While window is open
        while (glfwWindowShouldClose(window) == GLFW_FALSE) {

            animationState = animationState + 1;
            if (animationState > maxAniState) {
                animationState = 0;
                for (int i = 0; i < WorldData.entities.size(); i++) {
                    WorldData.entities.get(i).lastLoc[0] = WorldData.entities.get(i).getX();
                    WorldData.entities.get(i).lastLoc[1] = WorldData.entities.get(i).getY();
                }
                //GameLoop.run();
            }

            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            float scaleFactor = 1.0f + scale * 0.25f;

            glPushMatrix();
            glScalef(scaleFactor, scaleFactor, 1f);


            //DRAW TILES
            drawTiles();

            //DRAW ENTITIES
            drawEntities();




            //Send Update
            glPopMatrix();
            //Say Drawing Is Finished
            glfwSwapBuffers(window);


        }

        glDisable(GL_TEXTURE_2D);

        glfwDestroyWindow(window);
    }

    private void drawEntities() {
        //for (int x = 0; x < Reference.characters.length; x++) {
            for (int i = 0; i < WorldData.entities.size(); i++) {
                //Output.write("Drawing: " + WorldData.entities.get(i).getName());

                    //Set Texture
                    //TARGET, MIPMAP LEVEL, INTERNAL FORMAT, WIDTH, HEIGHT, FORMAT, TYPE OF DATA, IMAGE
               // if (!(WorldData.entities.get(i) == WorldData.getPlayer())) {
                    if (WorldData.entities.get(i).spriteId == -1) {
                        //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, WorldData.entities.get(i).getSprites()[0]);
                    } else {
                        //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, WorldData.entities.get(i).getSprites()[WorldData.entities.get(i).spriteId]);
                    }

                    //Enable Alpha (Transparency)
                    glEnable(GL_BLEND);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

                    //Start drawing
                    glBegin(GL_QUADS);

                    glTexCoord2f(0.0f, 0.0f);
                Output.write("Moving from: " + WorldData.entities.get(i).lastLoc[0] + ", " + WorldData.entities.get(i).lastLoc[1] + " to: " + WorldData.entities.get(i).getX() + ", " + WorldData.entities.get(i).getY());
                Output.write(""+animationState/maxAniState);
                glVertex2f((int) ((WorldData.entities.get(i).lastLoc[0]+(animationState/maxAniState)*(WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0]))* w + offsetX),(int)((WorldData.entities.get(i).lastLoc[1]+(animationState/maxAniState)*(WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1])) * h + offsetY));

                    glTexCoord2f(1.0f, 0.0f);
                    glVertex2f((int)((WorldData.entities.get(i).lastLoc[0]+(animationState/maxAniState)*(WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0])) * w + w + offsetX), (int)((WorldData.entities.get(i).lastLoc[1]+(animationState/maxAniState)*(WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1])) * h + offsetY));

                    glTexCoord2f(1.0f, 1.0f);
                    glVertex2f((int)((WorldData.entities.get(i).lastLoc[0]+(animationState/maxAniState)*(WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0])) * w + w + offsetX), (int)((WorldData.entities.get(i).lastLoc[1]+(animationState/maxAniState)*(WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1])) * h + h + offsetY));

                    glTexCoord2f(0.0f, 1.0f);
                    glVertex2f((int)((WorldData.entities.get(i).lastLoc[0]+(animationState/maxAniState)*(WorldData.entities.get(i).getX()-WorldData.entities.get(i).lastLoc[0])) * w + offsetX), (int)((WorldData.entities.get(i).lastLoc[1]+(animationState/maxAniState)*(WorldData.entities.get(i).getY()-WorldData.entities.get(i).lastLoc[1])) * h + h + offsetY));

                    //End Drawing
                    glEnd();
                    //}
               /* }
                else {
                    int newOffsetX = (-WorldData.getPlayer().getX()+Drawer.ww/2/Drawer.w)*Drawer.w;
                    int newOffsetY = (-WorldData.getPlayer().getY()+Drawer.wh/2/Drawer.h)*Drawer.h;
                    if (WorldData.entities.get(i).spriteId == -1) {
                        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, WorldData.entities.get(i).getSprites()[0]);
                    } else {
                        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, WorldData.entities.get(i).getSprites()[WorldData.entities.get(i).spriteId]);
                    }

                    //Enable Alpha (Transparency)
                    glEnable(GL_BLEND);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

                    //Start drawing
                    glBegin(GL_QUADS);

                    glTexCoord2f(0.0f, 0.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w + newOffsetX, WorldData.entities.get(i).getY() * h + newOffsetY);

                    glTexCoord2f(1.0f, 0.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w + w + newOffsetX, WorldData.entities.get(i).getY() * h + newOffsetY);

                    glTexCoord2f(1.0f, 1.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w + w + newOffsetX, WorldData.entities.get(i).getY() * h + h + newOffsetY);

                    glTexCoord2f(0.0f, 1.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w + newOffsetX, WorldData.entities.get(i).getY() * h + h + newOffsetY);

                    //End Drawing
                    glEnd();
                }*/
            }
        //}
    }

    private void drawTiles() {
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                        if ((w & 3) != 0)
                            glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w & 1));
                        //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, WorldData.tiles[a][b].texture);

                    glBegin(GL_QUADS);

                    glTexCoord2f(0.0f, 0.0f);
                    glVertex2f(a * w + offsetX, b * h + offsetY);

                    glTexCoord2f(1.0f, 0.0f);
                    glVertex2f(a * w + w + offsetX, b * h+ offsetY);

                    glTexCoord2f(1.0f, 1.0f);
                    glVertex2f(a * w + w + offsetX, b * h + h+ offsetY);

                    glTexCoord2f(0.0f, 1.0f);
                    glVertex2f(a * w+ offsetX, b * h + h+ offsetY);
                    glEnd();
                }
            }
        }
    }

    @Override
    public void run() {
        ScheduledFuture gameLoop = null;
        ScheduledFuture animator = null;
        try {
            init();

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            //gameLoop = executor.scheduleWithFixedDelay(new GameLoop(), 0, 1000, TimeUnit.MILLISECONDS);

            animator = executor.scheduleWithFixedDelay(new Animator(), 0, 5800, TimeUnit.MICROSECONDS);

            loop();

        } finally {
            gameLoop.cancel(true);
            animator.cancel(true);
            try {
                destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void destroy() {

        if (debugProc != null)
            debugProc.release();
        scrollfun.release();
        framebufferSizefun.release();
        windowSizefun.release();
        glfwTerminate();
        errorfun.release();
    }

    private ByteBuffer convertBItoBB(BufferedImage bi) {
        ByteBuffer byteBuffer;
        DataBuffer dataBuffer = bi.getRaster().getDataBuffer();

        if (dataBuffer instanceof DataBufferByte) {
            byte[] pixelData = ((DataBufferByte) dataBuffer).getData();
            byteBuffer = ByteBuffer.wrap(pixelData);
        } else if (dataBuffer instanceof DataBufferUShort) {
            short[] pixelData = ((DataBufferUShort) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 2);
            byteBuffer.asShortBuffer().put(ShortBuffer.wrap(pixelData));
        } else if (dataBuffer instanceof DataBufferShort) {
            short[] pixelData = ((DataBufferShort) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 2);
            byteBuffer.asShortBuffer().put(ShortBuffer.wrap(pixelData));
        } else if (dataBuffer instanceof DataBufferInt) {
            int[] pixelData = ((DataBufferInt) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 4);
            byteBuffer.asIntBuffer().put(IntBuffer.wrap(pixelData));
        } else {
            throw new IllegalArgumentException("Not implemented for data buffer type: " + dataBuffer.getClass());
        }
        return byteBuffer;
    }

    public static void setCamera(int x, int y) {
        Output.write("Setting camera to " + x/w + ", " + y/h);
        offsetX = x;
        offsetY = y;
    }
}