import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaGUI extends JFrame {
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Projeto> projetos = new ArrayList<>();
    private List<Equipe> equipes = new ArrayList<>();
    
    private int proximoIdUsuario = 1;
    private int proximoIdProjeto = 1;
    private int proximoIdEquipe = 1;
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel menuPrincipal;
    private JPanel painelDetalhesProjeto;
    
    // Seletor de datas visual
    private JPanel criarSeletorData(String dataInicial) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);
        
        String[] partes = dataInicial != null && dataInicial.matches("\\d{2}/\\d{2}/\\d{4}") 
            ? dataInicial.split("/") 
            : new String[]{"01", "01", "2024"};
        
        String[] dias = new String[31];
        for (int i = 0; i < 31; i++) {
            dias[i] = String.format("%02d", i + 1);
        }
        JComboBox<String> comboDia = new JComboBox<>(dias);
        comboDia.setSelectedItem(partes[0]);
        
        String[] meses = {"01-Jan", "02-Fev", "03-Mar", "04-Abr", "05-Mai", "06-Jun", 
                         "07-Jul", "08-Ago", "09-Set", "10-Out", "11-Nov", "12-Dez"};
        JComboBox<String> comboMes = new JComboBox<>(meses);
        comboMes.setSelectedIndex(Integer.parseInt(partes[1]) - 1);
        
        String[] anos = new String[20];
        int anoAtual = 2024;
        for (int i = 0; i < 20; i++) {
            anos[i] = String.valueOf(anoAtual - 5 + i);
        }
        JComboBox<String> comboAno = new JComboBox<>(anos);
        comboAno.setSelectedItem(partes[2]);
        
        panel.add(comboDia);
        panel.add(new JLabel("/"));
        panel.add(comboMes);
        panel.add(new JLabel("/"));
        panel.add(comboAno);
        
        return panel;
    }
    
    private String obterDataDoSeletor(JPanel seletorPanel) {
        Component[] components = seletorPanel.getComponents();
        JComboBox<?> comboDia = (JComboBox<?>) components[0];
        JComboBox<?> comboMes = (JComboBox<?>) components[2];
        JComboBox<?> comboAno = (JComboBox<?>) components[4];
        
        String dia = (String) comboDia.getSelectedItem();
        String mes = ((String) comboMes.getSelectedItem()).substring(0, 2);
        String ano = (String) comboAno.getSelectedItem();
        
        return dia + "/" + mes + "/" + ano;
    }

    public SistemaGUI() {
        setTitle("Sistema de Gestao de Projetos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        carregarDados();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        menuPrincipal = criarMenuPrincipal();
        mainPanel.add(menuPrincipal, "MENU");
        mainPanel.add(criarPainelUsuarios(), "USUARIOS");
        mainPanel.add(criarPainelProjetos(), "PROJETOS");
        mainPanel.add(criarPainelEquipes(), "EQUIPES");
        
        add(mainPanel);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarDados();
            }
        });
    }
    
    // Menu principal com cards de projetos
    private JPanel criarMenuPrincipal() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS));
        topBar.setBackground(new Color(240, 240, 245));
        
        JLabel creditos = new JLabel("Criado por leonardo2sc@gmail.com");
        creditos.setFont(new Font("Arial", Font.PLAIN, 12));
        creditos.setForeground(Color.GRAY);
        creditos.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        creditos.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titulo = new JLabel("Sistema de Gestao de Projetos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topBar.add(creditos);
        topBar.add(titulo);
        headerPanel.add(topBar, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(new Color(240, 240, 245));
        statsPanel.add(criarCardEstatistica("Usuarios", usuarios.size(), new Color(52, 152, 219)));
        statsPanel.add(criarCardEstatistica("Projetos", projetos.size(), new Color(46, 204, 113)));
        statsPanel.add(criarCardEstatistica("Equipes", equipes.size(), new Color(155, 89, 182)));
        headerPanel.add(statsPanel, BorderLayout.CENTER);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel projetosPanel = new JPanel(new BorderLayout());
        projetosPanel.setBackground(new Color(240, 240, 245));
        
        JLabel projetosTitulo = new JLabel("Projetos Recentes", SwingConstants.LEFT);
        projetosTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        projetosTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        projetosPanel.add(projetosTitulo, BorderLayout.NORTH);
        
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(new Color(240, 240, 245));
        
        if (projetos.isEmpty()) {
            JLabel semProjetos = new JLabel("Nenhum projeto cadastrado");
            semProjetos.setFont(new Font("Arial", Font.ITALIC, 14));
            semProjetos.setForeground(Color.GRAY);
            semProjetos.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            cardsContainer.add(semProjetos);
        } else {
            for (Projeto p : projetos) {
                cardsContainer.add(criarCardProjeto(p));
                cardsContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        projetosPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(projetosPanel, BorderLayout.CENTER);
        
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        botoesPanel.setBackground(new Color(240, 240, 245));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnUsuarios = criarBotaoSimples("Usuarios", new Color(52, 152, 219));
        btnUsuarios.addActionListener(e -> cardLayout.show(mainPanel, "USUARIOS"));
        
        JButton btnProjetos = criarBotaoSimples("Projetos", new Color(46, 204, 113));
        btnProjetos.addActionListener(e -> cardLayout.show(mainPanel, "PROJETOS"));
        
        JButton btnEquipes = criarBotaoSimples("Equipes", new Color(155, 89, 182));
        btnEquipes.addActionListener(e -> cardLayout.show(mainPanel, "EQUIPES"));
        
        JButton btnSair = criarBotaoSimples("Sair", new Color(231, 76, 60));
        btnSair.addActionListener(e -> {
            salvarDados();
            System.exit(0);
        });
        
        botoesPanel.add(btnUsuarios);
        botoesPanel.add(btnProjetos);
        botoesPanel.add(btnEquipes);
        botoesPanel.add(btnSair);
        
        panel.add(botoesPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarCardEstatistica(String titulo, int valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblValor = new JLabel(String.valueOf(valor), SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 36));
        lblValor.setForeground(cor);
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setForeground(Color.GRAY);
        
        card.add(lblValor, BorderLayout.CENTER);
        card.add(lblTitulo, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel criarCardProjeto(Projeto projeto) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                abrirDetalhesProjeto(projeto);
            }
            public void mouseEntered(MouseEvent evt) {
                card.setBackground(new Color(245, 245, 250));
            }
            public void mouseExited(MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setOpaque(false);
        
        JLabel lblNome = new JLabel(projeto.getNomeProjeto());
        lblNome.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel lblGerente = new JLabel("Gerente: " + projeto.getGerenteResponsavel().getNomeCompleto());
        lblGerente.setFont(new Font("Arial", Font.PLAIN, 12));
        lblGerente.setForeground(Color.GRAY);
        
        JLabel lblDatas = new JLabel("Inicio: " + projeto.getDataInicio() + " | Termino: " + projeto.getDataTerminoPrevista());
        lblDatas.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDatas.setForeground(Color.GRAY);
        
        infoPanel.add(lblNome);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblGerente);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblDatas);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        JLabel lblStatus = new JLabel(projeto.getStatus().toString());
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setOpaque(true);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        Color corStatus = getCorStatus(projeto.getStatus());
        lblStatus.setBackground(corStatus);
        lblStatus.setForeground(Color.WHITE);
        
        card.add(lblStatus, BorderLayout.EAST);
        
        return card;
    }
    
    private Color getCorStatus(StatusProjeto status) {
        switch (status) {
            case PLANEJADO: return new Color(52, 152, 219);
            case EM_ANDAMENTO: return new Color(241, 196, 15);
            case CONCLUIDO: return new Color(46, 204, 113);
            case CANCELADO: return new Color(231, 76, 60);
            default: return Color.GRAY;
        }
    }
    
    private JButton criarBotaoSimples(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    // Tela de detalhes do projeto
    private void abrirDetalhesProjeto(Projeto projeto) {
        if (painelDetalhesProjeto != null) {
            mainPanel.remove(painelDetalhesProjeto);
        }
        painelDetalhesProjeto = criarPainelDetalhesProjeto(projeto);
        mainPanel.add(painelDetalhesProjeto, "DETALHES_PROJETO");
        cardLayout.show(mainPanel, "DETALHES_PROJETO");
    }
    
    private JPanel criarPainelDetalhesProjeto(Projeto projeto) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JLabel titulo = new JLabel(projeto.getNomeProjeto());
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 240, 245));
        
        JLabel lblStatus = new JLabel("Status: " + projeto.getStatus());
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblGerente = new JLabel("Gerente: " + projeto.getGerenteResponsavel().getNomeCompleto());
        lblGerente.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblDatas = new JLabel("Periodo: " + projeto.getDataInicio() + " ate " + projeto.getDataTerminoPrevista());
        lblDatas.setFont(new Font("Arial", Font.PLAIN, 14));
        
        infoPanel.add(titulo);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblStatus);
        infoPanel.add(lblGerente);
        infoPanel.add(lblDatas);
        
        headerPanel.add(infoPanel, BorderLayout.WEST);
        
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new BoxLayout(botoesPanel, BoxLayout.Y_AXIS));
        botoesPanel.setBackground(new Color(240, 240, 245));
        
        JButton btnConcluir = new JButton("Concluir Projeto");
        btnConcluir.setPreferredSize(new Dimension(150, 35));
        btnConcluir.setMaximumSize(new Dimension(150, 35));
        btnConcluir.setBackground(new Color(46, 204, 113));
        btnConcluir.setForeground(Color.WHITE);
        btnConcluir.setFocusPainted(false);
        btnConcluir.setBorderPainted(false);
        btnConcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConcluir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja marcar este projeto como concluido?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                projeto.setStatus(StatusProjeto.CONCLUIDO);
                salvarDados();
                abrirDetalhesProjeto(projeto);
            }
        });
        
        if (projeto.getStatus() == StatusProjeto.CONCLUIDO) {
            btnConcluir.setEnabled(false);
            btnConcluir.setText("Projeto Concluido");
            btnConcluir.setBackground(Color.GRAY);
        }
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(150, 35));
        btnVoltar.setMaximumSize(new Dimension(150, 35));
        btnVoltar.addActionListener(e -> {
            atualizarMenuPrincipal();
            cardLayout.show(mainPanel, "MENU");
        });
        
        botoesPanel.add(btnConcluir);
        botoesPanel.add(Box.createVerticalStrut(5));
        botoesPanel.add(btnVoltar);
        
        headerPanel.add(botoesPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel tasksPanel = new JPanel(new BorderLayout());
        tasksPanel.setBackground(new Color(240, 240, 245));
        
        JPanel tituloTasksPanel = new JPanel(new BorderLayout());
        tituloTasksPanel.setBackground(new Color(240, 240, 245));
        
        JLabel tituloTasks = new JLabel("Tasks do Projeto");
        tituloTasks.setFont(new Font("Arial", Font.BOLD, 18));
        tituloTasks.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnNovaTask = new JButton("+ Nova Task");
        btnNovaTask.setBackground(new Color(46, 204, 113));
        btnNovaTask.setForeground(Color.WHITE);
        btnNovaTask.setFocusPainted(false);
        btnNovaTask.setBorderPainted(false);
        btnNovaTask.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNovaTask.addActionListener(e -> {
            cadastrarTaskDireta(projeto);
            abrirDetalhesProjeto(projeto);
        });
        
        tituloTasksPanel.add(tituloTasks, BorderLayout.WEST);
        tituloTasksPanel.add(btnNovaTask, BorderLayout.EAST);
        tasksPanel.add(tituloTasksPanel, BorderLayout.NORTH);
        
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(new Color(240, 240, 245));
        
        if (projeto.getTasks().isEmpty()) {
            JLabel semTasks = new JLabel("Nenhuma task cadastrada neste projeto");
            semTasks.setFont(new Font("Arial", Font.ITALIC, 14));
            semTasks.setForeground(Color.GRAY);
            semTasks.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            cardsContainer.add(semTasks);
        } else {
            for (Task t : projeto.getTasks()) {
                cardsContainer.add(criarCardTask(t, projeto));
                cardsContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        tasksPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(tasksPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarCardTask(Task task, Projeto projeto) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(task.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel lblDescricao = new JLabel(task.getDescricao());
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescricao.setForeground(Color.GRAY);
        
        String equipeNome = task.getEquipe() != null ? task.getEquipe().getNomeEquipe() : "Sem equipe";
        JLabel lblEquipe = new JLabel("Equipe: " + equipeNome + " | Prazo: " + task.getDataLimite());
        lblEquipe.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEquipe.setForeground(Color.GRAY);
        
        infoPanel.add(lblTitulo);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblDescricao);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblEquipe);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        JPanel acoesPanel = new JPanel();
        acoesPanel.setLayout(new BoxLayout(acoesPanel, BoxLayout.Y_AXIS));
        acoesPanel.setBackground(Color.WHITE);
        
        JButton btnStatus = new JButton(task.isConcluida() ? "Concluida" : "Pendente");
        btnStatus.setFont(new Font("Arial", Font.BOLD, 11));
        btnStatus.setPreferredSize(new Dimension(100, 25));
        btnStatus.setMaximumSize(new Dimension(100, 25));
        btnStatus.setBackground(task.isConcluida() ? new Color(46, 204, 113) : new Color(241, 196, 15));
        btnStatus.setForeground(Color.WHITE);
        btnStatus.setFocusPainted(false);
        btnStatus.setBorderPainted(false);
        btnStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStatus.addActionListener(e -> {
            task.setConcluida(!task.isConcluida());
            salvarDados();
            abrirDetalhesProjeto(projeto);
        });
        
        JButton btnRemover = new JButton("Remover");
        btnRemover.setFont(new Font("Arial", Font.PLAIN, 11));
        btnRemover.setPreferredSize(new Dimension(100, 25));
        btnRemover.setMaximumSize(new Dimension(100, 25));
        btnRemover.setBackground(new Color(231, 76, 60));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFocusPainted(false);
        btnRemover.setBorderPainted(false);
        btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemover.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente remover esta task?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                projeto.getTasks().removeIf(t -> t.getId() == task.getId());
                salvarDados();
                abrirDetalhesProjeto(projeto);
            }
        });
        
        acoesPanel.add(btnStatus);
        acoesPanel.add(Box.createVerticalStrut(5));
        acoesPanel.add(btnRemover);
        
        card.add(acoesPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void cadastrarTaskDireta(Projeto projeto) {
        JTextField tituloField = new JTextField();
        JTextField descField = new JTextField();
        JPanel dataLimiteSeletor = criarSeletorData("31/12/2024");
        
        String[] equipesNomes = equipes.stream()
            .map(e -> "ID: " + e.getId() + " - " + e.getNomeEquipe())
            .toArray(String[]::new);
        
        JComboBox<String> equipeCombo = null;
        if (equipesNomes.length > 0) {
            equipeCombo = new JComboBox<>(equipesNomes);
        }
        
        Object[] message = {
            "Titulo:", tituloField,
            "Descricao:", descField,
            "Data Limite:", dataLimiteSeletor
        };
        
        if (equipeCombo != null) {
            message = new Object[]{
                "Titulo:", tituloField,
                "Descricao:", descField,
                "Data Limite:", dataLimiteSeletor,
                "Equipe:", equipeCombo
            };
        }
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Cadastrar Task", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            Equipe equipe = null;
            if (equipeCombo != null && equipeCombo.getSelectedItem() != null) {
                String equipeEscolha = (String) equipeCombo.getSelectedItem();
                int idEquipe = Integer.parseInt(equipeEscolha.split(" - ")[0].replace("ID: ", ""));
                equipe = buscarEquipe(idEquipe);
            }
            
            String dataLimite = obterDataDoSeletor(dataLimiteSeletor);
            
            Task task = new Task(projeto.getProximoIdTask(), 
                tituloField.getText(), descField.getText(),
                dataLimite, equipe);
            
            projeto.getTasks().add(task);
            projeto.incrementarIdTask();
            salvarDados();
            JOptionPane.showMessageDialog(this, "Task cadastrada com sucesso!");
        }
    }
    
    private void atualizarMenuPrincipal() {
        mainPanel.remove(menuPrincipal);
        menuPrincipal = criarMenuPrincipal();
        mainPanel.add(menuPrincipal, "MENU");
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    // Painéis de gerenciamento
    private JPanel criarPainelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gerenciar Usuarios");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> lista = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(lista);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            cadastrarUsuario();
            atualizarListaUsuarios(listModel);
        });
        
        JButton btnRemover = new JButton("Remover");
        btnRemover.addActionListener(e -> {
            removerUsuario();
            atualizarListaUsuarios(listModel);
        });
        
        JButton btnListar = new JButton("Atualizar");
        btnListar.addActionListener(e -> atualizarListaUsuarios(listModel));
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            atualizarMenuPrincipal();
            cardLayout.show(mainPanel, "MENU");
        });
        
        btnPanel.add(btnCadastrar);
        btnPanel.add(btnRemover);
        btnPanel.add(btnListar);
        btnPanel.add(btnVoltar);
        
        panel.add(btnPanel, BorderLayout.EAST);
        
        atualizarListaUsuarios(listModel);
        
        return panel;
    }
    
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
        
        JPanel btnPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            cadastrarProjeto();
            atualizarListaProjetos(listModel);
        });
        
        JButton btnRemover = new JButton("Remover");
        btnRemover.addActionListener(e -> {
            removerProjeto();
            atualizarListaProjetos(listModel);
        });
        
        JButton btnListar = new JButton("Atualizar");
        btnListar.addActionListener(e -> atualizarListaProjetos(listModel));
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            atualizarMenuPrincipal();
            cardLayout.show(mainPanel, "MENU");
        });
        
        btnPanel.add(btnCadastrar);
        btnPanel.add(btnRemover);
        btnPanel.add(btnListar);
        btnPanel.add(btnVoltar);
        
        panel.add(btnPanel, BorderLayout.EAST);
        
        atualizarListaProjetos(listModel);
        
        return panel;
    }
    
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
        
        JPanel btnPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            cadastrarEquipe();
            atualizarListaEquipes(listModel);
        });
        
        JButton btnAdicionar = new JButton("Adicionar Membro");
        btnAdicionar.addActionListener(e -> {
            adicionarMembroEquipe();
            atualizarListaEquipes(listModel);
        });
        
        JButton btnRemover = new JButton("Remover Equipe");
        btnRemover.addActionListener(e -> {
            removerEquipe();
            atualizarListaEquipes(listModel);
        });
        
        JButton btnListar = new JButton("Atualizar");
        btnListar.addActionListener(e -> atualizarListaEquipes(listModel));
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            atualizarMenuPrincipal();
            cardLayout.show(mainPanel, "MENU");
        });
        
        btnPanel.add(btnCadastrar);
        btnPanel.add(btnAdicionar);
        btnPanel.add(btnRemover);
        btnPanel.add(btnListar);
        btnPanel.add(btnVoltar);
        
        panel.add(btnPanel, BorderLayout.EAST);
        
        atualizarListaEquipes(listModel);
        
        return panel;
    }
    
    // Métodos de cadastro e operações
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
            Usuario usuario = new Usuario(proximoIdUsuario++, nomeField.getText(), 
                emailField.getText(), cargoField.getText(), 
                PerfilUsuario.valueOf((String) perfilCombo.getSelectedItem()));
            usuarios.add(usuario);
            salvarDados();
            JOptionPane.showMessageDialog(this, "Usuario cadastrado com sucesso!");
        }
    }
    
    private void removerUsuario() {
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum usuario cadastrado.");
            return;
        }
        
        String[] opcoes = usuarios.stream()
            .map(u -> "ID: " + u.getId() + " - " + u.getNomeCompleto())
            .toArray(String[]::new);
        
        String escolha = (String) JOptionPane.showInputDialog(this, 
            "Selecione o usuario:", "Remover Usuario",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha != null) {
            int id = Integer.parseInt(escolha.split(" - ")[0].replace("ID: ", ""));
            usuarios.removeIf(u -> u.getId() == id);
            salvarDados();
            JOptionPane.showMessageDialog(this, "Usuario removido!");
        }
    }
    
    private void cadastrarProjeto() {
        if (usuarios.stream().noneMatch(u -> u.getPerfil() == PerfilUsuario.GERENTE)) {
            JOptionPane.showMessageDialog(this, "Cadastre um gerente primeiro!");
            return;
        }
        
        JTextField nomeField = new JTextField();
        JTextField descField = new JTextField();
        JPanel dataInicioSeletor = criarSeletorData("01/01/2024");
        JPanel dataFimSeletor = criarSeletorData("31/12/2024");
        
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
            "Data Inicio:", dataInicioSeletor,
            "Data Termino:", dataFimSeletor,
            "Status:", statusCombo,
            "Gerente:", gerenteCombo
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Cadastrar Projeto", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String gerenteEscolha = (String) gerenteCombo.getSelectedItem();
            int idGerente = Integer.parseInt(gerenteEscolha.split(" - ")[0].replace("ID: ", ""));
            Usuario gerente = buscarUsuario(idGerente);
            
            String dataInicio = obterDataDoSeletor(dataInicioSeletor);
            String dataFim = obterDataDoSeletor(dataFimSeletor);
            
            Projeto projeto = new Projeto(proximoIdProjeto++, 
                nomeField.getText(), descField.getText(),
                dataInicio, dataFim,
                StatusProjeto.valueOf((String) statusCombo.getSelectedItem()), gerente);
            
            projetos.add(projeto);
            salvarDados();
            JOptionPane.showMessageDialog(this, "Projeto cadastrado com sucesso!");
        }
    }
    
    private void removerProjeto() {
        if (projetos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum projeto cadastrado.");
            return;
        }
        
        String[] opcoes = projetos.stream()
            .map(p -> "ID: " + p.getId() + " - " + p.getNomeProjeto())
            .toArray(String[]::new);
        
        String escolha = (String) JOptionPane.showInputDialog(this, 
            "Selecione o projeto:", "Remover Projeto",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha != null) {
            int id = Integer.parseInt(escolha.split(" - ")[0].replace("ID: ", ""));
            projetos.removeIf(p -> p.getId() == id);
            salvarDados();
            JOptionPane.showMessageDialog(this, "Projeto removido!");
        }
    }
    
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
    
    private void adicionarMembroEquipe() {
        if (equipes.isEmpty() || usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cadastre equipes e usuarios primeiro!");
            return;
        }
        
        String[] equipesOpcoes = equipes.stream()
            .map(e -> "ID: " + e.getId() + " - " + e.getNomeEquipe())
            .toArray(String[]::new);
        
        String escolhaEquipe = (String) JOptionPane.showInputDialog(this, 
            "Selecione a equipe:", "Adicionar Membro",
            JOptionPane.QUESTION_MESSAGE, null, equipesOpcoes, equipesOpcoes[0]);
        
        if (escolhaEquipe != null) {
            int idEquipe = Integer.parseInt(escolhaEquipe.split(" - ")[0].replace("ID: ", ""));
            Equipe equipe = buscarEquipe(idEquipe);
            
            String[] usuariosOpcoes = usuarios.stream()
                .map(u -> "ID: " + u.getId() + " - " + u.getNomeCompleto())
                .toArray(String[]::new);
            
            String escolhaUsuario = (String) JOptionPane.showInputDialog(this, 
                "Selecione o usuario:", "Adicionar Membro",
                JOptionPane.QUESTION_MESSAGE, null, usuariosOpcoes, usuariosOpcoes[0]);
            
            if (escolhaUsuario != null) {
                int idUsuario = Integer.parseInt(escolhaUsuario.split(" - ")[0].replace("ID: ", ""));
                Usuario usuario = buscarUsuario(idUsuario);
                
                equipe.getMembros().add(usuario);
                salvarDados();
                JOptionPane.showMessageDialog(this, "Membro adicionado!");
            }
        }
    }
    
    private void removerEquipe() {
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma equipe cadastrada.");
            return;
        }
        
        String[] opcoes = equipes.stream()
            .map(e -> "ID: " + e.getId() + " - " + e.getNomeEquipe())
            .toArray(String[]::new);
        
        String escolha = (String) JOptionPane.showInputDialog(this, 
            "Selecione a equipe:", "Remover Equipe",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha != null) {
            int id = Integer.parseInt(escolha.split(" - ")[0].replace("ID: ", ""));
            equipes.removeIf(e -> e.getId() == id);
            salvarDados();
            JOptionPane.showMessageDialog(this, "Equipe removida!");
        }
    }
    
    // Métodos de atualização de listas
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
    
    // Métodos auxiliares
    private Usuario buscarUsuario(int id) {
        return usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
    
    private Equipe buscarEquipe(int id) {
        return equipes.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }
    
    // Persistência
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
    
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File("dados.dat");
        if (!arquivo.exists()) return;
        
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
