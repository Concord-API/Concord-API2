package concord.concord.controllers;

import concord.concord.DAO.AulaDAO;
import concord.concord.DAO.CursoDAO;
import concord.concord.DAO.ProfessorDAO;
import concord.concord.DAO.DisciplinaDAO;
import concord.concord.models.Aula;
import concord.concord.models.Professor;
import concord.concord.models.Curso;
import concord.concord.models.Disciplina;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class AulaController {

    @FXML
    private TableView<Aula> table;
    @FXML
    private TableColumn<Aula, String> diciplinaCol;
    @FXML
    private TableColumn<Aula, String> professorCol;
    @FXML
    private TableColumn<Aula, String> horarioCol;
    @FXML
    private TableColumn<Aula, String> diaCol;
    @FXML
    private TableColumn<Aula, String> cursoCol;

    private final ObservableList<Aula> classList = FXCollections.observableArrayList();
    private final ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();
    private final ObservableList<String> horarios = FXCollections.observableArrayList("07:10", "08:00", "09:15", "10:05", "10:55", "11:45", "18:45", "19:35", "20:35", "21:25", "22:15");
    private final ObservableList<String> dias = FXCollections.observableArrayList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sabado");

    private final AulaDAO aulaDAO = new AulaDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    private ObservableList<Professor> professores = FXCollections.observableArrayList();
    private ObservableList<Curso> cursos = FXCollections.observableArrayList();

    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        diciplinaCol.setCellValueFactory(new PropertyValueFactory<>("diciplina"));
        professorCol.setCellValueFactory(new PropertyValueFactory<>("professor"));
        cursoCol.setCellValueFactory(new PropertyValueFactory<>("curso"));
        diaCol.setCellValueFactory(new PropertyValueFactory<>("dia"));
        horarioCol.setCellValueFactory(new PropertyValueFactory<>("horario"));

        carregarProfessoresECursos();
        carregarDisciplinas();
        carregarAulasDoBanco();
    }

    private void carregarProfessoresECursos() {
        professores.setAll(professorDAO.buscarTodos());
        cursos.setAll(cursoDAO.buscarCursos());
    }

    private void carregarDisciplinas() {
        disciplinas.setAll(disciplinaDAO.buscarTodas());
    }

    @FXML
    public void showAddDialog() {
        Dialog<Aula> dialog = createClassDialog("Adicionar Aula", null);
        Optional<Aula> result = dialog.showAndWait();

        result.ifPresent(aula -> {
            boolean professorOcupado = aulaDAO.professorAula(aula.getProfessor(), aula.getDia(), aula.getHorario());

            if (professorOcupado) {
                mostrarAlerta("Erro", "Horário Indisponível", "O professor já tem uma aula nesse horário!");
            } else {
                aulaDAO.adicionarAula(aula);
                classList.add(aula);
                table.refresh();
            }
        });
    }

    @FXML
    public void showEditDialog() {
        Aula selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Aula> dialog = createClassDialog("Editar Aula", selected);
            Optional<Aula> result = dialog.showAndWait();
            result.ifPresent(updated -> {
                if (!validarAula(updated, selected.getId())) {
                    mostrarAlerta("Erro", "Conflito de Horário", "Este professor já tem uma aula neste horário e dia!");
                    return;
                }
                aulaDAO.editarAula(selected.getId(), updated);
                selected.setDiciplina(updated.getDiciplina());
                selected.setProfessor(updated.getProfessor());
                selected.setCurso(updated.getCurso());
                selected.setDia(updated.getDia());
                selected.setHorario(updated.getHorario());
                table.refresh();
            });
        }
    }

    private boolean validarAula(Aula aula, int idAtual) {
        return !aulaDAO.existeConflitoDeHorario(aula.getProfessor(), aula.getDia(), aula.getHorario(), idAtual);
    }

    @FXML
    public void deleteSelectedClass() {
        Aula selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            aulaDAO.deletarAula(selected.getId());
            classList.remove(selected);
            table.refresh();
        }
    }

    private void carregarAulasDoBanco() {
        classList.clear();
        List<Aula> aulas = aulaDAO.buscarTodasAulas();
        classList.addAll(aulas);
        table.setItems(classList);
    }

    private Dialog<Aula> createClassDialog(String title, Aula existing) {
        Dialog<Aula> dialog = new Dialog<>();
        dialog.setTitle(title);
    
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        ComboBox<Disciplina> disciplinaComboBox = new ComboBox<>(disciplinas);
        ComboBox<Professor> professorComboBox = new ComboBox<>(professores);
        ComboBox<Curso> courseComboBox = new ComboBox<>(cursos);
        ComboBox<String> diaComboBox = new ComboBox<>(dias);
        ComboBox<String> horarioComboBox = new ComboBox<>(horarios);
    
        if (existing != null) {
            disciplinaComboBox.setValue(disciplinas.stream()
                .filter(d -> d.getNome().equals(existing.getDiciplina()))
                .findFirst()
                .orElse(null));
            professorComboBox.setValue(existing.getProfessor());
            courseComboBox.setValue(existing.getCurso());
            diaComboBox.setValue(existing.getDia());
            horarioComboBox.setValue(existing.getHorario());
        }
    
        dialogPane.setContent(new VBox(10,
                new Label("Disciplina:"), disciplinaComboBox,
                new Label("Professor (a):"), professorComboBox,
                new Label("Curso:"), courseComboBox,
                new Label("Dia:"), diaComboBox,
                new Label("Horário:"), horarioComboBox
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Disciplina disciplinaSelecionada = disciplinaComboBox.getValue();
                Professor professorSelecionado = professorComboBox.getValue();
                Curso cursoSelecionado = courseComboBox.getValue();
                String diaSelecionado = diaComboBox.getValue();
                String horarioSelecionado = horarioComboBox.getValue();

                if (disciplinaSelecionada == null || professorSelecionado == null || 
                    cursoSelecionado == null || diaSelecionado == null || horarioSelecionado == null) {
                    mostrarAlerta("Erro", "Campos Incompletos", "Por favor, preencha todos os campos!");
                    return null;
                }

                return new Aula(
                    disciplinaSelecionada.getNome(),
                    professorSelecionado,
                    cursoSelecionado,
                    diaSelecionado,
                    horarioSelecionado
                );
            }
            return null;
        });

        return dialog;
    }
}