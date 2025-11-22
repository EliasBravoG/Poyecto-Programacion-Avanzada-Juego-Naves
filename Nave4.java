package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 {

    // Atributos principales
    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private float maxSpeed = 3.2f;              // velocidad máxima permitida
    private float accelerationFactor = 1.1f;  // cuánto aumenta la velocidad con cada rebote
    private boolean invencible = false;
    private int tiempoInvencibleMax = 80;    // duración en frames
    private int tiempoInvencible = 0;
 // Control de disparo (cooldown)
    private float tiempoUltimoDisparo = 0f;
    private final float COOLDOWN_DISPARO = 0.17f;  // segundos entre disparos


    // Patrón Strategy
    private DisparoStrategy disparoStrategy;

    // Constructor
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
        
        Bullet.setBulletSpeed(5f);

        // Estrategia de disparo por defecto
        this.disparoStrategy = new DisparoSimple();
    }

    // Actualización de movimiento y estado
    public void update(SpriteBatch batch, float delta) {
    	float accel = 0.35f;       // Más responsivo
    	float friccion = 0.03f;    // Menos pérdida de velocidad
    	float x = spr.getX();
    	float y = spr.getY();

        if (!herido && !invencible) {
            // Movimiento con teclas (aceleración gradual)
        	if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  xVel -= accel;
        	if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) xVel += accel;
        	if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  yVel -= accel;
        	if (Gdx.input.isKeyPressed(Input.Keys.UP))    yVel += accel;

            // Aplicar fricción (ralentiza suavemente)
            xVel *= (1 - friccion);
            yVel *= (1 - friccion);

            // Limitar velocidad máxima
            xVel = Math.max(-maxSpeed, Math.min(maxSpeed, xVel));
            yVel = Math.max(-maxSpeed, Math.min(maxSpeed, yVel));

            // Mantener dentro de pantalla
            if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth()) {
                xVel = -xVel * 0.5f;
            }
            if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight()) {
                yVel = -yVel * 0.5f;
            }

            spr.setPosition(x + xVel, y + yVel);

        } else {
            // Efecto cuando está herido
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        // Efecto visual de invencibilidad (parpadeo)
        if (invencible) {
            tiempoInvencible--;
            if (tiempoInvencible % 10 < 5) spr.draw(batch);
            if (tiempoInvencible <= 0) invencible = false;
        } else {
            spr.draw(batch);
        }
    }


 // Método disparar (usa el Strategy )
    public void disparar(PantallaJuego juego, SpriteBatch batch) {
        tiempoUltimoDisparo += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && tiempoUltimoDisparo >= COOLDOWN_DISPARO) {
            disparoStrategy.disparar(this, juego, batch);
            soundBala.play();
            tiempoUltimoDisparo = 0f;
        }
    }





    // Colisiones
    public boolean checkCollision(Ball2 b) {
        if (!invencible && !herido && b.getArea().overlaps(spr.getBoundingRectangle())) {

            if (xVel == 0) xVel += b.getXSpeed() / 2f;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) (xVel / 2f));
            xVel = -xVel * accelerationFactor;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2f;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) (yVel / 2f));
            yVel = -yVel * accelerationFactor;
            b.setySpeed(-b.getySpeed());

            vidas--;
            herido = true;
            invencible = true;
            tiempoHerido = tiempoHeridoMax;
            tiempoInvencible = tiempoInvencibleMax;
            sonidoHerido.play();

            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }

    // Getters y setters
    public boolean estaDestruido() { return !herido && destruida; }
    public boolean estaHerido() { return herido; }
    public int getVidas() { return vidas; }
    public void setVidas(int vidas2) { vidas = vidas2; }
    public int getX() { return (int) spr.getX(); }
    public int getY() { return (int) spr.getY(); }
    public float getWidth() { return spr.getWidth(); }
    public float getHeight() { return spr.getHeight(); }
    public Texture getTxBala() { return txBala; }
    public Sound getSoundBala() { return soundBala; }

    // Métodos del patrón Strategy
    public void setDisparoStrategy(DisparoStrategy disparoStrategy) {
        this.disparoStrategy = disparoStrategy;
    }

    public DisparoStrategy getDisparoStrategy() {
        return disparoStrategy;
    }
}
