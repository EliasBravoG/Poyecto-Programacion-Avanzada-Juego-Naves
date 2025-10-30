package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Enemy extends Entity implements Movible {

    public Enemy(float x, float y, float speed) {
        super(x, y, speed, new Texture("aGreySmall.png"));
    }

    @Override
    public void move(float deltaTime) {
        y -= speed * deltaTime;
        if (y < -texture.getHeight()) {
            y = Gdx.graphics.getHeight() + MathUtils.random(100, 300);
            x = MathUtils.random(0, Gdx.graphics.getWidth() - texture.getWidth());
        }
    }

    @Override
    public void update(float deltaTime) {
        move(deltaTime);
    }
}
