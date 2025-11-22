package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    // HUD
    private String tipoDisparo = "Simple";

    // Control de niveles
    private boolean nivelCompletado = false;
    private float tiempoTransicion = 0f;
    private boolean cambiandoNivel = false; // Previene llamadas múltiples

    public PantallaJuego(int ronda, int vidas, int score,
                         int velXAsteroides, int velYAsteroides, int cantAsteroides) {

        this.game = SpaceNavigation.getInstance();  // Singleton
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 640);

        this.nivelCompletado = false;
        this.tiempoTransicion = 0f;
        this.cambiandoNivel = false;

        // Sonidos y música
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        // Crear la nave
        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30,
                new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        nave.setVidas(vidas);

        // Por defecto: disparo simple
        nave.setDisparoStrategy(new DisparoSimple());

        crearAsteroides();
    }

    private void crearAsteroides() {
        Random r = new Random();
        balls1.clear();
        balls2.clear();

        Texture texturaAsteroide;
        if (ronda % 9 >= 6) {
            texturaAsteroide = new Texture(Gdx.files.internal("aGreyLarge.png"));
        } else if (ronda % 9 >= 3) {
            texturaAsteroide = new Texture(Gdx.files.internal("aGreyMedium4.png"));
        } else {
            texturaAsteroide = new Texture(Gdx.files.internal("aGreySmall.png"));
        }

        int baseSize = 20 + (ronda % 9) * 2;

        for (int i = 0; i < cantAsteroides; i++) {
            int size = baseSize + r.nextInt(5);
            Ball2 bb = new Ball2(
                r.nextInt((int) Gdx.graphics.getWidth() - size),
                50 + r.nextInt((int) Gdx.graphics.getHeight() - 100),
                size,
                velXAsteroides + r.nextInt(3),
                velYAsteroides + r.nextInt(3),
                texturaAsteroide
            );
            balls1.add(bb);
            balls2.add(bb);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // HUD
        game.getFont().draw(batch, "Vidas: " + nave.getVidas() + "  Ronda: " + ronda, 10, 30);
        game.getFont().draw(batch, "Score: " + score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore: " + game.getHighScore(),
                Gdx.graphics.getWidth() / 2 - 100, 30);
        game.getFont().draw(batch, "Disparo: " + tipoDisparo,
                Gdx.graphics.getWidth() - 300, 60);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            nave.setDisparoStrategy(new DisparoSimple());
            tipoDisparo = "Simple";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            nave.setDisparoStrategy(new DisparoDoble());
            tipoDisparo = "Doble";
        }

        // Transición entre niveles
        if (nivelCompletado && !cambiandoNivel) {
            tiempoTransicion += delta;
            game.getFont().draw(batch, "Nivel " + (ronda + 1) + " en camino...",
                    Gdx.graphics.getWidth() / 2 - 100,
                    Gdx.graphics.getHeight() / 2);

            if (tiempoTransicion > 2f) {
                cambiandoNivel = true;
                Gdx.app.postRunnable(() -> siguienteNivel());
            }

            batch.end();
            return;
        }

        // Actualización del juego
        nave.update(batch, delta);
        nave.disparar(this, batch);

        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update();
            b.draw(batch);

            for (int j = 0; j < balls1.size(); j++) {
                if (b.checkCollision(balls1.get(j))) {
                    explosionSound.play();
                    balls1.remove(j);
                    balls2.remove(j);
                    j--;
                    score += 10;
                }
            }
            if (b.isDestroyed()) {
                balas.remove(i);
                i--;
            }
        }

        for (int i = 0; i < balls1.size(); i++) {
            Ball2 ball = balls1.get(i);
            ball.update();
            ball.draw(batch);
        }

        limpiarAsteroidesFuera();

        for (Ball2 ball : balls1) {
            if (ball.getXSpeed() == 0 && ball.getySpeed() == 0) {
                ball.setXSpeed(1 + (int)(Math.random() * 2));
                ball.setySpeed(1 + (int)(Math.random() * 2));
            }
        }

        for (int i = 0; i < balls1.size(); i++) {
            Ball2 ball1 = balls1.get(i);
            for (int j = i + 1; j < balls2.size(); j++) {
                Ball2 ball2 = balls2.get(j);
                ball1.checkCollision(ball2);
            }
        }

        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            if (nave.checkCollision(b)) {
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }
        }

        if (!nivelCompletado && !nave.estaDestruido()) {
            if (balls1.isEmpty()) {
                nivelCompletado = true;
                tiempoTransicion = 0;
            }
        }

        if (nave.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver();
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }

        batch.end();
    }

    private void siguienteNivel() {
        balls1.clear();
        balls2.clear();
        balas.clear();

        nivelCompletado = false;
        tiempoTransicion = 0f;
        cambiandoNivel = false;

        int nuevaRonda = ronda + 1;

        float factorVelocidad = 1.0f + (nuevaRonda / 2) * 0.1f;
        int nuevaVelX = Math.min((int)(velXAsteroides * factorVelocidad), 5);
        int nuevaVelY = Math.min((int)(velYAsteroides * factorVelocidad), 5);
        int nuevaCantidad = Math.min(cantAsteroides + (nuevaRonda < 5 ? 1 : 0), 20);

        Screen ss = new PantallaJuego(
                nuevaRonda,
                nave.getVidas(),
                score,
                nuevaVelX,
                nuevaVelY,
                nuevaCantidad
        );
        ss.resize(1200, 800);
        game.setScreen(ss);
        dispose();
    }

    private void limpiarAsteroidesFuera() {
        float margen = 60;
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            float bx = b.getArea().x;
            float by = b.getArea().y;

            if (bx < -margen || bx > Gdx.graphics.getWidth() + margen ||
                by < -margen || by > Gdx.graphics.getHeight() + margen) {
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }
        }
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override public void show() { gameMusic.play(); }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        explosionSound.dispose();
        gameMusic.dispose();
    }
}
