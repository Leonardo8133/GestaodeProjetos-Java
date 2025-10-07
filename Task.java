import java.io.Serializable;

/**
 * Classe que representa uma tarefa dentro de um projeto
 */
public class Task implements Serializable {
    private int id;
    private String titulo;
    private String descricao;
    private String dataLimite;
    private Equipe equipe;
    private boolean concluida;

    public Task(int id, String titulo, String descricao, String dataLimite, Equipe equipe) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataLimite = dataLimite;
        this.equipe = equipe;
        this.concluida = false;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getDataLimite() { return dataLimite; }
    public void setDataLimite(String dataLimite) { this.dataLimite = dataLimite; }
    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
}

