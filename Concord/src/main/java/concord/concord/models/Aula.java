package concord.concord.models;

public class Aula {
    private int id;
    private Disciplina disciplina;
    private Professor professor;
    private String dia;
    private String horarioInicio;
    private String horarioTermino;

    public Aula(Disciplina disciplina, Professor professor, String dia, String horarioInicio, String horarioTermino) {
        this.disciplina = disciplina;
        this.professor = professor;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.dia = dia;
    }

    public Aula(int id, Disciplina disciplina, Professor professor, String dia, String horarioInicio, String horarioTermino) {
        this.id = id;
        this.disciplina = disciplina;
        this.professor = professor;
        this.dia = dia;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}