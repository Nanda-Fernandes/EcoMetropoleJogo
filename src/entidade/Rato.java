package entidade;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import principal.PainelJogo;

public class Rato extends Entidade {

    PainelJogo pj;
    private int contadorAcao = 0;

    public Rato(PainelJogo pj) {
        this.pj = pj;

        // Define a caixa de colisão do rato
        areaSolida = new Rectangle();
        areaSolida.x = 3;
        areaSolida.y = 18;
        areaSolida.width = 42;
        areaSolida.height = 30;
        areaSolidaDefaultX = areaSolida.x;
        areaSolidaDefaultY = areaSolida.y;

        velocidade = 1; 
        direcao = "baixo";
        getImagemRato();
    }

    public void getImagemRato() {
        try {
            cima1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoEsquerda-1.png"));
            cima2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoEsquerda-2.png"));
            baixo1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoDireita-1.png"));
            baixo2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoDireita-2.png"));
            esquerda1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoEsquerda-1.png"));
            esquerda2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoEsquerda-2.png"));
            direita1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoDireita-1.png"));
            direita2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ratoDireita-2.png"));
        } catch (Exception e) {
            System.out.println("ERRO: Nao foi possivel carregar os sprites do Rato em res/sprites/");
        }
    }

    public void setAcao() {
        contadorAcao++;

        // O rato escolhe uma nova direção a cada 2 segundos (120 frames)
        if (contadorAcao == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) direcao = "cima";
            else if (i <= 50) direcao = "baixo";
            else if (i <= 75) direcao = "esquerda";
            else direcao = "direita";

            contadorAcao = 0;
        }
    }

    public void atualizar() {
        setAcao();

        colisaoLigada = false;
        pj.cChecker.checarTile(this);

        if (!colisaoLigada) {
            switch (direcao) {
                case "cima": y -= velocidade; break;
                case "baixo": y += velocidade; break;
                case "esquerda": x -= velocidade; break;
                case "direita": x += velocidade; break;
            }
        } else {
            contadorAcao = 119;
        }

        // Animação de caminhada
        contadorSprite++;
        if (contadorSprite > 15) {
            numeroSprite = (numeroSprite == 1) ? 2 : 1;
            contadorSprite = 0;
        }
    }

    public void desenhar(java.awt.Graphics2D g2, PainelJogo pj) {
        BufferedImage imagem = null;

        switch (direcao) {
            case "cima":
                imagem = (numeroSprite == 1) ? cima1 : cima2;
                break;
            case "baixo":
                imagem = (numeroSprite == 1) ? baixo1 : baixo2;
                break;
            case "esquerda":
                imagem = (numeroSprite == 1) ? esquerda1 : esquerda2;
                break;
            case "direita":
                imagem = (numeroSprite == 1) ? direita1 : direita2;
                break;
        }

        if (imagem != null) {
            g2.drawImage(imagem, x, y, pj.tamanhoTile, pj.tamanhoTile, null);
        }
    }
}