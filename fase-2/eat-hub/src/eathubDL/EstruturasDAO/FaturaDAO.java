package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssComercial.Fatura;
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
 * DAO responsável pela gestão de faturas.
 *
 * Representa o conjunto de faturas persistidos na base de dados,
 * onde a chave é o identificador da fatura (idFatura)
 * e o valor é o respetivo objeto Fatura.
 *
 * Implementa o padrão Singleton.
 */
public class FaturaDAO implements Map<String, Fatura> {

    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static FaturaDAO singleton = null;
    
    /**
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o valor máximo presente na base de dados
     */
    private FaturaDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /**
     * Método que devolve a instância única da classe FaturaDAO
     * 
     * @return Instância única da classe FaturaDAO
     */
    public static FaturaDAO getInstance() {
        if (singleton == null) {
            singleton = new FaturaDAO();
        }
        return singleton;
    }
    
    /**
     * Método que gera um novo ID para uma Fatura
     * 
     * @return Novo ID gerado
     */
    public String generateNewId() {
        this.maxId++;
        return "FAT" + (maxId);
    }
    
    /**
     * Método que carrega o ID máximo presente na base de dados
     * 
     * @return ID máximo presente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("FAT")) {
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
     * Método que devolve o número de faturas na base de dados
     * 
     * @return Número de faturas na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Fatura")) {
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
     * Método que devolve se a base de dados está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que devolve se a base de dados contém uma fatura com o ID dado
     * 
     * @param key ID da fatura a procurar
     * @return true se a fatura existir, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement pstm = conn.prepareStatement("SELECT IdFatura FROM Fatura WHERE IdFatura=?")) {
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
     * Método que devolve se a base de dados contém uma fatura igual à dada
     * 
     * @param value Fatura a procurar
     * @return true se a fatura existir, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Fatura f = (Fatura) value;
        return this.containsKey(f.getIdFatura());
    }
    
    /** 
     * Método que devolve a fatura com o ID dado
     * 
     * @param key ID da fatura a procurar
     * @return Fatura com o ID dado
     */
    @Override
    public Fatura get(Object key) {
        Fatura novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Fatura WHERE idFatura='" + key.toString() + "'")) {
            if (rs.next()) {
                String idFatura = rs.getString("idFatura");
                String idPagamento = rs.getString("idPagamento_FK");
                double valor = rs.getDouble("valor");
                int contribuinte = rs.getInt("contribuinte");
                String idPedido = rs.getString("idPedido_FK");
                
                PedidoDAO pedidoDAO = PedidoDAO.getInstance();
                novo = new Fatura(idFatura, idPagamento, pedidoDAO.get(idPedido), valor, contribuinte);
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
     * Método que insere ou atualiza uma fatura na base de dados
     * 
     * @param key ID da fatura a inserir/atualizar
     * @param value Fatura a inserir/atualizar
     * @return Fatura anterior com o mesmo ID, ou null se não existia
     */
    @Override
    public Fatura put(String key, Fatura value) {
        Fatura res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Fatura SET idPagamento_FK=?, valor=?, contribuinte=?, idPedido_FK=? WHERE idFatura=?")) {
                    pstm.setString(1, value.getIdPagamento());
                    pstm.setDouble(2, value.getValor());
                    pstm.setInt(3, value.getNrContribuinte());
                    pstm.setString(4, value.getPedido().getCodPedido());
                    pstm.setString(5, value.getIdFatura());
                    pstm.executeUpdate();
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Fatura (idFatura, idPagamento_FK, valor, contribuinte, idPedido_FK) VALUES (?, ?, ?, ?, ?)")) {
                    pstm.setString(1, value.getIdFatura());
                    pstm.setString(2, value.getIdPagamento());
                    pstm.setDouble(3, value.getValor());
                    pstm.setInt(4, value.getNrContribuinte());
                    pstm.setString(5, value.getPedido().getCodPedido());
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
     * Método que remove uma fatura da base de dados
     * 
     * @param key ID da fatura a remover
     * @return Fatura removida
     */
    @Override
    public Fatura remove(Object key) {
        Fatura t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Fatura WHERE idFatura='" + key.toString() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere todas as faturas de um mapa na base de dados
     * 
     * @param m Mapa com as faturas a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Fatura> m) {
        for(Fatura f : m.values()) {
            this.put(f.getIdFatura(), f);
        }
    }
    
    /** 
     * Método que remove todas as faturas da base de dados
     * 
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Fatura");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs das faturas na base de dados
     * 
     * @return Conjunto de IDs das faturas na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT IdFatura FROM Fatura")) {
            while (rs.next()) {
                res.add(rs.getString("IdFatura"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de faturas na base de dados
     * 
     * @return Coleção de faturas na base de dados
     */
    @Override
    public Collection<Fatura> values() {
        Fatura novo = null;
        Collection<Fatura> faturas = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Fatura")) {
            while (rs.next()) {
                String idFatura = rs.getString("idFatura");
                String idPagamento = rs.getString("idPagamento_FK");
                double valor = rs.getDouble("valor");
                int contribuinte = rs.getInt("contribuinte");
                String idPedido = rs.getString("idPedido_FK");
                
                PedidoDAO pedidoDAO = PedidoDAO.getInstance();
                novo = new Fatura(idFatura, idPagamento, pedidoDAO.get(idPedido), valor, contribuinte);
                faturas.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return faturas;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Fatura) na base de dados
     * 
     * @return Conjunto de entradas (ID, Fatura) na base de dados
     */
    @Override
    public Set<Entry<String, Fatura>> entrySet() {
        Fatura novo = null;
        Set<Entry<String, Fatura>> faturas = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Fatura")) {
            while (rs.next()) {
                String idFatura = rs.getString("idFatura");
                String idPagamento = rs.getString("idPagamento_FK");
                double valor = rs.getDouble("valor");
                int contribuinte = rs.getInt("contribuinte");
                String idPedido = rs.getString("idPedido_FK");
                
                PedidoDAO pedidoDAO = PedidoDAO.getInstance();
                novo = new Fatura(idFatura, idPagamento, pedidoDAO.get(idPedido), valor, contribuinte);
                faturas.add(new AbstractMap.SimpleEntry<>(idFatura, novo));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return faturas;
    }
}
