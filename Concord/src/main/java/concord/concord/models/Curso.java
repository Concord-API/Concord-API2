package concord.concord.models;

public class Curso {
    private int id;
    private String nome;
    private String sigla;
    private int duracao;
    private String descricao;
    private int coordenadorId;
    private String coordenadorNome;

    public Curso() {}

    public Curso(int id, String nome, String sigla, int duracao, String descricao, int coordenadorId) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.duracao = duracao;
        this.descricao = descricao;
        this.coordenadorId = coordenadorId;
    }

    public Curso(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }


    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getCoordenadorId() { return coordenadorId; }
    public void setCoordenadorId(int coordenadorId) { this.coordenadorId = coordenadorId; }

    public String getCoordenadorNome() { return coordenadorNome; }
    public void setCoordenadorNome(String coordenadorNome) { this.coordenadorNome = coordenadorNome; }

    @Override
    public String toString() {
        return nome;
    }
}