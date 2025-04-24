package concord.concord.models;

public class Aula {
    private int id;
    private String className;
    private Professor professor;
    private Curso course;
    private String day;
    private String time;




    public Aula(String className, Professor professor,Curso course, String day, String time) {
        this.className = className;
        this.professor = professor;
        this.time = time;
        this.course = course;
        this.day = day;
    }


    public Aula(int id, String className, Professor professor,Curso course, String day, String time) {
        this.id = id;
        this.className = className;
        this.professor = professor;
        this.course = course;
        this.day = day;
        this.time = time;


    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Curso getCourse() {
        return course;
    }

    public void setCourse(Curso course) {
        this.course = course;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}