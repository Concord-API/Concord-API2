package concord.concord.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import concord.concord.Database;
import concord.concord.models.Professor;

public class ProfessorDAO {

    public void adicionarProfessor(Professor professor) {
        String sql = "INSERT INTO professores (nome, email, telefone, matricula, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getEmail());
            stmt.setString(3, professor.getTelefone());
            stmt.setString(4, professor.getMatricula());
            stmt.setInt(5, professor.getStatus());

            stmt.executeUpdate();
            System.out.println("Professor adicionado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void editarProfessor(Professor professor) {
        String sql = "UPDATE professores SET nome = ?, email = ?, telefone = ?, matricula = ?, status = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getEmail());
            stmt.setString(3, professor.getTelefone());
            stmt.setString(4, professor.getMatricula());
            stmt.setInt(5, professor.getStatus());
            stmt.setInt(6, professor.getId());

            stmt.executeUpdate();
            System.out.println("Professor atualizado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void excluirProfessor(int id) {
        String sql = "DELETE FROM professores WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Professor excluído com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Professor> buscarProfessores() {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT id, nome FROM professores WHERE status = 1"; // Status = 1 (ativos)

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Professor professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setNome(rs.getString("nome"));
                professores.add(professor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professores;
    }

    public List<Professor> buscarTodos() {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT * FROM professores";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Professor professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setNome(rs.getString("nome"));
                professor.setEmail(rs.getString("email"));
                professor.setTelefone(rs.getString("telefone"));
                professor.setMatricula(rs.getString("matricula"));
                professor.setStatus(rs.getInt("status"));

                professores.add(professor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professores;
    }
}
