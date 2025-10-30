package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity implements Movible {

    public Player(float x, float y) {
        super(x, y, 300, new Texture("Rocket2.png"));
    }

    @Override
    public void move(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= speed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += speed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) y += speed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= speed * deltaTime;

        // Mantener dentro de los límites de pantalla
        x = Math.max(0, Math.min(Gdx.graphics.getWidth() - texture.getWidth(), x));
        y = Math.max(0, Math.min(Gdx.graphics.getHeight() - texture.getHeight(), y));
    }

    @Override
    public void update(float deltaTime) {
        move(deltaTime);
    }
}
