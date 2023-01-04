package com.junkiedan.infinity;

import com.junkiedan.components.Sprite;
import com.junkiedan.components.SpriteRenderer;
import com.junkiedan.components.Spritesheet;
import com.junkiedan.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        Spritesheet spritesheet = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        GameObject obj1 = new GameObject("Object 1", new Transform(
                new Vector2f(100, 100), new Vector2f(256, 256)
        ));
        obj1.addComponent(new SpriteRenderer(spritesheet.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 1", new Transform(
                new Vector2f(400, 100), new Vector2f(256, 256)
        ));
        obj2.addComponent(new SpriteRenderer(spritesheet.getSprite(18)));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
    }
    @Override
    public void update(float dt) {
//        System.out.println("FPS: " + (1.0f / dt));
        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }


}
