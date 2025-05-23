package concord.concord.controllers;

import concord.concord.DAO.AulaDAO;
import concord.concord.DAO.ProfessorDAO;
import concord.concord.DAO.DisciplinaDAO;
import concord.concord.DAO.DisciplinaProfessorDAO;
import concord.concord.DAO.TurmaAulaDAO;
import concord.concord.DAO.TurmaDAO;
import concord.concord.models.Turma;
import concord.concord.models.TurmaAula;
import concord.concord.models.Aula;
import concord.concord.models.Professor;
import concord.concord.models.Disciplina;
import concord.concord.models.DisciplinaProfessor;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TurmaAulaController {
    @FXML
    private ListView<Turma> turmasListView;
    
    @FXML
    private Label modalidadeLabel;
    
    @FXML
    private Label turnoLabel;
    
    @FXML
    private Label periodoLabel;
    
    @FXML
    private Label cursoLabel;
    
    @FXML
    private TableView<Aula> disciplinasTableView;
    
    @FXML
    private TableColumn<Aula, String> disciplinaCol;
    
    @FXML
    private TableColumn<Aula, String> professorCol;
    
    @FXML
    private TableColumn<Aula, String> diaCol;
    
    @FXML
    private TableColumn<Aula, String> horarioCol;
    
    @FXML
    private ComboBox<String> diaFilterComboBox;
    
    @FXML
    private Button addAulaButton;
    
    @FXML
    private Button editAulaButton;
    
    @FXML
    private Button deleteAulaButton;
    
    private final TurmaAulaDAO turmaAulaDAO = new TurmaAulaDAO();
    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final AulaDAO aulaDAO = new AulaDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final DisciplinaProfessorDAO disciplinaProfessorDAO = new DisciplinaProfessorDAO();
    
    private final ObservableList<Aula> aulasList = FXCollections.observableArrayList();
    private List<Aula> todasAulasDaTurma;
    
    private final ObservableList<String> diasDaSemana = FXCollections.observableArrayList("Todos", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sabado");
    private final ObservableList<String> horariosPossiveis = FXCollections.observableArrayList("07:10", "08:00", "09:15", "10:05", "10:55", "11:45", "18:45", "19:35", "20:35", "21:25", "22:15");
    
    private ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();
    private ObservableList<Professor> professores = FXCollections.observableArrayList();
    
    private static TurmaAulaController instance;

    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    @FXML
    public void initialize() {
        instance = this;
        // Configurar a lista de turmas
        ObservableList<Turma> turmas = FXCollections.observableArrayList(turmaDAO.buscarTodas());
        turmasListView.setItems(turmas);
        turmasListView.setCellFactory(lv -> new ListCell<Turma>() {
            @Override
            protected void updateItem(Turma turma, boolean empty) {
                super.updateItem(turma, empty);
                if (empty || turma == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - %s - %s", 
                        turma.getCurso().getNome(),
                        turma.getPeriodo(),
                        turma.getModalidade()));
                }
            }
        });
        
        
        // Configurar a tabela de disciplinas
        disciplinaCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDisciplina().getNome()
            )
        );
        
        professorCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getProfessor().getNome()
            )
        );
        
        diaCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDia()
            )
        );
        
        horarioCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getHorarioInicio() + " - " + 
                cellData.getValue().getHorarioTermino()
            )
        );
        
        disciplinasTableView.setItems(aulasList);
        
        // Configurar o ComboBox de filtro de dia
        diaFilterComboBox.setItems(diasDaSemana);
        diaFilterComboBox.getSelectionModel().selectFirst(); // Selecionar "Todos" por padrão
        
        // Adicionar listener para seleção de turma
        turmasListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    atualizarDetalhesTurma(newValue);
                } else {
                    limparDetalhesTurma();
                }
            }
        );
        
        // Adicionar listener para seleção de dia no filtro
        diaFilterComboBox.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (todasAulasDaTurma != null) {
                    filtrarAulasPorDia(newValue);
                }
            }
        );

        // Desabilitar botões de ação de aula inicialmente
        setAulaActionButtonsDisabled(true);

        // Adicionar listener para habilitar botões de ação de aula quando uma turma for selecionada
        turmasListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                setAulaActionButtonsDisabled(newValue == null);
            }
        );

        // Carregar os dados iniciais da primeira turma, se houver
        if (!turmas.isEmpty()) {
            turmasListView.getSelectionModel().selectFirst();
            // atualizarDetalhesTurma é chamado pelo listener de seleção
        }
        carregarDisciplinas();
        carregarProfessores();
    }
    
    private void setAulaActionButtonsDisabled(boolean disabled) {
        addAulaButton.setDisable(disabled);
        editAulaButton.setDisable(disabled);
        deleteAulaButton.setDisable(disabled);
    }
    
    public void recarregarTurmaAtual() {
        Turma turmaSelecionada = turmasListView.getSelectionModel().getSelectedItem();
        if (turmaSelecionada != null) {
            atualizarDetalhesTurma(turmaSelecionada);
        }
    }
    
    private void atualizarDetalhesTurma(Turma turma) {
        // Atualizar informações da turma
        modalidadeLabel.setText(turma.getModalidade());
        turnoLabel.setText(turma.getTurno());
        periodoLabel.setText(turma.getPeriodo());
        cursoLabel.setText(turma.getCurso().getNome());

        // Carregar todas as aulas da turma
        List<TurmaAula> relacionamentos = turmaAulaDAO.buscarPorTurma(turma.getId());
        todasAulasDaTurma = relacionamentos.stream()
                .map(TurmaAula::getAula)
                .collect(Collectors.toList());

        // Aplicar filtro de dia atual
        filtrarAulasPorDia(diaFilterComboBox.getSelectionModel().getSelectedItem());
    }
    
    private void limparDetalhesTurma() {
        modalidadeLabel.setText("");
        turnoLabel.setText("");
        periodoLabel.setText("");
        cursoLabel.setText("");
        aulasList.clear();
        todasAulasDaTurma = null;
        setAulaActionButtonsDisabled(true);
    }
    
    private void filtrarAulasPorDia(String diaSelecionado) {
        aulasList.clear();
        if (todasAulasDaTurma != null) {
            if ("Todos".equals(diaSelecionado)) {
                aulasList.addAll(todasAulasDaTurma);
            } else {
                List<Aula> aulasFiltradas = todasAulasDaTurma.stream()
                        .filter(aula -> aula.getDia().equals(diaSelecionado))
                        .collect(Collectors.toList());
                aulasList.addAll(aulasFiltradas);
            }
        }
        disciplinasTableView.refresh(); // Força o TableView a redesenhar com a lista atualizada
    }

    // Lógica de Adicionar, Editar e Excluir Aula (adaptada do AulaController)

    private void carregarProfessores() {
        professores.setAll(professorDAO.buscarTodos());
    }

    private void carregarDisciplinas() {
        disciplinas.setAll(disciplinaDAO.buscarTodas());
    }

    private boolean validarHorarios(String horarioInicio, String horarioTermino) {
        String[] inicio = horarioInicio.split(":");
        String[] termino = horarioTermino.split(":");

        int horaInicio = Integer.parseInt(inicio[0]);
        int minutoInicio = Integer.parseInt(inicio[1]);
        int horaTermino = Integer.parseInt(termino[0]);
        int minutoTermino = Integer.parseInt(termino[1]);

        // Converter para minutos para facilitar a comparação
        int totalMinutosInicio = horaInicio * 60 + minutoInicio;
        int totalMinutosTermino = horaTermino * 60 + minutoTermino;

        return totalMinutosTermino > totalMinutosInicio;
    }

    private double calcularHorasAula(String horarioInicio, String horarioTermino) {
        String[] inicio = horarioInicio.split(":");
        String[] termino = horarioTermino.split(":");

        int horaInicio = Integer.parseInt(inicio[0]);
        int minutoInicio = Integer.parseInt(inicio[1]);
        int horaTermino = Integer.parseInt(termino[0]);
        int minutoTermino = Integer.parseInt(termino[1]);

        // Converter para minutos
        int totalMinutosInicio = horaInicio * 60 + minutoInicio;
        int totalMinutosTermino = horaTermino * 60 + minutoTermino;

        // Calcular diferença em horas
        return (totalMinutosTermino - totalMinutosInicio) / 60.0;
    }

    private double calcularCargaHorariaDiaria(Professor professor, String dia, int idAulaAtual) {
        double cargaHorariaTotal = 0;
        // Aqui precisamos buscar as aulas DO PROFESSOR, não da turma atual
        // Reutilizar o método do AulaDAO
        List<Aula> aulasDoProfessor = aulaDAO.buscarAulasPorProfessorEDia(professor.getId(), dia);

        for (Aula aula : aulasDoProfessor) {
            if (aula.getId() != idAulaAtual) { // Não contar a aula atual (em caso de edição)
                double horasAula = calcularHorasAula(aula.getHorarioInicio(), aula.getHorarioTermino());
                cargaHorariaTotal += horasAula;
            }
        }
        return cargaHorariaTotal;
    }

    private boolean validarCargaHoraria(Professor professor, String dia, String horarioInicio, String horarioTermino, int idAulaAtual) {
        double horasNovaAula = calcularHorasAula(horarioInicio, horarioTermino);
        double cargaHorariaAtual = calcularCargaHorariaDiaria(professor, dia, idAulaAtual);
        double cargaHorariaTotal = cargaHorariaAtual + horasNovaAula;

        return cargaHorariaTotal <= professor.getCargaHoraria();
    }

    private boolean validarAula(Aula aula, int idAtual) {
         // Verificar conflito de horário para o professor em qualquer turma
        return !aulaDAO.existeConflitoDeHorario(aula.getProfessor(), aula.getDia(), aula.getHorarioInicio(), idAtual);
    }

    private Dialog<Aula> createAulaDialog(String title, Aula existing) {
        Dialog<Aula> dialog = new Dialog<>();
        dialog.setTitle(title);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Disciplina> disciplinaComboBox = new ComboBox<>(disciplinas);
        ComboBox<Professor> professorComboBox = new ComboBox<>(); // Popula dinamicamente
        ComboBox<String> diaComboBox = new ComboBox<>(diasDaSemana.filtered(dia -> !dia.equals("Todos"))); // Exclui "Todos"
        ComboBox<String> horarioInicioComboBox = new ComboBox<>(horariosPossiveis);
        ComboBox<String> horarioTerminoComboBox = new ComboBox<>(horariosPossiveis);

        // Configurar como os ComboBoxes exibem os itens
        disciplinaComboBox.setCellFactory(lv -> new ListCell<Disciplina>() {
            @Override
            protected void updateItem(Disciplina item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
        disciplinaComboBox.setButtonCell(new ListCell<Disciplina>() {
            @Override
            protected void updateItem(Disciplina item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        professorComboBox.setCellFactory(lv -> new ListCell<Professor>() {
            @Override
            protected void updateItem(Professor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome()); // Mostrar apenas o nome no diálogo
                }
            }
        });

        professorComboBox.setButtonCell(new ListCell<Professor>() {
            @Override
            protected void updateItem(Professor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome()); // Mostrar apenas o nome no diálogo
                }
            }
        });

        // Listener para carregar professores ao selecionar disciplina
        disciplinaComboBox.setOnAction(e -> {
            Disciplina disciplinaSelecionada = disciplinaComboBox.getValue();
            if (disciplinaSelecionada != null) {
                List<DisciplinaProfessor> relacionamentos = disciplinaProfessorDAO.buscarPorDisciplina(disciplinaSelecionada.getId());
                ObservableList<Professor> professoresDaDisciplina = FXCollections.observableArrayList();
                for (DisciplinaProfessor dp : relacionamentos) {
                    professoresDaDisciplina.add(dp.getProfessor());
                }
                professorComboBox.setItems(professoresDaDisciplina);
            } else {
                professorComboBox.setItems(FXCollections.observableArrayList());
            }
            professorComboBox.setValue(null); // Limpar seleção de professor
        });

        // Preencher diálogo com dados existentes para edição
        if (existing != null) {
            disciplinaComboBox.setValue(existing.getDisciplina());
            // Disparar o listener da disciplina para carregar os professores corretos
            disciplinaComboBox.fireEvent(new javafx.event.ActionEvent());
            professorComboBox.setValue(existing.getProfessor());
            diaComboBox.setValue(existing.getDia());
            horarioInicioComboBox.setValue(existing.getHorarioInicio());
            horarioTerminoComboBox.setValue(existing.getHorarioTermino());
        }

        VBox vbox = new VBox(10,
                new Label("Disciplina:"), disciplinaComboBox,
                new Label("Professor(a):"), professorComboBox,
                new Label("Dia:"), diaComboBox,
                new Label("Horário Início:"), horarioInicioComboBox,
                new Label("Horário Término:"), horarioTerminoComboBox
        );
        dialogPane.setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (disciplinaComboBox.getValue() == null || professorComboBox.getValue() == null ||
                        diaComboBox.getValue() == null ||
                        horarioInicioComboBox.getValue() == null ||
                        horarioTerminoComboBox.getValue() == null) {
                    mostrarAlerta("Erro", "Campos Obrigatórios", "Por favor, preencha todos os campos!");
                    return null;
                }

                return new Aula(
                        disciplinaComboBox.getValue(),
                        professorComboBox.getValue(),
                        diaComboBox.getValue(),
                        horarioInicioComboBox.getValue(),
                        horarioTerminoComboBox.getValue()
                );
            }
            return null;
        });

        return dialog;
    }

    public static void atualizarListaTurmas() {
        if (instance != null) {
            ObservableList<Turma> turmas = FXCollections.observableArrayList(instance.turmaDAO.buscarTodas());
            instance.turmasListView.setItems(turmas);
        }
    }

    @FXML
    private void showAddAulaDialog() {
        Turma turmaSelecionada = turmasListView.getSelectionModel().getSelectedItem();
        if (turmaSelecionada == null) {
            mostrarAlerta("Aviso", "Seleção Necessária", "Por favor, selecione uma turma primeiro.");
            return;
        }

        Dialog<Aula> dialog = createAulaDialog("Adicionar Aula para Turma " + turmaSelecionada.getCurso().getNome() + " - " + turmaSelecionada.getModalidade(), null);
        Optional<Aula> result = dialog.showAndWait();

        result.ifPresent(novaAula -> {
            // Validar horários
            if (!validarHorarios(novaAula.getHorarioInicio(), novaAula.getHorarioTermino())) {
                mostrarAlerta("Erro", "Horários Inválidos",
                        "O horário de término deve ser posterior ao horário de início!");
                return;
            }

            // Validar conflito de horário na mesma turma
            if (existeConflitoHorarioNaTurma(turmaSelecionada, novaAula)) {
                mostrarAlerta("Erro", "Conflito de Horário",
                        "Já existe uma aula neste horário para esta turma!");
                return;
            }

            // Validar carga horária do professor
            if (!validarCargaHoraria(novaAula.getProfessor(), novaAula.getDia(), novaAula.getHorarioInicio(), novaAula.getHorarioTermino(), 0)) {
                 mostrarAlerta("Erro", "Carga Horária Excedida",
                        String.format("Adicionar esta aula (%s - %s) excederia a carga horária diária do professor %s!",
                                novaAula.getHorarioInicio(), novaAula.getHorarioTermino(), novaAula.getProfessor().getNome()));
                return;
            }

            // Validar conflito de horário para o professor (em qualquer turma)
             if (!validarAula(novaAula, 0)) {
                 mostrarAlerta("Erro", "Conflito de Horário",
                        String.format("O professor %s já tem uma aula neste horário e dia (%s - %s)!",
                                novaAula.getProfessor().getNome(), novaAula.getDia(), novaAula.getHorarioInicio()));
                 return;
             }

            try {
                // Adicionar a Aula ao banco de dados e obter o ID gerado
                int novaAulaId = aulaDAO.adicionarAula(novaAula);

                // Criar um objeto Aula com o ID retornado para o relacionamento
                Aula aulaComId = new Aula(novaAulaId, novaAula.getDisciplina(), novaAula.getProfessor(), novaAula.getDia(), novaAula.getHorarioInicio(), novaAula.getHorarioTermino());

                // Criar e adicionar o relacionamento Turma-Aula usando a aula com ID
                TurmaAula turmaAula = new TurmaAula(turmaSelecionada, aulaComId);
                turmaAulaDAO.adicionar(turmaAula);

                // Atualizar a view
                atualizarDetalhesTurma(turmaSelecionada);
                atualizarListaTurmas(); // Atualizar a lista de turmas

            } catch (RuntimeException e) {
                mostrarAlerta("Erro", "Erro ao Adicionar Aula", e.getMessage());
            }
        });
    }

    private boolean existeConflitoHorarioNaTurma(Turma turma, Aula novaAula) {
        List<TurmaAula> aulasDaTurma = turmaAulaDAO.buscarPorTurma(turma.getId());
        
        for (TurmaAula ta : aulasDaTurma) {
            Aula aula = ta.getAula();
            if (aula.getDia().equals(novaAula.getDia())) {
                // Converter horários para minutos para facilitar a comparação
                int inicioNova = converterHorarioParaMinutos(novaAula.getHorarioInicio());
                int terminoNova = converterHorarioParaMinutos(novaAula.getHorarioTermino());
                int inicioExistente = converterHorarioParaMinutos(aula.getHorarioInicio());
                int terminoExistente = converterHorarioParaMinutos(aula.getHorarioTermino());

                // Verificar sobreposição de horários
                if ((inicioNova >= inicioExistente && inicioNova < terminoExistente) ||
                    (terminoNova > inicioExistente && terminoNova <= terminoExistente) ||
                    (inicioNova <= inicioExistente && terminoNova >= terminoExistente)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int converterHorarioParaMinutos(String horario) {
        String[] partes = horario.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        return horas * 60 + minutos;
    }

    @FXML
    private void showEditAulaDialog() {
        Turma turmaSelecionada = turmasListView.getSelectionModel().getSelectedItem();
        Aula aulaSelecionada = disciplinasTableView.getSelectionModel().getSelectedItem();
        if (aulaSelecionada == null) {
            mostrarAlerta("Aviso", "Seleção Necessária", "Por favor, selecione uma aula na tabela para editar.");
            return;
        }

         if (turmaSelecionada == null) {
             // Isso não deve acontecer se uma aula está selecionada, mas por segurança
             mostrarAlerta("Aviso", "Seleção Necessária", "Por favor, selecione uma turma na lista.");
             return;
         }

        Dialog<Aula> dialog = createAulaDialog("Editar Aula", aulaSelecionada);
        Optional<Aula> result = dialog.showAndWait();

        result.ifPresent(aulaAtualizada -> {
            // Validar horários
            if (!validarHorarios(aulaAtualizada.getHorarioInicio(), aulaAtualizada.getHorarioTermino())) {
                mostrarAlerta("Erro", "Horários Inválidos",
                        "O horário de término deve ser posterior ao horário de início!");
                return;
            }

             // Validar carga horária do professor (excluindo a aula atual da contagem)
            if (!validarCargaHoraria(aulaAtualizada.getProfessor(), aulaAtualizada.getDia(), aulaAtualizada.getHorarioInicio(), aulaAtualizada.getHorarioTermino(), aulaSelecionada.getId())) {
                 mostrarAlerta("Erro", "Carga Horária Excedida",
                        String.format("A edição desta aula (%s - %s) excederia a carga horária diária do professor %s!",
                                aulaAtualizada.getHorarioInicio(), aulaAtualizada.getHorarioTermino(), aulaAtualizada.getProfessor().getNome()));
                return;
            }

             // Validar conflito de horário para o professor (excluindo a aula atual)
             if (!validarAula(aulaAtualizada, aulaSelecionada.getId())) {
                 mostrarAlerta("Erro", "Conflito de Horário",
                        String.format("O professor %s já tem uma aula neste horário e dia (%s - %s)!",
                                aulaAtualizada.getProfessor().getNome(), aulaAtualizada.getDia(), aulaAtualizada.getHorarioInicio()));
                 return;
             }

            try {
                aulaDAO.editarAula(aulaSelecionada.getId(), aulaAtualizada);
                // Atualizar a view
                atualizarDetalhesTurma(turmaSelecionada);
            } catch (RuntimeException e) {
                 mostrarAlerta("Erro", "Erro ao Editar Aula", e.getMessage());
            }
        });
    }

    @FXML
    private void showDeleteAulaDialog() {
        Turma turmaSelecionada = turmasListView.getSelectionModel().getSelectedItem();
        Aula aulaSelecionada = disciplinasTableView.getSelectionModel().getSelectedItem();
        if (aulaSelecionada == null) {
            mostrarAlerta("Aviso", "Seleção Necessária", "Por favor, selecione uma aula na tabela para excluir.");
            return;
        }

        if (turmaSelecionada == null) {
            // Isso não deve acontecer se uma aula está selecionada, mas por segurança
            mostrarAlerta("Aviso", "Seleção Necessária", "Por favor, selecione uma turma na lista.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar Exclusão");
        confirmDialog.setHeaderText("Excluir Aula");
        confirmDialog.setContentText("Tem certeza que deseja excluir a aula " + aulaSelecionada.getDisciplina().getNome() + " - " + aulaSelecionada.getProfessor().getNome() + "?");

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Excluir a Aula do banco de dados. Isso deve remover os relacionamentos Turma-Aula via CASCADE DELETE se configurado no DB.
                // Se não houver CASCADE DELETE, seria necessário remover explicitamente os relacionamentos aqui.
                aulaDAO.deletarAula(aulaSelecionada.getId());
                // Atualizar a view
                atualizarDetalhesTurma(turmaSelecionada);
            } catch (RuntimeException e) {
                mostrarAlerta("Erro", "Erro ao Excluir Aula", e.getMessage());
            }
        }
    }
} 