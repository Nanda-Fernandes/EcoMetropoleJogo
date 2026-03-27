package mapa;

import javax.imageio.ImageIO;

public class OBJ_CascaBanana extends ObjetoSuper {

    public OBJ_CascaBanana() {
        
        nome = "Casca de Banana";
        
        try {
            imagem = ImageIO.read(getClass().getResourceAsStream("/res/sprites/casca-banana.png"));
            
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem da Casca de Banana");
            e.printStackTrace();
        }
    }
}