package principal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControleTeclado implements KeyListener {

    PainelJogo pj;
    public boolean cima, baixo, esquerda, direita;

    public ControleTeclado(PainelJogo pj) {
        this.pj = pj;
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();

        if (pj.estadoJogo == pj.estadoMenu) {
            if (codigo == KeyEvent.VK_W || codigo == KeyEvent.VK_UP) {
                pj.uiManager.comandoNum = (pj.uiManager.comandoNum == 0) ? 1 : 0;
            }
            if (codigo == KeyEvent.VK_S || codigo == KeyEvent.VK_DOWN) {
                pj.uiManager.comandoNum = (pj.uiManager.comandoNum == 1) ? 0 : 1;
            }
            if (codigo == KeyEvent.VK_ENTER) {
                if (pj.uiManager.comandoNum == 0) { // Botão START
                    pj.nivel = 1;
                    pj.configurarObjetos();
                    pj.estadoJogo = pj.estadoPlay;
                } else if (pj.uiManager.comandoNum == 1) { // Botão NÍVEIS
                    pj.estadoJogo = pj.estadoSelecaoNivel;
                }
            }
        }
        
        else if (pj.estadoJogo == pj.estadoSelecaoNivel) {
            if (codigo == KeyEvent.VK_W || codigo == KeyEvent.VK_UP) pj.uiManager.slotLin = (pj.uiManager.slotLin <= 0) ? 2 : pj.uiManager.slotLin - 1;
            if (codigo == KeyEvent.VK_S || codigo == KeyEvent.VK_DOWN) pj.uiManager.slotLin = (pj.uiManager.slotLin >= 2) ? 0 : pj.uiManager.slotLin + 1;
            if (codigo == KeyEvent.VK_A || codigo == KeyEvent.VK_LEFT) pj.uiManager.slotCol = (pj.uiManager.slotCol <= 0) ? 2 : pj.uiManager.slotCol - 1;
            if (codigo == KeyEvent.VK_D || codigo == KeyEvent.VK_RIGHT) pj.uiManager.slotCol = (pj.uiManager.slotCol >= 2) ? 0 : pj.uiManager.slotCol + 1;
            
            if (codigo == KeyEvent.VK_ENTER) {
                int n = (pj.uiManager.slotLin * 3) + pj.uiManager.slotCol + 1;
                if (pj.uiManager.niveisDesbloqueados[n]) {
                    pj.nivel = n;
                    pj.configurarObjetos();
                    pj.estadoJogo = pj.estadoPlay;
                }
            }
            if (codigo == KeyEvent.VK_ESCAPE) pj.estadoJogo = pj.estadoMenu;
        }

        else if (pj.estadoJogo == pj.estadoPlay && pj.uiManager.jogoFinalizado) {
            if (codigo == KeyEvent.VK_ENTER) {
                pj.uiManager.jogoFinalizado = false;
                pj.configurarObjetos(); // Reinicia o nível atual
            }
            if (codigo == KeyEvent.VK_ESCAPE) {
                pj.uiManager.jogoFinalizado = false;
                pj.estadoJogo = pj.estadoMenu; // Volta ao início
            }
        }

        else if (pj.estadoJogo == pj.estadoPlay && pj.uiManager.vitoriaNivel) {
            if (codigo == KeyEvent.VK_ENTER) {
                pj.nivel++;
            if (pj.nivel > 9) {
                pj.uiManager.vitoriaTotal = true;
                pj.uiManager.vitoriaNivel = false;
            }
                else {
                    pj.uiManager.niveisDesbloqueados[pj.nivel] = true;
                    pj.uiManager.vitoriaNivel = false;
                    pj.configurarObjetos();
                }
            }
        }

        else if (pj.estadoJogo == pj.estadoPlay && pj.uiManager.vitoriaTotal) {
            if (codigo == KeyEvent.VK_ENTER || codigo == KeyEvent.VK_ESCAPE) {
                pj.uiManager.vitoriaTotal = false;
                pj.estadoJogo = pj.estadoMenu;
            }
        }

        if (pj.estadoJogo == pj.estadoPlay && !pj.uiManager.jogoFinalizado && !pj.uiManager.vitoriaNivel && !pj.uiManager.vitoriaTotal) {
            if (codigo == KeyEvent.VK_W || codigo == KeyEvent.VK_UP) cima = true;
            if (codigo == KeyEvent.VK_S || codigo == KeyEvent.VK_DOWN) baixo = true;
            if (codigo == KeyEvent.VK_A || codigo == KeyEvent.VK_LEFT) esquerda = true;
            if (codigo == KeyEvent.VK_D || codigo == KeyEvent.VK_RIGHT) direita = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int codigo = e.getKeyCode();
        if (codigo == KeyEvent.VK_W || codigo == KeyEvent.VK_UP) cima = false;
        if (codigo == KeyEvent.VK_S || codigo == KeyEvent.VK_DOWN) baixo = false;
        if (codigo == KeyEvent.VK_A || codigo == KeyEvent.VK_LEFT) esquerda = false;
        if (codigo == KeyEvent.VK_D || codigo == KeyEvent.VK_RIGHT) direita = false;
    }
}