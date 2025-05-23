package concord.concord.models;

public class TurmaAula {
    private int id;
    private Turma turma;
    private Aula aula;

    public TurmaAula() {}

    public TurmaAula(Turma turma, Aula aula) {
        this.turma = turma;
        this.aula = aula;
    }

    public TurmaAula(int id, Turma turma, Aula aula) {
        this.id = id;
        this.turma = turma;
        this.aula = aula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
} 