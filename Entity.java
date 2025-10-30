package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
    protected float x, y;
    protected float speed;
    protected Texture texture;

    public Entity(float x, float y, float speed, Texture texture) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.texture = texture;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public abstract void move(float deltaTime);

    public abstract void update(float deltaTime);

    // Getters y setters para encapsulamiento
    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
}
