package principal;

import entidade.*;
import java.awt.*;
import javax.swing.JPanel;
import mapa.*;

public class PainelJogo extends JPanel implements Runnable {

    // Configurações de Tela
    public final int tamanhoTile = 48; 
    public final int maxColunas = 16, maxLinhas = 12;
    public final int larguraTela = tamanhoTile * maxColunas, alturaTela = tamanhoTile * maxLinhas; 

    // Estados do Jogo
    public int estadoJogo;
    public final int estadoMenu = 0, estadoSelecaoNivel = 1, estadoPlay = 2;

    // Motor do Jogo
    public Thread threadJogo;
    public ControleTeclado teclado = new ControleTeclado(this);
    public GerenciadorMapa mapaM = new GerenciadorMapa(this);
    public VerificadorColisao cChecker = new VerificadorColisao(this);
    public UI uiManager = new UI(this);
    
    // Entidades e Objetos
    public Jogador jogador; 
    public Rato ratos[] = new Rato[10];
    public ObjetoSuper obj[] = new ObjetoSuper[200];
    public NuvemPoluicao nuvensP[] = new NuvemPoluicao[5];
    
    public int totalBananaNoMapa = 0, nivel = 1;

    public PainelJogo() {
        this.setPreferredSize(new Dimension(larguraTela, alturaTela));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(teclado);
        this.setFocusable(true);
        
        estadoJogo = estadoMenu;
        jogador = new Jogador(this, teclado);
    }

    public void configurarObjetos() {
        mapaM.carregarMapa("/res/mapas/mapa0" + nivel + ".txt");
        
        uiManager.jogoFinalizado = false;
        uiManager.vitoriaNivel = false;
        uiManager.vitoriaTotal = false;

        for(int i = 0; i < obj.length; i++) obj[i] = null;
        for(int i = 0; i < ratos.length; i++) ratos[i] = null;
        for(int i = 0; i < nuvensP.length; i++) nuvensP[i] = null;

        int indiceObj = 0; totalBananaNoMapa = 0;
        for (int col = 0; col < maxColunas; col++) {
            for (int lin = 0; lin < maxLinhas; lin++) {
                int tileTipo = mapaM.mapaCodigo[col][lin];
                // Coloca bananas no asfalto (0) ou grama (1), longe do spawn do jogador
                if ((tileTipo == 0 || tileTipo == 1) && !(col <= 2 && lin <= 2)) {
                    if (indiceObj < obj.length) {
                        obj[indiceObj] = new OBJ_CascaBanana();
                        obj[indiceObj].mundoX = col * tamanhoTile;
                        obj[indiceObj].mundoY = lin * tamanhoTile;
                        indiceObj++; totalBananaNoMapa++;
                    }
                }
            }
        }

        // Configuração de Inimigos por Nível
        switch (nivel) {
            case 1, 2 -> {
                ratos[0] = new Rato(this);
                ratos[0].x = tamanhoTile * 13; ratos[0].y = tamanhoTile * 10;
            }
            case 3 -> {
                ratos[0] = new Rato(this);
                ratos[0].x = tamanhoTile * 13; ratos[0].y = tamanhoTile * 10;
                ratos[0].velocidade = 3; // Rato um pouco mais rápido
            }
            case 4 -> {
                ratos[0] = new Rato(this); ratos[0].x = tamanhoTile * 13; ratos[0].y = tamanhoTile * 10;
                ratos[1] = new Rato(this); ratos[1].x = tamanhoTile * 13; ratos[1].y = tamanhoTile * 2;
                ratos[0].velocidade = 3; ratos[1].velocidade = 3;
            }
            case 5 -> {
                // Dois ratos no canto Superior Direito
                ratos[0] = new Rato(this); ratos[0].x = tamanhoTile * 14; ratos[0].y = tamanhoTile * 1;
                ratos[1] = new Rato(this); ratos[1].x = tamanhoTile * 14; ratos[1].y = tamanhoTile * 2;
                // Dois ratos no canto Inferior Esquerdo
                ratos[2] = new Rato(this); ratos[2].x = tamanhoTile * 1;  ratos[2].y = tamanhoTile * 10;
                ratos[3] = new Rato(this); ratos[3].x = tamanhoTile * 2;  ratos[3].y = tamanhoTile * 10;
                // Dois ratos no canto Inferior Direito
                ratos[4] = new Rato(this); ratos[4].x = tamanhoTile * 14; ratos[4].y = tamanhoTile * 10;
                ratos[5] = new Rato(this); ratos[5].x = tamanhoTile * 13; ratos[5].y = tamanhoTile * 10;
            }
            case 6 -> {
                ratos[0] = new Rato(this); ratos[0].x = tamanhoTile * 13; ratos[0].y = tamanhoTile * 10;
                ratos[1] = new Rato(this); ratos[1].x = tamanhoTile * 5;  ratos[1].y = tamanhoTile * 10;
                ratos[0].velocidade = 3; ratos[1].velocidade = 3;
            }
            case 7 -> {
                ratos[0] = new Rato(this);
                ratos[0].x = tamanhoTile * 13; ratos[0].y = tamanhoTile * 10;
                nuvensP[0] = new NuvemPoluicao(this);
                nuvensP[0].x = tamanhoTile * 10; nuvensP[0].y = tamanhoTile * 2;
            }
            case 8 -> {
                nuvensP[0] = new NuvemPoluicao(this);
                nuvensP[0].x = tamanhoTile * 10; nuvensP[0].y = tamanhoTile * 2;
                for (int i = 0; i < 2; i++) {
                    ratos[i] = new Rato(this);
                    ratos[i].x = tamanhoTile * (12 - i); ratos[i].y = tamanhoTile * 10;
                }
            }
            case 9 -> {
                nuvensP[0] = new NuvemPoluicao(this);
                nuvensP[0].x = tamanhoTile * 10; nuvensP[0].y = tamanhoTile * 2;
                nuvensP[1] = new NuvemPoluicao(this);
                nuvensP[1].x = tamanhoTile * 2;  nuvensP[1].y = tamanhoTile * 10;
            }
        }

        jogador.x = tamanhoTile; 
        jogador.y = tamanhoTile;
        jogador.contadorBanana = 0;
    }

