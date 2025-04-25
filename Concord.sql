drop database concord;

CREATE DATABASE IF NOT EXISTS concord;
USE concord;


GRANT ALL PRIVILEGES ON concord.* TO 'coordenador'@'localhost';


FLUSH PRIVILEGES;

CREATE TABLE professores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) Unique,
    telefone VARCHAR(20),
    matricula VARCHAR(20) unique,
    status int default 1
);

CREATE TABLE curso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sigla VARCHAR(10),
    duracao INT,
    modalidade varchar(50),
    turno varchar(50),
    descricao TEXT,
    coordenador_id INT,
    FOREIGN KEY (coordenador_id) REFERENCES professores(id)
);

CREATE TABLE aulas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    diciplina VARCHAR(100) NOT NULL,
    dia varchar(20) NOT NULL,
    horario varchar(20) NOT NULL,
    curso_id INT,
    professor_id INT,
    FOREIGN KEY (curso_id) REFERENCES curso(id),
    FOREIGN KEY (professor_id) REFERENCES professores(id)
);

INSERT INTO professores (nome, email, telefone, matricula, status) VALUES
('Adriana da Silva Jacinto', 'adriana.da@senac.local', '1198001000', 'MAT0001', 1),
('Adilson Lucimar Simões', 'adilson.lucimar@senac.local', '1198001001', 'MAT0002', 1),
('Alfred Makoto Kabayama', 'alfred.makoto@senac.local', '1198001002', 'MAT0003', 1),
('Ana Cecília Rodrigues Medeiros', 'ana.cecilia@senac.local', '1198001003', 'MAT0004', 1),
('Ana Maria Pereira', 'ana.maria@senac.local', '1198001004', 'MAT0005', 1),
('Andrea Marques de Carvalho', 'andrea.marques@senac.local', '1198001005', 'MAT0006', 1),
('Antônio Egydio São Thiago Graça', 'antonio.egydio@senac.local', '1198001006', 'MAT0007', 1),
('Antônio Josivaldo Dantas Filho', 'antonio.josivaldo@senac.local', '1198001007', 'MAT0008', 1),
('Antônio Wellington Sales Rios', 'antonio.wellington@senac.local', '1198001008', 'MAT0009', 1),
('Arlindo Strottmann', 'arlindo.strottmann@senac.local', '1198001009', 'MAT0010', 1),
('Bruno Peruchi Trevisan', 'bruno.peruchi@senac.local', '1198001010', 'MAT0011', 1),
('Carlos Augusto Lombardi Garcia', 'carlos.augusto@senac.local', '1198001011', 'MAT0012', 1),
('Carlos Eduardo Bastos', 'carlos.eduardo@senac.local', '1198001012', 'MAT0013', 1),
('Carlos Henrique Loureiro Feichas', 'carlos.henrique@senac.local', '1198001013', 'MAT0014', 1),
('Carlos Lineu de Faria e Alves', 'carlos.lineu@senac.local', '1198001014', 'MAT0015', 1),
('Cássia Cristina Bordini Cintra', 'cassia.cristina@senac.local', '1198001015', 'MAT0016', 1),
('Celso de Oliveira', 'celso.de@senac.local', '1198001016', 'MAT0017', 1),
('Cícero Soares da Silva', 'cicero.soares@senac.local', '1198001017', 'MAT0018', 1),
('Cláudio Etelvino de Lima', 'claudio.etelvino@senac.local', '1198001018', 'MAT0019', 1),
('Danielle Cristina de Morais Amorim', 'danielle.cristina@senac.local', '1198001019', 'MAT0020', 1),
('Dawilmar Guimarães de Araújo', 'dawilmar.guimaraes@senac.local', '1198001020', 'MAT0021', 1),
('Dercy Felix da Silva', 'dercy.felix@senac.local', '1198001021', 'MAT0022', 1),
('Diogo Branquinho Ramos', 'diogo.branquinho@senac.local', '1198001022', 'MAT0023', 1),
('Edmar de Queiroz Figueiredo', 'edmar.de@senac.local', '1198001023', 'MAT0024', 1),
('Eduardo Clemente Medeiros', 'eduardo.clemente@senac.local', '1198001024', 'MAT0025', 1),
('Eduardo de Castro Faustino Coelho', 'eduardo.de@senac.local', '1198001025', 'MAT0026', 1),
('Eduardo Sakaue', 'eduardo.sakaue@senac.local', '1198001026', 'MAT0027', 1),
('Eliane Penha Mergulhão Dias', 'eliane.penha@senac.local', '1198001027', 'MAT0028', 1),
('Emanuel Mineda Carneiro', 'emanuel.mineda@senac.local', '1198001028', 'MAT0029', 1),
('Érico Luciano Pagotto', 'erico.luciano@senac.local', '1198001029', 'MAT0030', 1),
('Fabiana Eloisa Passador', 'fabiana.eloisa@senac.local', '1198001030', 'MAT0031', 1),
('Fabiano Sabha Walczak', 'fabiano.sabha@senac.local', '1198001031', 'MAT0032', 1),
('Fábio José Santos de Oliveira', 'fabio.jose@senac.local', '1198001032', 'MAT0033', 1),
('Fábio Luciano Pagotto', 'fabio.luciano@senac.local', '1198001033', 'MAT0034', 1),
('Fernando Masanori Ashikaga', 'fernando.masanori@senac.local', '1198001034', 'MAT0035', 1),
('Geraldo José Lombardi de Souza', 'geraldo.jose@senac.local', '1198001035', 'MAT0036', 1),
('Gerson Carlos Favalli', 'gerson.carlos@senac.local', '1198001036', 'MAT0037', 1),
('Gerson da Penha Neto', 'gerson.da@senac.local', '1198001037', 'MAT0038', 1),
('Giuliano Araujo Bertoti', 'giuliano.araujo@senac.local', '1198001038', 'MAT0039', 1),
('Guaraci Lima de Moraes', 'guaraci.lima@senac.local', '1198001039', 'MAT0040', 1),
('Heide Heloise Bernardi', 'heide.heloise@senac.local', '1198001040', 'MAT0041', 1),
('Herculano Camargo Ortiz', 'herculano.camargo@senac.local', '1198001041', 'MAT0042', 1),
('Hudson Alberto Bode', 'hudson.alberto@senac.local', '1198001042', 'MAT0043', 1),
('Jean Carlos Lourenço Costa', 'jean.carlos@senac.local', '1198001043', 'MAT0044', 1),
('Joares Lidovino dos Reis', 'joares.lidovino@senac.local', '1198001044', 'MAT0045', 1),
('Jorge Tadao Matsushima', 'jorge.tadao@senac.local', '1198001045', 'MAT0046', 1),
('José Jaetis Rosario', 'jose.jaetis@senac.local', '1198001046', 'MAT0047', 1),
('José Walmir Gonçalves Duque', 'jose.walmir@senac.local', '1198001047', 'MAT0048', 1),
('Juliana Forin Pasquini Martinez', 'juliana.forin@senac.local', '1198001048', 'MAT0049', 1),
('Leonidas Lopes de Melo', 'leonidas.lopes@senac.local', '1198001049', 'MAT0050', 1),
('Lise Virgínia Vieira de Azevedo', 'lise.virginia@senac.local', '1198001050', 'MAT0051', 1),
('Lucas Giovanetti', 'lucas.giovanetti@senac.local', '1198001051', 'MAT0052', 1),
('Lucas Gonçalves Nadalete', 'lucas.goncalves@senac.local', '1198001052', 'MAT0053', 1),
('Luiz Alberto Nolasco Fonseca', 'luiz.alberto@senac.local', '1198001053', 'MAT0054', 1),
('Luiz Antonio Tozi', 'luiz.antonio@senac.local', '1198001054', 'MAT0055', 1),
('Manoel Roman Filho', 'manoel.roman@senac.local', '1198001055', 'MAT0056', 1),
('Marcos Allan Ferreira Gonçalves', 'marcos.allan@senac.local', '1198001056', 'MAT0057', 1),
('Marcos da Silva e Souza', 'marcos.da@senac.local', '1198001057', 'MAT0058', 1),
('Marcos Paulo Lobo de Candia', 'marcos.paulo@senac.local', '1198001058', 'MAT0059', 1),
('Marluce Gavião Sacramento Dias', 'marluce.gaviao@senac.local', '1198001059', 'MAT0060', 1),
('Martin Lugones', 'martin.lugones@senac.local', '1198001060', 'MAT0061', 1),
('Marcus Vinícius do Nascimento', 'marcus.vinicius@senac.local', '1198001061', 'MAT0062', 1),
('Maria Aparecida Miranda de Souza', 'maria.aparecida@senac.local', '1198001062', 'MAT0063', 1),
('Maria Goreti Lopes Cepinho', 'maria.goreti@senac.local', '1198001063', 'MAT0064', 1),
('Nanci de Oliveira', 'nanci.de@senac.local', '1198001064', 'MAT0065', 1),
('Newton Eizo Yamada', 'newton.eizo@senac.local', '1198001065', 'MAT0066', 1),
('Nilo Castro dos Santos', 'nilo.castro@senac.local', '1198001066', 'MAT0067', 1),
('Nilo Jeronimo Vieira', 'nilo.jeronimo@senac.local', '1198001067', 'MAT0068', 1),
('Reinaldo Fagundes dos Santos', 'reinaldo.fagundes@senac.local', '1198001068', 'MAT0069', 1),
('Reinaldo Gen Ichiro Arakaki', 'reinaldo.gen@senac.local', '1198001069', 'MAT0070', 1),
('Reinaldo Viveiros Carraro', 'reinaldo.viveiros@senac.local', '1198001070', 'MAT0071', 1),
('Renato Galvão da Silveira Mussi', 'renato.galvao@senac.local', '1198001071', 'MAT0072', 1),
('Renata Cristiane Fusverk da Silva', 'renata.cristiane@senac.local', '1198001072', 'MAT0073', 1),
('Rita de Cássia M. Sales Contini', 'rita.de@senac.local', '1198001073', 'MAT0074', 1),
('Rodrigo Elias Pereira', 'rodrigo.elias@senac.local', '1198001074', 'MAT0075', 1),
('Rogério Benedito de Andrade', 'rogerio.benedito@senac.local', '1198001075', 'MAT0076', 1),
('Roque Antonio de Moura', 'roque.antonio@senac.local', '1198001076', 'MAT0077', 1),
('Rubens Barreto da Silva', 'rubens.barreto@senac.local', '1198001077', 'MAT0078', 1),
('Samuel Martin Maresti', 'samuel.martin@senac.local', '1198001078', 'MAT0079', 1),
('Santiago Martin Lugones', 'santiago.martin@senac.local', '1198001079', 'MAT0080', 1),
('Sanzara Nhiaaia Jardim Costa Hassmann', 'sanzara.nhiaaia@senac.local', '1198001080', 'MAT0081', 1),
('Teresinha de Fátima Nogueira', 'teresinha.de@senac.local', '1198001081', 'MAT0082', 1),
('Vera Lúcia Monteiro', 'vera.lucia@senac.local', '1198001082', 'MAT0083', 1),
('Viviane Ribeiro de Siqueira', 'viviane.ribeiro@senac.local', '1198001083', 'MAT0084', 1),
('Wagner Luiz de Oliveira', 'wagner.luiz@senac.local', '1198001084', 'MAT0085', 1);

