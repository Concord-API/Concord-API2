package concord.concord.DAO;

import concord.concord.Database;
import concord.concord.models.Aula;
import concord.concord.models.Curso;
import concord.concord.models.Professor;
import concord.concord.models.Disciplina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO {
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    public boolean professorAula(Professor professor, String dia, String horarioInicio) {
        String sql = "SELECT COUNT(*) FROM aula WHERE disci_prof_id IN (SELECT id FROM disciplina_professor WHERE professor_id = ?) AND dia = ? AND horario_inicio = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professor.getId());
            stmt.setString(2, dia);
            stmt.setString(3, horarioInicio);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int adicionarAula(Aula aula) {
        String sql = "INSERT INTO aula(disci_prof_id, dia, horario_inicio, horario_termino) VALUES (?, ?, ?, ?)";
        int generatedId = 0;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int disciProfId = buscarDisciProfId(aula.getDisciplina().getId(), aula.getProfessor().getId());
            
            if (disciProfId == 0) {
                throw new RuntimeException("Relacionamento entre professor e disciplina não encontrado");
            }
            
            stmt.setInt(1, disciProfId);
            stmt.setString(2, aula.getDia());
            stmt.setString(3, aula.getHorarioInicio());
            stmt.setString(4, aula.getHorarioTermino());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar aula: " + e.getMessage());
        }
        return generatedId;
    }

    public void editarAula(int id, Aula aula) {
        String sql = "UPDATE aula SET disci_prof_id = ?, dia = ?, horario_inicio = ?, horario_termino = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int disciProfId = buscarDisciProfId(aula.getDisciplina().getId(), aula.getProfessor().getId());
            if (disciProfId == 0) {
                throw new RuntimeException("Relacionamento entre professor e disciplina não encontrado");
            }
            
            stmt.setInt(1, disciProfId);
            stmt.setString(2, aula.getDia());
            stmt.setString(3, aula.getHorarioInicio());
            stmt.setString(4, aula.getHorarioTermino());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int buscarDisciProfId(int disciplinaId, int professorId) {
        String sql = "SELECT id FROM disciplina_professor WHERE disciplina_id = ? AND professor_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, disciplinaId);
            stmt.setInt(2, professorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Encontrado disci_prof_id: " + id + " para disciplina: " + disciplinaId + " e professor: " + professorId);
                return id;
            } else {
                System.out.println("Nenhum relacionamento encontrado para disciplina: " + disciplinaId + " e professor: " + professorId);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar disci_prof_id: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean existeConflitoDeHorario(Professor professor, String dia, String horarioInicio, int idAtual) {
        String sql = "SELECT COUNT(*) FROM aula WHERE disci_prof_id IN (SELECT id FROM disciplina_professor WHERE professor_id = ?) AND dia = ? AND horario_inicio = ? AND id != ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professor.getId());
            stmt.setString(2, dia);
            stmt.setString(3, horarioInicio);
            stmt.setInt(4, idAtual);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deletarAula(int id) {
        String sql = "DELETE FROM aula WHERE id=?";
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
        String sql = "SELECT a.*, dp.professor_id, dp.disciplina_id, d.nome as disciplina_nome " +
                    "FROM aula a " +
                    "JOIN disciplina_professor dp ON a.disci_prof_id = dp.id " +
                    "JOIN disciplina d ON dp.disciplina_id = d.id";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Professor professor = buscarProfessorPorId(rs.getInt("professor_id"));
                Disciplina disciplina = new Disciplina(
                    rs.getInt("disciplina_id"),
                    rs.getString("disciplina_nome")
                );

                Aula aula = new Aula(
                    rs.getInt("id"),
                    disciplina,
                    professor,
                    rs.getString("dia"),
                    rs.getString("horario_inicio"),
                    rs.getString("horario_termino")
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
        String sql = "SELECT * FROM professor WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                professor = new Professor(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getInt("carga_horaria"),
                    rs.getString("matricula"),
                    rs.getInt("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professor;
    }

    public List<Aula> buscarAulasPorProfessorEDia(int professorId, String dia) {
        List<Aula> lista = new ArrayList<>();
        String sql = "SELECT a.*, dp.professor_id, dp.disciplina_id, d.nome as disciplina_nome " +
                    "FROM aula a " +
                    "JOIN disciplina_professor dp ON a.disci_prof_id = dp.id " +
                    "JOIN disciplina d ON dp.disciplina_id = d.id " +
                    "WHERE dp.professor_id = ? AND a.dia = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professorId);
            stmt.setString(2, dia);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Professor professor = buscarProfessorPorId(rs.getInt("professor_id"));
                Disciplina disciplina = new Disciplina(
                    rs.getInt("disciplina_id"),
                    rs.getString("disciplina_nome")
                );

                Aula aula = new Aula(
                    rs.getInt("id"),
                    disciplina,
                    professor,
                    rs.getString("dia"),
                    rs.getString("horario_inicio"),
                    rs.getString("horario_termino")
                );
                lista.add(aula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Aula buscarPorId(int id) {
        String sql = "SELECT a.*, dp.professor_id, dp.disciplina_id, d.nome as disciplina_nome " +
                    "FROM aula a " +
                    "JOIN disciplina_professor dp ON a.disci_prof_id = dp.id " +
                    "JOIN disciplina d ON dp.disciplina_id = d.id " +
                    "WHERE a.id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Professor professor = buscarProfessorPorId(rs.getInt("professor_id"));
                Disciplina disciplina = new Disciplina(
                    rs.getInt("disciplina_id"),
                    rs.getString("disciplina_nome")
                );

                return new Aula(
                    rs.getInt("id"),
                    disciplina,
                    professor,
                    rs.getString("dia"),
                    rs.getString("horario_inicio"),
                    rs.getString("horario_termino")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
