package concord.concord.controllers;

import concord.concord.DAO.TurmaDAO;
import concord.concord.DAO.CursoDAO;
import concord.concord.models.Turma;
import concord.concord.models.Curso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class TurmaController {

    @FXML
    private TableView<Turma> table;
    @FXML
    private TableColumn<Turma, String> modalidadeCol;
    @FXML
    private TableColumn<Turma, String> turnoCol;
    @FXML
    private TableColumn<Turma, String> periodoCol;
    @FXML
    private TableColumn<Turma, String> cursoCol;

    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final ObservableList<Turma> turmaList = FXCollections.observableArrayList();
    private final ObservableList<Curso> cursos = FXCollections.observableArrayList();
    private final ObservableList<String> modalidades = FXCollections.observableArrayList("Presencial", "EAD", "Híbrido");
    private final ObservableList<String> turnos = FXCollections.observableArrayList("Matutino", "Vespertino", "Noturno");
    private final ObservableList<String> periodos = FXCollections.observableArrayList("1º", "2º", "3º", "4º", "5º", "6º");

    @FXML
    public void initialize() {
        modalidadeCol.setCellValueFactory(new PropertyValueFactory<>("modalidade"));
        turnoCol.setCellValueFactory(new PropertyValueFactory<>("turno"));
        periodoCol.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        cursoCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getCurso().getNome()
        ));

        carregarCursos();
        carregarTurmas();
    }

    private void carregarCursos() {
        cursos.clear();
        cursos.addAll(cursoDAO.buscarCursos());
    }

    private void carregarTurmas() {
        turmaList.setAll(turmaDAO.buscarTodas());
        table.setItems(turmaList);
    }

    @FXML
    public void showAddDialog() {
        Dialog<Turma> dialog = createTurmaDialog("Adicionar Turma", null);
        dialog.showAndWait().ifPresent(turma -> {
            turmaDAO.adicionar(turma);
            carregarTurmas();
            TurmaAulaController.atualizarListaTurmas();
        });
    }

    @FXML
    public void showEditDialog() {
        Turma selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Turma> dialog = createTurmaDialog("Editar Turma", selected);
            dialog.showAndWait().ifPresent(turma -> {
                turma.setId(selected.getId());
                turmaDAO.atualizar(turma);
                carregarTurmas();
                TurmaAulaController.atualizarListaTurmas();
            });
        }
    }

    @FXML
    public void showDeleteDialog() {
        Turma selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Exclusão");
            alert.setHeaderText("Excluir Turma");
            alert.setContentText("Tem certeza que deseja excluir esta turma?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    turmaDAO.deletar(selected.getId());
                    carregarTurmas();
                    TurmaAulaController.atualizarListaTurmas();
                }
            });
        }
    }

    private Dialog<Turma> createTurmaDialog(String title, Turma existing) {
        // Recarregar lista de cursos antes de criar o diálogo
        carregarCursos();

        Dialog<Turma> dialog = new Dialog<>();
        dialog.setTitle(title);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<String> modalidadeComboBox = new ComboBox<>(modalidades);
        ComboBox<String> turnoComboBox = new ComboBox<>(turnos);
        ComboBox<String> periodoComboBox = new ComboBox<>(periodos);
        ComboBox<Curso> cursoComboBox = new ComboBox<>(cursos);

        cursoComboBox.setCellFactory(lv -> new ListCell<Curso>() {
            @Override
            protected void updateItem(Curso item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
        cursoComboBox.setButtonCell(new ListCell<Curso>() {
            @Override
            protected void updateItem(Curso item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        if (existing != null) {
            modalidadeComboBox.setValue(existing.getModalidade());
            turnoComboBox.setValue(existing.getTurno());
            periodoComboBox.setValue(existing.getPeriodo());
            cursoComboBox.setValue(existing.getCurso());
        }

        dialogPane.setContent(new VBox(10,
            new Label("Modalidade:"), modalidadeComboBox,
            new Label("Turno:"), turnoComboBox,
            new Label("Período:"), periodoComboBox,
            new Label("Curso:"), cursoComboBox
        ));

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (modalidadeComboBox.getValue() == null || turnoComboBox.getValue() == null ||
                    periodoComboBox.getValue() == null || cursoComboBox.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Campos Obrigatórios");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, preencha todos os campos!");
                    alert.showAndWait();
                    return null;
                }

                return new Turma(
                    modalidadeComboBox.getValue(),
                    turnoComboBox.getValue(),
                    periodoComboBox.getValue(),
                    cursoComboBox.getValue()
                );
            }
            return null;
        });

        return dialog;
    }
} 