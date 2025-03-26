package concord.concord.controllers;

import concord.concord.models.Aula;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import concord.concord.Database;

import java.util.List;
import java.util.Optional;

public class ControllerCadAula {

    @FXML
    private TableView<Aula> table;
    @FXML
    private TableColumn<Aula, String> classNameCol;
    @FXML
    private TableColumn<Aula, String> professorCol;
    @FXML
    private TableColumn<Aula, String> timeCol;
    @FXML
    private TableColumn<Aula, String> dayCol;
    @FXML
    private TableColumn<Aula, String> courseCol;

    private ObservableList<Aula> classList = FXCollections.observableArrayList();
    private final Database database = new Database();
    private final ObservableList<String> professores = FXCollections.observableArrayList("Professor A", "Professor B", "Professor C");
    private final ObservableList<String> aulas = FXCollections.observableArrayList("Matemática", "História", "Física");
    private final ObservableList<String> horarios = FXCollections.observableArrayList("08:00", "10:00", "14:00", "16:00");
    private final ObservableList<String> dias = FXCollections.observableArrayList("Segunda", "Terça", "Quarta", "Quinta", "Sexta");
    private final ObservableList<String> cursos = FXCollections.observableArrayList("Engenharia", "Direito", "Medicina", "Administração");


    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        database.conectar();
        database.criarTabela();

        classNameCol.setCellValueFactory(new PropertyValueFactory<>("className"));
        professorCol.setCellValueFactory(new PropertyValueFactory<>("professor"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("day"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        carregarAulasDoBanco();
    }

    @FXML
    public void showAddDialog() {
        Dialog<Aula> dialog = createClassDialog("Adicionar Aula", null);
        Optional<Aula> result = dialog.showAndWait();

        result.ifPresent(aula -> {

            boolean professorOcupado = database.professorAula(aula.getProfessor(), aula.getDay(), aula.getTime());

            if (professorOcupado) {
                mostrarAlerta("Erro", "Horário Indisponível", "O professor já tem uma aula nesse horário!");
            } else {
                database.adicionarAula(aula.getClassName(), aula.getProfessor(), aula.getCourse(), aula.getDay(), aula.getTime());
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
                database.editarAula(selected.getId(), updated.getClassName(), updated.getProfessor(), updated.getCourse(), updated.getDay(), updated.getTime());
                selected.setClassName(updated.getClassName());
                selected.setProfessor(updated.getProfessor());
                selected.setCourse(updated.getCourse());
                selected.setDay(updated.getDay());
                selected.setTime(updated.getTime());
                table.refresh();
            });
        }
    }

    @FXML
    public void deleteSelectedClass() {
        Aula selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            database.deletarAula(selected.getId());
            classList.remove(selected);
            table.refresh();
        }
    }

    private void carregarAulasDoBanco() {
        classList.clear();
        List<Aula> aulas = database.buscarTodasAulas();
        classList.addAll(aulas);
        table.setItems(classList);
    }

    private Dialog<Aula> createClassDialog(String title, Aula existing) {
        Dialog<Aula> dialog = new Dialog<>();
        dialog.setTitle(title);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<String> classNameComboBox = new ComboBox<>(aulas);
        ComboBox<String> professorComboBox = new ComboBox<>(professores);
        ComboBox<String> courseComboBox = new ComboBox<>(cursos);
        ComboBox<String> dayComboBox = new ComboBox<>(dias);
        ComboBox<String> timeComboBox = new ComboBox<>(horarios);

        if (existing != null) {
            classNameComboBox.setValue(existing.getClassName());
            professorComboBox.setValue(existing.getProfessor());
            courseComboBox.setValue(existing.getCourse());
            dayComboBox.setValue(existing.getDay());
            timeComboBox.setValue(existing.getTime());
        }

        dialogPane.setContent(new VBox(10,
                new Label("Nome da Aula:"), classNameComboBox,
                new Label("Professor:"), professorComboBox,
                new Label("Curso:"), courseComboBox,
                new Label("Dia:"), dayComboBox,
                new Label("Horário:"), timeComboBox

        ));

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Aula(classNameComboBox.getValue(), professorComboBox.getValue(), courseComboBox.getValue(), dayComboBox.getValue(), timeComboBox.getValue());
            }
            return null;
        });

        return dialog;
    }
}