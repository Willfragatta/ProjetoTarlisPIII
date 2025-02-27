package Willi.model;

import java.sql.Timestamp;

public class HistoricoDTO {
    private String codigoRastreamento;
    private Timestamp dataConsulta;
    private String status;
    private String cliente;

    public HistoricoDTO(String codigoRastreamento, Timestamp dataConsulta, String status, String cliente) {
        this.codigoRastreamento = codigoRastreamento;
        this.dataConsulta = dataConsulta;
        this.status = status;
        this.cliente = cliente;
    }

    // Getters
    public String getCodigoRastreamento() { return codigoRastreamento; }
    public Timestamp getDataConsulta() { return dataConsulta; }
    public String getStatus() { return status; }
    public String getCliente() { return cliente; }
} 