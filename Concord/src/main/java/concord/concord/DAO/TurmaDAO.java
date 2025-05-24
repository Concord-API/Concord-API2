package concord.concord.DAO;

import concord.concord.Database;
import concord.concord.models.Turma;
import concord.concord.models.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    private final CursoDAO cursoDAO = new CursoDAO();

    public void adicionar(Turma turma) {
        String sql = "INSERT INTO turma (modalidade, turno, periodo, curso_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, turma.getModalidade());
            stmt.setString(2, turma.getTurno());
            stmt.setString(3, turma.getPeriodo());
            stmt.setInt(4, turma.getCurso().getId());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                turma.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Turma> buscarTodas() {
        List<Turma> turmas = new ArrayList<>();
        String sql = "SELECT t.*, c.id as curso_id, c.nome as curso_nome FROM turma t " +
                    "LEFT JOIN curso c ON t.curso_id = c.id";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Turma turma = new Turma();
                turma.setId(rs.getInt("id"));
                turma.setModalidade(rs.getString("modalidade"));
                turma.setTurno(rs.getString("turno"));
                turma.setPeriodo(rs.getString("periodo"));

                Curso curso = new Curso();
                curso.setId(rs.getInt("curso_id"));
                curso.setNome(rs.getString("curso_nome"));
                turma.setCurso(curso);
                
                turmas.add(turma);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return turmas;
    }

    public void atualizar(Turma turma) {
        String sql = "UPDATE turma SET modalidade = ?, turno = ?, periodo = ?, curso_id = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, turma.getModalidade());
            stmt.setString(2, turma.getTurno());
            stmt.setString(3, turma.getPeriodo());
            stmt.setInt(4, turma.getCurso().getId());
            stmt.setInt(5, turma.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM turma WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Turma buscarPorId(int id) {
        String sql = "SELECT t.*, c.id as curso_id, c.nome as curso_nome FROM turma t " +
                    "LEFT JOIN curso c ON t.curso_id = c.id " +
                    "WHERE t.id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Turma turma = new Turma();
                turma.setId(rs.getInt("id"));
                turma.setModalidade(rs.getString("modalidade"));
                turma.setTurno(rs.getString("turno"));
                turma.setPeriodo(rs.getString("periodo"));
                
                Curso curso = new Curso();
                curso.setId(rs.getInt("curso_id"));
                curso.setNome(rs.getString("curso_nome"));
                turma.setCurso(curso);
                
                return turma;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 