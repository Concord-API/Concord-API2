package concord.concord.DAO;

import concord.concord.Database;
import concord.concord.models.Turma;
import concord.concord.models.Aula;
import concord.concord.models.TurmaAula;
import concord.concord.models.Curso;
import concord.concord.models.Disciplina;
import concord.concord.models.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaAulaDAO {
    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final AulaDAO aulaDAO = new AulaDAO();

    public void adicionar(TurmaAula turmaAula) {
        // Verificar se a turma e a aula existem
        Turma turma = turmaDAO.buscarPorId(turmaAula.getTurma().getId());
        Aula aula = aulaDAO.buscarPorId(turmaAula.getAula().getId());
        
        if (turma == null || aula == null) {
            throw new RuntimeException("Turma ou Aula não encontrados");
        }

        // Verificar se o relacionamento já existe
        if (verificarRelacionamentoExistente(turma.getId(), aula.getId())) {
            throw new RuntimeException("Esta turma já está relacionada com esta aula");
        }

        String sql = "INSERT INTO turma_aula (turma_id, aula_id) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, turma.getId());
            stmt.setInt(2, aula.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar relacionamento: " + e.getMessage());
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM turma_aula WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TurmaAula> buscarPorTurma(int turmaId) {
        List<TurmaAula> lista = new ArrayList<>();
        String sql = "SELECT ta.id, a.id as aula_id FROM turma_aula ta " +
                    "JOIN aula a ON ta.aula_id = a.id " +
                    "WHERE ta.turma_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, turmaId);
            ResultSet rs = stmt.executeQuery();

            Turma turma = turmaDAO.buscarPorId(turmaId);
            if (turma == null) {
                 return lista;
            }

            while (rs.next()) {
                int turmaAulaId = rs.getInt("ta.id");
                int aulaId = rs.getInt("aula_id");

                // Buscar a Aula completa pelo ID
                Aula aula = aulaDAO.buscarPorId(aulaId);

                if (aula != null) {
                    TurmaAula ta = new TurmaAula(
                        turmaAulaId,
                        turma,
                        aula
                    );
                    lista.add(ta);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<TurmaAula> buscarPorAula(int aulaId) {
        List<TurmaAula> turmaAulas = new ArrayList<>();
        String sql = "SELECT ta.*, t.modalidade, t.turno, t.periodo, " +
                    "c.id as curso_id, c.nome as curso_nome " +
                    "FROM turma_aula ta " +
                    "JOIN turma t ON ta.turma_id = t.id " +
                    "LEFT JOIN curso c ON t.curso_id = c.id " +
                    "WHERE ta.aula_id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, aulaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TurmaAula turmaAula = new TurmaAula();
                turmaAula.setId(rs.getInt("id"));
                
                // Criar e configurar a turma
                Turma turma = new Turma();
                turma.setId(rs.getInt("turma_id"));
                turma.setModalidade(rs.getString("modalidade"));
                turma.setTurno(rs.getString("turno"));
                turma.setPeriodo(rs.getString("periodo"));
                
                // Criar e configurar o curso
                Curso curso = new Curso();
                curso.setId(rs.getInt("curso_id"));
                curso.setNome(rs.getString("curso_nome"));
                turma.setCurso(curso);
                
                turmaAula.setTurma(turma);
                turmaAulas.add(turmaAula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return turmaAulas;
    }

    public boolean verificarRelacionamentoExistente(int turmaId, int aulaId) {
        String sql = "SELECT COUNT(*) FROM turma_aula WHERE turma_id = ? AND aula_id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, turmaId);
            stmt.setInt(2, aulaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TurmaAula> buscarTodos() {
        List<TurmaAula> turmaAulas = new ArrayList<>();
        String sql = "SELECT ta.*, t.modalidade, t.turno, t.periodo, " +
                    "c.id as curso_id, c.nome as curso_nome, " +
                    "a.id as aula_id, dp.disciplina_id, d.nome as disciplina_nome, " +
                    "dp.professor_id, p.nome as professor_nome " +
                    "FROM turma_aula ta " +
                    "JOIN turma t ON ta.turma_id = t.id " +
                    "LEFT JOIN curso c ON t.curso_id = c.id " +
                    "JOIN aula a ON ta.aula_id = a.id " +
                    "JOIN disciplina_professor dp ON a.disci_prof_id = dp.id " +
                    "JOIN disciplina d ON dp.disciplina_id = d.id " +
                    "JOIN professor p ON dp.professor_id = p.id";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TurmaAula turmaAula = new TurmaAula();
                turmaAula.setId(rs.getInt("id"));
                
                // Criar e configurar a turma
                Turma turma = new Turma();
                turma.setId(rs.getInt("turma_id"));
                turma.setModalidade(rs.getString("modalidade"));
                turma.setTurno(rs.getString("turno"));
                turma.setPeriodo(rs.getString("periodo"));
                
                // Criar e configurar o curso
                Curso curso = new Curso();
                curso.setId(rs.getInt("curso_id"));
                curso.setNome(rs.getString("curso_nome"));
                turma.setCurso(curso);
                
                // Criar e configurar a aula
                Aula aula = aulaDAO.buscarPorId(rs.getInt("aula_id"));
                if (aula == null) {
                    continue; // Pula para o próximo registro se a aula não for encontrada
                }
                
                turmaAula.setAula(aula);
                turmaAula.setTurma(turma);
                turmaAulas.add(turmaAula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return turmaAulas;
    }
} 