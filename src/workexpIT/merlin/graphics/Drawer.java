package workexpIT.merlin.graphics;

/**
 * Created by ict11 on 2016-02-03.
 */

import com.sun.prism.Texture;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.libffi.Closure;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import static org.lwjgl.glfw.GLFW.*; // allows us to create windows
import static org.lwjgl.opengl.GL11.*; // gives us access to things like "GL_TRUE" which we'll need
import static org.lwjgl.system.MemoryUtil.*; // allows us to use 'NULL' in our code, note this is slightly different from java's 'null'

import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer; // Used for getting the primary monitor later on.
import org.lwjgl.glfw.*;
import workexpIT.merlin.data.WorldData;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.stb.STBImage.*;
import static workexpIT.merlin.data.IOUtil.*;

import org.lwjgl.opengl.GLUtil;
import workexpIT.merlin.entities.Player;

import javax.imageio.ImageIO;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.*;


public class Drawer implements Runnable {

    private int w;
    private int h;
    private int comp;

    private GLFWErrorCallback errorfun;
    private GLFWWindowSizeCallback windowSizefun;
    private GLFWFramebufferSizeCallback framebufferSizefun;
    private GLFWKeyCallback keyfun;
    private GLFWScrollCallback scrollfun;

    private long window;
    private int ww = 800;
    private int wh = 600;

    private boolean ctrlDown;

    private int scale;

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

        //KeyListener
        keyfun = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                ctrlDown = (mods & GLFW_MOD_CONTROL) != 0;
                if (action == GLFW_RELEASE)
                    return;

                switch (key) {
                    case GLFW_KEY_ESCAPE:
                        glfwSetWindowShouldClose(window, GLFW_TRUE);
                        break;
                    case GLFW_KEY_KP_ADD:
                    case GLFW_KEY_EQUAL:
                        setScale(scale + 1);
                        break;
                    case GLFW_KEY_KP_SUBTRACT:
                    case GLFW_KEY_MINUS:
                        setScale(scale - 1);
                        break;
                    case GLFW_KEY_0:
                    case GLFW_KEY_KP_0:
                        if (ctrlDown)
                            setScale(0);
                        break;
                    case GLFW_KEY_UP:
                        Merlin.movePlayer(Merlin.UP);
                        break;
                    case GLFW_KEY_RIGHT:
                        Merlin.movePlayer(Merlin.RIGHT);
                        break;
                    case GLFW_KEY_LEFT:
                        Merlin.movePlayer(Merlin.LEFT);
                        break;
                    case GLFW_KEY_DOWN:
                        Merlin.movePlayer(Merlin.DOWN);
                        break;
                }
            }
        };

        //Scroll Listener
        scrollfun = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if (ctrlDown)
                    setScale(scale + (int) yoffset);
            }
        };

        errorfun.set();

        //Create & Configure Window
        createWindow();

    }

    private void createWindow() {
        if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);

        this.window = glfwCreateWindow(ww, wh, "STB Image Demo", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        windowSizefun.set(window);
        framebufferSizefun.set(window);
        keyfun.set(window);
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
        String file = "resources/graphics/materials/0.png";

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

    private void setScale(int scale) {
        this.scale = max(-3, scale);
    }

    private void loop() {

        //Configure Texture Stuff
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glEnable(GL_TEXTURE_2D);

        //While window is open
        while (glfwWindowShouldClose(window) == GLFW_FALSE) {
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
                //if (WorldData.entities.get(i).getName() == Reference.characters[x]) {

                    //Set Texture
                    //TARGET, MIPMAP LEVEL, INTERNAL FORMAT, WIDTH, HEIGHT, FORMAT, TYPE OF DATA, IMAGE
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, WorldData.entities.get(i).getSprites()[0]);

                    //Enable Alpha (Transparency)
                    glEnable(GL_BLEND);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

                    //Start drawing
                    glBegin(GL_QUADS);

                    glTexCoord2f(0.0f, 0.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w, WorldData.entities.get(i).getY() * h);

                    glTexCoord2f(1.0f, 0.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w + w, WorldData.entities.get(i).getY() * h);

                    glTexCoord2f(1.0f, 1.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w + w, WorldData.entities.get(i).getY() * h + h);

                    glTexCoord2f(0.0f, 1.0f);
                    glVertex2f(WorldData.entities.get(i).getX() * w, WorldData.entities.get(i).getY() * h + h);

                    //End Drawing
                    glEnd();
                //}
            }
        //}
    }

    private void drawTiles() {
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                        if ((w & 3) != 0)
                            glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w & 1));
                        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, WorldData.tiles[a][b].texture);

                    glBegin(GL_QUADS);

                    glTexCoord2f(0.0f, 0.0f);
                    glVertex2f(a * w, b * h);

                    glTexCoord2f(1.0f, 0.0f);
                    glVertex2f(a * w + w, b * h);

                    glTexCoord2f(1.0f, 1.0f);
                    glVertex2f(a * w + w, b * h + h);

                    glTexCoord2f(0.0f, 1.0f);
                    glVertex2f(a * w, b * h + h);
                    glEnd();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            init();

            loop();
        } finally {
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
        keyfun.release();
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
}