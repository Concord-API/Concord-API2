    drop database if exists concord;
    drop user if exists'coordenador'@'localhost';

    create database if not exists concord;

    use concord;


    create user if not exists 'coordenador'@'localhost' identified by 'fatec123';

    grant all privileges on concord.* to 'coordenador'@'localhost';

    flush privileges;

    CREATE TABLE professor (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        telefone VARCHAR(20),
        carga_horaria INT DEFAULT 240,
        matricula VARCHAR(20) NOT NULL UNIQUE,
        status INT DEFAULT 1
    );

    CREATE TABLE disciplina (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nome VARCHAR(100) NOT NULL
    );

    CREATE TABLE curso (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        sigla VARCHAR(10),
        duracao INT,
        descricao TEXT,
        coordenador_id INT,
        FOREIGN KEY (coordenador_id) REFERENCES professor(id)
    );
    
     CREATE TABLE disciplina_professor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    professor_id INT,
    disciplina_id INT NOT NULL,
	FOREIGN KEY (disciplina_id) REFERENCES disciplina(id),
	FOREIGN KEY (professor_id) REFERENCES professor(id)
    );

    CREATE TABLE aula (
        id INT AUTO_INCREMENT PRIMARY KEY,
        dia VARCHAR(20) NOT NULL,
        horario_inicio TIME NOT NULL,
        horario_termino TIME NOT NULL,
        disci_prof_id INT NOT NULL,
        FOREIGN KEY (disci_prof_id) REFERENCES disciplina_professor(id)
    );

    CREATE TABLE turma (
        id INT AUTO_INCREMENT PRIMARY KEY,
        modalidade VARCHAR(50),
        turno VARCHAR(50),
        periodo VARCHAR(50),
        curso_id INT,
        FOREIGN KEY (curso_id) REFERENCES curso(id)
    );

    CREATE TABLE turma_aula (
        id INT AUTO_INCREMENT PRIMARY KEY,
        turma_id INT NOT NULL,
        aula_id INT NOT NULL,
        FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
        FOREIGN KEY (aula_id) REFERENCES aula(id) ON DELETE CASCADE
    );