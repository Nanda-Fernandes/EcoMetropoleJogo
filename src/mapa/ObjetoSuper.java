package mapa;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.PainelJogo;

public class ObjetoSuper {

    public BufferedImage imagem;
    public String nome;
    public boolean colisao = false;
    
    // Posição no mundo (em pixels)
    public int mundoX, mundoY;
    
    // Define a área de colisão do objeto para o ativista conseguir "pegar"
    public Rectangle areaSolida = new Rectangle(0, 0, 48, 48);
    public int areaSolidaDefaultX = 0;
    public int areaSolidaDefaultY = 0;

    public void desenhar(Graphics2D g2, PainelJogo pj) {
        if (imagem != null) {
            g2.drawImage(imagem, mundoX, mundoY, pj.tamanhoTile, pj.tamanhoTile, null);
        }
    }
}