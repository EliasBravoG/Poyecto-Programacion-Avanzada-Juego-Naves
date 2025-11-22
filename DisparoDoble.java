package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DisparoDoble implements DisparoStrategy {

    @Override
    public void disparar(Nave4 nave, PantallaJuego juego, SpriteBatch batch){
        // Dos balas paralelas
        Bullet bala1 = new Bullet(
                nave.getX() + nave.getWidth() / 4,
                nave.getY() + nave.getHeight() - 5,
                0, 3, nave.getTxBala()
        );
        Bullet bala2 = new Bullet(
                nave.getX() + (nave.getWidth() * 3 / 4) - 5,
                nave.getY() + nave.getHeight() - 5,
                0, 3, nave.getTxBala()
        );
        juego.agregarBala(bala1);
        juego.agregarBala(bala2);
        nave.getSoundBala().play();
    }
}
 