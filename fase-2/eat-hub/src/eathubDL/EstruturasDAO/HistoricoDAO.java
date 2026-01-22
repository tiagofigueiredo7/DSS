package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssCadeia.Historico;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import eathubLN.ssCadeia.Estatistica;

/**
 * DAO responsável pela gestão de históricos.
 *
 * Representa o conjunto de históricos persistidos na base de dados,
 * onde a chave é o identificador do histórico (idHistorico)
 * e o valor é o respetivo objeto Historico.
 *
 * Implementa o padrão Singleton.
 */
public class HistoricoDAO implements Map<String, Historico> {
    
    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static HistoricoDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o maior ID existente na base de dados
     */
    private HistoricoDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static HistoricoDAO getInstance() {
        if (singleton == null) {
            singleton = new HistoricoDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID único para um Historico
     * 
     * @return Novo ID único para um Historico
     */
    public String generateNewId() {
        this.maxId++;
        return "H" + (maxId);
    }
    
    /** 
     * Método que carrega o maior ID existente na base de dados
     * 
     * @return Maior ID existente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("H")) {
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
     * Método que devolve o número de itens individuais num pedido
     * 
     * @param idPedido ID do pedido
     * @return Número de itens no pedido
     */
    public int getNItensPedido(String idPedido) {
        int nItens = 0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {

            ResultSet rs = stm.executeQuery("SELECT * FROM MenuPedido WHERE idPedido_FK = '" + idPedido + "'");

            while (rs.next()) {
                nItens += rs.getInt("quantidade");
            }

            ResultSet rs2 = stm.executeQuery("SELECT * FROM PropostaPedido WHERE idPedido_FK = '" + idPedido + "'");

            while (rs2.next()) {
                nItens += rs2.getInt("quantidade");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return nItens;
    }

    /** 
     * Método que devolve o valor de um ingrediente
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @return Valor do ingrediente
     */
    public double getValorIngrediente(String nomeIngrediente) {
        double valor = 0.0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {

            ResultSet rs = stm.executeQuery("SELECT preco FROM Ingrediente WHERE nome = '" + nomeIngrediente + "'");

            if (rs.next()) {
                valor = rs.getDouble("preco");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return valor;
    }

    /** 
     * Método que devolve o valor gasto numa proposta
     * 
     * @param idProposta ID da proposta
     * @return Valor gasto na proposta
     */
    public double getValorGastoProposta(String idProposta){
        double gasto = 0.0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {

            ResultSet rs = stm.executeQuery("SELECT * FROM IngredienteProposta WHERE idProposta_FK = '" + idProposta + "'");

            while (rs.next()) {
                String nomeIngr = rs.getString("nomeIngrediente_FK");
                gasto += this.getValorIngrediente(nomeIngr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return gasto;

    }

    /** 
     * Método que devolve o valor gasto num menu
     * 
     * @param codMenu Código do menu
     * @return Valor gasto no menu
     */
    public double getValorGastoMenu(String codMenu){
        double gasto = 0.0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {

            ResultSet rs = stm.executeQuery("SELECT * FROM PropostaMenu WHERE idM_FK = '" + codMenu + "'");

            while (rs.next()) {
                String idProposta = rs.getString("idProposta_FK");
                gasto += this.getValorGastoProposta(idProposta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return gasto;

    }

    /** 
     * Método que devolve o valor gasto num pedido
     * 
     * @param idPedido ID do pedido
     * @return Valor gasto no pedido
     */
    public double getValorGastoPedido(String idPedido) {
        double gasto = 0.0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {

            ResultSet rs = stm.executeQuery("SELECT * FROM MenuPedido WHERE idPedido_FK = '" + idPedido + "'");

            while (rs.next()) {
                String codMenu = rs.getString("idM_FK");
                int quantidade = rs.getInt("quantidade");
                gasto += this.getValorGastoMenu(codMenu) * quantidade;
            }

            ResultSet rs2 = stm.executeQuery("SELECT * FROM PropostaPedido WHERE idPedido_FK = '" + idPedido + "'");

            while (rs2.next()) {
                String idProposta = rs2.getString("idProposta_FK");
                int quantidade = rs2.getInt("quantidade");
                gasto += this.getValorGastoProposta(idProposta) * quantidade;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return gasto;
    }

    /** 
     * Método que calcula as estatísticas de um restaurante num determinado período
     * 
     * @param idRestaurante ID do restaurante
     * @param dataLimiteMin Data limite mínima para o cálculo das estatísticas
     * @return Estatísticas do restaurante no período especificado
     */
    public Estatistica calcularEstatisticasRestaurante(String idRestaurante, LocalDate dataLimiteMin) {
        if (dataLimiteMin == null) {
            return this.getEstatisticasFullRestaurante(idRestaurante);
        } else {

            int numeroPedidosVendidos = 0;
            double lucro = 0.0;
            int numeroItensVendidos = 0;
            double tempoMedioEntrega = 0.0;

            double valorTotal = 0.0;
            double valorGasto = 0.0;

            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                 Statement stm = conn.createStatement();) {

                ResultSet rs = stm.executeQuery("SELECT COUNT(*) AS numPedidos, AVG(p.tempoEspera) AS tempoMedioEntrega " +
                     "FROM Historico AS h " +
                     "INNER JOIN Pedido AS p ON h.idPedido_FK = p.idPedido " +
                     "WHERE h.idRestaurante_FK = '" + idRestaurante + "' AND h.dataFinalizacao >= '" + Date.valueOf(dataLimiteMin) + "'");

                if (rs.next()) {
                    numeroPedidosVendidos = rs.getInt("numPedidos");
                    tempoMedioEntrega = rs.getDouble("tempoMedioEntrega");
                }

                rs = stm.executeQuery("SELECT * FROM Historico " +
                     "WHERE idRestaurante_FK = '" + idRestaurante + "' AND dataFinalizacao >= '" + Date.valueOf(dataLimiteMin) + "'");

                while (rs.next()) {
                    String idPedido = rs.getString("idPedido_FK");
                    valorTotal += PedidoDAO.getInstance().get(idPedido).calculaValorTotalPedido();
                    numeroItensVendidos += this.getNItensPedido(idPedido);
                    valorGasto += this.getValorGastoPedido(idPedido);
                }

                lucro = valorTotal - valorGasto;
                if (lucro < 0) lucro = 0.0;
                return new Estatistica(dataLimiteMin, LocalDate.now(), lucro, numeroPedidosVendidos, numeroItensVendidos, tempoMedioEntrega);

            } catch (SQLException e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        }
    }

    /** 
     * Método que calcula as estatísticas completas de um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Estatísticas completas do restaurante
     */
    public Estatistica getEstatisticasFullRestaurante(String idRestaurante) {
        int numeroPedidosVendidos = 0;
        double lucro = 0.0;
        int numeroItensVendidos = 0;
        double tempoMedioEntrega = 0.0;
        LocalDate dataInicio = null;
        LocalDate dataFim = LocalDate.now();

        double valorTotal = 0.0;
        double valorGasto = 0.0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {

            ResultSet rs = stm.executeQuery("SELECT COUNT(*) AS numPedidos, AVG(p.tempoEspera) AS tempoMedioEntrega, MIN(h.dataFinalizacao) AS dataInicio " +
                 "FROM Historico AS h " +
                 "INNER JOIN Pedido AS p ON h.idPedido_FK = p.idPedido " +
                 "WHERE h.idRestaurante_FK = '" + idRestaurante + "'");

            if (rs.next()) {
                numeroPedidosVendidos = rs.getInt("numPedidos");
                tempoMedioEntrega = rs.getDouble("tempoMedioEntrega");
                Date dataInicioSQL = rs.getDate("dataInicio");
                if (dataInicioSQL != null) {
                    dataInicio = dataInicioSQL.toLocalDate();
                }
            }

            rs = stm.executeQuery("SELECT * FROM Historico " +
                 "WHERE idRestaurante_FK = '" + idRestaurante + "'");

            while (rs.next()) {
                String idPedido = rs.getString("idPedido_FK");
                valorTotal += PedidoDAO.getInstance().get(idPedido).calculaValorTotalPedido();
                numeroItensVendidos += this.getNItensPedido(idPedido);
                valorGasto += this.getValorGastoPedido(idPedido);
            }

            lucro = valorTotal - valorGasto;
            if (lucro < 0) lucro = 0.0;
            if (dataInicio == null) {
                dataInicio = dataFim;
            }
            return new Estatistica(dataInicio, dataFim, lucro, numeroPedidosVendidos, numeroItensVendidos, tempoMedioEntrega);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        
    }

    /** 
     * Método que calcula as estatísticas de toda a cadeia num determinado período
     * 
     * @param dataLimiteMin Data limite mínima para o cálculo das estatísticas
     * @return Estatísticas da cadeia no período especificado
     */
    public Estatistica calculaEstatisticasCadeia(LocalDate dataLimiteMin) {
        if (dataLimiteMin == null) {
            return this.getEstatisticasFullCadeia();
        } else {
            int numeroPedidosVendidos = 0;
            double lucro = 0.0;
            int numeroItensVendidos = 0;
            double tempoMedioEntrega = 0.0;

            double valorTotal = 0.0;
            double valorGasto = 0.0;

            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                 Statement stm = conn.createStatement();) {

                ResultSet rs = stm.executeQuery("SELECT COUNT(*) AS numPedidos, AVG(p.tempoEspera) AS tempoMedioEntrega " +
                     "FROM Historico AS h " +
                     "INNER JOIN Pedido AS p ON h.idPedido_FK = p.idPedido " +
                     "WHERE h.dataFinalizacao >= '" + Date.valueOf(dataLimiteMin) + "'");

                if (rs.next()) {
                    numeroPedidosVendidos = rs.getInt("numPedidos");
                    tempoMedioEntrega = rs.getDouble("tempoMedioEntrega");
                }

                rs = stm.executeQuery("SELECT * FROM Historico " +
                     "WHERE dataFinalizacao >= '" + Date.valueOf(dataLimiteMin) + "'");

                while (rs.next()) {
                    String idPedido = rs.getString("idPedido_FK");
                    valorTotal += PedidoDAO.getInstance().get(idPedido).calculaValorTotalPedido();
                    numeroItensVendidos += this.getNItensPedido(idPedido);
                    valorGasto += this.getValorGastoPedido(idPedido);
                }

                lucro = valorTotal - valorGasto;
                if (lucro < 0) lucro = 0.0;
                return new Estatistica(dataLimiteMin, LocalDate.now(), lucro, numeroPedidosVendidos, numeroItensVendidos, tempoMedioEntrega);

            } catch (SQLException e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        }
    }

    /** 
     * Método que calcula as estatísticas completas de toda a cadeia
     * 
     * @return Estatísticas completas da cadeia
     */
    public Estatistica getEstatisticasFullCadeia() {
        int numeroPedidosVendidos = 0;
        double lucro = 0.0;
        int numeroItensVendidos = 0;
        double tempoMedioEntrega = 0.0;
        LocalDate dataInicio = null;
        LocalDate dataFim = LocalDate.now();

        double valorTotal = 0.0;
        double valorGasto = 0.0;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {

            ResultSet rs = stm.executeQuery("SELECT COUNT(*) AS numPedidos, AVG(p.tempoEspera) AS tempoMedioEntrega, MIN(h.dataFinalizacao) AS dataInicio " +
                 "FROM Historico AS h " +
                 "INNER JOIN Pedido AS p ON h.idPedido_FK = p.idPedido");

            if (rs.next()) {
                numeroPedidosVendidos = rs.getInt("numPedidos");
                tempoMedioEntrega = rs.getDouble("tempoMedioEntrega");
                Date dataInicioSQL = rs.getDate("dataInicio");
                if (dataInicioSQL != null) {
                    dataInicio = dataInicioSQL.toLocalDate();
                }
            }

            rs = stm.executeQuery("SELECT * FROM Historico");

            while (rs.next()) {
                String idPedido = rs.getString("idPedido_FK");
                valorTotal += PedidoDAO.getInstance().get(idPedido).calculaValorTotalPedido();
                numeroItensVendidos += this.getNItensPedido(idPedido);
                valorGasto += this.getValorGastoPedido(idPedido);
            }

            lucro = valorTotal - valorGasto;
            if (lucro < 0) lucro = 0.0;
            if (dataInicio == null) {
                dataInicio = dataFim;
            }
            return new Estatistica(dataInicio, dataFim, lucro, numeroPedidosVendidos, numeroItensVendidos, tempoMedioEntrega);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        
    }

    /** 
     * Método que devolve a coleção de Historicos associados a um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Coleção de Historicos associados ao restaurante
     */
    public Collection<Historico> getValuesRestaurante(String idRestaurante) {
        Historico novo = null;
        Collection<Historico> historicos = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Historico WHERE idRestaurante_FK='" + idRestaurante + "'")) {
            while (rs.next()) {
                String idHistorico = rs.getString("idHistorico");
                String idPedido = rs.getString("idPedido_FK");
                Date dataFinalizacao = rs.getDate("dataFinalizacao");
                
                PedidoDAO pedidoDAO = PedidoDAO.getInstance();
                novo = new Historico(idHistorico, pedidoDAO.get(idPedido), idRestaurante, dataFinalizacao != null ? dataFinalizacao.toLocalDate() : null);
                historicos.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return historicos;
    }

    // Implementação dos métodos obrigatórios de Map

    /** 
     * Método que devolve o número de Historicos na base de dados
     * 
     * @return Número de Historicos na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Historico")) {
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
     * Método que verifica se a base de dados de Historicos está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /** 
     * Método que verifica se um determinado ID de Historico existe na base de dados
     * 
     * @param key ID do Historico
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement pstm = conn.prepareStatement("SELECT idHistorico FROM Historico WHERE idHistorico=?")) {
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
     * Método que verifica se um determinado Historico existe na base de dados
     * 
     * @param value Historico a verificar
     * @return true se o Historico existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Historico f = (Historico) value;
        return this.containsKey(f.getIdHistorico());
    }

    /** 
     * Método que devolve um Historico a partir do seu ID
     * 
     * @param key ID do Historico
     * @return Historico correspondente ao ID
     */
    @Override
    public Historico get(Object key) {
        Historico novo = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Historico WHERE idHistorico='" + key.toString() + "'")) {
            if (rs.next()) {
                String idHistorico = rs.getString("idHistorico");
                String idPedido = rs.getString("idPedido_FK");
                String idRestaurante = rs.getString("idRestaurante_FK");
                Date dataFinalizacao = rs.getDate("dataFinalizacao");

                
                PedidoDAO pedidoDAO = PedidoDAO.getInstance();
                novo = new Historico(idHistorico, pedidoDAO.get(idPedido), idRestaurante, dataFinalizacao != null ? dataFinalizacao.toLocalDate() : null);
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
     * Método que insere ou atualiza um Historico na base de dados
     * 
     * @param key ID do Historico
     * @param value Historico a inserir ou atualizar
     * @return Historico previamente associado ao ID, ou null se não existia
     */
    @Override
    public Historico put(String key, Historico value) {
        Historico res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Historico SET idPedido_FK=?, idRestaurante_FK=?, dataFinalizacao=NOW() WHERE idHistorico=?")) {
                    pstm.setString(1, value.getPedido().getCodPedido());
                    pstm.setString(2, value.getIdRestaurante());
                    pstm.setString(3, value.getIdHistorico());
                    pstm.executeUpdate();
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Historico (idHistorico, idPedido_FK, idRestaurante_FK, dataFinalizacao) VALUES (?, ?, ?, NOW())")) {
                    pstm.setString(1, value.getIdHistorico());
                    pstm.setString(2, value.getPedido().getCodPedido());
                    pstm.setString(3, value.getIdRestaurante());
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
     * Método que remove um Historico da base de dados
     * 
     * @param key ID do Historico a remover
     * @return Historico removido, ou null se não existia
     */
    @Override
    public Historico remove(Object key) {
        Historico t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Historico WHERE idHistorico='" + key.toString() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    /** 
     * Método que insere todos os Historicos de um mapa na base de dados
     * 
     * @param m Mapa de Historicos a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Historico> m) {
        for(Historico h : m.values()) {
            this.put(h.getIdHistorico(), h);
        }
    }

    /** 
     * Método que remove todos os Historicos da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Historico");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /** 
     * Método que devolve o conjunto de IDs dos Historicos na base de dados
     * 
     * @return Conjunto de IDs dos Historicos na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT IdHistorico FROM Historico")) {
            while (rs.next()) {
                res.add(rs.getString("IdHistorico"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    /** 
     * Método que devolve a coleção de Historicos na base de dados
     * 
     * @return Coleção de Historicos na base de dados
     */
    @Override
    public Collection<Historico> values() {
        Historico novo = null;
        Collection<Historico> historicos = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Historico")) {
            while (rs.next()) {
                String idHistorico = rs.getString("idHistorico");
                String idPedido = rs.getString("idPedido_FK");
                String idRestaurante = rs.getString("idRestaurante_FK");
                Date dataFinalizacao = rs.getDate("dataFinalizacao");
                
                PedidoDAO pedidoDAO = PedidoDAO.getInstance();
                novo = new Historico(idHistorico, pedidoDAO.get(idPedido), idRestaurante, dataFinalizacao != null ? dataFinalizacao.toLocalDate() : null);
                historicos.add(novo);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return historicos;
    }

    /** 
     * Método que devolve o conjunto de entradas (ID, Historico) na base de dados
     * 
     * @return Conjunto de entradas (ID, Historico) na base de dados
     */
    @Override
    public Set<Entry<String, Historico>> entrySet() {
        Historico novo = null;
        Set<Entry<String, Historico>> historicos = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Historico")) {
            while (rs.next()) {
                String idHistorico = rs.getString("idHistorico");
                String idPedido = rs.getString("idPedido_FK");
                String idRestaurante = rs.getString("idRestaurante_FK");
                Date dataFinalizacao = rs.getDate("dataFinalizacao");
                
                PedidoDAO pedidoDAO = PedidoDAO.getInstance();
                novo = new Historico(idHistorico, pedidoDAO.get(idPedido), idRestaurante, dataFinalizacao != null ? dataFinalizacao.toLocalDate() : null);
                historicos.add(new AbstractMap.SimpleEntry<>(idHistorico, novo));
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return historicos;
    }
}
