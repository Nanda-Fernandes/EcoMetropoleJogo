package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import mapa.OBJ_CascaBanana;

public class UI {

    PainelJogo pj;
    Font arial_20, arial_30, arial_40, arial_80B;
    BufferedImage imagemBanana;
    BufferedImage imagemMenu;
    BufferedImage imagemVitoria;
    
    // Controle de Interface
    public int comandoNum = 0; // 0: Start, 1: Níveis
    public int slotCol = 0;    
    public int slotLin = 0;    
    public boolean[] niveisDesbloqueados = new boolean[10];

    public boolean jogoFinalizado = false;
    public boolean vitoriaNivel = false;
    public boolean vitoriaTotal = false;

    public UI(PainelJogo pj) {
        this.pj = pj;
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_30 = new Font("Arial", Font.PLAIN, 30);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        niveisDesbloqueados[1] = true; // Começa com o 1 liberado

        try {
            OBJ_CascaBanana banana = new OBJ_CascaBanana();
            imagemBanana = banana.imagem;
            imagemMenu = ImageIO.read(getClass().getResourceAsStream("/res/menu/menu.png"));
            imagemVitoria = ImageIO.read(getClass().getResourceAsStream("/res/menu/telaFinal.png"));
        } catch (Exception e) {
            System.out.println("Erro ao carregar arquivos da UI (verifique res/menu/menu.png e telaFinal.png)");
        }
    }

    public void desenhar(Graphics2D g2) {
        if (pj.estadoJogo == pj.estadoMenu) {
            desenharMenuInicial(g2);
        } else if (pj.estadoJogo == pj.estadoSelecaoNivel) {
            desenharSelecaoNivel(g2);
        } else if (pj.estadoJogo == pj.estadoPlay) {
            if (vitoriaTotal) {
                desenharVitoriaTotal(g2);
            } else if (jogoFinalizado) {
                desenharGameOver(g2);
            } else if (vitoriaNivel) {
                desenharVitoriaNivel(g2);
            } else {
                desenharHUD(g2);
            }
        }
    }

    private void desenharMenuInicial(Graphics2D g2) {
        if (imagemMenu != null) {
            g2.drawImage(imagemMenu, 0, 0, pj.larguraTela, pj.alturaTela, null);
        } else {
            g2.setColor(new Color(20, 20, 20)); 
            g2.fillRect(0, 0, pj.larguraTela, pj.alturaTela);
        }

        g2.setFont(arial_80B.deriveFont(60f)); 

        String textoStart = "START";
        int xStart = getXforCenteredText(textoStart, g2);
        int yStart = pj.tamanhoTile * 8; 

        String textoNiveis = "NIVEIS";
        int xNiveis = getXforCenteredText(textoNiveis, g2);
        int yNiveis = yStart + 80; 

        desenharTextoPixado(g2, textoStart, xStart, yStart, comandoNum == 0);
        desenharTextoPixado(g2, textoNiveis, xNiveis, yNiveis, comandoNum == 1);
    }

    private void desenharTextoPixado(Graphics2D g2, String texto, int x, int y, boolean selecionado) {
        Font fonteOriginal = g2.getFont();

        g2.setColor(new Color(255, 0, 127)); 
        int offset = 5;
        g2.drawString(texto, x - offset, y - offset);
        g2.drawString(texto, x + offset, y - offset);
        g2.drawString(texto, x - offset, y + offset);
        g2.drawString(texto, x + offset, y + offset);
        g2.drawString(texto, x, y + offset + 3); 

        if (selecionado) {
            g2.setColor(new Color(0, 255, 255)); 
            g2.drawString(">", x - 55, y);
            g2.drawString("<", x + (int)g2.getFontMetrics().getStringBounds(texto, g2).getWidth() + 15, y);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString(texto, x, y);

        if (selecionado) {
            g2.setColor(new Color(255, 255, 255, 100)); 
            g2.setFont(fonteOriginal.deriveFont(Font.BOLD, fonteOriginal.getSize() - 5f)); 
            g2.drawString(texto, x + 3, y - 3);
            g2.setFont(fonteOriginal); 
        }
    }

    private void desenharSelecaoNivel(Graphics2D g2) {
    if (imagemMenu != null) {
        g2.drawImage(imagemMenu, 0, 0, pj.larguraTela, pj.alturaTela, null);
    } else {
        g2.setColor(new Color(15, 15, 15));
        g2.fillRect(0, 0, pj.larguraTela, pj.alturaTela);
    }

    g2.setFont(arial_30); 
    String titulo = "Selecione a Fase";
    int xTitulo = 50;
    int yTitulo = 70;

    g2.setColor(Color.WHITE);
    for(int i = -2; i <= 2; i++) {
        for(int j = -2; j <= 2; j++) {
            if(i != 0 || j != 0) g2.drawString(titulo, xTitulo + i, yTitulo + j);
        }
    }
    g2.setColor(Color.BLACK);
    g2.drawString(titulo, xTitulo, yTitulo);

    for (int i = 0; i < 9; i++) {
        int col = i % 3;
        int lin = i / 3;
        int n = i + 1;
        int x = 180 + (col * 140);
        int y = 120 + (lin * 120);

        if (niveisDesbloqueados[n]) {
            // NÍVEL LIBERADO (Cyan)
            g2.setColor(new Color(0, 200, 200));
            g2.fillRoundRect(x, y, 100, 100, 15, 15);
            
            g2.setColor(Color.WHITE);
            g2.setFont(arial_40);
            g2.drawString("" + n, x + 38, y + 65);
        } else {
            // NÍVEL BLOQUEADO (Cinza)
            g2.setColor(new Color(40, 40, 40));
            g2.fillRoundRect(x, y, 100, 100, 15, 15);
            
            // Desenho do Cadeado (Minimalista)
            g2.setColor(Color.GRAY);
            // Corpo do cadeado
            g2.fillRect(x + 35, y + 45, 30, 25);
            // Alça do cadeado (Arco)
            g2.setStroke(new BasicStroke(3));
            g2.drawArc(x + 40, y + 35, 20, 20, 0, 180);
        }

        if (slotCol == col && slotLin == lin) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(x - 5, y - 5, 110, 110, 20, 20);
        }
    }
    
    g2.setFont(arial_30); 
    String instrucao = "'esc' para voltar";
    int larguraTexto = g2.getFontMetrics().stringWidth(instrucao);
    int xEsc = pj.larguraTela - larguraTexto - 50; 
    int yEsc = 70; 

    g2.setColor(Color.WHITE);
    for(int i = -2; i <= 2; i++) {
        for(int j = -2; j <= 2; j++) {
            if(i != 0 || j != 0) g2.drawString(instrucao, xEsc + i, yEsc + j);
        }
    }
  
    g2.setColor(Color.BLACK);
    g2.drawString(instrucao, xEsc, yEsc);
}
    private void desenharVitoriaNivel(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, pj.larguraTela, pj.alturaTela);
        
