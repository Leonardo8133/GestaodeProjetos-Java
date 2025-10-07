import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um projeto no sistema
 * Um projeto possui informações básicas, status e um gerente responsável
 */
public class Projeto {
    private int id;
    
    // Nome do projeto
    private String nomeProjeto;
    
    // Descrição detalhada do projeto
    private String descricao;
    
    // Data de inicio.
    private String dataInicio;
    
    // Data termino prevista.
    private String dataTerminoPrevista;
    
    // Status atual do projeto
    private StatusProjeto status;
    
    // Gerente responsavel
    private Usuario gerenteResponsavel;
    
    // Lista de equipes alocadas no projeto
    private List<Equipe> equipes;

    public Projeto(int id, String nomeProjeto, String descricao, String dataInicio,
                   String dataTerminoPrevista, StatusProjeto status, Usuario gerenteResponsavel) {
        this.id = id;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = status;
        this.gerenteResponsavel = gerenteResponsavel;
        this.equipes = new ArrayList<>();
    }

    public int getId() { 
        return id; 
    }
    
    public String getNomeProjeto() { 
        return nomeProjeto; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
    
    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }
    
    public String getDataInicio() { 
        return dataInicio; 
    }
    
    public String getDataTerminoPrevista() { 
        return dataTerminoPrevista; 
    }
    
    public void setDataTerminoPrevista(String dataTerminoPrevista) { 
        this.dataTerminoPrevista = dataTerminoPrevista; 
    }
    
    public StatusProjeto getStatus() { 
        return status; 
    }
    
    public void setStatus(StatusProjeto status) { 
        this.status = status; 
    }
    
    public Usuario getGerenteResponsavel() { 
        return gerenteResponsavel; 
    }
    
    public void setGerenteResponsavel(Usuario gerenteResponsavel) { 
        this.gerenteResponsavel = gerenteResponsavel; 
    }
    
    public List<Equipe> getEquipes() { 
        return equipes; 
    }
}

