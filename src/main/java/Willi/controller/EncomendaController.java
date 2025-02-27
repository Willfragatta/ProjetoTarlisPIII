
package Willi.controller;

import Willi.view.TelaRastreamento;
import Willi.model.Encomenda;
import Willi.dao.DAOFactory;
import Willi.dao.EncomendaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EncomendaController {
    private final EncomendaDAO dao;
    private final TelaRastreamento view;
    public EncomendaController(TelaRastreamento view) {
        this.dao = DAOFactory.createEncomendaDAO();
        this.view = view;
    }
    
    public void salvarEncomenda(Encomenda e) {
        try {
            if(e.getId() == 0) {
                dao.inserir(e);
            } else {
                dao.atualizar(e);
            }
            view.atualizarTabela();
            JOptionPane.showMessageDialog(null, "Encomenda salva com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
        }
    }
    
    public void deletarEncomenda(int id) {
        try {
            dao.deletar(id);
            view.atualizarTabela();
            JOptionPane.showMessageDialog(null, "Encomenda removida com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar: " + ex.getMessage());
        }
    }
    
    public List<Encomenda> listarEncomendas() {
        try {
            return dao.listar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao listar: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
