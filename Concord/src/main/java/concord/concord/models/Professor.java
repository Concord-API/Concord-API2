package concord.concord.models;

public class Professor {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String matricula;
    private int status;


    public Professor() {}

    public Professor(int id, String nome, String email, String telefone, String matricula, int status) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.matricula = matricula;
        this.status = status;
    }

    public Professor(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    @Override
    public String toString() {
        return nome;
    }
}

