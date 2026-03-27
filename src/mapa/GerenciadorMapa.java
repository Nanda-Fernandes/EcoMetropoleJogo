package mapa;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import principal.PainelJogo;

public class GerenciadorMapa {
    PainelJogo pj;
    public Tile[] tile;
    public int mapaCodigo[][];

    public GerenciadorMapa(PainelJogo pj) {
        this.pj = pj;
        tile = new Tile[10];
        mapaCodigo = new int[pj.maxColunas][pj.maxLinhas];
        
        getImagemTile();
    }

    public void getImagemTile() {
        try {
            // TILE 0: asfalto (Chão livre)
            tile[0] = new Tile();
            tile[0].imagem = ImageIO.read(getClass().getResourceAsStream("/res/sprites/asfalto.png"));

            // TILE 1: grama (Chão livre)
            tile[1] = new Tile();
            tile[1].imagem = ImageIO.read(getClass().getResourceAsStream("/res/sprites/grama.png"));

            // TILE 2: Casa (Bloqueio)
            tile[2] = new Tile();
            tile[2].imagem = ImageIO.read(getClass().getResourceAsStream("/res/sprites/casa.png"));
            tile[2].colisao = true;
            
            // TILE 3: Predio (Bloqueio)
            tile[3] = new Tile();
            tile[3].imagem = ImageIO.read(getClass().getResourceAsStream("/res/sprites/predio.png"));
            tile[3].colisao = true;

            // TILE 4: loja (Bloqueio)
            tile[4] = new Tile();
            tile[4].imagem = ImageIO.read(getClass().getResourceAsStream("/res/sprites/loja.png"));
            tile[4].colisao = true;

        } catch (Exception e) {
            System.out.println("ERRO: Nao foi possivel carregar as imagens dos tiles em res/sprites/");
            e.printStackTrace();
        }
    }

    public void carregarMapa(String caminhoArquivo) {
        try {
            InputStream is = getClass().getResourceAsStream(caminhoArquivo);
            
            if (is == null) {
                System.out.println("ERRO: O arquivo de mapa nao existe: " + caminhoArquivo);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int lin = 0;

            while (lin < pj.maxLinhas) {
                String linha = br.readLine();
                
                if (linha == null || linha.isEmpty()) break;

                String numeros[] = linha.trim().split("\\s+");

                for (col = 0; col < pj.maxColunas; col++) {
                    if (col < numeros.length) {
                        int num = Integer.parseInt(numeros[col]);
                        mapaCodigo[col][lin] = num;
                    }
                }
                lin++;
            }
            br.close();
            System.out.println("Mapa " + caminhoArquivo + " carregado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("ERRO: Falha ao processar o arquivo de texto do mapa.");
            e.printStackTrace();
        }
    }

    public void desenhar(Graphics2D g2) {
        for (int lin = 0; lin < pj.maxLinhas; lin++) {
            for (int col = 0; col < pj.maxColunas; col++) {
                
                int tipoTile = mapaCodigo[col][lin];
                int x = col * pj.tamanhoTile;
                int y = lin * pj.tamanhoTile;
                
                if (tile[tipoTile] != null && tile[tipoTile].imagem != null) {
                    g2.drawImage(tile[tipoTile].imagem, x, y, pj.tamanhoTile, pj.tamanhoTile, null);
                }
            }
        }
    }
}