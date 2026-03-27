package entidade;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import principal.PainelJogo;

public class NuvemPoluicao extends Entidade {

    PainelJogo pj;

    public NuvemPoluicao(PainelJogo pj) {
        this.pj = pj;

        // Define a área de colisão da nuvem
        areaSolida = new Rectangle();
        areaSolida.x = 4;
        areaSolida.y = 4;
        areaSolida.width = 40;
        areaSolida.height = 40;
        
        velocidade = 1;
        getImagemNuvem();
    }

    public void getImagemNuvem() {
        try {
            baixo1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/nuvem-1.png"));
            baixo2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/nuvem-2.png"));
        } catch (Exception e) {
            System.out.println("ERRO: Nao foi possivel carregar os sprites da Nuvem em res/sprites/");
            e.printStackTrace();
        }
    }

    public void atualizar() {
        // Movimento de perseguição básica
        if (x < pj.jogador.x) x += velocidade;
        else if (x > pj.jogador.x) x -= velocidade;
        
        if (y < pj.jogador.y) y += velocidade;
        else if (y > pj.jogador.y) y -= velocidade;

        // Animação da nuvem
        contadorSprite++;
        if (contadorSprite > 20) {
            numeroSprite = (numeroSprite == 1) ? 2 : 1;
            contadorSprite = 0;
        }
    }

    public void desenhar(Graphics2D g2, PainelJogo pj) {
        BufferedImage imagem = null;

        // Alterna entre as duas imagens para dar efeito de movimento à fumaça
        if (numeroSprite == 1) {
            imagem = baixo1;
        } else {
            imagem = baixo2;
        }

        if (imagem != null) {
            g2.drawImage(imagem, x, y, pj.tamanhoTile, pj.tamanhoTile, null);
        }
    }
}