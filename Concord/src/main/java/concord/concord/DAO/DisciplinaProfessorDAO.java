package concord.concord.DAO;

import concord.concord.Database;
import concord.concord.models.Disciplina;
import concord.concord.models.Professor;
import concord.concord.models.DisciplinaProfessor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaProfessorDAO {
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    public void adicionar(DisciplinaProfessor disciplinaProfessor) {
        Professor professor = professorDAO.buscarPorId(disciplinaProfessor.getProfessor().getId());
        Disciplina disciplina = disciplinaDAO.buscarPorId(disciplinaProfessor.getDisciplina().getId());
        
        if (professor == null || disciplina == null) {
            throw new RuntimeException("Professor ou Disciplina não encontrados");
        }

        if (verificarRelacionamentoExistente(professor.getId(), disciplina.getId())) {
            throw new RuntimeException("Este professor já está relacionado com esta disciplina");
        }

        String sql = "INSERT INTO disciplina_professor (professor_id, disciplina_id) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professor.getId());
            stmt.setInt(2, disciplina.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar relacionamento: " + e.getMessage());
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM disciplina_professor WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DisciplinaProfessor> buscarPorProfessor(int professorId) {
        List<DisciplinaProfessor> lista = new ArrayList<>();
        String sql = "SELECT dp.id, d.* FROM disciplina_professor dp " +
                    "JOIN disciplina d ON dp.disciplina_id = d.id " +
                    "WHERE dp.professor_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professorId);
            ResultSet rs = stmt.executeQuery();

            Professor professor = professorDAO.buscarPorId(professorId);
            while (rs.next()) {
                Disciplina disciplina = new Disciplina(
                    rs.getInt("id"),
                    rs.getString("nome")
                );
                
                DisciplinaProfessor dp = new DisciplinaProfessor(
                    rs.getInt("dp.id"),
                    professor,
                    disciplina
                );
                lista.add(dp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<DisciplinaProfessor> buscarPorDisciplina(int disciplinaId) {
        List<DisciplinaProfessor> lista = new ArrayList<>();
        String sql = "SELECT dp.id as dp_id, p.* FROM disciplina_professor dp " +
                    "JOIN professor p ON dp.professor_id = p.id " +
                    "WHERE dp.disciplina_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, disciplinaId);
            ResultSet rs = stmt.executeQuery();

            Disciplina disciplina = disciplinaDAO.buscarPorId(disciplinaId);
            while (rs.next()) {
                Professor professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setNome(rs.getString("nome"));
                professor.setEmail(rs.getString("email"));
                professor.setTelefone(rs.getString("telefone"));
                professor.setCargaHoraria(rs.getInt("carga_horaria"));
                professor.setMatricula(rs.getString("matricula"));
                professor.setStatus(rs.getInt("status"));
                
                DisciplinaProfessor dp = new DisciplinaProfessor(
                    rs.getInt("dp_id"),
                    professor,
                    disciplina
                );
                lista.add(dp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean verificarRelacionamentoExistente(int professorId, int disciplinaId) {
        String sql = "SELECT COUNT(*) FROM disciplina_professor WHERE professor_id = ? AND disciplina_id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professorId);
            stmt.setInt(2, disciplinaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
} 