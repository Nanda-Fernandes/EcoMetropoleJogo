package entidade;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import principal.ControleTeclado;
import principal.PainelJogo;

public class Jogador extends Entidade {

    PainelJogo pj;
    ControleTeclado teclado;
    public int contadorBanana = 0; // Quantidade de banana coletadas

    public Jogador(PainelJogo pj, ControleTeclado teclado) {
        this.pj = pj;
        this.teclado = teclado;

        // Define a caixa de colisão
        areaSolida = new Rectangle();
        areaSolida.x = 8;
        areaSolida.y = 16;
        areaSolidaDefaultX = areaSolida.x;
        areaSolidaDefaultY = areaSolida.y;
        areaSolida.width = 32;
        areaSolida.height = 32;

        setValoresPadrao();
        getImagemJogador();
    }

    public void setValoresPadrao() {
        x = pj.tamanhoTile; // Começa na coluna 1
        y = pj.tamanhoTile; // Começa na linha 1
        velocidade = 4;
        direcao = "baixo";
    }

    public void getImagemJogador() {
        try {
            cima1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaAtras-1.png"));
            cima2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaAtras-2.png"));
            baixo1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaFrente-1.png"));
            baixo2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaFrente-2.png"));
            esquerda1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaEsquerda-1.png"));
            esquerda2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaEsquerda-2.png"));
            direita1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaDireita-1.png"));
            direita2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/ativistaDireita-2.png"));
        } catch (Exception e) {
            System.out.println("ERRO: Nao foi possivel carregar os sprites do Jogador em res/sprites/");
            e.printStackTrace();
        }
    }

    public void atualizar() {
        if (teclado.cima || teclado.baixo || teclado.esquerda || teclado.direita) {

            if (teclado.cima) direcao = "cima";
            else if (teclado.baixo) direcao = "baixo";
            else if (teclado.esquerda) direcao = "esquerda";
            else if (teclado.direita) direcao = "direita";

            colisaoLigada = false;
            pj.cChecker.checarTile(this);

            int indiceObj = pj.cChecker.checarObjeto(this, true);
            coletarBanana(indiceObj);

            // SE A COLISÃO ESTIVER DESLIGADA, O JOGADOR SE MOVE
            if (!colisaoLigada) {
                switch (direcao) {
                    case "cima": y -= velocidade; break;
                    case "baixo": y += velocidade; break;
                    case "esquerda": x -= velocidade; break;
                    case "direita": x += velocidade; break;
                }
            }

            // Animação de troca de frames
            contadorSprite++;
            if (contadorSprite > 12) {
                numeroSprite = (numeroSprite == 1) ? 2 : 1;
                contadorSprite = 0;
            }
        }
    }

    public void coletarBanana(int i) {
        if (i != 999) {
            // Remove a banana do mapa e aumenta o contador
            pj.obj[i] = null;
            contadorBanana++;
            
            if (pj.totalBananaNoMapa > 0 && contadorBanana >= pj.totalBananaNoMapa) {
                pj.uiManager.vitoriaNivel = true;
            }
        }
    }

    public void desenhar(Graphics2D g2, PainelJogo pj) {
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