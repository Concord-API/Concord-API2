package concord.concord.DAO;

import concord.concord.Database;
import concord.concord.models.Aula;
import concord.concord.models.Curso;
import concord.concord.models.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO {

    public boolean professorAula(Professor professor, String dia, String hora) {
        String sql = "SELECT COUNT(*) FROM aulas WHERE professor_id = ? AND dia = ? AND horario = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professor.getId());
            stmt.setString(2, dia);
            stmt.setString(3, hora);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void adicionarAula(Aula aula) {
        String sql = "INSERT INTO aulas(diciplina, professor_id, curso_id, dia, horario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aula.getdiciplina());
            stmt.setInt(2, aula.getProfessor().getId());
            stmt.setInt(3, aula.getCourse().getId());
            stmt.setString(4, aula.getdia());
            stmt.setString(5, aula.gethorario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void editarAula(int id, Aula aula) {
        String sql = "UPDATE aulas SET diciplina = ?, professor_id = ?, curso_id = ?, dia = ?, horario = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aula.getdiciplina());
            stmt.setInt(2, aula.getProfessor().getId());
            stmt.setInt(3, aula.getCourse().getId());
            stmt.setString(4, aula.getdia());
            stmt.setString(5, aula.gethorario());
            stmt.setInt(6, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean existeConflitoDeHorario(Professor professor, String dia, String hora, int idAtual) {
        String sql = "SELECT COUNT(*) FROM aulas WHERE professor_id = ? AND dia = ? AND horario = ? AND id != ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professor.getId());  // Corrigido para usar o ID do professor
            stmt.setString(2, dia);
            stmt.setString(3, hora);
            stmt.setInt(4, idAtual);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void deletarAula(int id) {
        String sql = "DELETE FROM aulas WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Aula> buscarTodasAulas() {
        List<Aula> lista = new ArrayList<>();
        String sql = "SELECT * FROM aulas";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Buscar o professor com base no professorID (supondo que exista uma coluna 'professorID')
                Professor professor = buscarProfessorPorId(rs.getInt("id"));
                Curso curso = buscarCursoPorId(rs.getInt("id"));

                Aula aula = new Aula(
                        rs.getInt("id"),
                        rs.getString("diciplina"),
                        professor,  // Agora você está passando o objeto Professor
                        curso,
                        rs.getString("dia"),
                        rs.getString("horario")
                );
                lista.add(aula);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Professor buscarProfessorPorId(int professorId) {
        Professor professor = null;
        String sql = "SELECT * FROM professores WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                professor = new Professor(
                        rs.getInt("id"),
                        rs.getString("nome")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professor;
    }

    private Curso buscarCursoPorId(int cursoID) {
        Curso curso = null;
        String sql = "SELECT * FROM curso WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cursoID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
               curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nome")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return curso;
    }
}
