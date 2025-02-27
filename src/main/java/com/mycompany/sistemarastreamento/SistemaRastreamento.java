package com.mycompany.sistemarastreamento;

import Willi.util.DatabaseSetup;
import Willi.view.TelaRastreamento;
import javax.swing.SwingUtilities;

public class SistemaRastreamento {
    public static void main(String[] args) {
        try {
            // Inicializa o banco de dados antes de qualquer operação
            DatabaseSetup.inicializarBancoDeDados();
            
            // Aguarda 1 segundo para garantir que o banco foi criado
            Thread.sleep(1000);
            
            SwingUtilities.invokeLater(() -> {
                TelaRastreamento tela = new TelaRastreamento();
                tela.setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



