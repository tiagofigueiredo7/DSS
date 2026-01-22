CREATE database EatHub;
USE EatHub;

CREATE TABLE Pedido (
    idPedido VARCHAR(50) PRIMARY KEY,
    tipoServico VARCHAR(50),
    tempoEspera INT,
    nContribuinte INT,
    notas TEXT
);

CREATE TABLE Menu (
    idM VARCHAR(50) PRIMARY KEY,
    precoTotal DECIMAL(10,2),
    nome VARCHAR(100)
);

CREATE TABLE MenuPedido (
    idM_FK VARCHAR(50),
    idPedido_FK VARCHAR(50),
    quantidade INT,
    PRIMARY KEY (idM_FK, idPedido_FK),
    FOREIGN KEY (idM_FK) REFERENCES Menu(idM),
    FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)
);

CREATE TABLE Proposta (
    idProposta VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100),
    preco DECIMAL(10,2)
);

CREATE TABLE PropostaMenu (
    idProposta_FK VARCHAR(50),
    idM_FK VARCHAR(50),
    PRIMARY KEY (idProposta_FK, idM_FK),
    FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta),
    FOREIGN KEY (idM_FK) REFERENCES Menu(idM)
);

CREATE TABLE PropostaPedido (
    idProposta_FK VARCHAR(50),
    idPedido_FK VARCHAR(50),
    quantidade INT,
    PRIMARY KEY (idProposta_FK, idPedido_FK),
    FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta),
    FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)
);

CREATE TABLE Ingrediente (
    nome VARCHAR(100) PRIMARY KEY,
    preco DECIMAL(10,2)
);

CREATE TABLE IngredienteProposta (
    nomeIngrediente_FK VARCHAR(100),
    idProposta_FK VARCHAR(50),
    PRIMARY KEY (nomeIngrediente_FK, idProposta_FK),
    FOREIGN KEY (nomeIngrediente_FK) REFERENCES Ingrediente(nome),
    FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta)
);

CREATE TABLE Alergenio (
    alergenio VARCHAR(100) PRIMARY KEY
);

CREATE TABLE AlergenioIngrediente (
    alergenio_FK VARCHAR(100),
    nomeIngrediente_FK VARCHAR(100),
    PRIMARY KEY (nomeIngrediente_FK, alergenio_FK),
    FOREIGN KEY (alergenio_FK) REFERENCES Alergenio(alergenio),
    FOREIGN KEY (nomeIngrediente_FK) REFERENCES Ingrediente(nome)
);

CREATE TABLE Pagamento (
    idPagamento VARCHAR(50) PRIMARY KEY,
    valor DECIMAL(10,2),
    idPedido VARCHAR(50),
    metodoPagamento VARCHAR(50),
);

CREATE TABLE Fatura (
    idFatura VARCHAR(50) PRIMARY KEY,
    idPagamento_FK VARCHAR(50),
    valor DECIMAL(10,2),
    contribuinte INT,
    idPedido_FK VARCHAR(50),
    FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)
    FOREIGN KEY (idPagamento_FK) REFERENCES Pagamento(idPagamento)
);

CREATE TABLE Talao (
    idTalao VARCHAR(50) PRIMARY KEY,
    idPedido_FK VARCHAR(50),
    FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)
);

CREATE TABLE Restaurante (
    idRestaurante VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100),
    idFuncionario_FK VARCHAR(50)
);

CREATE TABLE Funcionario (
    idFuncionario VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100),
    posto VARCHAR(50),
    tarefa VARCHAR(50),
    idRestaurante_FK VARCHAR(50),
    tipo VARCHAR(50),
    FOREIGN KEY (idRestaurante_FK) REFERENCES Restaurante(idRestaurante)
);

CREATE TABLE Stock (
    nomeIngrediente_FK VARCHAR(100),
    idRestaurante_FK VARCHAR(50),
    quantidade INT,
    PRIMARY KEY (nomeIngrediente_FK, idRestaurante_FK),
    FOREIGN KEY (nomeIngrediente_FK) REFERENCES Ingrediente(nome),
    FOREIGN KEY (idRestaurante_FK) REFERENCES Restaurante(idRestaurante)
);

CREATE TABLE MensagemGestor (
    idMensagem INT AUTO_INCREMENT,
    conteudo TEXT,
    dataEnvio DATETIME,
    idFuncionario_FK VARCHAR(50),
    FOREIGN KEY (idFuncionario_FK) REFERENCES Funcionario(idFuncionario),
    PRIMARY KEY (idMensagem)
)

CREATE TABLE EtapasProposta (
    etapa VARCHAR(255),
    idProposta_FK VARCHAR(50),
    ordem INT,
    PRIMARY KEY (idProposta_FK, ordem),
    FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta)
)

CREATE TABLE Historico (
    idHistorico VARCHAR(50) PRIMARY KEY,
    idPedido_FK VARCHAR(50),
    idRestaurante_FK VARCHAR(50),
    dataFinalizacao DATETIME,
    FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido),
    FOREIGN KEY (idRestaurante_FK) REFERENCES Restaurante(idRestaurante)
)

CREATE TABLE Password (
    idFuncionario_FK VARCHAR(50) PRIMARY KEY,
    codigo VARCHAR(255),
    FOREIGN KEY (idFuncionario_FK) REFERENCES Funcionario(idFuncionario)
)