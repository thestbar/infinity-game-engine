package com.junkiedan.infinity;

import com.junkiedan.components.Sprite;
import com.junkiedan.components.SpriteRenderer;
import com.junkiedan.components.Spritesheet;
import com.junkiedan.util.AssetPool;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet spritesheet;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        spritesheet = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(
                new Vector2f(100, 100), new Vector2f(256, 256)
        ), 12);
        obj1.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage1.png")
        )));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(
                new Vector2f(400, 100), new Vector2f(256, 256)
        ), 12);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage2.png")
        )));
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
        obj1.transform.position.x += 40 * dt;
        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }


}
