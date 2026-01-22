package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssPedidos.Menu;
import eathubLN.ssPedidos.Proposta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAO responsável pela gestão de menus.
 *
 * Representa o conjunto de menus persistidos na base de dados,
 * onde a chave é o identificador do menu (idMenu)
 * e o valor é o respetivo objeto Menu.
 *
 * Implementa o padrão Singleton.
 */
public class MenuDAO implements Map<String, Menu> {
    
    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static MenuDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o maior ID existente na base de dados
     */
    private MenuDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static MenuDAO getInstance() {
        if (singleton == null) {
            singleton = new MenuDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID único para um Menu
     * 
     * @return Novo ID único para um Menu
     */
    public String generateNewId() {
        this.maxId++;
        return "M" + (maxId);
    }
    
    /** 
     * Método que carrega o maior ID existente na base de dados
     * 
     * @return Maior ID existente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("M")) {
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
    
    // Implementação dos métodos obrigatórios de Map
    
    /** 
     * Método que devolve o número de Menus na base de dados
     * 
     * @return Número de Menus na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Menu")) {
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
     * Método que verifica se a base de dados de Menus está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Menu existe na base de dados
     * 
     * @param key ID do Menu
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT idM FROM Menu WHERE idM=?")) {
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
     * Método que verifica se um determinado Menu existe na base de dados
     * 
     * @param value Menu a verificar
     * @return true se o Menu existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Menu f = (Menu) value;
        return this.containsKey(f.getIdMenu());
    }
    
    /** 
     * Método que devolve um Menu a partir do seu ID
     * 
     * @param key ID do Menu
     * @return Menu correspondente ao ID
     */
    @Override
    public Menu get(Object key) {
        Menu m = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             Statement stm2 = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Menu WHERE idM='" + key + "'")) {
            if (rs.next()) {
                String idMenu = rs.getString("idM");
                String nome = rs.getString("nome");
                double precoTotal = rs.getDouble("precoTotal");

                ResultSet rs2 = stm2.executeQuery("SELECT idProposta_FK FROM PropostaMenu WHERE idM_FK='" + idMenu + "'");
                List<Proposta> propostas = new ArrayList<>();
                while (rs2.next()) {
                    String idProposta = rs2.getString("idProposta_FK");
                    Proposta p = PropostaDAO.getInstance().get(idProposta);
                    if (p != null) {
                        propostas.add(p);
                    }
                }
                m = new Menu(idMenu, nome, precoTotal, propostas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return m;
    }
    
    /** 
     * Método que insere ou atualiza um Menu na base de dados
     * 
     * @param key ID do Menu
     * @param value Menu a inserir ou atualizar
     * @return Menu previamente associado ao ID, ou null se não existia
     */
    @Override
    public Menu put(String key, Menu value) {
        Menu res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Menu SET nome=?, precoTotal=? WHERE idM=?")) {
                    pstm.setString(1, value.getNome());
                    pstm.setDouble(2, value.getPrecoTotal());
                    pstm.setString(3, value.getIdMenu());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();
                    stm.executeUpdate("DELETE FROM PropostaMenu WHERE idM_FK='" + value.getIdMenu() + "'");
                    for (Proposta p : value.getPropostasMenu()) {
                        stm.executeUpdate("INSERT INTO PropostaMenu (idProposta_FK, idM_FK) VALUES ('" + p.getIdProposta() + "', '" + value.getIdMenu() + "')");
                    }
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Menu (idM, precoTotal, nome) VALUES (?, ?, ?)")) {
                    pstm.setString(1, value.getIdMenu());
                    pstm.setDouble(2, value.getPrecoTotal());
                    pstm.setString(3, value.getNome());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();
                    for (Proposta p : value.getPropostasMenu()) {
                        stm.executeUpdate("INSERT INTO PropostaMenu (idProposta_FK, idM_FK) VALUES ('" + p.getIdProposta() + "', '" + value.getIdMenu() + "')");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que remove um Menu da base de dados
     * 
     * @param key ID do Menu a remover
     * @return Menu removido, ou null se não existia
     */
    @Override
    public Menu remove(Object key) {
        Menu t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM PropostaMenu WHERE idM_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Menu WHERE idM='" + key.toString() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere todos os Menus de um mapa na base de dados
     * 
     * @param m Mapa de Menus a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Menu> m) {
        for(Menu menu : m.values()) {
            this.put(menu.getIdMenu(), menu);
        }
    }
    
    /** 
     * Método que remove todos os Menus da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM PropostaMenu");
            stm.executeUpdate("DELETE FROM Menu");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs dos Menus na base de dados
     * 
     * @return Conjunto de IDs dos Menus na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idM FROM Menu")) {
            while (rs.next()) {
                res.add(rs.getString("idM"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Menus na base de dados
     * 
     * @return Coleção de Menus na base de dados
     */
    @Override
    public Collection<Menu> values() {
        Menu m = null;
        Collection<Menu> novo = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             Statement stm2 = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Menu")) {
            while (rs.next()) {
                String idMenu = rs.getString("idM");
                String nome = rs.getString("nome");
                double precoTotal = rs.getDouble("precoTotal");

                ResultSet rs2 = stm2.executeQuery("SELECT idProposta_FK FROM PropostaMenu WHERE idM_FK='" + idMenu + "'");
                List<Proposta> propostas = new ArrayList<>();
                while (rs2.next()) {
                    String idProposta = rs2.getString("idProposta_FK");
                    Proposta p = PropostaDAO.getInstance().get(idProposta);
                    if (p != null) {
                        propostas.add(p);
                    }
                }
                m = new Menu(idMenu, nome, precoTotal, propostas);
                novo.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Menu) na base de dados
     * 
     * @return Conjunto de entradas (ID, Menu) na base de dados
     */
    @Override
    public Set<Entry<String, Menu>> entrySet() {
        Menu m = null;
        Set<Entry<String, Menu>> novo = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             Statement stm2 = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Menu")) {
            while (rs.next()) {
                String idMenu = rs.getString("idM");
                String nome = rs.getString("nome");
                double precoTotal = rs.getDouble("precoTotal");

                ResultSet rs2 = stm2.executeQuery("SELECT idProposta_FK FROM PropostaMenu WHERE idM_FK='" + idMenu + "'");
                List<Proposta> propostas = new ArrayList<>();
                while (rs2.next()) {
                    String idProposta = rs2.getString("idProposta_FK");
                    Proposta p = PropostaDAO.getInstance().get(idProposta);
                    if (p != null) {
                        propostas.add(p);
                    }
                }
                m = new Menu(idMenu, nome, precoTotal, propostas);
                novo.add(new AbstractMap.SimpleEntry<>(idMenu, m));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
    }
}
