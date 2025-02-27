package Willi.util;

import java.sql.*;

public class DatabaseSetup {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "sistema_rastreamento";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "W.l.an987";

    public static void inicializarBancoDeDados() {
        criarDatabase();
        criarTabelas();
    }

    private static void criarDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Banco de dados criado ou já existente.");
            
        } catch (SQLException e) {
            System.err.println("Erro ao criar banco de dados: " + e.getMessage());
        }
    }

    private static void criarTabelas() {
        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                // Apenas criar as tabelas se não existirem (IF NOT EXISTS)
                String[] comandosSQL = {
                    // Tabela de Clientes
                    "CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "endereco VARCHAR(200)," +
                    "telefone VARCHAR(20)," +
                    "email VARCHAR(100)," +
                    "data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")",

                    // Tabela de Encomendas
                    "CREATE TABLE IF NOT EXISTS encomendas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "codigo VARCHAR(50) NOT NULL," +
                    "remetente_id INT," +
                    "destinatario_id INT," +
                    "endereco_saida VARCHAR(200)," +
                    "endereco_destino VARCHAR(200)," +
                    "peso DECIMAL(10,2)," +
                    "status VARCHAR(50)," +
                    "data_envio TIMESTAMP," +
                    "data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (remetente_id) REFERENCES clientes(id)," +
                    "FOREIGN KEY (destinatario_id) REFERENCES clientes(id)" +
                    ")",

                    // Tabela de Histórico de Rastreamento
                    "CREATE TABLE IF NOT EXISTS historico_rastreamento (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "encomenda_id INT," +
                    "status VARCHAR(50)," +
                    "data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "observacao TEXT," +
                    "FOREIGN KEY (encomenda_id) REFERENCES encomendas(id)" +
                    ")",

                    // Tabela de Eventos de Rastreamento
                    "CREATE TABLE IF NOT EXISTS eventos_rastreamento (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "encomenda_id INT," +
                    "tipo_evento VARCHAR(50)," +
                    "localizacao VARCHAR(100)," +
                    "data_evento TIMESTAMP," +
                    "descricao TEXT," +
                    "FOREIGN KEY (encomenda_id) REFERENCES encomendas(id)" +
                    ")",

                    // Índices para otimização
                    "CREATE INDEX IF NOT EXISTS idx_cliente_nome ON clientes(nome)",
                    "CREATE INDEX IF NOT EXISTS idx_encomenda_codigo ON encomendas(codigo)",
                    "CREATE INDEX IF NOT EXISTS idx_historico_data ON historico_rastreamento(data_registro)",
                    "CREATE INDEX IF NOT EXISTS idx_eventos_data ON eventos_rastreamento(data_evento)"
                };

                for (String comando : comandosSQL) {
                    stmt.executeUpdate(comando);
                }
                System.out.println("Tabelas criadas com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 