# Concord-API2

<div>
    <img src="./IMG/concord-logo.png" style="width:200px; height:200px;" alt="logo"/>
</div>

## 📚 Sobre o Projeto

O aplicativo **Concord Grid Manager** é um projeto voltado para a criação de um **Gerenciador de Horários de Aula**.

## 🎯 Objetivo

O objetivo do projeto é contruir uma grade que respeite os seguintes requisitos:
-  Definição de horário de aula para um semestre do curso, lembrando que cada disciplina 
possui uma quantidade de aulas obrigatória por semana. Exemplo: A disciplina 
Arquitetura e Modelagem de Banco de Dados possui 4 aulas por semana, portanto cada 
aula precisa ser definida individualmente;
-  Mecanismo para evitar sobreposição de horários (mesmo professor com disciplinas 
diferentes em um mesmo horário);
-  Sugestão de alocação, levando em conta os horários de aula já definidos;
-  Gerenciamento de semestre letivo, permitindo definir quais disciplinas fazem parte do 
semestre de um determinado curso;
-  Gerenciamento de cursos;
-  Gerenciamento de disciplinas;
-  Gerenciamento de professores;
-  Gerenciamento de horários. Apesar da hora aula ser padronizada em 50 minutos, cada 
curso pode definir o posicionamento das aulas de forma diferente.

## 🚀 Funcionalidades Atuais

-  Cadastro, edição e exclusão de disciplinas;
-  Cadastro, edição e exclusão de professores;
-  Cadastro, edição e exclusão cursos;
-  Cadastro, edição e exclusão de turmas;
-  Tela de visualização / edição das informações cadastradas;

## 🛠️ Tecnologias Utilizadas

-  JavaFX, IntelliJ, Maven, Scene builder, MySQL.

---
  
## 📌 User Story do Projeto

Histórias de usuário e as soluções implementadas para tornar o sistema funcional, organizado e fácil de usar.

### 🚀 Sprint 1 

**1. Organização da Grade**  
*Como coordenador,*  
quero montar e organizar a grade de aulas da semana,  
para que possa listar e visualizar da melhor forma.  
*Criado um sistema de distribuição de aulas.

**2. Ajustes Manuais**  
*Como coordenador,*  
quero poder fazer ajustes na grade de aulas antes de confirmar,  
para que tudo fique certo antes de divulgar para os professores e alunos.  
*Adicionada a opção de editar manualmente a grade antes de confirmar, com visualização prévia.

**3. Visualização da Grade**  
*Como coordenador,*  
quero ver a grade de aulas montada antes de finalizar,  
para que eu tenha certeza de que está tudo no lugar certo.  
*Criada uma visualização clara e organizada da grade para revisão antes da publicação.

---

### 🔧 Sprint 2 

**4. Cadastro de Professores**  
*Como coordenador,*  
quero cadastrar, editar e remover professores,  
para que eu possa manter os dados deles atualizados no sistema.  
*Desenvolvida uma tela simples para gerenciar professores com opções de adicionar, editar e excluir.

**5. Cadastro de Cursos**  
*Como coordenador,*  
quero cadastrar, editar e remover cursos,  
para que eu possa manter as informações sempre corretas.  
*Criada uma interface de cadastro de cursos, com campos editáveis e função de remoção.

**6. Aviso de Aulas Repetidas**  
*Como coordenador,*  
quero receber um aviso se eu tentar colocar um professor em duas aulas no mesmo horário,  
para que eu possa evitar erros na grade.  
*Implementada verificação automática que detecta e avisa sobre conflitos de horário no momento do agendamento.

---

### 📘 Sprint 3 

**7. Guia de Instalação**  
*Como pessoa que quer usar o sistema,*  
quero um passo a passo simples de como instalar,  
para que eu consiga usar o sistema sem dificuldades.  
*Criado um guia de instalação com todos os pré-requisitos, instruções detalhadas e exemplos.

**8. Manual de Uso**  
*Como novo usuário,*  
quero um manual fácil de entender,  
para que eu saiba como usar o sistema sem precisar de ajuda.  
*Produzido um manual simples com explicações diretas sobre como usar as principais funções.

**9. Gerenciar Horários**  
*Como coordenador,*  
quero adicionar, editar e remover horários,  
para que eu possa manter o calendário de aulas sempre certo.  
*Sistema de gerenciamento de horários adicionado com campos intuitivos e controle de validação.

**10. Gerenciar Disciplinas**  
*Como coordenador,*  
quero adicionar, editar e remover disciplinas,  
para que a lista de matérias fique sempre atualizada.  
*Tela de gerenciamento de disciplinas criada, com verificação para evitar repetições.

**11. Gerenciar o Semestre**  
*Como coordenador,*  
quero organizar as informações do semestre,  
para que tudo fique certinho com as aulas daquele período.  
*Adicionada funcionalidade para definir datas e vincular disciplinas e horários ao semestre atual.

**12. Aviso de Conflito**  
*Como coordenador,*  
quero receber um aviso se tiver algum problema com horários, aulas ou professores,  
para que eu consiga corrigir antes que afete alguém.  
*Criado sistema de alerta em tempo real para avisar sobre conflitos de horários, professores ou salas.

---

  
## 👥 Integrantes do Grupo

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="./IMG/joao baranov.jpg" width="100px;" height="130px" alt="Integrante 1"/>
        <br />
        <b>João Baranov</b>
      </td>
      <td align="center">
        <img src="./IMG/fabiano.jpg" width="100px;" height="130px" alt="Integrante 2"/>
        <br />
        <b>Fabiano Ribeiro</b>
      </td>
      <td align="center">
        <img src="./IMG/richard.jpeg" width="100px;" height="130px" alt="Integrante 3"/>
        <br />
        <b>Richard Cordeiro</b>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="./IMG/victor.jpeg" width="100px;" height="130px" alt="Integrante 4"/>
        <br />
        <b>Victor Nogueira</b>
      </td>
      <td align="center">
        <img src="./IMG/patricia.jpg" width="100px;" height="130px" alt="Integrante 5"/>
        <br />
        <b>Patricia Moraes</b>
  </td>
    </tr>
  </table>
</div>
  </table>
</div>


