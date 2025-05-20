package concord.concord.DAO;

import concord.concord.Database;
import concord.concord.models.Disciplina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {
    
    public void adicionarDisciplina(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (nome) VALUES (?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, disciplina.getNome());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void editarDisciplina(Disciplina disciplina) {
        String sql = "UPDATE disciplina SET nome = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, disciplina.getNome());
            stmt.setInt(2, disciplina.getId());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void excluirDisciplina(int id) {
        String sql = "DELETE FROM disciplina WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Disciplina> buscarTodas() {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT * FROM disciplina ORDER BY nome";
        
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Disciplina disciplina = new Disciplina(
                    rs.getInt("id"),
                    rs.getString("nome")
                );
                disciplinas.add(disciplina);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return disciplinas;
    }
    
    public Disciplina buscarPorId(int id) {
        String sql = "SELECT * FROM disciplina WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Disciplina(
                    rs.getInt("id"),
                    rs.getString("nome")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
} 