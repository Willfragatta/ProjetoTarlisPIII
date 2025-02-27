package Willi.view;

import Willi.model.Cliente;
import Willi.dao.ClienteDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroCliente extends JDialog {
    private ClienteDAO clienteDAO;
    private JTextField txtNome, txtEndereco, txtTelefone, txtEmail;
    private JTable tabela;
    private Cliente clienteAtual;

    public TelaCadastroCliente(Frame owner) {
        super(owner, "Cadastro de Clientes", true);
        clienteDAO = new ClienteDAO();
        configurarTela();
    }

    private void configurarTela() {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Painel de formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos do formulário
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(30);
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        txtEndereco = new JTextField(30);
        painelFormulario.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        txtTelefone = new JTextField(15);
        painelFormulario.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(30);
        painelFormulario.add(txtEmail, gbc);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");

        btnNovo.addActionListener(_ -> limparCampos());
        btnSalvar.addActionListener(_ -> salvarCliente());
        btnExcluir.addActionListener(_ -> excluirCliente());

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        painelFormulario.add(painelBotoes, gbc);

        // Tabela de clientes
        String[] colunas = {"ID", "Nome", "Endereço", "Telefone", "Email"};
        tabela = new JTable(new DefaultTableModel(colunas, 0));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(_ -> selecionarCliente());

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Clientes Cadastrados"));

        // Adiciona os componentes
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        atualizarTabela();
    }

    private void salvarCliente() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!");
            return;
        }

        Cliente cliente = clienteAtual != null ? clienteAtual : new Cliente();
        cliente.setNome(txtNome.getText());
        cliente.setEndereco(txtEndereco.getText());
        cliente.setTelefone(txtTelefone.getText());
        cliente.setEmail(txtEmail.getText());

        clienteDAO.salvar(cliente);
        limparCampos();
        atualizarTabela();
    }

    private void selecionarCliente() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            clienteAtual = new Cliente();
            clienteAtual.setId((int) tabela.getValueAt(linha, 0));
            clienteAtual.setNome((String) tabela.getValueAt(linha, 1));
            clienteAtual.setEndereco((String) tabela.getValueAt(linha, 2));
            clienteAtual.setTelefone((String) tabela.getValueAt(linha, 3));
            clienteAtual.setEmail((String) tabela.getValueAt(linha, 4));

            txtNome.setText(clienteAtual.getNome());
            txtEndereco.setText(clienteAtual.getEndereco());
            txtTelefone.setText(clienteAtual.getTelefone());
            txtEmail.setText(clienteAtual.getEmail());
        }
    }

    private void excluirCliente() {
        if (clienteAtual != null) {
            int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este cliente?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            
            if (opcao == JOptionPane.YES_OPTION) {
                clienteDAO.deletar(clienteAtual.getId());
                limparCampos();
                atualizarTabela();
            }
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        clienteAtual = null;
        tabela.clearSelection();
    }

    private void atualizarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);
        
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getEndereco(),
                c.getTelefone(),
                c.getEmail()
            });
        }
    }

    public Cliente getClienteSelecionado() {
        return clienteAtual;
    }
} 