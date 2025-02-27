package Willi.dao;

import Willi.dao.EncomendaDAO;

public class DAOFactory {
    public static EncomendaDAO createEncomendaDAO() {
        return new EncomendaDAO();
    }
}
