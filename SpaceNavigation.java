package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceNavigation extends Game {

    private static SpaceNavigation instance;  // Instancia única
    private SpriteBatch batch;
    private BitmapFont font;
    private int highScore;

    // Constructor privado que impide crear nuevas instancias
    private SpaceNavigation() {}

    // Método estático de acceso global
    public static SpaceNavigation getInstance() {
        if (instance == null) {
            instance = new SpaceNavigation();
        }
        return instance;
    }

    @Override
    public void create() {
        highScore = 0;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        setScreen(new PantallaMenu());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
