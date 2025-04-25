package concord.concord.models;

public class Aula {
    private int id;
    private String diciplina;
    private Professor professor;
    private Curso course;
    private String dia;
    private String horario;




    public Aula(String diciplina, Professor professor,Curso course, String dia, String horario) {
        this.diciplina = diciplina;
        this.professor = professor;
        this.horario = horario;
        this.course = course;
        this.dia = dia;
    }


    public Aula(int id, String diciplina, Professor professor,Curso course, String dia, String horario) {
        this.id = id;
        this.diciplina = diciplina;
        this.professor = professor;
        this.course = course;
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

    public String getdiciplina() {
        return diciplina;
    }

    public void setdiciplina(String diciplina) {
        this.diciplina = diciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String gethorario() {
        return horario;
    }

    public void sethorario(String horario) {
        this.horario = horario;
    }

    public Curso getCourse() {
        return course;
    }

    public void setCourse(Curso course) {
        this.course = course;
    }

    public String getdia() {
        return dia;
    }

    public void setdia(String dia) {
        this.dia = dia;
    }
}