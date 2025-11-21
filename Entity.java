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

    // TEMPLATE METHOD
    public final void update(SpriteBatch batch, float delta) {
        move(delta);      // Paso 1: movimiento (definido por subclase)
        render(batch);    // Paso 2: renderizado (definido por subclase)
        checkStatus();    // Paso 3: comportamiento común
    }

    // Métodos abstractos a implementar en subclases
    protected abstract void move(float delta);
    protected abstract void render(SpriteBatch batch);

    // Método común opcional
    protected void checkStatus() {
        if (speed < 0) speed = 0; // simple chequeo común
    }

    // Getters / Setters
    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
}
