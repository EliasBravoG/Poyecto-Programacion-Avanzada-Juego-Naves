package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

    //  Variables configurables
	private static float BULLET_SPEED = 5f;    // disparos más rápidos
	private static float BULLET_LIFETIME = 4f; // duran menos, mejora el ritmo

    private float xSpeed;
    private float ySpeed;
    private boolean destroyed = false;
    private Sprite spr;
    private float lifetime = 0f;

    public Bullet(float x, float y, float xSpeed, float ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed != 0 ? ySpeed : BULLET_SPEED;
    }

    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        lifetime += Gdx.graphics.getDeltaTime();

        // Destruir si sale de pantalla o supera su tiempo de vida
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) destroyed = true;
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) destroyed = true;
        if (lifetime > BULLET_LIFETIME) destroyed = true;
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.getArea())) {
            destroyed = true;
            return true;
        }
        return false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    // Métodos para cambiar velocidad fácilmente
    public static void setBulletSpeed(float newSpeed) {
        BULLET_SPEED = newSpeed;
    }

    public static void setBulletLifetime(float newLifetime) {
        BULLET_LIFETIME = newLifetime;
    }

    public static float getBulletSpeed() {
        return BULLET_SPEED;
    }
}
