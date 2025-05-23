package concord.concord.models;

public class DisciplinaProfessor {
    private int id;
    private Professor professor;
    private Disciplina disciplina;

    public DisciplinaProfessor() {}

    public DisciplinaProfessor(Professor professor, Disciplina disciplina) {
        this.professor = professor;
        this.disciplina = disciplina;
    }

    public DisciplinaProfessor(int id, Professor professor, Disciplina disciplina) {
        this.id = id;
        this.professor = professor;
        this.disciplina = disciplina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
} 