package concord.concord.controllers;

import concord.concord.DAO.CursoDAO;
import concord.concord.DAO.ProfessorDAO;
import concord.concord.models.Curso;
import concord.concord.models.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class CursoController {

    @FXML
    private TableView<Curso> table;
    @FXML
    private TableColumn<Curso, String> nomeCol;
    @FXML
    private TableColumn<Curso, String> siglaCol;
    @FXML
    private TableColumn<Curso, Integer> duracaoCol;

    @FXML
    private TableColumn<Curso, String> descricaoCol;
    @FXML
    private TableColumn<Curso, String> coordenadorCol;

    private final ObservableList<Curso> cursoList = FXCollections.observableArrayList();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private ObservableList<Professor> professores = FXCollections.observableArrayList();

    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        carregarProfessores();

        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        siglaCol.setCellValueFactory(new PropertyValueFactory<>("sigla"));
        duracaoCol.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        coordenadorCol.setCellValueFactory(new PropertyValueFactory<>("coordenadorNome"));

        carregarCursosDoBanco();
    }

    private void carregarProfessores() {
        professores.clear();
        professores.addAll(professorDAO.buscarTodos());
    }

    @FXML
    public void showAddDialog() {
        Dialog<Curso> dialog = createCursoDialog("Adicionar Curso", null);
        Optional<Curso> result = dialog.showAndWait();

        result.ifPresent(curso -> {
            cursoDAO.adicionarCurso(curso);
            
            Professor coordenador = professores.stream()
                .filter(p -> p.getId() == curso.getCoordenadorId())
                .findFirst()
                .orElse(null);
            
            if (coordenador != null) {
                curso.setCoordenadorNome(coordenador.getNome());
            }
            
            cursoList.add(curso);
            table.refresh();
        });
    }

    @FXML
    public void showEditDialog() {
        Curso selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<Curso> dialog = createCursoDialog("Editar Curso", selected);
            Optional<Curso> result = dialog.showAndWait();
            result.ifPresent(updated -> {
                cursoDAO.editarCurso(updated);
                selected.setNome(updated.getNome());
                selected.setSigla(updated.getSigla());
                selected.setDuracao(updated.getDuracao());
                selected.setDescricao(updated.getDescricao());
                selected.setCoordenadorId(updated.getCoordenadorId());


                Professor novoCoordenador = professores.stream()
                        .filter(p -> p.getId() == updated.getCoordenadorId())
                        .findFirst()
                        .orElse(null);
                if (novoCoordenador != null) {
                    selected.setCoordenadorNome(novoCoordenador.getNome());
                }

                table.refresh();
            });
        }
    }

    @FXML
    public void deleteSelectedCurso() {
        Curso selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cursoDAO.excluirCurso(selected.getId());
            cursoList.remove(selected);
            table.refresh();
        }
    }

    private void carregarCursosDoBanco() {
        cursoList.clear();
        cursoList.addAll(cursoDAO.buscarCursos());
        table.setItems(cursoList);
    }

    private Dialog<Curso> createCursoDialog(String title, Curso existing) {
        
        carregarProfessores();

        Dialog<Curso> dialog = new Dialog<>();
        dialog.setTitle(title);
    
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        TextField nomeField = new TextField();
        TextField siglaField = new TextField();
        TextField duracaoField = new TextField();
        TextArea descricaoArea = new TextArea();
        ComboBox<Professor> coordenadorComboBox = new ComboBox<>(professores);

        coordenadorComboBox.setCellFactory(lv -> new ListCell<Professor>() {
            @Override
            protected void updateItem(Professor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });

        coordenadorComboBox.setButtonCell(new ListCell<Professor>() {
            @Override
            protected void updateItem(Professor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
    
        if (existing != null) {
            nomeField.setText(existing.getNome());
            siglaField.setText(existing.getSigla());
            duracaoField.setText(String.valueOf(existing.getDuracao()));
            descricaoArea.setText(existing.getDescricao());
            
            Professor coordenadorAtual = professores.stream()
                .filter(p -> p.getId() == existing.getCoordenadorId())
                .findFirst()
                .orElse(null);
            
            coordenadorComboBox.setValue(coordenadorAtual);
        }
    
        dialogPane.setContent(new VBox(10,
                new Label("Nome:"), nomeField,
                new Label("Sigla:"), siglaField,
                new Label("Duração (semestres):"), duracaoField,
                new Label("Descrição:"), descricaoArea,
                new Label("Coordenador:*"), coordenadorComboBox
        ));
    
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (nomeField.getText().isEmpty() || siglaField.getText().isEmpty() || 
                    duracaoField.getText().isEmpty() || coordenadorComboBox.getValue() == null) {
                    mostrarAlerta("Erro", "Campos Obrigatórios", 
                        "Por favor, preencha todos os campos obrigatórios!\n" +
                        "Nome, Sigla, Duração e Coordenador são obrigatórios.");
                    return null;
                }
                
                try {
                    Curso novoCurso = new Curso();
                    novoCurso.setId(existing != null ? existing.getId() : 0);
                    novoCurso.setNome(nomeField.getText());
                    novoCurso.setSigla(siglaField.getText());
                    novoCurso.setDuracao(Integer.parseInt(duracaoField.getText()));
                    novoCurso.setDescricao(descricaoArea.getText());
                    
                    Professor coordenadorSelecionado = coordenadorComboBox.getValue();
                    if (coordenadorSelecionado == null) {
                        mostrarAlerta("Erro", "Coordenador Obrigatório", 
                            "É necessário selecionar um coordenador para o curso.");
                        return null;
                    }
                    
                    novoCurso.setCoordenadorId(coordenadorSelecionado.getId());
                    novoCurso.setCoordenadorNome(coordenadorSelecionado.getNome());
                    return novoCurso;
                } catch (NumberFormatException e) {
                    mostrarAlerta("Erro", "Duração Inválida", "A duração deve ser um número inteiro!");
                    return null;
                }
            }
            return null;
        });
    
        return dialog;
    }
}
