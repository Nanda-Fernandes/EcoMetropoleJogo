package principal;

import javax.swing.JFrame;

public class Principal {
    public static void main(String[] args) {
    
        // 1. Criar a janela (Frame)
        JFrame janela = new JFrame();
        
        // 2. Configurações básicas da janela
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false); 
        janela.setTitle("Eco Metropole"); 

        // 3. Instanciar o Painel do Jogo
        PainelJogo painelJogo = new PainelJogo();
        janela.add(painelJogo);

        // 4. Ajustar o tamanho da janela ao tamanho do painel (768x576 pixels)
        janela.pack();

        // 5. Centralizar na tela e exibir
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);

        // 6. Iniciar o Game Loop
        painelJogo.iniciarThread();
    }
}