package Willi.model;

import Willi.model.Cliente;
import java.util.Date;

public class Encomenda {
    private int id;
    private String codigo;
    private Cliente remetente;
    private Cliente destinatario;
    private String enderecoSaida;
    private String enderecoDestino;
    private double peso;
    private String status;
    private Date dataEnvio;
    private Date dataAtualizacao;

    public Encomenda() {
        this.dataEnvio = new Date();
        this.dataAtualizacao = new Date();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Cliente getRemetente() { return remetente; }
    public void setRemetente(Cliente remetente) { this.remetente = remetente; }
    public Cliente getDestinatario() { return destinatario; }
    public void setDestinatario(Cliente destinatario) { this.destinatario = destinatario; }
    public String getEnderecoSaida() { return enderecoSaida; }
    public void setEnderecoSaida(String enderecoSaida) { this.enderecoSaida = enderecoSaida; }
    public String getEnderecoDestino() { return enderecoDestino; }
    public void setEnderecoDestino(String enderecoDestino) { this.enderecoDestino = enderecoDestino; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(Date dataEnvio) { this.dataEnvio = dataEnvio; }
    public Date getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(Date dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
