package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssPedidos.Pedido;
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
 * DAO responsável pela gestão de pedidos.
 *
 * Representa o conjunto de pedidos persistidos na base de dados,
 * onde a chave é o identificador do pedido (idPedido)
 * e o valor é o respetivo objeto Pedido.
 *
 * Implementa o padrão Singleton.
 */
public class PedidoDAO implements Map<String, Pedido> {
    
    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static PedidoDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o maior ID existente na base de dados
     */
    private PedidoDAO() {
        this.maxId = loadMaxIdFromDatabase();
        // Vai buscar o maior ID ao inciar o PedidoDAO
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static PedidoDAO getInstance() {
        if (singleton == null) {
            singleton = new PedidoDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID único para um Pedido
     * 
     * @return Novo ID único para um Pedido
     */
    public String generateNewId() {
        this.maxId++;
        return "PED" + (maxId);
    }
    
    /** 
     * Método que carrega o maior ID existente na base de dados
     * 
     * @return Maior ID existente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("PED")) {
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

    /** 
     * Método que devolve o conjunto de IDs dos Restaurantes na base de dados
     * 
     * @return Conjunto de IDs dos Restaurantes na base de dados
     */
    public Set<String> getIdsRestaurantes(){
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
     * Método que devolve a coleção de códigos dos Menus associados a um Pedido
     * 
     * @param idPedido ID do Pedido
     * @return Coleção de códigos dos Menus associados ao Pedido
     */
    public Collection<String> getCodsMenusPedido(String idPedido) {
        Collection<String> codsMenus = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT idM_FK, quantidade FROM MenuPedido WHERE idPedido_FK='" + idPedido + "'")) {

            while(rs.next()) {
                int quantidade = rs.getInt("quantidade");
                String codMenu = rs.getString("idM_FK");
                while(quantidade > 0) {
                    codsMenus.add(codMenu);
                    quantidade--;
                }
            }
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return codsMenus;
    }
    
    /** 
     * Método que devolve a coleção de códigos das Propostas associadas a um Pedido
     * 
     * @param idPedido ID do Pedido
     * @return Coleção de códigos das Propostas associadas ao Pedido
     */
    public Collection<String> getCodsPropostasPedido(String idPedido) {
        Collection<String> codsP = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT idProposta_FK, quantidade FROM PropostaPedido WHERE idPedido_FK='" + idPedido + "'")) {

            while(rs.next()) {
                String codProposta = rs.getString("idProposta_FK");
                int quantidade = rs.getInt("quantidade");
                while(quantidade > 0) {
                    codsP.add(codProposta);
                    quantidade--;
                }
            }
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return codsP;
    }
    
    // Implementação dos métodos obrigatórios de Map
    
    /** 
     * Método que devolve o número de Pedidos na base de dados
     * 
     * @return Número de Pedidos na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Pedido")) {
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
     * Método que verifica se a base de dados de Pedidos está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Pedido existe na base de dados
     * 
     * @param key ID do Pedido
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement pstm = conn.prepareStatement("SELECT idPedido FROM Pedido WHERE idPedido=?")) {
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
     * Método que verifica se um determinado Pedido existe na base de dados
     * 
     * @param value Pedido
     * @return true se o Pedido existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Pedido f = (Pedido) value;
        return this.containsKey(f.getCodPedido());
    }
    
    /** 
     * Método que devolve um Pedido a partir do seu ID
     * 
     * @param key ID do Pedido
     * @return Pedido correspondente ao ID
     */
    @Override
    public Pedido get(Object key) {
        Pedido novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Pedido WHERE idPedido ='" + key.toString() + "'")) {
            if(rs.next()) {
                String tipoServico = rs.getString("tipoServico");
                double tempoEspera = rs.getDouble("tempoEspera");
                int nContribuinte = rs.getInt("nContribuinte");
                String notas = rs.getString("notas");

                List<String> codsMenus = (List<String>) this.getCodsMenusPedido(key.toString());
                List<String> codsPropostas = (List<String>) this.getCodsPropostasPedido(key.toString());
                novo = new Pedido(key.toString(), tempoEspera, nContribuinte, notas, codsMenus, codsPropostas, tipoServico);
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
     * Método que insere ou atualiza um Pedido na base de dados
     * 
     * @param key ID do Pedido
     * @param value Pedido a inserir ou atualizar
     * @return Pedido previamente associado ao ID, ou null se não existia
     */
    @Override
    public Pedido put(String key, Pedido value) {
        Pedido res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Pedido SET tipoServico=?, nContribuinte=?, notas=?, tempoEspera=? WHERE idPedido=?")) {
                    pstm.setString(1, value.getTipoPedido().toString());
                    pstm.setInt(2, value.getNrContribuinte());
                    pstm.setString(3, value.getNotas());
                    pstm.setDouble(4, value.getTempoEspera());
                    pstm.setString(5, value.getCodPedido());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();
                    stm.executeUpdate("DELETE FROM PropostaPedido WHERE idPedido_FK='" + key.toString() + "'");
                    stm.executeUpdate("DELETE FROM MenuPedido WHERE idPedido_FK='" + key.toString() + "'");

                    // Inserir os menus associados
                    for (String codMenu : value.getCodMenus()) {
                        ResultSet rs =  stm.executeQuery("SELECT quantidade FROM MenuPedido WHERE idM_FK = '" + codMenu + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        if(rs.next()) {
                            int quantidade = rs.getInt("quantidade");
                            quantidade++;
                            stm.executeUpdate("UPDATE MenuPedido SET quantidade = " + quantidade + " WHERE idM_FK = '" + codMenu + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        } else {
                            stm.executeUpdate("INSERT INTO MenuPedido (idM_FK, idPedido_FK, quantidade) VALUES ('" + codMenu + "', '" + value.getCodPedido() + "', 1)");
                        }
                    }

                    // Inserir as propostas associadas
                    for (String codProposta : value.getCodPropostas()) {
                        ResultSet rs =  stm.executeQuery("SELECT quantidade FROM PropostaPedido WHERE idProposta_FK = '" + codProposta + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        if(rs.next()) {
                            int quantidade = rs.getInt("quantidade");
                            quantidade++;
                            stm.executeUpdate("UPDATE PropostaPedido SET quantidade = " + quantidade + " WHERE idProposta_FK = '" + codProposta + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        } else {
                            stm.executeUpdate("INSERT INTO PropostaPedido (idProposta_FK, idPedido_FK, quantidade) VALUES ('" + codProposta + "', '" + value.getCodPedido() + "', 1)");
                        }
                    }
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Pedido (idPedido, tipoServico, tempoEspera, nContribuinte, notas) VALUES (?, ?, ?, ?, ?)")) {
                    pstm.setString(1, value.getCodPedido());
                    pstm.setString(2, value.getTipoPedido().toString());
                    pstm.setDouble(3, value.getTempoEspera());
                    pstm.setInt(4, value.getNrContribuinte());
                    pstm.setString(5, value.getNotas());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();

                    // Inserir os menus associados
                    for (String codMenu : value.getCodMenus()) {
                        ResultSet rs =  stm.executeQuery("SELECT quantidade FROM MenuPedido WHERE idM_FK = '" + codMenu + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        if(rs.next()) {
                            int quantidade = rs.getInt("quantidade");
                            quantidade++;
                            stm.executeUpdate("UPDATE MenuPedido SET quantidade = " + quantidade + " WHERE idM_FK = '" + codMenu + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        } else {
                            stm.executeUpdate("INSERT INTO MenuPedido (idM_FK, idPedido_FK, quantidade) VALUES ('" + codMenu + "', '" + value.getCodPedido() + "', 1)");
                        }
                    }

                    // Inserir as propostas associadas
                    for (String codProposta : value.getCodPropostas()) {
                        ResultSet rs =  stm.executeQuery("SELECT quantidade FROM PropostaPedido WHERE idProposta_FK = '" + codProposta + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        if(rs.next()) {
                            int quantidade = rs.getInt("quantidade");
                            quantidade++;
                            stm.executeUpdate("UPDATE PropostaPedido SET quantidade = " + quantidade + " WHERE idProposta_FK = '" + codProposta + "' AND idPedido_FK = '" + value.getCodPedido() + "'");
                        } else {
                            stm.executeUpdate("INSERT INTO PropostaPedido (idProposta_FK, idPedido_FK, quantidade) VALUES ('" + codProposta + "', '" + value.getCodPedido() + "', 1)");
                        }
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
     * Método que remove um Pedido da base de dados
     * 
     * @param key ID do Pedido a remover
     * @return Pedido removido, ou null se não existia
     */
    @Override
    public Pedido remove(Object key) {
        Pedido p = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT idPagamento FROM Pagamento WHERE idPedido='" + key.toString() + "'");
            if (rs.next()) {
                String idPagamento = rs.getString("idPagamento");
                stm.executeUpdate("DELETE FROM Fatura WHERE idPagamento_FK='" + idPagamento + "'");
                stm.executeUpdate("DELETE FROM Pagamento WHERE idPagamento='" + idPagamento + "'");
            }
            stm.executeUpdate("DELETE FROM Talao WHERE idPedido_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Historico WHERE idPedido_FK='" + key.toString() + "'");

            stm.executeUpdate("DELETE FROM MenuPedido WHERE idPedido_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM PropostaPedido WHERE idPedido_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Pedido WHERE idPedido='" + key.toString() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return p;
    }
    
    /** 
     * Método que insere todos os Pedidos de um mapa na base de dados
     * 
     * @param m Mapa de Pedidos a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Pedido> m) {
        for(Pedido p : m.values()) {
            this.put(p.getCodPedido(), p);
        }
    }
    
    /** 
     * Método que remove todos os Pedidos da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Fatura");
            stm.executeUpdate("DELETE FROM Pagamento");
            stm.executeUpdate("DELETE FROM Talao");
            stm.executeUpdate("DELETE FROM Historico");
            stm.executeUpdate("DELETE FROM MenuPedido");
            stm.executeUpdate("DELETE FROM PropostaPedido");
            stm.executeUpdate("DELETE FROM Pedido");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs dos Pedidos na base de dados
     * 
     * @return Conjunto de IDs dos Pedidos na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idPedido FROM Pedido")) {
            while (rs.next()) {
                res.add(rs.getString("idPedido"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Pedidos na base de dados
     * 
     * @return Coleção de Pedidos na base de dados
     */
    @Override
    public Collection<Pedido> values() {
        Pedido novo = null;
        Collection<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Pedido")) {
            while(rs.next()) {
                String idPedido = rs.getString("idPedido");
                String tipoServico = rs.getString("tipoServico");
                double tempoEspera = rs.getDouble("tempoEspera");
                int nContribuinte = rs.getInt("nContribuinte");
                String notas = rs.getString("notas");

                List<String> codsMenus = (List<String>) this.getCodsMenusPedido(idPedido);
                List<String> codsPropostas = (List<String>) this.getCodsPropostasPedido(idPedido);
                novo = new Pedido(idPedido, tempoEspera, nContribuinte, notas, codsMenus, codsPropostas, tipoServico);
                pedidos.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return pedidos;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Pedido) na base de dados
     * 
     * @return Conjunto de entradas (ID, Pedido) na base de dados
     */
    @Override
    public Set<Entry<String, Pedido>> entrySet() {
        Pedido novo = null;
        Set<Entry<String, Pedido>> pedidos = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Pedido")) {
            while(rs.next()) {
                String idPedido = rs.getString("idPedido");
                String tipoServico = rs.getString("tipoServico");
                double tempoEspera = rs.getDouble("tempoEspera");
                int nContribuinte = rs.getInt("nContribuinte");
                String notas = rs.getString("notas");

                List<String> codsMenus = (List<String>) this.getCodsMenusPedido(idPedido);
                List<String> codsPropostas = (List<String>) this.getCodsPropostasPedido(idPedido);
                novo = new Pedido(idPedido, tempoEspera, nContribuinte, notas, codsMenus, codsPropostas, tipoServico);
                pedidos.add(new AbstractMap.SimpleEntry<>(idPedido, novo));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return pedidos;
    }
}