INSERT INTO curso (nome, sigla, duracao, modalidade, turno, descricao, coordenador_id) VALUES
('Análise e Desenvolvimento de Sitemas', 'ADS', 5, 'Semipresencial', 'Manhã', 'O curso de Análise e Desenvolvimento de Sitemas prepara profissionais com competências técnicas e práticas para atuar na área.', 65),
('Banco de Dados', 'BD', 6, 'Presencial', 'Noite', 'O curso de Banco de Dados prepara profissionais com competências técnicas e práticas para atuar na área.', 32),
('Desenvolvimento de Software Multiplataforma', 'DSM', 8, 'Presencial', 'Manhã/Noite', 'O curso de Desenvolvimento de Software Multiplataforma prepara profissionais com competências técnicas e práticas para atuar na área.', 37),
('Gestão de Produção Industrial', 'GPI', 7, 'Semipresencial', 'Noite', 'O curso de Gestão de Produção Industrial prepara profissionais com competências técnicas e práticas para atuar na área.', 25),
('Logística', 'L', 7, 'EAD', 'Noite', 'O curso de Logística prepara profissionais com competências técnicas e práticas para atuar na área.', 19),
('Logística', 'L', 4, 'Presencial', 'Noite', 'O curso de Logística prepara profissionais com competências técnicas e práticas para atuar na área.', 32),
('Manufatura Avançada', 'MA', 5, 'Semipresencial', 'Manhã/Noite', 'O curso de Manufatura Avançada prepara profissionais com competências técnicas e práticas para atuar na área.', 6),
('Manutenção de Aeronaves', 'MA', 8, 'EAD', 'Manhã/Noite', 'O curso de Manutenção de Aeronaves prepara profissionais com competências técnicas e práticas para atuar na área.', 9),
('Projetos de Estruturas Aeronáuticas', 'PEA', 4, 'Semipresencial', 'Manhã/Noite', 'O curso de Projetos de Estruturas Aeronáuticas prepara profissionais com competências técnicas e práticas para atuar na área.', 3);

select * from aulas;


