package Willi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static ConexaoDB instance;
    private Connection conexao;
    
    private ConexaoDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sistema_rastreamento",
                "root", "W.l.an987");
        } catch (ClassNotFoundException | SQLException e) {
        }
    }
    
    public static ConexaoDB getInstance() {
        if (instance == null) {
            instance = new ConexaoDB();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return conexao;
    }
}
