package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssComercial.Talao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * DAO responsável pela gestão de talões.
 *
 * Representa o conjunto de talões persistidos na base de dados,
 * onde a chave é o identificador do talão (idTalao)
 * e o valor é o respetivo objeto Talao.
 *
 * Implementa o padrão Singleton.
 */
public class TalaoDAO implements Map<String, Talao> {

    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static TalaoDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o maior ID existente na base de dados
     */
    private TalaoDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static TalaoDAO getInstance() {
        if (singleton == null) {
            singleton = new TalaoDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID único para um Talão
     * 
     * @return Novo ID único para um Talão
     */
    public String generateNewId() {
        this.maxId++;
        return "TAL" + (maxId);
    }
    
    /** 
     * Método que carrega o maior ID existente na base de dados
     * 
     * @return Maior ID existente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("TAL")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
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
    
    // Implementação dos métodos obrigatórios de Map
    
    /** 
     * Método que devolve o número de Talões na base de dados
     * 
     * @return Número de Talões na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Talao")) {
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
     * Método que verifica se a base de dados de Talões está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Talão existe na base de dados
     * 
     * @param key ID do Talão
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement pstm = conn.prepareStatement("SELECT idTalao FROM Talao WHERE idTalao=?")) {
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
     * Método que verifica se um determinado Talão existe na base de dados
     * 
     * @param value Talão a verificar
     * @return true se o Talão existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Talao f = (Talao) value;
        return this.containsKey(f.getIdTalao());
    }
    
    /** 
     * Método que devolve um Talão a partir do seu ID
     * 
     * @param key ID do Talão
     * @return Talão correspondente ao ID
     */
    @Override
    public Talao get(Object key) {
        Talao novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Talao WHERE idTalao='" + key.toString() + "'")) {
            if(rs.next()) {
                String idTalao = rs.getString("idTalao");
                String idPedido = rs.getString("idPedido_FK");
                novo = new Talao(idTalao, idPedido);
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
     * Método que insere ou atualiza um Talão na base de dados
     * 
     * @param key ID do Talão
     * @param value Talão a inserir ou atualizar
     * @return Talão previamente associado ao ID, ou null se não existia
     */
    @Override
    public Talao put(String key, Talao value) {
        Talao res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Talao SET idPedido_FK=? WHERE idTalao=?")) {
                    pstm.setString(1, value.getCodPedidoTalao());
                    pstm.setString(2, value.getIdTalao());
                    pstm.executeUpdate();
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Talao (idTalao, idPedido_FK) VALUES (?, ?)")) {
                    pstm.setString(1, value.getIdTalao());
                    pstm.setString(2, value.getCodPedidoTalao());
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
     * Método que remove um Talão da base de dados
     * 
     * @param key ID do Talão a remover
     * @return Talão removido, ou null se não existia
     */
    @Override
    public Talao remove(Object key) {
        Talao t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Talao WHERE idTalao='" + key.toString() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere todos os Talões de um mapa na base de dados
     * 
     * @param m Mapa de Talões a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Talao> m) {
        for(Talao t : m.values()) {
            this.put(t.getIdTalao(), t);
        }
    }
    
    /** 
     * Método que remove todos os Talões da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Talao");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs dos Talões na base de dados
     * 
     * @return Conjunto de IDs dos Talões na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idTalao FROM Talao")) {
            while (rs.next()) {
                res.add(rs.getString("idTalao"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Talões na base de dados
     * 
     * @return Coleção de Talões na base de dados
     */
    @Override
    public Collection<Talao> values() {
        Talao novo = null;
        Collection<Talao> taloes = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Talao")) {
            while (rs.next()) {
                String idTalao = rs.getString("idTalao");
                String idPedido = rs.getString("idPedido_FK");
                novo = new Talao(idTalao, idPedido);
                taloes.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return taloes;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Talão) na base de dados
     * 
     * @return Conjunto de entradas (ID, Talão) na base de dados
     */
    @Override
    public Set<Entry<String, Talao>> entrySet() {
        Talao novo = null;
        Set<Entry<String, Talao>> taloes = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Talao")) {
            while (rs.next()) {
                String idTalao = rs.getString("idTalao");
                String idPedido = rs.getString("idPedido_FK");
                novo = new Talao(idTalao, idPedido);
                taloes.add(new AbstractMap.SimpleEntry<>(idTalao, novo));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return taloes;
    }
}
