package eathubDL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOconfig {
    public static final String USERNAME = "me";
    public static final String PASSWORD = "Mypass12345678!";
    private static final String DATABASE = "EatHub";     
    //private static final String DRIVER = "jdbc:mariadb";        // Usar para MariaDB
    private static final String DRIVER = "jdbc:mysql";        // Usar para MySQL
    public static final String URL = DRIVER+"://localhost:3306/"+DATABASE;


    public static void CreateBD(){

        String[] sqlCreates = {
        "CREATE TABLE IF NOT EXISTS Pedido (" +
            "idPedido VARCHAR(50) PRIMARY KEY," +
            "tipoServico VARCHAR(50)," +
            "tempoEspera INT," + ///Talvez mudar para DECIMAL(10,2)
            "nContribuinte INT," +
            "notas TEXT" +
        ")",

        "CREATE TABLE IF NOT EXISTS Menu (" +
            "idM VARCHAR(50) PRIMARY KEY," +
            "precoTotal DECIMAL(10,2)," +
            "nome VARCHAR(100)" +
        ")",

        "CREATE TABLE IF NOT EXISTS MenuPedido (" +
            "idM_FK VARCHAR(50)," +
            "idPedido_FK VARCHAR(50)," +
            "quantidade INT," +
            "PRIMARY KEY (idM_FK, idPedido_FK)," +
            "FOREIGN KEY (idM_FK) REFERENCES Menu(idM)," +
            "FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Proposta (" +
            "idProposta VARCHAR(50) PRIMARY KEY," +
            "nome VARCHAR(100)," +
            "preco DECIMAL(10,2)" +
        ")",

        "CREATE TABLE IF NOT EXISTS PropostaMenu (" +
            "idProposta_FK VARCHAR(50)," +
            "idM_FK VARCHAR(50)," +
            "PRIMARY KEY (idProposta_FK, idM_FK)," +
            "FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta)," +
            "FOREIGN KEY (idM_FK) REFERENCES Menu(idM)" +
        ")",

        "CREATE TABLE IF NOT EXISTS PropostaPedido (" +
            "idProposta_FK VARCHAR(50)," +
            "idPedido_FK VARCHAR(50)," +
            "quantidade INT," +
            "PRIMARY KEY (idProposta_FK, idPedido_FK)," +
            "FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta)," +
            "FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Ingrediente (" +
            "nome VARCHAR(100) PRIMARY KEY" +
        ")",

        "CREATE TABLE IF NOT EXISTS IngredienteProposta (" +
            "nomeIngrediente_FK VARCHAR(100)," +
            "idProposta_FK VARCHAR(50)," +
            "PRIMARY KEY (nomeIngrediente_FK, idProposta_FK)," +
            "FOREIGN KEY (nomeIngrediente_FK) REFERENCES Ingrediente(nome)," +
            "FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Alergenio (" +
            "alergenio VARCHAR(100) PRIMARY KEY" +
        ")",

        "CREATE TABLE IF NOT EXISTS AlergenioIngrediente (" +
            "alergenio_FK VARCHAR(100)," +
            "nomeIngrediente_FK VARCHAR(100)," +
            "PRIMARY KEY (nomeIngrediente_FK, alergenio_FK)," +
            "FOREIGN KEY (alergenio_FK) REFERENCES Alergenio(alergenio)," +
            "FOREIGN KEY (nomeIngrediente_FK) REFERENCES Ingrediente(nome)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Pagamento (" +
            "idPagamento VARCHAR(50) PRIMARY KEY," +
            "valor DECIMAL(10,2)," +
            "idPedido VARCHAR(50)," +
            "metodoPagamento VARCHAR(50)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Fatura (" +
            "idFatura VARCHAR(50) PRIMARY KEY," +
            "idPagamento_FK VARCHAR(50)," +
            "valor DECIMAL(10,2)," +
            "contribuinte INT," +
            "idPedido_FK VARCHAR(50)," +
            "FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)," +
            "FOREIGN KEY (idPagamento_FK) REFERENCES Pagamento(idPagamento)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Talao (" +
            "idTalao VARCHAR(50) PRIMARY KEY," +
            "idPedido_FK VARCHAR(50)," +
            "FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Restaurante (" +
            "idRestaurante VARCHAR(50) PRIMARY KEY," +
            "nome VARCHAR(100)," +
            "idFuncionario_FK VARCHAR(50)" +  // Sem FK inicialmente
        ")",

        "CREATE TABLE IF NOT EXISTS Funcionario (" +
            "idFuncionario VARCHAR(50) PRIMARY KEY," +
            "nome VARCHAR(100)," +
            "posto VARCHAR(50)," +
            "tarefa VARCHAR(50)," +
            "idRestaurante_FK VARCHAR(50)," +
            "tipo VARCHAR(50)," +
            "FOREIGN KEY (idRestaurante_FK) REFERENCES Restaurante(idRestaurante)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Stock (" +
            "nomeIngrediente_FK VARCHAR(100)," +
            "idRestaurante_FK VARCHAR(50)," +
            "quantidade INT," +
            "PRIMARY KEY (nomeIngrediente_FK, idRestaurante_FK)," +
            "FOREIGN KEY (nomeIngrediente_FK) REFERENCES Ingrediente(nome)," +
            "FOREIGN KEY (idRestaurante_FK) REFERENCES Restaurante(idRestaurante)" +
        ")",

        "CREATE TABLE IF NOT EXISTS MensagemGestor (" +
            "idMensagem INT AUTO_INCREMENT," +
            "conteudo TEXT," +
            "dataEnvio DATETIME," +
            "idFuncionario_FK VARCHAR(50)," +
            "FOREIGN KEY (idFuncionario_FK) REFERENCES Funcionario(idFuncionario)," +
            "PRIMARY KEY (idMensagem)" +
        ")",

        "CREATE TABLE IF NOT EXISTS EtapasProposta (" +
            "etapa VARCHAR(255)," +
            "idProposta_FK VARCHAR(50)," +
            "ordem INT," +
            "PRIMARY KEY (idProposta_FK, ordem)," +
            "FOREIGN KEY (idProposta_FK) REFERENCES Proposta(idProposta)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Historico (" +
            "idHistorico VARCHAR(50)," +
            "idPedido_FK VARCHAR(50)," +
            "idRestaurante_FK VARCHAR(50)," +
            "dataFinalizacao DATETIME," +
            "PRIMARY KEY (idHistorico)," +
            "FOREIGN KEY (idPedido_FK) REFERENCES Pedido(idPedido)," +
            "FOREIGN KEY (idRestaurante_FK) REFERENCES Restaurante(idRestaurante)" +
        ")",

        "CREATE TABLE IF NOT EXISTS Password (" +
            "idFuncionario_FK VARCHAR(50) PRIMARY KEY," +
            "codigo VARCHAR(255)," +
            "FOREIGN KEY (idFuncionario_FK) REFERENCES Funcionario(idFuncionario)" +
        ")"
    };

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            for (String sql : sqlCreates) {
                stm.executeUpdate(sql);
            }
            
        } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        // Adicionar a FK depois de ambas as tabelas existirem, apenas se não existir
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
                stm.executeUpdate(
                    "ALTER TABLE Restaurante ADD CONSTRAINT fk_restaurante_chefe " +
                    "FOREIGN KEY (idFuncionario_FK) REFERENCES Funcionario(idFuncionario)"
                );
        } catch (SQLException e) {
            // Constraint já existe, ignorar
        }
    }
}
    

