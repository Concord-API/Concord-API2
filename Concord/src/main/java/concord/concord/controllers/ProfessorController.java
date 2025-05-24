package concord.concord.controllers;

import concord.concord.DAO.ProfessorDAO;
import concord.concord.DAO.DisciplinaDAO;
import concord.concord.DAO.DisciplinaProfessorDAO;
import concord.concord.models.Professor;
import concord.concord.models.Disciplina;
import concord.concord.models.DisciplinaProfessor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;
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
    private TableColumn<Professor, String> cargaHorariaCol;
    @FXML
    private TableColumn<Professor, String> matriculaCol;
    @FXML
    private TableColumn<Professor, String> statusCol;

    private final ObservableList<Professor> professorList = FXCollections.observableArrayList();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final DisciplinaProfessorDAO disciplinaProfessorDAO = new DisciplinaProfessorDAO();

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
        cargaHorariaCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCargaHoraria())));
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
            try {
                professorDAO.adicionarProfessor(professor);
                carregarProfessoresDoBanco(); 
                table.refresh();
            } catch (IllegalArgumentException e) {
                mostrarAlerta("Erro", "Erro ao Adicionar", e.getMessage());
            }
        });
    }

    @FXML
    public void showEditDialog() {
        Professor selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Professor> dialog = createProfessorDialog("Editar Professor", selected);
            Optional<Professor> result = dialog.showAndWait();
            result.ifPresent(updated -> {
                try {
                    professorDAO.editarProfessor(updated);
                    carregarProfessoresDoBanco(); 
                    table.refresh();
                } catch (IllegalArgumentException e) {
                    mostrarAlerta("Erro", "Erro ao Editar", e.getMessage());
                }
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

    @FXML
    public void showRelacionarDisciplinaDialog() {
        Professor selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            mostrarAlerta("Aviso", "Seleção Necessária", "Por favor, selecione um professor para relacionar disciplinas.");
            return;
        }

        
        Professor professorAtual = professorDAO.buscarPorId(selected.getId());
        if (professorAtual == null) {
            mostrarAlerta("Erro", "Professor não encontrado", "O professor selecionado não existe mais no banco de dados.");
            carregarProfessoresDoBanco(); 
            return;
        }

        
        List<Disciplina> todasDisciplinas = disciplinaDAO.buscarTodas();
        if (todasDisciplinas.isEmpty()) {
            mostrarAlerta("Aviso", "Sem Disciplinas", "Não há disciplinas cadastradas no sistema. Por favor, cadastre uma disciplina primeiro.");
            return;
        }

        Dialog<DisciplinaProfessor> dialog = new Dialog<>();
        dialog.setTitle("Relacionar Disciplinas");
        dialog.setHeaderText("Relacionar Disciplinas ao Professor: " + selected.getNome());

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
 
        ComboBox<Disciplina> disciplinaComboBox = new ComboBox<>(FXCollections.observableArrayList(todasDisciplinas));
        
        ListView<DisciplinaProfessor> disciplinasRelacionadasList = new ListView<>();
        disciplinasRelacionadasList.setItems(FXCollections.observableArrayList(
            disciplinaProfessorDAO.buscarPorProfessor(selected.getId())
        ));
        
        disciplinasRelacionadasList.setCellFactory(lv -> new ListCell<DisciplinaProfessor>() {
            @Override
            protected void updateItem(DisciplinaProfessor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisciplina().getNome());
                }
            }
        });

        Button removerButton = new Button("Remover Disciplina");
        removerButton.setOnAction(e -> {
            DisciplinaProfessor selectedRelacionamento = disciplinasRelacionadasList.getSelectionModel().getSelectedItem();
            if (selectedRelacionamento != null) {
                disciplinaProfessorDAO.remover(selectedRelacionamento.getId());
                disciplinasRelacionadasList.getItems().remove(selectedRelacionamento);
            }
        });

        VBox content = new VBox(10);
        content.getChildren().addAll(
            new Label("Selecione a Disciplina:"),
            disciplinaComboBox,
            new Label("Disciplinas Relacionadas:"),
            disciplinasRelacionadasList,
            removerButton
        );

        dialogPane.setContent(content);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Disciplina disciplinaSelecionada = disciplinaComboBox.getValue();
                if (disciplinaSelecionada == null) {
                    mostrarAlerta("Erro", "Seleção Necessária", "Por favor, selecione uma disciplina.");
                    return null;
                }

                try {
                    DisciplinaProfessor novoDisciplinaProfessor = new DisciplinaProfessor(selected, disciplinaSelecionada);
                    disciplinaProfessorDAO.adicionar(novoDisciplinaProfessor);
                    
                    disciplinasRelacionadasList.getItems().setAll(
                        disciplinaProfessorDAO.buscarPorProfessor(selected.getId())
                    );
                    
                    return novoDisciplinaProfessor;
                } catch (RuntimeException e) {
                    mostrarAlerta("Erro", "Erro ao Relacionar", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            disciplinasRelacionadasList.getItems().add(result);
        });
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
        TextField cargaHorariaField = new TextField();
        TextField matriculaField = new TextField();
        ComboBox<String> statusComboBox = new ComboBox<>(FXCollections.observableArrayList("Ativo", "Inativo"));
        statusComboBox.setValue("Ativo");

        if (existing != null) {
            nomeField.setText(existing.getNome());
            emailField.setText(existing.getEmail());
            telefoneField.setText(existing.getTelefone());
            cargaHorariaField.setText(String.valueOf(existing.getCargaHoraria()));
            matriculaField.setText(existing.getMatricula());
            statusComboBox.setValue(existing.getStatus() == 1 ? "Ativo" : "Inativo");
        }

        dialogPane.setContent(new VBox(10,
                new Label("Nome:"), nomeField,
                new Label("Email:"), emailField,
                new Label("Telefone:"), telefoneField,
                new Label("Carga Horária (máx. 11):"), cargaHorariaField,
                new Label("Matrícula:"), matriculaField,
                new Label("Status:"), statusComboBox
        ));

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (nomeField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    telefoneField.getText().isEmpty() || cargaHorariaField.getText().isEmpty() ||
                    matriculaField.getText().isEmpty()) {
                    mostrarAlerta("Erro", "Campos Obrigatórios", "Por favor, preencha todos os campos!");
                    return null;
                }

                try {
                    int cargaHoraria = Integer.parseInt(cargaHorariaField.getText());
                    if (cargaHoraria <= 0 || cargaHoraria > 11) {
                        mostrarAlerta("Erro", "Carga Horária Inválida", "A carga horária deve estar entre 1 e 11!");
                        return null;
                    }

                    int status = statusComboBox.getValue().equals("Ativo") ? 1 : 0;
                    return new Professor(
                        existing != null ? existing.getId() : 0,
                        nomeField.getText(),
                        emailField.getText(),
                        telefoneField.getText(),
                        cargaHoraria,
                        matriculaField.getText(),
                        status
                    );
                } catch (NumberFormatException e) {
                    mostrarAlerta("Erro", "Carga Horária Inválida", "A carga horária deve ser um número válido!");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }
}

