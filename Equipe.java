import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Classe que representa uma equipe
 * Uma equipe é composta por vários usuários (membros)
 */
public class Equipe implements Serializable {
    private int id;
    
    // Nome da equipe
    private String nomeEquipe;
    
    // Descrição da equipe
    private String descricao;
    
    // Lista de membros da equipe
    private List<Usuario> membros;


    // Construtor para criar uma nova equipe
    public Equipe(int id, String nomeEquipe, String descricao) {
        this.id = id;
        this.nomeEquipe = nomeEquipe;
        this.descricao = descricao;
        this.membros = new ArrayList<>();
    }

    public int getId() { 
        return id; 
    }
    
    public String getNomeEquipe() { 
        return nomeEquipe; 
    }
    
    public void setNomeEquipe(String nomeEquipe) { 
        this.nomeEquipe = nomeEquipe; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
    
    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }
    
    public List<Usuario> getMembros() { 
        return membros; 
    }
}

