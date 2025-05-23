package concord.concord.controllers;

import concord.concord.DAO.DisciplinaDAO;
import concord.concord.models.Disciplina;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class DisciplinaController {

    @FXML
    private TableView<Disciplina> table;
    @FXML
    private TableColumn<Disciplina, String> nomeCol;

    private final ObservableList<Disciplina> disciplinaList = FXCollections.observableArrayList();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

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
        carregarDisciplinasDoBanco();
    }

    @FXML
    public void showAddDialog() {
        Dialog<Disciplina> dialog = createDisciplinaDialog("Adicionar Disciplina", null);
        Optional<Disciplina> result = dialog.showAndWait();

        result.ifPresent(disciplina -> {
            disciplinaDAO.adicionar(disciplina);
            disciplinaList.add(disciplina);
            table.refresh();
        });
    }

    @FXML
    public void showEditDialog() {
        Disciplina selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Disciplina> dialog = createDisciplinaDialog("Editar Disciplina", selected);
            Optional<Disciplina> result = dialog.showAndWait();
            result.ifPresent(updated -> {
                disciplinaDAO.editar(updated);
                selected.setNome(updated.getNome());
                table.refresh();
            });
        }
    }

    @FXML
    public void deleteSelectedDisciplina() {
        Disciplina selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            disciplinaDAO.excluir(selected.getId());
            disciplinaList.remove(selected);
            table.refresh();
        }
    }

    private void carregarDisciplinasDoBanco() {
        disciplinaList.clear();
        disciplinaList.addAll(disciplinaDAO.buscarTodas());
        table.setItems(disciplinaList);
    }

    private Dialog<Disciplina> createDisciplinaDialog(String title, Disciplina existing) {
        Dialog<Disciplina> dialog = new Dialog<>();
        dialog.setTitle(title);
    
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        TextField nomeField = new TextField();
    
        if (existing != null) {
            nomeField.setText(existing.getNome());
        }
    
        dialogPane.setContent(new VBox(10,
                new Label("Nome:"), nomeField
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String nome = nomeField.getText().trim();
                
                if (nome.isEmpty()) {
                    mostrarAlerta("Erro", "Campo Incompleto", "Por favor, preencha o nome da disciplina!");
                    return null;
                }

                if (existing != null) {
                    existing.setNome(nome);
                    return existing;
                } else {
                    Disciplina novaDisciplina = new Disciplina();
                    novaDisciplina.setNome(nome);
                    return novaDisciplina;
                }
            }
            return null;
        });

        return dialog;
    }
} 