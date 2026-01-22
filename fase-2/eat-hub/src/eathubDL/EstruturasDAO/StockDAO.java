package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssCadeia.Pair;
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
import java.util.List;
import eathubLN.ssPedidos.Ingrediente;

/**
 * DAO responsável pela gestão de stock.
 *
 * Representa o stock de produtos por restaurante,
 * onde a chave é um par (idRestaurante, idProduto)
 * e o valor corresponde à quantidade disponível.
 *
 * Implementa o padrão Singleton.
 */
public class StockDAO implements Map<Pair<String, String>, Integer> {

    /** Instância única da classe */
    private static StockDAO singleton = null;

    /** 
     * Construtor privado para evitar instanciação externa 
    */
    private StockDAO() {
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static StockDAO getInstance() {
        if (singleton == null) {
            singleton = new StockDAO();
        }
        return singleton;
    }

    public boolean registar_Uso_Ingredientes(String idRestaurante, List<Ingrediente> lista){
        for(Ingrediente ing : lista){
            Pair<String, String> key = new Pair<>(idRestaurante, ing.getNome());
            Integer quantidadeAtual = this.get(key);
            if(quantidadeAtual == null || quantidadeAtual < 1){
                return false; // Ingrediente não existe ou quantidade insuficiente
            }
            this.put(key, quantidadeAtual - 1);
        }
        return true;

    }

    /**
     * Método que devolve o conjunto de entradas (chave, valor) do Stock de um restaurante específico
     * 
     * @param idRestaurante ID do restaurante
     * @return Conjunto de entradas (chave, valor) do Stock do restaurante
     */
    public Set<Entry<Pair<String, String>, Integer>> entrySetRestaurante(String idRestaurante) {
        int quantidade = 0;
        Set<Entry<Pair<String, String>, Integer>> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Stock WHERE idRestaurante_FK='" + idRestaurante + "'")) {
            while (rs.next()) {
                Pair<String, String> key = new Pair<>(rs.getString("idRestaurante_FK"), rs.getString("nomeIngrediente_FK"));
                quantidade = rs.getInt("quantidade");
                res.add(new AbstractMap.SimpleEntry<>(key, quantidade));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    // Implementação dos métodos obrigatórios de Map
    
    /** 
     * Método que devolve o número de Stocks na base de dados
     * 
     * @return Número de Stocks na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Stock")) {
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
     * Método que verifica se a base de dados de Stocks está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Stock existe na base de dados
     * 
     * @param key ID do Stock
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Pair)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Pair<String, String> pair = (Pair<String, String>) key;

        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement pstm = conn.prepareStatement("SELECT nomeIngrediente_FK, idRestaurante_FK FROM Stock WHERE nomeIngrediente_FK=? AND idRestaurante_FK=?")) {
            pstm.setString(1, pair.getSecond());
            pstm.setString(2, pair.getFirst());
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
     * Método que verifica se um determinado Stock existe na base de dados
     * 
     * @param value Stock a verificar
     * @return true se o Stock existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {//Nao se vai usar
        return false;
    }
    
    /** 
     * Método que devolve um Stock a partir do seu ID
     * 
     * @param key ID do Stock
     * @return Stock correspondente ao ID
     */
    @Override
    public Integer get(Object key) {
        if (!(key instanceof Pair)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Pair<String, String> pair = (Pair<String, String>) key;

        Integer quantidade = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT quantidade FROM Stock WHERE nomeIngrediente_FK='" + pair.getSecond() + "' AND idRestaurante_FK='" + pair.getFirst() + "'")) {
            if(rs.next()) {
                quantidade = rs.getInt("quantidade");
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return quantidade;
    }
    
    /** 
     * Método que insere ou atualiza um Stock na base de dados
     * 
     * @param key ID do Stock
     * @param value Stock a inserir ou atualizar
     * @return Stock previamente associado ao ID, ou null se não existia
     */
    @Override
    public Integer put(Pair<String, String> key, Integer value) {
       Integer res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Stock SET quantidade=? WHERE nomeIngrediente_FK=? AND idRestaurante_FK=?")) {
                    pstm.setInt(1, value);
                    pstm.setString(2, key.getSecond());
                    pstm.setString(3, key.getFirst());
                    pstm.executeUpdate();
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Stock (nomeIngrediente_FK, idRestaurante_FK, quantidade) VALUES (?, ?, ?)")) {
                    pstm.setString(1, key.getSecond());
                    pstm.setString(2, key.getFirst());
                    pstm.setInt(3, value);
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
     * Método que remove um Stock da base de dados
     * 
     * @param key ID do Stock a remover
     * @return Stock removido, ou null se não existia
     */
    @Override
    public Integer remove(Object key) {
        if (!(key instanceof Pair)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Pair<String, String> pair = (Pair<String, String>) key;

        int t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Stock WHERE nomeIngrediente_FK='" + pair.getSecond() + "' AND idRestaurante_FK='" + pair.getFirst() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere todos os Stocks de um mapa na base de dados
     * 
     * @param m Mapa de Stocks a inserir
     */
    @Override
    public void putAll(Map<? extends Pair<String, String>, ? extends Integer> m) {
        for(Map.Entry<? extends Pair<String, String>, ? extends Integer> e : m.entrySet()) {
            this.put(e.getKey(), e.getValue());
        }
    }
    
    /** 
     * Método que remove todos os Stocks da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Stock");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs dos Stocks na base de dados
     * 
     * @return Conjunto de IDs dos Stocks na base de dados
     */
    @Override
    public Set<Pair<String, String>> keySet() {
        Set<Pair<String, String>> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idRestaurante_FK, nomeIngrediente_FK FROM Stock")) {
            while (rs.next()) {
                res.add(new Pair<>(rs.getString("idRestaurante_FK"), rs.getString("nomeIngrediente_FK")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Stocks na base de dados
     * 
     * @return Coleção de Stocks na base de dados
     */
    @Override
    public Collection<Integer> values() {
        int quantidade = 0;
        Collection<Integer> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT quantidade FROM Stock")) {
            while (rs.next()) {
                quantidade = rs.getInt("quantidade");
                res.add(quantidade);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Stock) na base de dados
     * 
     * @return Conjunto de entradas (ID, Stock) na base de dados
     */
    @Override
    public Set<Entry<Pair<String, String>, Integer>> entrySet() {
        int quantidade = 0;
        Set<Entry<Pair<String, String>, Integer>> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Stock")) {
            while (rs.next()) {
                Pair<String, String> key = new Pair<>(rs.getString("idRestaurante_FK"), rs.getString("nomeIngrediente_FK"));
                quantidade = rs.getInt("quantidade");
                res.add(new AbstractMap.SimpleEntry<>(key, quantidade));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }



	

}
