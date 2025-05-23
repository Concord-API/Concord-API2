package concord.concord.DAO;

import concord.concord.Database;
import concord.concord.models.Curso;
import concord.concord.models.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CursoDAO {

    public void adicionarCurso(Curso curso) {
        String sql = "INSERT INTO curso (nome, sigla, duracao, descricao, coordenador_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getSigla());
            stmt.setInt(3, curso.getDuracao());
            stmt.setString(4, curso.getDescricao());
            stmt.setInt(5, curso.getCoordenadorId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarCurso(Curso curso) {
        String sql = "UPDATE curso SET nome = ?, sigla = ?, duracao = ?, descricao = ?, coordenador_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getSigla());
            stmt.setInt(3, curso.getDuracao());
            stmt.setString(4, curso.getDescricao());
            stmt.setInt(5, curso.getCoordenadorId());
            stmt.setInt(6, curso.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluirCurso(int id) {
        String sql = "DELETE FROM curso WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Curso> buscarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT c.id, c.nome, c.sigla, c.duracao, c.descricao, c.coordenador_id, u.nome AS coordenador_nome " +
                    "FROM curso c JOIN professor u ON c.coordenador_id = u.id";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                curso.setSigla(rs.getString("sigla"));
                curso.setDuracao(rs.getInt("duracao"));
                curso.setDescricao(rs.getString("descricao"));
                curso.setCoordenadorId(rs.getInt("coordenador_id"));
                curso.setCoordenadorNome(rs.getString("coordenador_nome"));

                cursos.add(curso);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }

    public Curso buscarPorId(int id) {
        String sql = "SELECT c.*, p.nome AS coordenador_nome FROM curso c " +
                    "LEFT JOIN professor p ON c.coordenador_id = p.id " +
                    "WHERE c.id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                curso.setSigla(rs.getString("sigla"));
                curso.setDuracao(rs.getInt("duracao"));
                curso.setDescricao(rs.getString("descricao"));
                curso.setCoordenadorId(rs.getInt("coordenador_id"));
                curso.setCoordenadorNome(rs.getString("coordenador_nome"));
                return curso;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
