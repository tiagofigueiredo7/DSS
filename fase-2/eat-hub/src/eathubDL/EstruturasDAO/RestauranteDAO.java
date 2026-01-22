package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssCadeia.ChefeRestaurante;
import eathubLN.ssCadeia.Restaurante;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAO responsável pela gestão de restaurantes.
 *
 * Representa o conjunto de restaurantes persistidos na base de dados,
 * onde a chave é o identificador do restaurante (idRestaurante)
 * e o valor é o respetivo objeto Restaurante.
 *
 * Implementa o padrão Singleton.
 */
public class RestauranteDAO implements Map<String, Restaurante> {
    
    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static RestauranteDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o maior ID existente na base de dados
     */
    private RestauranteDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static RestauranteDAO getInstance() {
        if (singleton == null) {
            singleton = new RestauranteDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID único para um Restaurante
     * 
     * @return Novo ID único para um Restaurante
     */
    public String generateNewId() {
        this.maxId++;
        return "R" + (maxId);
    }
    
    /** 
     * Método que carrega o maior ID existente na base de dados
     * 
     * @return Maior ID existente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("R")) {
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
     * Método que atualiza o chefe de um restaurante na base de dados
     * 
     * @param idRestaurante ID do restaurante
     * @param novoChefe ID do novo chefe do restaurante
     * @throws IllegalArgumentException Se já existir um chefe de restaurante no restaurante
     */
    public void atualizaChefeRestaurante(String idRestaurante, String novoChefe){
        if (this.getChefeRestaurante(idRestaurante) != null) {
            throw new IllegalArgumentException("Já existe um chefe de restaurante no restaurante " + idRestaurante + ".");
        }
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("UPDATE Restaurante SET idFuncionario_FK='" + novoChefe + "' WHERE idRestaurante='" + idRestaurante + "'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

    }

    /** 
     * Método que devolve o chefe de um restaurante a partir do ID do restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Chefe do restaurante, ou null se não existir
     */
    public ChefeRestaurante getChefeRestaurante(String idRestaurante) {
        ChefeRestaurante chefe = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT idFuncionario_FK FROM Restaurante WHERE idRestaurante='" + idRestaurante + "'")) {
            
            String idFuncionario_FK = null;

            if(rs.next()) {
                idFuncionario_FK = rs.getString("idFuncionario_FK");
            } else {
                return null; // Restaurante não encontrado
            }

            ResultSet rss = stm.executeQuery("SELECT * FROM Funcionario WHERE idFuncionario='" + idFuncionario_FK + "'");
            if (rss.next()){
                String nomeFunc = rss.getString("nome");
                String posto = rss.getString("posto");
                String tarefa = rss.getString("tarefa");
                chefe = new ChefeRestaurante(idFuncionario_FK,nomeFunc,posto,tarefa);
            } else {
                chefe = null; // Chefe do restaurante não existe
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return chefe;
    }
    
    // Implementação dos métodos obrigatórios de Map

    /** 
     * Método que devolve o número de Restaurantes na base de dados
     * 
     * @return Número de Restaurantes na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Restaurante")) {
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
     * Método que verifica se a base de dados de Restaurantes está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Restaurante existe na base de dados
     * 
     * @param key ID do Restaurante
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement pstm = conn.prepareStatement("SELECT idRestaurante FROM Restaurante WHERE idRestaurante=?")) {
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
     * Método que verifica se um determinado Restaurante existe na base de dados
     * 
     * @param value Restaurante a verificar
     * @return true se o Restaurante existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Restaurante f = (Restaurante) value;
        return this.containsKey(f.getIdRestaurante());
    }
    
    /** 
     * Método que devolve um Restaurante a partir do seu ID
     * 
     * @param key ID do Restaurante
     * @return Restaurante correspondente ao ID
     */
    @Override
    public Restaurante get(Object key) {
        Restaurante novo = null;
        ChefeRestaurante chefe = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Restaurante WHERE idRestaurante='" + key.toString() + "'")) {
            
            String idFuncionario_FK = null;
            String nomeRes = null;

            if(rs.next()) {
                nomeRes = rs.getString("nome");
                idFuncionario_FK = rs.getString("idFuncionario_FK");
            } else {
                return null; // Restaurante não encontrado
            }

            ResultSet rss = stm.executeQuery("SELECT * FROM Funcionario WHERE idFuncionario='" + idFuncionario_FK + "'");
            if (rss.next()){
                String nomeFunc = rss.getString("nome");
                String posto = rss.getString("posto");
                String tarefa = rss.getString("tarefa");
                chefe = new ChefeRestaurante(idFuncionario_FK,nomeFunc,posto,tarefa);
            } else {
                chefe = null; // Chefe do restaurante não existe
            }

            FuncionarioDAO fdao = FuncionarioDAO.getInstance();
            Collection<String> funcionarios = fdao.getIdsFuncionariosRestaurante(key.toString());
            List<String> idsFunc = (List<String>) funcionarios;
            novo = new Restaurante(key.toString(), nomeRes, chefe, idsFunc);
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
    }
    
    /** 
     * Método que insere ou atualiza um Restaurante na base de dados
     * 
     * @param key ID do Restaurante
     * @param value Restaurante a inserir ou atualizar
     * @return Restaurante previamente associado ao ID, ou null se não existia
     */
    @Override
    public Restaurante put(String key, Restaurante value) {
        Restaurante res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Restaurante SET nome=?, idFuncionario_FK=? WHERE idRestaurante=?")) {
                    pstm.setString(1, value.getNome());
                    pstm.setString(2, value.getChefeRestaurante().getIDFunc());
                    pstm.setString(3, value.getIdRestaurante());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();
                    stm.executeUpdate("UPDATE Funcionario SET idRestaurante_FK = NULL WHERE idRestaurante_FK='" + key.toString() + "'");
                    for (String idFunc : value.getCodsFuncionarios()) {
                        stm.executeUpdate("UPDATE Funcionario SET idRestaurante_FK='" + value.getIdRestaurante() + "' WHERE idFuncionario='" + idFunc + "'");
                    }

                    String chefeAnterior = res.getChefeRestaurante().getIDFunc();
                    if (chefeAnterior != null){
                        stm.executeUpdate("UPDATE Funcionario SET tipo='Funcionario' WHERE idFuncionario='" + chefeAnterior + "'");
                    }

                    stm.executeUpdate("UPDATE Funcionario SET idRestaurante_FK='" + value.getIdRestaurante() + "', tipo='ChefeRestaurante' WHERE idFuncionario='" + value.getChefeRestaurante().getIDFunc() + "'");
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Restaurante (idRestaurante, nome, idFuncionario_FK) VALUES (?, ?, ?)")) {
                    pstm.setString(1, value.getIdRestaurante());
                    pstm.setString(2, value.getNome());
                    pstm.setString(3, value.getChefeRestaurante().getIDFunc());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();
                    for (String idFunc : value.getCodsFuncionarios()) {
                        stm.executeUpdate("UPDATE Funcionario SET idRestaurante_FK='" + value.getIdRestaurante() + "' WHERE idFuncionario='" + idFunc + "'");
                    }

                    stm.executeUpdate("UPDATE Funcionario SET idRestaurante_FK='" + value.getIdRestaurante() + "', tipo='ChefeRestaurante' WHERE idFuncionario='" + value.getChefeRestaurante().getIDFunc() + "'");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que remove um Restaurante da base de dados
     * 
     * @param key ID do Restaurante a remover
     * @return Restaurante removido, ou null se não existia
     */
    @Override
    public Restaurante remove(Object key) {
        Restaurante t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT idFuncionario_FK FROM Restaurante WHERE idRestaurante='" + key.toString() + "'");
            if (rs.next()){
                String idChefe = rs.getString("idFuncionario_FK");
                stm.executeUpdate("UPDATE Funcionario SET tipo='Funcionario', idRestaurante_FK = NULL WHERE idFuncionario='" + idChefe + "'");
            }
            stm.executeUpdate("UPDATE Funcionario SET idRestaurante_FK = NULL WHERE idRestaurante_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Stock WHERE idRestaurante_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Historico WHERE idRestaurante_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Restaurante WHERE idRestaurante='" + key.toString() + "'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere todos os Restaurantes de um mapa na base de dados
     * 
     * @param m Mapa de Restaurantes a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Restaurante> m) {
        for(Restaurante r : m.values()) {
            this.put(r.getIdRestaurante(), r);
        }
    }
    
    /** 
     * Método que remove todos os Restaurantes da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("UPDATE Funcionario SET idRestaurante_FK = NULL");
            stm.executeUpdate("UPDATE Funcionario SET tipo='Funcionario' WHERE tipo='ChefeRestaurante'");
            stm.execute("DELETE FROM Stock");
            stm.executeUpdate("DELETE FROM Historico");
            stm.executeUpdate("DELETE FROM Restaurante");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs dos Restaurantes na base de dados
     * 
     * @return Conjunto de IDs dos Restaurantes na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idRestaurante FROM Restaurante")) {
            while (rs.next()) {
                res.add(rs.getString("idRestaurante"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Restaurantes na base de dados
     * 
     * @return Coleção de Restaurantes na base de dados
     */
    @Override
    public Collection<Restaurante> values() {
        Restaurante novo = null;
        ChefeRestaurante chefe = null;
        Collection<Restaurante> restaurantes = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Restaurante")) {
            
            String idFuncionario_FK = null;
            String nomeRes = null;
            String idRestaurante = null;

            while (rs.next()) {
                idRestaurante = rs.getString("idRestaurante");
                nomeRes = rs.getString("nome");
                idFuncionario_FK = rs.getString("idFuncionario_FK");

                try (Statement stm2 = conn.createStatement();
                     ResultSet rss = stm2.executeQuery("SELECT * FROM Funcionario WHERE idFuncionario='" + idFuncionario_FK + "'")) {
                    if (rss.next()){
                        String nomeFunc = rss.getString("nome");
                        String posto = rss.getString("posto");
                        String tarefa = rss.getString("tarefa");
                        chefe = new ChefeRestaurante(idFuncionario_FK,nomeFunc,posto,tarefa);
                    } else {
                        chefe = null; // Chefe do restaurante não existe
                    }
                }

                FuncionarioDAO fdao = FuncionarioDAO.getInstance();
                Collection<String> funcionarios = fdao.getIdsFuncionariosRestaurante(idRestaurante);
                List<String> idsFunc = (List<String>) funcionarios;
                novo = new Restaurante(idRestaurante, nomeRes, chefe, idsFunc);
                restaurantes.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return restaurantes;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Restaurante) na base de dados
     * 
     * @return Conjunto de entradas (ID, Restaurante) na base de dados
     */
    @Override
    public Set<Entry<String, Restaurante>> entrySet() {
        Restaurante novo = null;
        ChefeRestaurante chefe = null;
        Set<Entry<String, Restaurante>> restaurantes = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Restaurante")) {
            
            String idFuncionario_FK = null;
            String nomeRes = null;
            String idRestaurante = null;

            while (rs.next()) {
                idRestaurante = rs.getString("idRestaurante");
                nomeRes = rs.getString("nome");
                idFuncionario_FK = rs.getString("idFuncionario_FK");

                try (Statement stm2 = conn.createStatement();
                     ResultSet rss = stm2.executeQuery("SELECT * FROM Funcionario WHERE idFuncionario='" + idFuncionario_FK + "'")) {
                    if (rss.next()){
                        String nomeFunc = rss.getString("nome");
                        String posto = rss.getString("posto");
                        String tarefa = rss.getString("tarefa");
                        chefe = new ChefeRestaurante(idFuncionario_FK,nomeFunc,posto,tarefa);
                    } else {
                        chefe = null; // Chefe do restaurante não existe
                    }
                }

                FuncionarioDAO fdao = FuncionarioDAO.getInstance();
                Collection<String> funcionarios = fdao.getIdsFuncionariosRestaurante(idRestaurante);
                List<String> idsFunc = (List<String>) funcionarios;
                novo = new Restaurante(idRestaurante, nomeRes, chefe, idsFunc);
                restaurantes.add(new AbstractMap.SimpleEntry<>(idRestaurante, novo));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return restaurantes;
    }
}