    public void iniciarThread() { 
        threadJogo = new Thread(this); 
        threadJogo.start(); 
    }

    @Override
    public void run() {
        double intervalo = 1000000000 / 60;
        double proximo = System.nanoTime() + intervalo;
        while (threadJogo != null) { 
            atualizar(); 
            repaint();
            
            double tempo = (proximo - System.nanoTime()) / 1000000;
            if (tempo > 0) {
                try {
                    Thread.sleep((long) tempo);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            proximo += intervalo;
        }
    }

    public void atualizar() {
        if (estadoJogo == estadoPlay && !uiManager.jogoFinalizado && !uiManager.vitoriaTotal && !uiManager.vitoriaNivel) {
            jogador.atualizar();
            
            for (Rato r : ratos) {
                if (r != null) { 
                    r.atualizar(); 
                    if (cChecker.checarEntidade(jogador, r)) {
                        uiManager.jogoFinalizado = true;
                    }
                }
            }

            for (NuvemPoluicao n : nuvensP) {
                if (n != null) {
                    n.atualizar();
                    if (cChecker.checarEntidade(jogador, n)) {
                        uiManager.jogoFinalizado = true;
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (estadoJogo == estadoPlay) {
            mapaM.desenhar(g2);
            for (ObjetoSuper o : obj) if (o != null) o.desenhar(g2, this);
            for (Rato r : ratos) if (r != null) r.desenhar(g2, this);
            jogador.desenhar(g2, this);
            
            for (NuvemPoluicao n : nuvensP) {
                if (n != null) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    n.desenhar(g2, this);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }
        }
        
        uiManager.desenhar(g2);
        g2.dispose();
    }
}