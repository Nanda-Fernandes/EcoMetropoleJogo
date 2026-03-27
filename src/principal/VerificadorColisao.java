package principal;

import entidade.Entidade;

public class VerificadorColisao {

    PainelJogo pj;

    public VerificadorColisao(PainelJogo pj) {
        this.pj = pj;
    }

    public void checarTile(Entidade entidade) {

        // Localização da área sólida da entidade em pixels no mundo
        int entidadeEsquerdaX = entidade.x + entidade.areaSolida.x;
        int entidadeDireitaX = entidade.x + entidade.areaSolida.x + entidade.areaSolida.width;
        int entidadeCimaY = entidade.y + entidade.areaSolida.y;
        int entidadeBaixoY = entidade.y + entidade.areaSolida.y + entidade.areaSolida.height;

        // Converte as coordenadas de pixels para colunas e linhas do mapa
        int colunaEsquerda = entidadeEsquerdaX / pj.tamanhoTile;
        int colunaDireita = entidadeDireitaX / pj.tamanhoTile;
        int linhaCima = entidadeCimaY / pj.tamanhoTile;
        int linhaBaixo = entidadeBaixoY / pj.tamanhoTile;
        
        // Proteção inicial contra saída das bordas do array
        if (colunaEsquerda < 0) colunaEsquerda = 0;
        if (colunaDireita >= pj.maxColunas) colunaDireita = pj.maxColunas - 1;
        if (linhaCima < 0) linhaCima = 0;
        if (linhaBaixo >= pj.maxLinhas) linhaBaixo = pj.maxLinhas - 1;

        int tileNum1, tileNum2;

        switch (entidade.direcao) {
            case "cima" -> {
                linhaCima = (entidadeCimaY - entidade.velocidade) / pj.tamanhoTile;
                if (linhaCima >= 0 && linhaCima < pj.maxLinhas) {
                    // Garante que o número do tile não ultrapasse o tamanho do array 
                    tileNum1 = Math.min(pj.mapaM.mapaCodigo[colunaEsquerda][linhaCima], pj.mapaM.tile.length - 1);
                    tileNum2 = Math.min(pj.mapaM.mapaCodigo[colunaDireita][linhaCima], pj.mapaM.tile.length - 1);
                    
                    if ((pj.mapaM.tile[tileNum1] != null && pj.mapaM.tile[tileNum1].colisao) || 
                        (pj.mapaM.tile[tileNum2] != null && pj.mapaM.tile[tileNum2].colisao)) {
                        entidade.colisaoLigada = true;
                    }
                } else {
                    entidade.colisaoLigada = true; // Impede sair do mapa
                }
            }

            case "baixo" -> {
                linhaBaixo = (entidadeBaixoY + entidade.velocidade) / pj.tamanhoTile;
                if (linhaBaixo >= 0 && linhaBaixo < pj.maxLinhas) {
                    tileNum1 = pj.mapaM.mapaCodigo[colunaEsquerda][linhaBaixo];
                    tileNum2 = pj.mapaM.mapaCodigo[colunaDireita][linhaBaixo];
                    if ((pj.mapaM.tile[tileNum1] != null && pj.mapaM.tile[tileNum1].colisao) || 
                        (pj.mapaM.tile[tileNum2] != null && pj.mapaM.tile[tileNum2].colisao)) {
                        entidade.colisaoLigada = true;
                    }
                } else {
                    entidade.colisaoLigada = true;
                }
            }

            case "esquerda" -> {
                colunaEsquerda = (entidadeEsquerdaX - entidade.velocidade) / pj.tamanhoTile;
                if (colunaEsquerda >= 0 && colunaEsquerda < pj.maxColunas) {
                    tileNum1 = pj.mapaM.mapaCodigo[colunaEsquerda][linhaCima];
                    tileNum2 = pj.mapaM.mapaCodigo[colunaEsquerda][linhaBaixo];
                    if ((pj.mapaM.tile[tileNum1] != null && pj.mapaM.tile[tileNum1].colisao) || 
                        (pj.mapaM.tile[tileNum2] != null && pj.mapaM.tile[tileNum2].colisao)) {
                        entidade.colisaoLigada = true;
                    }
                } else {
                    entidade.colisaoLigada = true;
                }
            }

            case "direita" -> {
                colunaDireita = (entidadeDireitaX + entidade.velocidade) / pj.tamanhoTile;
                if (colunaDireita >= 0 && colunaDireita < pj.maxColunas) {
                    tileNum1 = pj.mapaM.mapaCodigo[colunaDireita][linhaCima];
                    tileNum2 = pj.mapaM.mapaCodigo[colunaDireita][linhaBaixo];
                    if ((pj.mapaM.tile[tileNum1] != null && pj.mapaM.tile[tileNum1].colisao) || 
                        (pj.mapaM.tile[tileNum2] != null && pj.mapaM.tile[tileNum2].colisao)) {
                        entidade.colisaoLigada = true;
                    }
                } else {
                    entidade.colisaoLigada = true;
                }
            }
        }
    }

    /**
     * Verifica se o jogador encostou em algum objeto (banana).
     * @return O índice do objeto no array, ou 999 se não houver colisão.
     */
    public int checarObjeto(Entidade entidade, boolean jogador) {
        int indice = 999;

        for (int i = 0; i < pj.obj.length; i++) {
            if (pj.obj[i] != null) {
              
                int entX = entidade.x + entidade.areaSolida.x;
                int entY = entidade.y + entidade.areaSolida.y;
                int objX = pj.obj[i].mundoX + pj.obj[i].areaSolida.x;
                int objY = pj.obj[i].mundoY + pj.obj[i].areaSolida.y;

                if (entX < objX + pj.obj[i].areaSolida.width &&
                    entX + entidade.areaSolida.width > objX &&
                    entY < objY + pj.obj[i].areaSolida.height &&
                    entY + entidade.areaSolida.height > objY) {
                    
                    if (pj.obj[i].colisao) {
                        entidade.colisaoLigada = true;
                    }
                    if (jogador) {
                        indice = i;
                    }
                }
            }
        }
        return indice;
    }

    public boolean checarEntidade(Entidade entidade, Entidade alvo) {
       
        int entX = entidade.x + entidade.areaSolida.x;
        int entY = entidade.y + entidade.areaSolida.y;
        
        int alvoX = alvo.x + alvo.areaSolida.x;
        int alvoY = alvo.y + alvo.areaSolida.y;
        
        return entX < alvoX + alvo.areaSolida.width &&
               entX + entidade.areaSolida.width > alvoX &&
               entY < alvoY + alvo.areaSolida.height &&
               entY + entidade.areaSolida.height > alvoY;
    }
}