        g2.setFont(arial_40);
        g2.setColor(Color.YELLOW);
        String t = "Nivel " + pj.nivel + " Limpo!";
        g2.drawString(t, getXforCenteredText(t, g2), pj.alturaTela / 2 - 40);

        g2.setColor(Color.CYAN);
        String btn = "CONTINUAR";
        int x = getXforCenteredText(btn, g2);
        int y = pj.alturaTela / 2 + 60;
        g2.drawString(btn, x, y);
        g2.drawString(">", x - 40, y);
    }

    private void desenharHUD(Graphics2D g2) {
        g2.setFont(arial_20);

        String nivelTexto = "Nivel: " + pj.nivel;
        String bananaTexto = "x " + pj.jogador.contadorBanana + " / " + pj.totalBananaNoMapa;

        g2.setColor(Color.BLACK);
        g2.drawString(nivelTexto, 19, 35); g2.drawString(nivelTexto, 21, 35);
        g2.drawString(nivelTexto, 20, 34); g2.drawString(nivelTexto, 20, 36);
        g2.setColor(Color.WHITE);
        g2.drawString(nivelTexto, 20, 35);

        g2.drawImage(imagemBanana, pj.larguraTela - 120, 18, 20, 20, null); 

        g2.setColor(Color.BLACK);
        g2.drawString(bananaTexto, pj.larguraTela - 96, 35); g2.drawString(bananaTexto, pj.larguraTela - 94, 35);
        g2.drawString(bananaTexto, pj.larguraTela - 95, 34); g2.drawString(bananaTexto, pj.larguraTela - 95, 36);
        g2.setColor(Color.WHITE);
        g2.drawString(bananaTexto, pj.larguraTela - 95, 35);
    }

    private void desenharGameOver(Graphics2D g2) {
    g2.setColor(new Color(0, 0, 0, 200));
    g2.fillRect(0, 0, pj.larguraTela, pj.alturaTela);
    
    g2.setFont(arial_80B);
    g2.setColor(Color.RED);
    String texto = "GAME OVER";
    int x = getXforCenteredText(texto, g2);
    int y = pj.alturaTela / 2;
    g2.drawString(texto, x, y);

    // Instrução para recomeçar
    g2.setFont(arial_20);
    g2.setColor(Color.WHITE);
    String subTexto = "ENTER para tentar novamente ou ESC para o Menu";
    g2.drawString(subTexto, getXforCenteredText(subTexto, g2), y + 60);
}

    private void desenharVitoriaTotal(Graphics2D g2) {
        if (imagemVitoria != null) {
            g2.drawImage(imagemVitoria, 0, 0, pj.larguraTela, pj.alturaTela, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, pj.larguraTela, pj.alturaTela);
        }

        g2.setFont(arial_30);

        String texto = "PARABENS! Você venceu todos os desafios!\n" +
                       "O lixo foi removido e a poluição se dissipou.\n" +
                       "A Metrópole agora esta limpa e verde novamente!\n\n" +
                       "[ENTER] para voltar ao Menu";

        int y = pj.alturaTela / 2 - 80;
        for (String linha : texto.split("\n")) {
            int x = getXforCenteredText(linha, g2);

            g2.setColor(Color.WHITE);
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (i != 0 || j != 0) g2.drawString(linha, x + i, y + j);
                }
            }

            g2.setColor(Color.BLACK);
            g2.drawString(linha, x, y);
            y += 40;
        }
    }

    public int getXforCenteredText(String texto, Graphics2D g2) {
        int comprimento = (int) g2.getFontMetrics().getStringBounds(texto, g2).getWidth();
        return pj.larguraTela / 2 - comprimento / 2;
    }
}