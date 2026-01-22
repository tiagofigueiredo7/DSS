package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssCadeia.ChefeRestaurante;
import eathubLN.ssCadeia.Funcionario;
import eathubLN.ssCadeia.Gestor;
import eathubLN.ssCadeia.MensagemGestor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * DAO responsável pela gestão de funcionários.
 *
 * Representa o conjunto de funcionários persistidos na base de dados,
 * onde a chave é o identificador do funcionário (idFuncionario)
 * e o valor é o respetivo objeto Funcionario.
 *
 * Implementa o padrão Singleton.
 */
public class FuncionarioDAO implements Map<String, Funcionario> {
    
    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static FuncionarioDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o valor máximo presente na base de dados
     */
    private FuncionarioDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static FuncionarioDAO getInstance() {
        if (singleton == null) {
            singleton = new FuncionarioDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID para um Funcionario
     * 
     * @return Novo ID gerado
     */
    public String generateNewId() {
        this.maxId++;
        return "F" + (maxId);
    }
    
    /** 
     * Método que carrega o ID máximo presente na base de dados
     * 
     * @return ID máximo presente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("F")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException e) {
                    // Ignora IDs com formato inválido
                }
            }
        }
        
        return max;
    }

    /** 
     * Método que valida a password de um Funcionario
     * 
     * @param idFuncionario ID do Funcionario
     * @param password Password a validar
     * @return true se a password for válida, false caso contrário
     */
    public boolean validarPassword(String idFuncionario, String password) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT codigo FROM Password WHERE idFuncionario_FK = ?")) {
            pstm.setString(1, idFuncionario);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("codigo");
                    return storedPassword != null && storedPassword.equals(password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 
     * Método que associa uma password a um Funcionario na base de dados
     * 
     * @param idFuncionario ID do Funcionario
     * @param password Password a associar
     */
    public void associarFuncionarioPassword(String idFuncionario, String password) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO Password (idFuncionario_FK, codigo) VALUES (?, ?)")) {
            pstm.setString(1, idFuncionario);
            pstm.setString(2, password);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Método que insere um Funcionario na base de dados, atribuindo-o a um restaurante
     * 
     * @param key ID do Funcionario a inserir
     * @param value Funcionario a inserir
     * @param idRestaurante ID do restaurante ao qual o Funcionario será atribuído
     * @return Funcionario previamente associado ao ID, ou null se não existia
     */
    public Funcionario putCompleto(String key, Funcionario value, String idRestaurante) {///Usar para atribuir funcionario a restaurante
        Funcionario res = this.put(key, value);
        
        // Só atualiza o restaurante se idRestaurante não for null ou vazio
        if (idRestaurante != null && !idRestaurante.trim().isEmpty()) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
                
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Funcionario SET idRestaurante_FK=? WHERE idFuncionario=?")) {
                    pstm.setString(1, idRestaurante);
                    pstm.setString(2, value.getIDFunc());
                    pstm.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        }
        return res;
    }

    /** 
     * Método que devolve o ID do restaurante associado a um Funcionario
     * 
     * @param idFuncionario ID do Funcionario
     * @return ID do restaurante associado ao Funcionario
     */
    public String getRestauranteFuncionario(String idFuncionario) {
        String novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT idRestaurante_FK FROM Funcionario WHERE idFuncionario='" + idFuncionario + "'")) {
            if(rs.next()) {
                novo = rs.getString("idRestaurante_FK");
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
    }

    /** 
     * Método que devolve a coleção de Funcionarios associados a um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Coleção de Funcionarios associados ao restaurante
     */
    public Collection<Funcionario> getFuncionariosRestaurante(String idRestaurante) {
        Collection<Funcionario> funcs = new ArrayList<>();

        Collection<String> codsFunc = this.getIdsFuncionariosRestaurante(idRestaurante);

        for (String cod : codsFunc) {
            funcs.add(this.get(cod));
        }

        return funcs;
    }

    /** 
     * Método que devolve a coleção de IDs dos Funcionarios associados a um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Coleção de IDs dos Funcionarios associados ao restaurante
     */
    public Collection<String> getIdsFuncionariosRestaurante(String idRestaurante) {
        Collection<String> codsFunc = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT idFuncionario FROM Funcionario WHERE idRestaurante_FK='" + idRestaurante + "'")) {

            while(rs.next()) {
                String codFunc = rs.getString("idFuncionario");
                codsFunc.add(codFunc);
            }
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return codsFunc;
    }

    /** 
     * Método que devolve o Gestor presente na base de dados
     * 
     * @return Gestor presente na base de dados
     */
    public Gestor getGestor() {
        Gestor novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Funcionario WHERE tipo='Gestor'")) {
            if(rs.next()) {
                String idFuncionario = rs.getString("idFuncionario");
                String nome = rs.getString("nome");
                String posto = rs.getString("posto");
                String tarefa = rs.getString("tarefa");

                novo = new Gestor(idFuncionario, nome, posto, tarefa);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
        
    }

    /** 
     * Método que devolve a coleção de mensagens do gestor presentes na base de dados
     * 
     * @return Coleção de mensagens do gestor presentes na base de dados
     */
    public Collection<MensagemGestor> getMensagensGestor() {
        Collection<MensagemGestor> mensagens = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT conteudo, dataEnvio, idFuncionario_FK, idMensagem FROM MensagemGestor")) {

            while (rs.next()) {
                String conteudo = rs.getString("conteudo");
                Date data = rs.getDate("dataEnvio");
                String idGestor = rs.getString("idFuncionario_FK");
                int id = rs.getInt("idMensagem");
                MensagemGestor msg = new MensagemGestor(id, conteudo, data, idGestor);
                mensagens.add(msg);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return mensagens;
    }

    /** 
     * Método que envia uma mensagem para o gestor, inserindo-a na base de dados
     * 
     * @param conteudo Conteúdo da mensagem a enviar
     * @param idGestor ID do gestor a quem a mensagem será enviada
     * @return true se a mensagem foi enviada com sucesso
     */
    public boolean enviar_mensagem_gestor(String conteudo, String idGestor) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO MensagemGestor (conteudo, dataEnvio, idFuncionario_FK) VALUES (?, NOW(), ?)")) {
            pstm.setString(1, conteudo);
            pstm.setString(2, idGestor);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /** 
     * Método que remove uma mensagem do gestor da base de dados
     * 
     * @param idMensagem ID da mensagem a remover
     * @return MensagemGestor removida, ou null se não existia
     */
    public MensagemGestor remover_mensagem_gestor(int idMensagem) {
        MensagemGestor t = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Primeiro obter a mensagem
            try (PreparedStatement pstm = conn.prepareStatement("SELECT * FROM MensagemGestor WHERE idMensagem=?")) {
                pstm.setInt(1, idMensagem);
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        String conteudo = rs.getString("conteudo");
                        Date data = rs.getDate("dataEnvio");
                        String idGestor = rs.getString("idFuncionario_FK");
                        t = new MensagemGestor(idMensagem, conteudo, data, idGestor);
                    } else {
                        return null; // Mensagem não encontrada
                    }
                }
            }
            // Depois remover
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM MensagemGestor WHERE idMensagem=?")) {
                pstm.setInt(1, idMensagem);
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    // Implementação dos métodos obrigatórios de Map
    
    /** 
     * Método que devolve o número de Funcionarios na base de dados
     * 
     * @return Número de Funcionarios na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Funcionario")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }
    
    /** 
     * Método que verifica se a base de dados de Funcionarios está vazia
     * 
     * @return true se a base de dados de Funcionarios estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Funcionario existe na base de dados
     * 
     * @param key ID do Funcionario a verificar
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT idFuncionario FROM Funcionario WHERE idFuncionario=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                r = rs.next();  // A chave existe na tabela
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }
    
    /** 
     * Método que verifica se um determinado Funcionario existe na base de dados
     * 
     * @param value Funcionario a verificar
     * @return true se o Funcionario existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Funcionario f = (Funcionario) value;
        return this.containsKey(f.getIDFunc());
    }
    
    /** 
     * Método que devolve um Funcionario da base de dados
     * 
     * @param key ID do Funcionario a devolver
     * @return Funcionario correspondente ao ID, ou null se não existir
     */
    @Override
    public Funcionario get(Object key) {
        Funcionario novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Funcionario WHERE idFuncionario='" + key.toString() + "'")) {
            if(rs.next()) {
                String idFuncionario = rs.getString("idFuncionario");
                String nome = rs.getString("nome");
                String posto = rs.getString("posto");
                String tarefa = rs.getString("tarefa");
                String tipo = rs.getString("tipo");

                if (tipo.equals("Gestor")) {
                    novo = new Gestor(idFuncionario, nome, posto, tarefa);
                } else if( tipo.equals("ChefeRestaurante")) {
                    novo = new ChefeRestaurante(idFuncionario, nome, posto, tarefa);
                } else {
                    novo = new Funcionario(idFuncionario, nome, posto, tarefa);
                }
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
    }
    
    /** 
     * Método que insere um Funcionario na base de dados
     * 
     * @param key ID do Funcionario a inserir
     * @param value Funcionario a inserir
     * @return Funcionario previamente associado ao ID, ou null se não existia
     */
    @Override
    public Funcionario put(String key, Funcionario value) {///Não usar sozinho
        Funcionario res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Funcionario SET nome=?, posto=?, tarefa=?, idRestaurante_FK=NULL, tipo=? WHERE idFuncionario=?")) {
                    pstm.setString(1, value.getNomeFunc());
                    pstm.setString(2, value.getPostoFunc());
                    pstm.setString(3, value.getTarefaFunc());
                    pstm.setString(4, value.getTipoFunc());
                    pstm.setString(5, value.getIDFunc());
                    pstm.executeUpdate();
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Funcionario (idFuncionario, nome, posto, tarefa, idRestaurante_FK, tipo) VALUES (?, ?, ?, ?, NULL, ?)")) {
                    pstm.setString(1, value.getIDFunc());
                    pstm.setString(2, value.getNomeFunc());
                    pstm.setString(3, value.getPostoFunc());
                    pstm.setString(4, value.getTarefaFunc());
                    pstm.setString(5, value.getTipoFunc());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que remove um Funcionario da base de dados
     * 
     * @param key ID do Funcionario a remover
     * @return Funcionario removido, ou null se não existia
     */
    @Override
    public Funcionario remove(Object key) {
        Funcionario t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            if (t instanceof ChefeRestaurante) {
                stm.executeUpdate("UPDATE Restaurante SET idFuncionario_FK = NULL WHERE idFuncionario_FK='" + key.toString() + "'");
            }
            stm.executeUpdate("DELETE FROM Password WHERE idFuncionario_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Funcionario WHERE idFuncionario='" + key.toString() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere vários Funcionarios na base de dados
     * 
     * @param m Mapa de IDs e Funcionarios a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Funcionario> m) {
        for(Funcionario f : m.values()) {
            this.put(f.getIDFunc(), f);
        }
    }
    
    /** 
     * Método que remove todos os Funcionarios da base de dados
     * 
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM MensagemGestor");
            stm.executeUpdate("UPDATE Restaurante SET idFuncionario_FK = NULL");
            stm.executeUpdate("DELETE FROM Funcionario");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs dos Funcionarios na base de dados
     * 
     * @return Conjunto de IDs dos Funcionarios na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idFuncionario FROM Funcionario")) {
            while (rs.next()) {
                res.add(rs.getString("idFuncionario"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Funcionarios na base de dados
     * 
     * @return Coleção de Funcionarios na base de dados
     */
    @Override
    public Collection<Funcionario> values() {
        Funcionario novo = null;
        Collection<Funcionario> funcs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Funcionario")) {
            while (rs.next()) {
                String idFuncionario = rs.getString("idFuncionario");
                String nome = rs.getString("nome");
                String posto = rs.getString("posto");
                String tarefa = rs.getString("tarefa");
                String tipo = rs.getString("tipo");

                if (tipo.equals("Gestor")) {
                    novo = new Gestor(idFuncionario, nome, posto, tarefa);
                } else if( tipo.equals("ChefeRestaurante")) {
                    novo = new ChefeRestaurante(idFuncionario, nome, posto, tarefa);
                } else {
                    novo = new Funcionario(idFuncionario, nome, posto, tarefa);
                }
                funcs.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return funcs;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Funcionario) na base de dados
     * 
     * @return Conjunto de entradas (ID, Funcionario) na base de dados
     */
    @Override
    public Set<Entry<String, Funcionario>> entrySet() {
        Funcionario novo = null;
        Set<Entry<String, Funcionario>> funcs = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Funcionario")) {
            while (rs.next()) {
                String idFuncionario = rs.getString("idFuncionario");
                String nome = rs.getString("nome");
                String posto = rs.getString("posto");
                String tarefa = rs.getString("tarefa");
                String tipo = rs.getString("tipo");

                if (tipo.equals("Gestor")) {
                    novo = new Gestor(idFuncionario, nome, posto, tarefa);
                } else if( tipo.equals("ChefeRestaurante")) {
                    novo = new ChefeRestaurante(idFuncionario, nome, posto, tarefa);
                } else {
                    novo = new Funcionario(idFuncionario, nome, posto, tarefa);
                }
                funcs.add(new AbstractMap.SimpleEntry<>(idFuncionario, novo));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return funcs;
    }
}
