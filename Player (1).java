package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Entity {

    public Player(float x, float y, float speed, Texture texture) {
        super(x, y, speed, texture);
    }

    @Override
    protected void move(float delta) {
       
        x += speed * delta;
    }

    @Override
    protected void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}
