import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaGUI extends JFrame {
    // Listas para armazenar dados em memória
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Projeto> projetos = new ArrayList<>();
    private List<Equipe> equipes = new ArrayList<>();
    
    // Contadores para IDs - Isso permite que o usuario nao precise escolher um ID
    private int proximoIdUsuario = 1;
    private int proximoIdProjeto = 1;
    private int proximoIdEquipe = 1;
    
    // Componentes da interface
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public SistemaGUI() {
        setTitle("Sistema de Gestao de Projetos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Centraliza a janela na tela
        setLocationRelativeTo(null);
        
        // Carrega dados salvos
        carregarDados();
        
        // Inicializa o cardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Adiciona os botoes do menu principal
        mainPanel.add(criarMenuPrincipal(), "MENU");
        mainPanel.add(criarPainelUsuarios(), "USUARIOS");
        mainPanel.add(criarPainelProjetos(), "PROJETOS");
        mainPanel.add(criarPainelEquipes(), "EQUIPES");
        
        add(mainPanel);
        
        // Salva dados ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarDados();
            }
        });
    }
    
    // Cria o menu principal
    private JPanel criarMenuPrincipal() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titulo = new JLabel("Sistema de Gestao de Projetos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titulo, gbc);
        
        // Botao de gerenciar usuarios
        gbc.gridy++; // Incrementa a posicao y
        JButton btnUsuarios = new JButton("Gerenciar Usuarios");
        btnUsuarios.setPreferredSize(new Dimension(250, 40));
        btnUsuarios.addActionListener(e -> cardLayout.show(mainPanel, "USUARIOS"));
        panel.add(btnUsuarios, gbc);
        
        // Botao de gerenciar projetos
        gbc.gridy++; // Incrementa a posicao y
        JButton btnProjetos = new JButton("Gerenciar Projetos");
        btnProjetos.setPreferredSize(new Dimension(250, 40));
        btnProjetos.addActionListener(e -> cardLayout.show(mainPanel, "PROJETOS"));
        panel.add(btnProjetos, gbc);
        
        // Botao de gerenciar equipes
        gbc.gridy++; // Incrementa a posicao y
        JButton btnEquipes = new JButton("Gerenciar Equipes");
        btnEquipes.setPreferredSize(new Dimension(250, 40));
        btnEquipes.addActionListener(e -> cardLayout.show(mainPanel, "EQUIPES"));
        panel.add(btnEquipes, gbc);
        
        // Botao de sair
        gbc.gridy++; // Incrementa a posicao y
        JButton btnSair = new JButton("Sair");
        btnSair.setPreferredSize(new Dimension(250, 40));
        btnSair.addActionListener(e -> System.exit(0));
        panel.add(btnSair, gbc);
        
        return panel;
    }
    
    // Painel de gerenciamento de usuários
    private JPanel criarPainelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gerenciar Usuarios");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);
        
        // Lista de Usuarios
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> lista = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(lista);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            cadastrarUsuario();
            atualizarListaUsuarios(listModel);
        });
        
        JButton btnListar = new JButton("Atualizar");
        btnListar.addActionListener(e -> atualizarListaUsuarios(listModel));
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        
        btnPanel.add(btnCadastrar);
        btnPanel.add(btnListar);
        btnPanel.add(btnVoltar);
        
        panel.add(btnPanel, BorderLayout.EAST);
        
        atualizarListaUsuarios(listModel);
        
        return panel;
    }
    
    // Painel de gerenciamento de projetos
    private JPanel criarPainelProjetos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gerenciar Projetos");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> lista = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(lista);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            cadastrarProjeto();
            atualizarListaProjetos(listModel);
        });
        
        JButton btnListar = new JButton("Atualizar");
        btnListar.addActionListener(e -> atualizarListaProjetos(listModel));
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        
        btnPanel.add(btnCadastrar);
        btnPanel.add(btnListar);
        btnPanel.add(btnVoltar);
        
        panel.add(btnPanel, BorderLayout.EAST);
        
        atualizarListaProjetos(listModel);
        
        return panel;
    }
    
    // Painel de gerenciamento de equipes
    private JPanel criarPainelEquipes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gerenciar Equipes");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> lista = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(lista);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            cadastrarEquipe();
            atualizarListaEquipes(listModel);
        });
        
        JButton btnListar = new JButton("Atualizar");
        btnListar.addActionListener(e -> atualizarListaEquipes(listModel));
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        
        btnPanel.add(btnCadastrar);
        btnPanel.add(btnListar);
        btnPanel.add(btnVoltar);
        
        panel.add(btnPanel, BorderLayout.EAST);
        
        atualizarListaEquipes(listModel);
        
        return panel;
    }
    
    // Cadastra um novo usuário
    private void cadastrarUsuario() {
        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField cargoField = new JTextField();
        
        String[] perfis = {"GERENTE", "COLABORADOR"};
        JComboBox<String> perfilCombo = new JComboBox<>(perfis);
        
        Object[] message = {
            "Nome:", nomeField,
            "Email:", emailField,
            "Cargo:", cargoField,
            "Perfil:", perfilCombo
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Cadastrar Usuario", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText();
            String email = emailField.getText();
            String cargo = cargoField.getText();
            PerfilUsuario perfil = PerfilUsuario.valueOf((String) perfilCombo.getSelectedItem());
            
            Usuario usuario = new Usuario(proximoIdUsuario++, nome, email, cargo, perfil);
            usuarios.add(usuario);
            salvarDados();
            
            JOptionPane.showMessageDialog(this, "Usuario cadastrado com sucesso!");
        }
    }
    
    // Cadastra um novo projeto
    private void cadastrarProjeto() {
        if (usuarios.stream().noneMatch(u -> u.getPerfil() == PerfilUsuario.GERENTE)) {
            JOptionPane.showMessageDialog(this, "Cadastre um gerente primeiro!");
            return;
        }
        
        JTextField nomeField = new JTextField();
        JTextField descField = new JTextField();
        JTextField dataInicioField = new JTextField();
        JTextField dataFimField = new JTextField();
        
        String[] statusOpcoes = {"PLANEJADO", "EM_ANDAMENTO", "CONCLUIDO", "CANCELADO"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOpcoes);
        
        String[] gerentes = usuarios.stream()
            .filter(u -> u.getPerfil() == PerfilUsuario.GERENTE)
            .map(u -> "ID: " + u.getId() + " - " + u.getNomeCompleto())
            .toArray(String[]::new);
        JComboBox<String> gerenteCombo = new JComboBox<>(gerentes);
        
        Object[] message = {
            "Nome do Projeto:", nomeField,
            "Descricao:", descField,
            "Data Inicio (dd/mm/aaaa):", dataInicioField,
            "Data Termino (dd/mm/aaaa):", dataFimField,
            "Status:", statusCombo,
            "Gerente:", gerenteCombo
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Cadastrar Projeto", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String gerenteEscolha = (String) gerenteCombo.getSelectedItem();
            int idGerente = Integer.parseInt(gerenteEscolha.split(" - ")[0].replace("ID: ", ""));
            Usuario gerente = buscarUsuario(idGerente);
            
            StatusProjeto status = StatusProjeto.valueOf((String) statusCombo.getSelectedItem());
            
            Projeto projeto = new Projeto(proximoIdProjeto++, 
                nomeField.getText(), descField.getText(),
                dataInicioField.getText(), dataFimField.getText(),
                status, gerente);
            
            projetos.add(projeto);
            salvarDados();
            JOptionPane.showMessageDialog(this, "Projeto cadastrado com sucesso!");
        }
    }
    
    // Cadastra uma nova equipe
    private void cadastrarEquipe() {
        JTextField nomeField = new JTextField();
        JTextField descField = new JTextField();
        
        Object[] message = {
            "Nome da Equipe:", nomeField,
            "Descricao:", descField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Cadastrar Equipe", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            Equipe equipe = new Equipe(proximoIdEquipe++, nomeField.getText(), descField.getText());
            equipes.add(equipe);
            salvarDados();
            JOptionPane.showMessageDialog(this, "Equipe cadastrada com sucesso!");
        }
    }
    
    // Atualiza lista de usuários
    private void atualizarListaUsuarios(DefaultListModel<String> listModel) {
        listModel.clear();
        if (usuarios.isEmpty()) {
            listModel.addElement("Nenhum usuario cadastrado.");
        } else {
            for (Usuario u : usuarios) {
                listModel.addElement(String.format("ID: %d | %s | %s | %s", 
                    u.getId(), u.getNomeCompleto(), u.getPerfil(), u.getCargo()));
            }
        }
    }
    
    // Atualiza lista de projetos
    private void atualizarListaProjetos(DefaultListModel<String> listModel) {
        listModel.clear();
        if (projetos.isEmpty()) {
            listModel.addElement("Nenhum projeto cadastrado.");
        } else {
            for (Projeto p : projetos) {
                listModel.addElement(String.format("ID: %d | %s | Status: %s | Gerente: %s", 
                    p.getId(), p.getNomeProjeto(), p.getStatus(), 
                    p.getGerenteResponsavel().getNomeCompleto()));
            }
        }
    }
    
    // Atualiza lista de equipes
    private void atualizarListaEquipes(DefaultListModel<String> listModel) {
        listModel.clear();
        if (equipes.isEmpty()) {
            listModel.addElement("Nenhuma equipe cadastrada.");
        } else {
            for (Equipe e : equipes) {
                listModel.addElement(String.format("ID: %d | %s | Membros: %d", 
                    e.getId(), e.getNomeEquipe(), e.getMembros().size()));
            }
        }
    }
    
    // Busca usuário por ID
    private Usuario buscarUsuario(int id) {
        return usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
    
    // Salva os dados em arquivo
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("dados.dat"))) {
            oos.writeObject(usuarios);
            oos.writeObject(projetos);
            oos.writeObject(equipes);
            oos.writeInt(proximoIdUsuario);
            oos.writeInt(proximoIdProjeto);
            oos.writeInt(proximoIdEquipe);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
    
    // Carrega os dados do arquivo
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File("dados.dat");
        if (!arquivo.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            usuarios = (List<Usuario>) ois.readObject();
            projetos = (List<Projeto>) ois.readObject();
            equipes = (List<Equipe>) ois.readObject();
            proximoIdUsuario = ois.readInt();
            proximoIdProjeto = ois.readInt();
            proximoIdEquipe = ois.readInt();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaGUI gui = new SistemaGUI();
            gui.setVisible(true);
        });
    }
}

