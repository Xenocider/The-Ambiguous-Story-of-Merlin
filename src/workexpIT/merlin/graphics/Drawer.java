package workexpIT.merlin.graphics;

/**
 * Created by ict11 on 2016-02-03.
 */

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.libffi.Closure;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import static org.lwjgl.glfw.GLFW.*; // allows us to create windows
import static org.lwjgl.opengl.GL11.*; // gives us access to things like "GL_TRUE" which we'll need
import static org.lwjgl.system.MemoryUtil.*; // allows us to use 'NULL' in our code, note this is slightly different from java's 'null'

import java.io.IOException;
import java.nio.ByteBuffer; // Used for getting the primary monitor later on.
import org.lwjgl.glfw.*;
import workexpIT.merlin.data.WorldData;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import static org.lwjgl.stb.STBImage.*;
import static workexpIT.merlin.data.IOUtil.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.libffi.Closure;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Drawer implements Runnable {

    private ByteBuffer image[] = new ByteBuffer[Reference.numOfMaterials];

    private int w;
    private int h;
    private int comp;

    private GLFWErrorCallback           errorfun;
    private  GLFWWindowSizeCallback      windowSizefun;
    private  GLFWFramebufferSizeCallback framebufferSizefun;
    private  GLFWKeyCallback             keyfun;
    private  GLFWScrollCallback          scrollfun;

    private long window;
    private int ww = 800;
    private int wh = 600;

    private boolean ctrlDown;

    private int scale;

    private Closure debugProc;

    public void init() {
        for (int i = 0; i < Reference.numOfMaterials; i++) {
            String imagePath = "resources/graphics/materials/"+i+".jpg";
            Output.write("WORKING");

            ByteBuffer imageBuffer;
            try {
                imageBuffer = ioResourceToByteBuffer(imagePath, 8 * 1024);
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

            System.out.println("Image width: " + w.get(0));
            System.out.println("Image height: " + h.get(0));
            System.out.println("Image components: " + comp.get(0));
            System.out.println("Image HDR: " + (stbi_is_hdr_from_memory(imageBuffer) == 1));

            // Decode the image
            ByteBuffer img = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (img == null)
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            image[i] = img;


            this.w = w.get(0);
            this.h = h.get(0);
            this.comp = comp.get(0);
        }

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

        keyfun = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                ctrlDown = (mods & GLFW_MOD_CONTROL) != 0;
                if ( action == GLFW_RELEASE )
                    return;

                switch ( key ) {
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
                        if ( ctrlDown )
                            setScale(0);
                        break;
                }
            }
        };

        scrollfun = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if ( ctrlDown )
                    setScale(scale + (int)yoffset);
            }
        };
        errorfun.set();
        if ( glfwInit() != GLFW_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);

        this.window = glfwCreateWindow(ww, wh, "STB Image Demo", NULL, NULL);
        if ( window == NULL )
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

    private void setScale(int scale) {
        this.scale = max(-3, scale);
    }

    private void loop() {

        int texID = glGenTextures();
        System.out.println(texID);

        glBindTexture(GL_TEXTURE_2D, texID);

        if ( comp == 3 ) {
            if ( (w & 3) != 0 )
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w & 1));
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, image[0]);
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, image[0]);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glEnable(GL_TEXTURE_2D);

        while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            float scaleFactor = 1.0f + scale * 0.25f;

            glPushMatrix();
            glScalef(scaleFactor, scaleFactor, 1f);

            for (int a = 0; a < WorldData.tiles.length; a++) {
                for (int b = 0; b < WorldData.tiles[a].length; b++) {
                    if (WorldData.tiles[a][b] != null) {
                        if (comp == 3) {
                            if ((w & 3) != 0)
                                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w & 1));
                            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, image[WorldData.tiles[a][b].getId()]);
                        } else {
                            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, image[WorldData.tiles[a][b].getId()]);

                            glEnable(GL_BLEND);
                            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                        }

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

            glPopMatrix();

            glfwSwapBuffers(window);
        }

        glDisable(GL_TEXTURE_2D);

        glfwDestroyWindow(window);
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
        for (int i = 0; i < image.length; i++) {
            stbi_image_free(image[i]);
        }

        if ( debugProc != null )
            debugProc.release();
        scrollfun.release();
        keyfun.release();
        framebufferSizefun.release();
        windowSizefun.release();
        glfwTerminate();
        errorfun.release();
    }

}
