package concord.concord;

import concord.concord.models.Aula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String url = "jdbc:sqlite:concord.db";


    public void conectar() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS aulas ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " className TEXT NOT NULL,"
                + " professor TEXT NOT NULL,"
                + " time TEXT NOT NULL,"
                + " course TEXT NOT NULL,"
                + " day TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void adicionarAula(String className, String professor,String course,  String day, String time) {
        String sql = "INSERT INTO aulas(className, professor,course, day,time) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, className);
            pstmt.setString(2, professor);
            pstmt.setString(3, course);
            pstmt.setString(4, day);
            pstmt.setString(5, time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void editarAula(int id, String className, String professor, String course, String day, String time) {
        String sql = "UPDATE aulas SET className = ?, professor = ?, course = ?, day = ?, time = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, className);
            pstmt.setString(2, professor);
            pstmt.setString(3, course);
            pstmt.setString(4, day);
            pstmt.setString(5, time);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void deletarAula(int id) {
        String sql = "DELETE FROM aulas WHERE id = ?";

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public List<Aula> buscarTodasAulas() {
        List<Aula> aulas = new ArrayList<>();
        String sql = "SELECT id, className, professor, course, day, time FROM aulas";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aula aula = new Aula(
                        rs.getInt("id"),
                        rs.getString("className"),
                        rs.getString("professor"),
                        rs.getString("course"),
                        rs.getString("day"),
                        rs.getString("time")


                );
                aulas.add(aula);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }



        return aulas;
    }

    public boolean professorAula(String professor, String day, String time) {
        String sql = "SELECT 1 FROM aulas WHERE professor = ? AND day = ? AND time = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, professor);
            stmt.setString(2, day);
            stmt.setString(3, time);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean existeConflitoDeHorario(String professor, String dia, String horario, int idAtual) {
        String sql = "SELECT COUNT(*) FROM aulas WHERE professor = ? AND day = ? AND time = ? AND id <> ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, professor);
            stmt.setString(2, dia);
            stmt.setString(3, horario);
            stmt.setInt(4, idAtual); // Ignorar a própria aula no check
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
