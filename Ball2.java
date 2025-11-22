package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 {
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;
    private Sprite spr;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setSize(size, size);

        // Asegurar posición inicial dentro de pantalla
        this.x = Math.max(size, Math.min(x, Gdx.graphics.getWidth() - size));
        this.y = Math.max(size, Math.min(y, Gdx.graphics.getHeight() - size));

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        spr.setPosition(this.x, this.y);
    }

    public void update() {
        // Actualizar posición
        x += xSpeed;
        y += ySpeed;

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // Rebote horizontal
        if (x < 0) {
            x = 0;
            xSpeed = Math.abs(xSpeed); // rebota hacia la derecha
        } else if (x + spr.getWidth() > screenW) {
            x = screenW - spr.getWidth();
            xSpeed = -Math.abs(xSpeed); // rebota hacia la izquierda
        }

        // Rebote vertical
        if (y < 0) {
            y = 0;
            ySpeed = Math.abs(ySpeed); // rebota hacia arriba
        } else if (y + spr.getHeight() > screenH) {
            y = screenH - spr.getHeight();
            ySpeed = -Math.abs(ySpeed); // rebota hacia abajo
        }

        spr.setPosition(x, y);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.spr.getBoundingRectangle())) {
            // rebote mas realista entre dos bolas 
            float tempX = xSpeed;
            float tempY = ySpeed;
            xSpeed = b2.xSpeed;
            ySpeed = b2.ySpeed;
            b2.xSpeed = tempX;
            b2.ySpeed = tempY;

            // separa un poco los asteroides para evitar que se "peguen"
            x += xSpeed;
            y += ySpeed;
            b2.x += b2.xSpeed;
            b2.y += b2.ySpeed;
        }
    }

    public float getXSpeed() { return xSpeed; }
    public void setXSpeed(float xSpeed) { this.xSpeed = xSpeed; }
    public float getySpeed() { return ySpeed; }
    public void setySpeed(float ySpeed) { this.ySpeed = ySpeed; }
}
