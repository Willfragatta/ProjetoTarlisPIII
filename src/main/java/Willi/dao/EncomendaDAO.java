package Willi.dao;

import Willi.dao.ClienteDAO;
import Willi.model.Encomenda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EncomendaDAO {
    private Connection conn;
    
    public EncomendaDAO() {
        this.conn = ConexaoDB.getInstance().getConnection();
    }

    public void inserir(Encomenda e) throws SQLException {
        String sql = "INSERT INTO encomendas (codigo, remetente_id, destinatario_id, endereco_saida, endereco_destino, peso, status, data_envio) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, e.getCodigo());
        stmt.setInt(2, e.getRemetente().getId());
        stmt.setInt(3, e.getDestinatario().getId());
        stmt.setString(4, e.getEnderecoSaida());
        stmt.setString(5, e.getEnderecoDestino());
        stmt.setDouble(6, e.getPeso());
        stmt.setString(7, e.getStatus());
        stmt.setTimestamp(8, new java.sql.Timestamp(e.getDataEnvio().getTime()));
        stmt.execute();
    }
    
    public List<Encomenda> listar() throws SQLException {
        List<Encomenda> encomendas = new ArrayList<>();
        String sql = "SELECT e.*, " +
                    "r.nome as remetente_nome, r.endereco as remetente_endereco, r.telefone as remetente_telefone, r.email as remetente_email, " +
                    "d.nome as destinatario_nome, d.endereco as destinatario_endereco, d.telefone as destinatario_telefone, d.email as destinatario_email " +
                    "FROM encomendas e " +
                    "JOIN clientes r ON e.remetente_id = r.id " +
                    "JOIN clientes d ON e.destinatario_id = d.id";
        
        ResultSet rs = conn.createStatement().executeQuery(sql);
        ClienteDAO clienteDAO = new ClienteDAO();
        
        while(rs.next()) {
            Encomenda e = new Encomenda();
            e.setId(rs.getInt("id"));
            e.setCodigo(rs.getString("codigo"));
            e.setRemetente(clienteDAO.buscarPorId(rs.getInt("remetente_id")));
            e.setDestinatario(clienteDAO.buscarPorId(rs.getInt("destinatario_id")));
            e.setEnderecoSaida(rs.getString("endereco_saida"));
            e.setEnderecoDestino(rs.getString("endereco_destino"));
            e.setPeso(rs.getDouble("peso"));
            e.setStatus(rs.getString("status"));
            e.setDataEnvio(rs.getTimestamp("data_envio"));
            encomendas.add(e);
        }
        
        return encomendas;
    }
    
    public void atualizar(Encomenda e) throws SQLException {
        String sql = "UPDATE encomendas SET codigo=?, remetente_id=?, destinatario_id=?, " +
                    "endereco_saida=?, endereco_destino=?, peso=?, status=?, data_envio=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, e.getCodigo());
        stmt.setInt(2, e.getRemetente().getId());
        stmt.setInt(3, e.getDestinatario().getId());
        stmt.setString(4, e.getEnderecoSaida());
        stmt.setString(5, e.getEnderecoDestino());
        stmt.setDouble(6, e.getPeso());
        stmt.setString(7, e.getStatus());
        stmt.setTimestamp(8, new java.sql.Timestamp(e.getDataEnvio().getTime()));
        stmt.setInt(9, e.getId());
        stmt.execute();
    }
    
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM encomendas WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
    }
}
