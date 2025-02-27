package Willi.view;

import Willi.model.HistoricoDTO;
import Willi.model.Encomenda;
import Willi.model.Cliente;
import Willi.dao.ClienteDAO;
import Willi.controller.EncomendaController;
import Willi.controller.HistoricoEncomendas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TelaRastreamento extends JFrame {
    private EncomendaController controller;
    private JTable tabela;
    private JTextField txtCodigo, txtEnderecoSaida, txtEnderecoDestino, txtPeso;
    private JComboBox<String> cmbStatus;
    private JButton btnSelecionarRemetente, btnSelecionarDestinatario;
    private Cliente remetenteSelecionado, destinatarioSelecionado;
    private Encomenda encomendaAtual;
    private HistoricoEncomendas historico;
    
    public TelaRastreamento() {
        historico = new HistoricoEncomendas();
        controller = new EncomendaController(this);
        configurarTela();
    }
    
    private void configurarTela() {
        setTitle("Sistema de Rastreamento de Encomendas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Painel principal com margens
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel esquerdo para cadastro
        JPanel painelEsquerdo = new JPanel(new BorderLayout(5, 5));
        painelEsquerdo.setPreferredSize(new Dimension(400, 0));
        
        // Formulário de cadastro
        JPanel painelCadastro = new JPanel(new GridBagLayout());
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Cadastro de Encomenda"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código
        adicionarCampo(painelCadastro, "Código:", txtCodigo = new JTextField(20), gbc, 0);
        
        // Remetente
        adicionarCampo(painelCadastro, "Remetente:", 
            btnSelecionarRemetente = new JButton("Selecionar Remetente"), gbc, 1);
        btnSelecionarRemetente.addActionListener(_ -> selecionarRemetente());
        
        // Destinatário
        adicionarCampo(painelCadastro, "Destinatário:", 
            btnSelecionarDestinatario = new JButton("Selecionar Destinatário"), gbc, 2);
        btnSelecionarDestinatario.addActionListener(_ -> selecionarDestinatario());
        
        // Endereço de Saída
        adicionarCampo(painelCadastro, "Endereço Saída:", 
            txtEnderecoSaida = new JTextField(20), gbc, 3);
        
        // Endereço de Destino
        adicionarCampo(painelCadastro, "Endereço Destino:", 
            txtEnderecoDestino = new JTextField(20), gbc, 4);
        
        // Peso
        adicionarCampo(painelCadastro, "Peso (kg):", 
            txtPeso = new JTextField(10), gbc, 5);
        
        // Status
        adicionarCampo(painelCadastro, "Status:", 
            cmbStatus = new JComboBox<>(new String[]{"Pendente", "Em Trânsito", "Entregue"}), gbc, 6);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.add(criarBotao("Novo", _ -> limparCampos()));
        painelBotoes.add(criarBotao("Salvar", _ -> salvarEncomenda()));
        painelBotoes.add(criarBotao("Excluir", _ -> excluirEncomendaSelecionada()));
        painelBotoes.add(criarBotao("Histórico", _ -> mostrarTelaHistorico()));
        painelBotoes.add(criarBotao("Cadastrar Clientes", _ -> {
            TelaCadastroCliente tela = new TelaCadastroCliente(this);
            tela.setVisible(true);
        }));
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        painelCadastro.add(painelBotoes, gbc);
        
        painelEsquerdo.add(painelCadastro, BorderLayout.NORTH);
        
        // Painel direito para tabela
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        
        // Configuração da tabela
        String[] colunas = {"ID", "Código", "Remetente", "Destinatário", "Status", "Data Envio"};
        tabela = new JTable(new DefaultTableModel(colunas, 0));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(_ -> selecionarEncomenda());
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Encomendas Cadastradas"));
        painelDireito.add(scrollPane, BorderLayout.CENTER);
        
        // Adiciona os painéis ao painel principal
        painelPrincipal.add(painelEsquerdo, BorderLayout.WEST);
        painelPrincipal.add(painelDireito, BorderLayout.CENTER);
        
        // Adiciona o painel principal ao frame
        add(painelPrincipal);
        
        // Atualiza a tabela
        atualizarTabela();
    }
    
    private void adicionarCampo(JPanel painel, String label, JComponent campo, 
            GridBagConstraints gbc, int linha) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        painel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        painel.add(campo, gbc);
    }
    
    private JButton criarBotao(String texto, ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(acao);
        return botao;
    }
    
    private void selecionarEncomenda() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            encomendaAtual = new Encomenda();
            encomendaAtual.setId((int) tabela.getValueAt(linhaSelecionada, 0));
            encomendaAtual.setCodigo((String) tabela.getValueAt(linhaSelecionada, 1));
            
            // Buscar cliente remetente e destinatário do banco
            ClienteDAO clienteDAO = new ClienteDAO();
            remetenteSelecionado = clienteDAO.buscarPorId((int) tabela.getValueAt(linhaSelecionada, 2));
            destinatarioSelecionado = clienteDAO.buscarPorId((int) tabela.getValueAt(linhaSelecionada, 3));
            
            encomendaAtual.setRemetente(remetenteSelecionado);
            encomendaAtual.setDestinatario(destinatarioSelecionado);
            encomendaAtual.setStatus((String) tabela.getValueAt(linhaSelecionada, 4));
            
            txtCodigo.setText(encomendaAtual.getCodigo());
            txtEnderecoSaida.setText(encomendaAtual.getRemetente().getEndereco());
            txtEnderecoDestino.setText(encomendaAtual.getDestinatario().getEndereco());
            txtPeso.setText(String.valueOf(encomendaAtual.getPeso()));
            cmbStatus.setSelectedItem(encomendaAtual.getStatus());
        }
    }
    
    private void salvarEncomenda() {
        if (txtCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
            return;
        }
        
        Encomenda e = encomendaAtual != null ? encomendaAtual : new Encomenda();
        e.setCodigo(txtCodigo.getText());
        e.setRemetente(remetenteSelecionado);
        e.setDestinatario(destinatarioSelecionado);
        e.setStatus(cmbStatus.getSelectedItem().toString());
        e.setPeso(Double.parseDouble(txtPeso.getText()));
        e.setDataEnvio(new Date());
        
        controller.salvarEncomenda(e);
        historico.adicionarRastreamento(e.getCodigo(), e.getStatus(), e.getId());
        limparCampos();
    }
    
    private void excluirEncomendaSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir esta encomenda?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            
            if (opcao == JOptionPane.YES_OPTION) {
                int id = (int) tabela.getValueAt(linhaSelecionada, 0);
                controller.deletarEncomenda(id);
                limparCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma encomenda para excluir");
        }
    }
    
    public void atualizarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);
        
        List<Encomenda> encomendas = controller.listarEncomendas();
        for (Encomenda e : encomendas) {
            modelo.addRow(new Object[]{
                e.getId(),
                e.getCodigo(),
                e.getRemetente().getId(),
                e.getDestinatario().getId(),
                e.getStatus(),
                e.getDataEnvio()
            });
        }
    }
    
    private void limparCampos() {
        txtCodigo.setText("");
        txtEnderecoSaida.setText("");
        txtEnderecoDestino.setText("");
        txtPeso.setText("");
        cmbStatus.setSelectedIndex(0);
        encomendaAtual = null;
        tabela.clearSelection();
    }

    private void mostrarTelaHistorico() {
        JDialog dialogHistorico = new JDialog(this, "Histórico de Rastreamentos", true);
        dialogHistorico.setSize(800, 600);
        dialogHistorico.setLocationRelativeTo(this);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        // Campo de busca por cliente
        JTextField txtBuscaCliente = new JTextField(20);
        JButton btnBuscarCliente = new JButton("Buscar por Cliente");
        btnBuscarCliente.addActionListener(_ -> {
            atualizarTabelaHistorico(historico.getHistoricoPorCliente(txtBuscaCliente.getText()));
        });

        // Campos de data
        JTextField txtDataInicio = new JTextField(10);
        JTextField txtDataFim = new JTextField(10);
        JButton btnBuscarPeriodo = new JButton("Buscar por Período");
        btnBuscarPeriodo.addActionListener(_ -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dataInicio = sdf.parse(txtDataInicio.getText());
                Date dataFim = sdf.parse(txtDataFim.getText());
                atualizarTabelaHistorico(historico.getHistoricoPorPeriodo(dataInicio, dataFim));
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(dialogHistorico, 
                    "Digite as datas no formato dd/MM/yyyy",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        painelFiltros.add(new JLabel("Cliente:"));
        painelFiltros.add(txtBuscaCliente);
        painelFiltros.add(btnBuscarCliente);
        painelFiltros.add(new JLabel("Data Início:"));
        painelFiltros.add(txtDataInicio);
        painelFiltros.add(new JLabel("Data Fim:"));
        painelFiltros.add(txtDataFim);
        painelFiltros.add(btnBuscarPeriodo);

        // Tabela de histórico
        String[] colunas = {"Código", "Cliente", "Status", "Data da Consulta"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabelaHistorico = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaHistorico);

        // Botão para atualizar/mostrar todos
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.addActionListener(_ -> {
            atualizarTabelaHistorico(historico.getHistorico());
        });

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnMostrarTodos);

        // Adiciona os componentes ao painel principal
        painelPrincipal.add(painelFiltros, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        dialogHistorico.add(painelPrincipal);
        dialogHistorico.setVisible(true);

        // Carrega os dados iniciais
        atualizarTabelaHistorico(historico.getHistorico());
    }

    private void atualizarTabelaHistorico(List<HistoricoDTO> listaHistorico) {
        DefaultTableModel modelo = (DefaultTableModel) ((JTable) ((JScrollPane) ((JDialog) 
            SwingUtilities.getWindowAncestor((Component) getContentPane()
            .getComponent(0)))
            .getContentPane()
            .getComponent(0))
            .getViewport()
            .getView())
            .getModel();

        modelo.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        for (HistoricoDTO dto : listaHistorico) {
            modelo.addRow(new Object[]{
                dto.getCodigoRastreamento(),
                dto.getCliente(),
                dto.getStatus(),
                sdf.format(dto.getDataConsulta())
            });
        }
    }

    private void selecionarRemetente() {
        TelaCadastroCliente tela = new TelaCadastroCliente(this);
        tela.setVisible(true);
        remetenteSelecionado = tela.getClienteSelecionado();
        if (remetenteSelecionado != null) {
            btnSelecionarRemetente.setText("Remetente: " + remetenteSelecionado.getNome());
        }
    }

    private void selecionarDestinatario() {
        TelaCadastroCliente tela = new TelaCadastroCliente(this);
        tela.setVisible(true);
        destinatarioSelecionado = tela.getClienteSelecionado();
        if (destinatarioSelecionado != null) {
            btnSelecionarDestinatario.setText("Destinatário: " + destinatarioSelecionado.getNome());
        }
    }
}