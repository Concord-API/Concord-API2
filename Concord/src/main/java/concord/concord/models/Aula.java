package concord.concord.models;

public class Aula {
    private int id;
    private String diciplina;
    private Professor professor;
    private Curso curso;
    private String dia;
    private String horario;




    public Aula(String diciplina, Professor professor,Curso curso, String dia, String horario) {
        this.diciplina = diciplina;
        this.professor = professor;
        this.horario = horario;
        this.curso = curso;
        this.dia = dia;
    }


    public Aula(int id, String diciplina, Professor professor,Curso curso, String dia, String horario) {
        this.id = id;
        this.diciplina = diciplina;
        this.professor = professor;
        this.curso = curso;
        this.dia = dia;
        this.horario = horario;


    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiciplina() {
        return diciplina;
    }

    public void setDiciplina(String diciplina) {
        this.diciplina = diciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}