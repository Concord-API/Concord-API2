package concord.concord.controllers;

import concord.concord.models.Curso;
import concord.concord.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoController {


    public void adicionarCurso(Curso curso) {
        String sql = "INSERT INTO curso (nome, sigla, duracao, modalidade, turno, descricao, coordenador_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getSigla());
            stmt.setInt(3, curso.getDuracao());
            stmt.setString(4, curso.getModalidade());
            stmt.setString(5, curso.getTurno());
            stmt.setString(6, curso.getDescricao());
            stmt.setInt(7, curso.getCoordenadorId());

            stmt.executeUpdate();
            System.out.println("Curso adicionado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void editarCurso(Curso curso) {
        String sql = "UPDATE curso SET nome = ?, sigla = ?, duracao = ?, modalidade = ?, turno = ?, descricao = ?, coordenador_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getSigla());
            stmt.setInt(3, curso.getDuracao());
            stmt.setString(4, curso.getModalidade());
            stmt.setString(5, curso.getTurno());
            stmt.setString(6, curso.getDescricao());
            stmt.setInt(7, curso.getCoordenadorId());
            stmt.setInt(8, curso.getId());

            stmt.executeUpdate();
            System.out.println("Curso atualizado com sucesso!");

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
            System.out.println("Curso excluído com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Curso> buscarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM curso";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                curso.setSigla(rs.getString("sigla"));
                curso.setDuracao(rs.getInt("duracao"));
                curso.setModalidade(rs.getString("modalidade"));
                curso.setTurno(rs.getString("turno"));
                curso.setDescricao(rs.getString("descricao"));
                curso.setCoordenadorId(rs.getInt("coordenador_id"));

                cursos.add(curso);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }
}
