package concord.concord.controllers;

import concord.concord.DAO.ProfessorDAO;
import concord.concord.models.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;

import java.util.Optional;

public class ProfessorController {

    @FXML
    private TableView<Professor> table;
    @FXML
    private TableColumn<Professor, String> nomeCol;
    @FXML
    private TableColumn<Professor, String> emailCol;
    @FXML
    private TableColumn<Professor, String> telefoneCol;
    @FXML
    private TableColumn<Professor, String> matriculaCol;
    @FXML
    private TableColumn<Professor, String> statusCol;

    private final ObservableList<Professor> professorList = FXCollections.observableArrayList();
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefoneCol.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        matriculaCol.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        statusCol.setCellValueFactory(cellData -> {
            Integer status = cellData.getValue().getStatus();
            return new SimpleStringProperty(status == 1 ? "Ativo" : "Inativo");
        });

        carregarProfessoresDoBanco();
    }

    @FXML
    public void showAddDialog() {
        Dialog<Professor> dialog = createProfessorDialog("Adicionar Professor", null);
        Optional<Professor> result = dialog.showAndWait();

        result.ifPresent(professor -> {
            professorDAO.adicionarProfessor(professor);
            professorList.add(professor);
            table.refresh();
        });
    }

    @FXML
    public void showEditDialog() {
        Professor selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Professor> dialog = createProfessorDialog("Editar Professor", selected);
            Optional<Professor> result = dialog.showAndWait();
            result.ifPresent(updated -> {
                professorDAO.editarProfessor(updated);
                selected.setNome(updated.getNome());
                selected.setEmail(updated.getEmail());
                selected.setTelefone(updated.getTelefone());
                selected.setMatricula(updated.getMatricula());
                selected.setStatus(updated.getStatus());
                table.refresh();
            });
        }
    }

    @FXML
    public void deleteSelectedProfessor() {
        Professor selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            professorDAO.excluirProfessor(selected.getId());
            professorList.remove(selected);
            table.refresh();
        }
    }

    private void carregarProfessoresDoBanco() {
        professorList.clear();
        professorList.addAll(professorDAO.buscarTodos());
        table.setItems(professorList);
    }

    private Dialog<Professor> createProfessorDialog(String title, Professor existing) {
        Dialog<Professor> dialog = new Dialog<>();
        dialog.setTitle(title);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nomeField = new TextField();
        TextField emailField = new TextField();
        TextField telefoneField = new TextField();
        TextField matriculaField = new TextField();
        ComboBox<String> statusComboBox = new ComboBox<>(FXCollections.observableArrayList("Ativo", "Inativo"));
        statusComboBox.setValue("Ativo");


        if (existing != null) {
            nomeField.setText(existing.getNome());
            emailField.setText(existing.getEmail());
            telefoneField.setText(existing.getTelefone());
            matriculaField.setText(existing.getMatricula());
            statusComboBox.setValue(existing.getStatus() == 1 ? "Ativo" : "Inativo");

        }

        dialogPane.setContent(new VBox(10,
                new Label("Nome:"), nomeField,
                new Label("Email:"), emailField,
                new Label("Telefone:"), telefoneField,
                new Label("Matrícula:"), matriculaField,
                new Label("Status:"), statusComboBox
        ));

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (nomeField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    telefoneField.getText().isEmpty() || matriculaField.getText().isEmpty()) {
                    mostrarAlerta("Erro", "Campos Obrigatórios", "Por favor, preencha todos os campos!");
                    return null;
                }
                int status = statusComboBox.getValue().equals("Ativo") ? 1 : 0;
                return new Professor(
                    existing != null ? existing.getId() : 0,
                    nomeField.getText(),
                    emailField.getText(),
                    telefoneField.getText(),
                    matriculaField.getText(),
                    status
                );
            }
            return null;
        });

        return dialog;
    }
}

