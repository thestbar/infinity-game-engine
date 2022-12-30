package com.junkiedan.infinity;

import com.junkiedan.components.FontRenderer;
import com.junkiedan.components.SpriteRenderer;
import com.junkiedan.renderer.Shader;
import com.junkiedan.renderer.Texture;
import com.junkiedan.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
            // Vertices                // Color                         // UV Coordinates
            100.0f, 0.5f,   0.0f,      1.0f, 0.0f, 0.0f, 1.0f,          1, 1,  // Bottom right   (0)
           -0.5f,   100.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,          0, 0,  // Top left       (1)
            100.5f, 100.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,          1, 0,  // Top right      (2)
           -0.5f,   -0.5f,  0.0f,      1.0f, 1.0f, 0.0f, 1.0f,          0, 1   // Bottom left    (3)
    };

    private int vaoId, vboId, eboId;

    private Shader defaultShader;
    private Texture testTexture;

    GameObject testObj;
    private boolean firstTime = true;

    // Important: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x (1)       x (2)


                    x (3)       x (0)
             */
            2, 1, 0,    // Top right triangle
            0, 1, 3
    };

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        System.out.println("Creating 'Test Object'");
        this.testObj = new GameObject("Test Object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);


        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.png");

        // ====================================================================
        // Generate VAO, VBO, and EBO buffer objects and send to GPU
        // ====================================================================
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload the vertex buffer
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }
    @Override
    public void update(float dt) {
//        camera.position.x -= dt * 100f;
//        camera.position.y -= dt * 40.0f;

        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // Bind the VAO that we are using
        glBindVertexArray(vaoId);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if(firstTime) {
            System.out.println("Creating game object #2");
            GameObject go2 = new GameObject("GameObject Test 2");
            go2.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go2);
            firstTime = false;
        }


        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }


}
