package concord.concord.controllers;

import concord.concord.DAO.AulaDAO;


import concord.concord.Database;
import concord.concord.models.Aula;
import concord.concord.models.Professor;
import concord.concord.models.Curso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    private final ObservableList<Aula> classList = FXCollections.observableArrayList();

    private final AulaDAO aulaDAO = new AulaDAO();



    private final ObservableList<String> aulas = FXCollections.observableArrayList(
            "Administração de Banco de Dados", "Administração Geral", "Algoritmos e Lógica de Programação",
            "Arquitetura e Organização de Computadores", "Banco de Dados", "Banco de Dados – Relacional",
            "Banco de Dados - Não Relacional", "Computação em Nuvem I", "Computação em Nuvem II",
            "Comunicação e Expressão", "Comunicação Oral e Escrita", "Comércio Exterior",
            "Comércio Exterior e Logística", "Contabilidade", "Contabilidade Gerencial",
            "Custos e Tarifas Logísticas", "Custos Industriais", "Desenho", "Economia",
            "Economia e Finanças", "Economia e Finanças Empresariais", "Embalagens e Unitização",
            "Engenharia de Software I", "Engenharia de Software II", "Engenharia de Software III",
            "Ergonomia", "Espanhol I", "Espanhol II", "Estatística", "Estatística Aplicada",
            "Estatística Aplicada à Gestão", "Ética e Direito Empresarial",
            "Ética e Responsabilidade Profissional", "Ética Profissional e Patente",
            "Experiência do Usuário", "Fundamentos da Comunicação e Expressão",
            "Fundamentos da Comunicação Empresarial", "Fundamentos de Administração",
            "Fundamentos de Automação Industrial", "Fundamentos de Gestão da Qualidade",
            "Fundamentos de Gestão de Projetos", "Fundamentos de Matemática Financeira",
            "Gestão Ambiental Aplicada", "Gestão da Cadeia de Suprimentos",
            "Gestão da Produção Aplicada", "Gestão da Produção e Operações",
            "Gestão de Equipes", "Gestão de Marketing e Vendas", "Gestão de Pessoas",
            "Gestão de Projetos", "Gestão de Projetos Logísticos",
            "Gestão de Transporte de Cargas e Roteirização",
            "Gestão e Governança de Tecnologia da Informação", "Gestão Financeira",
            "Gestão Tributária nas Operações Logísticas", "Higiene e Segurança do Trabalho",
            "Informática", "Informática Aplicada à Logística", "Inglês I", "Inglês II",
            "Inglês III", "Inglês IV", "Inglês V", "Inglês VI", "Inglês para Logística I",
            "Inglês para Logística II", "Inglês para Logística III", "Inglês para Logística IV",
            "Inteligência Artificial", "Interação Humano-Computador", "Introdução à Contabilidade",
            "Jogos de Empresas", "Laboratório de Banco de Dados",
            "Laboratório de Desenvolvimento em BD I", "Laboratório de Desenvolvimento em BD II",
            "Laboratório de Desenvolvimento em BD III", "Laboratório de Desenvolvimento em BD IV",
            "Laboratório de Desenvolvimento em BD V", "Laboratório de Desenvolvimento em BD VI",
            "Laboratório de Desenvolvimento Multiplataforma", "Laboratório de Engenharia de Software",
            "Laboratório de Hardware", "Liderança e Empreendedorismo", "Linguagem de Programação",
            "Linguagem de Programação I", "Linguagem de Programação II",
            "Logística Empresarial", "Logística Verde", "Matemática Aplicada",
            "Matemática Discreta", "Matemática Financeira", "Mecânica",
            "Metodologia da Pesquisa Científica", "Metodologia da Pesquisa Científico-Tecnológica",
            "Metodologias de Desenvolvimento de Software", "Metodologias de Manutenção de Aeronaves",
            "Métodos para a Produção do Conhecimento", "Métodos Quantitativos de Gestão",
            "Modalidade e Intermodalidade", "Movimentação e Armazenagem",
            "Navegação Interior e Portos Marítimos", "Planejamento, Programação e Controle da Produção (PPCP)",
            "Planejamento Estratégico I", "Planejamento Estratégico II",
            "Processos de Produção", "Programação Avançada de Banco de Dados",
            "Programação de Scripts", "Programação em Microinformática",
            "Programação Linear e Aplicações", "Programação Orientada a Objetos",
            "Programação para Dispositivos Móveis", "Programação para Dispositivos Móveis I",
            "Programação para Dispositivos Móveis II", "Projeto de Fábrica",
            "Projeto de Produto I", "Projeto de Produto II", "Projeto de Trabalho de Graduação I",
            "Projeto de Trabalho de Graduação II", "Projeto Integrador Aplicado à Logística I",
            "Projeto Integrador Aplicado à Logística II", "Projeto Integrador Aplicado à Logística III",
            "Projeto Integrador Aplicado à Logística IV",
            "Projeto Integrador em Gestão da Produção Industrial I",
            "Projeto Integrador em Gestão da Produção Industrial II",
            "Projeto Integrador em GPI III", "Projeto Interdisciplinar VI",
            "Qualidade e Testes de Software", "Redes de Computadores",
            "Segurança da Informação", "Simulação Aplicada à Produção", "Simulação em Logística",
            "Sistemas de Informação", "Sistemas de Movimentação e Transporte",
            "Sistemas Operacionais I", "Sistemas Operacionais II", "Sociedade e Tecnologia",
            "Tecnologia da Informação Aplicada à Gestão de Operações e Processos",
            "Tecnologia da Informação Aplicada à Logística", "Tecnologia da Produção Industrial",
            "Tecnologia dos Transportes", "Tópicos Avançados em Banco de Dados",
            "Tópicos Especiais em Informática", "Transportes de Cargas Especiais",
            "Transporte Aéreo"
    );

    private final ObservableList<String> horarios = FXCollections.observableArrayList("07:10", "08:00", "09:15", "10:05", "10:55", "11:45", "18:45", "19:35", "20:35", "21:25", "22:15");
    private final ObservableList<String> dias = FXCollections.observableArrayList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sabado");

    private final ProfessorController professorController = new ProfessorController();
    private final CursoController cursoController = new CursoController();

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
        classNameCol.setCellValueFactory(new PropertyValueFactory<>("className"));
        professorCol.setCellValueFactory(new PropertyValueFactory<>("professor"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("day"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        carregarProfessoresECursos();
        carregarAulasDoBanco();
    }

    private void carregarProfessoresECursos() {
        professores.setAll(professorController.buscarTodos());
        cursos.setAll(cursoController.buscarCursos());
    }


    @FXML
    public void showAddDialog() {
        Dialog<Aula> dialog = createClassDialog("Adicionar Aula", null);
        Optional<Aula> result = dialog.showAndWait();

        result.ifPresent(aula -> {
            boolean professorOcupado = aulaDAO.professorAula(aula.getProfessor(), aula.getDay(), aula.getTime());

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
                selected.setClassName(updated.getClassName());
                selected.setProfessor(updated.getProfessor());
                selected.setCourse(updated.getCourse());
                selected.setDay(updated.getDay());
                selected.setTime(updated.getTime());
                table.refresh();
            });
        }
    }

    private boolean validarAula(Aula aula, int idAtual) {
        return !aulaDAO.existeConflitoDeHorario(aula.getProfessor(), aula.getDay(), aula.getTime(), idAtual);
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
    
        ComboBox<String> classNameComboBox = new ComboBox<>(aulas);
        ComboBox<Professor> professorComboBox = new ComboBox<>(professores);
        ComboBox<Curso> courseComboBox = new ComboBox<>(cursos);
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
                new Label("Matéria:"), classNameComboBox,
                new Label("Professor (a):"), professorComboBox,
                new Label("Curso:"), courseComboBox,
                new Label("Dia:"), dayComboBox,
                new Label("Horário:"), timeComboBox
        ));
    
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                
                if (classNameComboBox.getValue() == null || professorComboBox.getValue() == null || 
                    courseComboBox.getValue() == null || dayComboBox.getValue() == null || 
                    timeComboBox.getValue() == null) {
                    
                    mostrarAlerta("Erro", "Campos Obrigatórios", "Por favor, preencha todos os campos!");
                    return null; 
                }
                
                return new Aula(classNameComboBox.getValue(), professorComboBox.getValue(),
                                courseComboBox.getValue(), dayComboBox.getValue(), timeComboBox.getValue());
            }
            return null;
        });
    
        return dialog;
    }

}