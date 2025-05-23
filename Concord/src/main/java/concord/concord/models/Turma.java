package concord.concord.models;

public class Turma {
    private int id;
    private String modalidade;
    private String turno;    private String periodo;
    private Curso curso;

    public Turma() {}

    public Turma(String modalidade, String turno, String periodo, Curso curso) {
        this.modalidade = modalidade;
        this.turno = turno;
        this.periodo = periodo;
        this.curso = curso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
} 