package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DisparoSimple implements DisparoStrategy {

    @Override
    public void disparar(Nave4 nave, PantallaJuego juego, SpriteBatch batch) {

        Bullet bala = new Bullet(
                nave.getX() + nave.getWidth() / 2 - 5,
                nave.getY() + nave.getHeight() - 5,
                0, 3, nave.getTxBala()
        );
        juego.agregarBala(bala);
        nave.getSoundBala().play();
    }
}
