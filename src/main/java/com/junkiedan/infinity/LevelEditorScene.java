package com.junkiedan.infinity;

import com.junkiedan.components.Sprite;
import com.junkiedan.components.SpriteRenderer;
import com.junkiedan.components.Spritesheet;
import com.junkiedan.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

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

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
        spriteFlipTimeLeft -= dt;
        if(spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if(spriteIndex > 4) {
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(spritesheet.getSprite(spriteIndex));
        }

        obj1.transform.position.x += 100 * dt;
//        System.out.println("FPS: " + (1.0f / dt));
        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }


}
