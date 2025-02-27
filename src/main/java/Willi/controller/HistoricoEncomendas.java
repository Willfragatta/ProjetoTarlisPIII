package Willi.controller;

import Willi.model.HistoricoDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoEncomendas {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sistema_rastreamento";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "W.l.an987";

    public HistoricoEncomendas() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS historico_rastreamento ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "codigo_rastreamento VARCHAR(50),"
                + "data_consulta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "status VARCHAR(50),"
                + "cliente_id INT,"
                + "FOREIGN KEY (cliente_id) REFERENCES encomendas(id)"
                + ")";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adicionarRastreamento(String codigo, String status, int clienteId) {
        String sql = "INSERT INTO historico_rastreamento (codigo_rastreamento, status, cliente_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            pstmt.setString(2, status);
            pstmt.setInt(3, clienteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HistoricoDTO> getHistorico() {
        List<HistoricoDTO> historico = new ArrayList<>();
        String sql = "SELECT h.codigo_rastreamento, h.data_consulta, h.status, e.cliente "
                + "FROM historico_rastreamento h "
                + "JOIN encomendas e ON h.cliente_id = e.id "
                + "ORDER BY h.data_consulta DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                HistoricoDTO dto = new HistoricoDTO(
                    rs.getString("codigo_rastreamento"),
                    rs.getTimestamp("data_consulta"),
                    rs.getString("status"),
                    rs.getString("cliente")
                );
                historico.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historico;
    }

    public List<HistoricoDTO> getHistoricoPorCliente(String nomeCliente) {
        List<HistoricoDTO> historico = new ArrayList<>();
        String sql = "SELECT h.codigo_rastreamento, h.data_consulta, h.status, c.nome as cliente "
                + "FROM historico_rastreamento h "
                + "JOIN encomendas e ON h.cliente_id = e.id "
                + "JOIN clientes c ON e.cliente_id = c.id "
                + "WHERE c.nome LIKE ? "
                + "ORDER BY h.data_consulta DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nomeCliente + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                HistoricoDTO dto = new HistoricoDTO(
                    rs.getString("codigo_rastreamento"),
                    rs.getTimestamp("data_consulta"),
                    rs.getString("status"),
                    rs.getString("cliente")
                );
                historico.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historico;
    }

    public List<HistoricoDTO> getHistoricoPorPeriodo(java.util.Date dataInicio, java.util.Date dataFim) {
        List<HistoricoDTO> historico = new ArrayList<>();
        String sql = "SELECT h.codigo_rastreamento, h.data_consulta, h.status, c.nome as cliente "
                + "FROM historico_rastreamento h "
                + "JOIN encomendas e ON h.cliente_id = e.id "
                + "JOIN clientes c ON e.cliente_id = c.id "
                + "WHERE h.data_consulta BETWEEN ? AND ? "
                + "ORDER BY h.data_consulta DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, new java.sql.Timestamp(dataInicio.getTime()));
            pstmt.setTimestamp(2, new java.sql.Timestamp(dataFim.getTime()));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                HistoricoDTO dto = new HistoricoDTO(
                    rs.getString("codigo_rastreamento"),
                    rs.getTimestamp("data_consulta"),
                    rs.getString("status"),
                    rs.getString("cliente")
                );
                historico.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historico;
    }
}
