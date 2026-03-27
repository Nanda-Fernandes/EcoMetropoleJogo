package entidade;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//Classe base para todos os objetos móveis do jogo (Jogador, Ratos, NPC).

public class Entidade {

    // Posição no mundo (em pixel)
    public int x, y;
    public int velocidade;

    // Imagens para animação
    public BufferedImage cima1, cima2, baixo1, baixo2, esquerda1, esquerda2, direita1, direita2;
    public String direcao = "baixo"; // Direção inicial padrão

    // Controle de animação (Troca de frames)
    public int contadorSprite = 0;
    public int numeroSprite = 1;

    // COLISÃO: Define a caixa de impacto da entidade
    public Rectangle areaSolida = new Rectangle(0, 0, 48, 48);
    
    // Variáveis auxiliares para o VerificadorColisao
    public int areaSolidaDefaultX, areaSolidaDefaultY;
    public boolean colisaoLigada = false;

    //Construtor padrão
    public Entidade() {
        // Guarda os valores originais para resets, se necessário
        areaSolidaDefaultX = areaSolida.x;
        areaSolidaDefaultY = areaSolida.y;
    }
}