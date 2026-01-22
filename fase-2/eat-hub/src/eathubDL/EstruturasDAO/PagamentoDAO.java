package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssComercial.Pagamento;
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
 * DAO responsável pela gestão de pagamentos.
 *
 * Representa o conjunto de pagamentos persistidos na base de dados,
 * onde a chave é o identificador do pagamento (idPagamento)
 * e o valor é o respetivo objeto Pagamento.
 *
 * Implementa o padrão Singleton.
 */
public class PagamentoDAO implements Map<String, Pagamento>{
    
    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static PagamentoDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o maior ID existente na base de dados
     */
    private PagamentoDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static PagamentoDAO getInstance() {
        if (singleton == null) {
            singleton = new PagamentoDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID único para um Pagamento
     * 
     * @return Novo ID único para um Pagamento
     */
    public String generateNewId() {
        this.maxId++;
        return "PAG" + (maxId);
    }
    
    /** 
     * Método que carrega o maior ID existente na base de dados
     * 
     * @return Maior ID existente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("PAG")) {
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
     * Método que devolve o número de Pagamentos na base de dados
     * 
     * @return Número de Pagamentos na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Pagamento")) {
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
     * Método que verifica se a base de dados de Pagamentos está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Pagamento existe na base de dados
     * 
     * @param key ID do Pagamento
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT IdPagamento FROM Pagamento WHERE IdPagamento=?")) {
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
     * Método que verifica se um determinado Pagamento existe na base de dados
     * 
     * @param value Pagamento a verificar
     * @return true se o Pagamento existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Pagamento f = (Pagamento) value;
        return this.containsKey(f.getIdPagamento());
    }
    
    /** 
     * Método que devolve um Pagamento a partir do seu ID
     * 
     * @param key ID do Pagamento
     * @return Pagamento correspondente ao ID
     */
    @Override
    public Pagamento get(Object key) {
        Pagamento novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Pagamento WHERE idPagamento ='" + key.toString() + "'")) {
            if(rs.next()) {
                String idPagamento = rs.getString("idPagamento");
                double valor = rs.getDouble("valor");
                String idPedido = rs.getString("idPedido");
                String metodoPagamento = rs.getString("metodoPagamento");
                novo = new Pagamento(idPagamento,valor,idPedido,metodoPagamento);
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
     * Método que insere ou atualiza um Pagamento na base de dados
     * 
     * @param key ID do Pagamento
     * @param value Pagamento a inserir ou atualizar
     * @return Pagamento previamente associado ao ID, ou null se não existia
     */
    @Override
    public Pagamento put(String key, Pagamento value) {
        Pagamento res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Pagamento SET valor=?, idPedido=?, metodoPagamento=? WHERE idPagamento=?")) {
                    pstm.setDouble(1, value.getValorPagamento());
                    pstm.setString(2, value.getIdPedido());
                    pstm.setString(3, value.getMetodoPagamento().toString());
                    pstm.setString(4, value.getIdPagamento());
                    pstm.executeUpdate();
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Pagamento (idPagamento, valor, idPedido, metodoPagamento) VALUES (?, ?, ?, ?)")) {
                    pstm.setString(1, value.getIdPagamento());
                    pstm.setDouble(2, value.getValorPagamento());
                    pstm.setString(3, value.getIdPedido());
                    pstm.setString(4, value.getMetodoPagamento().toString());
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
     * Método que remove um Pagamento da base de dados
     * 
     * @param key ID do Pagamento a remover
     * @return Pagamento removido, ou null se não existia
     */
    @Override
    public Pagamento remove(Object key) {
        Pagamento t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Fatura WHERE idPagamento_FK='" + key.toString() + "'"); 
            stm.executeUpdate("DELETE FROM Pagamento WHERE idPagamento='" + key.toString() + "'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere todos os Pagamentos de um mapa na base de dados
     * 
     * @param m Mapa de Pagamentos a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Pagamento> m) {
        for(Pagamento p : m.values()) {
            this.put(p.getIdPagamento(), p);
        }
    }
    
    /** 
     * Método que remove todos os Pagamentos da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Fatura");
            stm.executeUpdate("DELETE FROM Pagamento");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs dos Pagamentos na base de dados
     * 
     * @return Conjunto de IDs dos Pagamentos na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idPagamento FROM Pagamento")) {
            while (rs.next()) {
                res.add(rs.getString("idPagamento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Pagamentos na base de dados
     * 
     * @return Coleção de Pagamentos na base de dados
     */
    @Override
    public Collection<Pagamento> values() {
        Pagamento novo = null;
        Collection<Pagamento> pagamentos = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Pagamento")) {
            while (rs.next()) {
                String idPagamento = rs.getString("idPagamento");
                double valor = rs.getDouble("valor");
                String idPedido = rs.getString("idPedido");
                String metodoPagamento = rs.getString("metodoPagamento");
                novo = new Pagamento(idPagamento,valor,idPedido,metodoPagamento);
                pagamentos.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return pagamentos;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Pagamento) na base de dados
     * 
     * @return Conjunto de entradas (ID, Pagamento) na base de dados
     */
    @Override
    public Set<Entry<String, Pagamento>> entrySet() {
        Pagamento novo = null;
        Set<Entry<String, Pagamento>> pagamentos = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Pagamento")) {
            while (rs.next()) {
                String idPagamento = rs.getString("idPagamento");
                double valor = rs.getDouble("valor");
                String idPedido = rs.getString("idPedido");
                String metodoPagamento = rs.getString("metodoPagamento");
                novo = new Pagamento(idPagamento,valor,idPedido,metodoPagamento);
                pagamentos.add(new AbstractMap.SimpleEntry<>(idPagamento, novo));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return pagamentos;
    }
}
