import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    
    // Nome completo do usuario
    private String nomeCompleto;
    
    // Email para contato
    private String email;
    
    // Cargo do usuario
    private String cargo;
    
    // Perfil de acesso (COLABORADOR ou GERENTE)
    private PerfilUsuario perfil;

    // Construtor para criar um novo usuário
    public Usuario(int id, String nomeCompleto, String email, String cargo, PerfilUsuario perfil) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.cargo = cargo;
        this.perfil = perfil;
    }

    public int getId() { 
        return id; 
    }
    
    public String getNomeCompleto() { 
        return nomeCompleto; 
    }
    
    public void setNomeCompleto(String nomeCompleto) { 
        this.nomeCompleto = nomeCompleto; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getCargo() { 
        return cargo; 
    }
    
    public void setCargo(String cargo) { 
        this.cargo = cargo; 
    }
    
    public PerfilUsuario getPerfil() { 
        return perfil; 
    }
    
    public void setPerfil(PerfilUsuario perfil) { 
        this.perfil = perfil; 
    }
}

