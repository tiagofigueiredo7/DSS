package eathubDL.EstruturasDAO;

import eathubDL.DAOconfig;
import eathubLN.ssPedidos.Ingrediente;
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
 * DAO responsável pela gestão de propostas.
 *
 * Representa o conjunto de propostas persistidas na base de dados,
 * onde a chave é o identificador da proposta (idProposta)
 * e o valor é o respetivo objeto Proposta.
 *
 * Implementa o padrão Singleton.
 */
public class PropostaDAO implements Map<String, Proposta> {
    
    /** ID máximo guardado */
    private int maxId;

    /** Instância única da classe */
    private static PropostaDAO singleton = null;
    
    /** 
     * Construtor privado para evitar instanciação externa
     * 
     * Inicializa o maxId com o maior ID existente na base de dados
     */
    private PropostaDAO() {
        this.maxId = loadMaxIdFromDatabase();
    }

    /** 
     * Método que devolve a instância única da classe
     * 
     * @return Instância única da classe
     */
    public static PropostaDAO getInstance() {
        if (singleton == null) {
            singleton = new PropostaDAO();
        }
        return singleton;
    }
    
    /** 
     * Método que gera um novo ID único para uma Proposta
     * 
     * @return Novo ID único para uma Proposta
     */
    public String generateNewId() {
        this.maxId++;
        return "PROP" + (maxId);
    }
    
    /** 
     * Método que carrega o maior ID existente na base de dados
     * 
     * @return Maior ID existente na base de dados
     */
    private int loadMaxIdFromDatabase() {
        int max = 0;
        for (String id : this.keySet()) {
            if (id.startsWith("PROP")) {
                try {
                    int num = Integer.parseInt(id.substring(4));
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
     * Método que adiciona um alergenio à base de dados
     * 
     * @param alergenio Alergenio a adicionar
     */
    public void addAlergenio(String alergenio){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO Alergenio (alergenio) VALUES (?)")) {
            pstm.setString(1, alergenio);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Método que remove um alergenio da base de dados
     * 
     * @param alergenio Alergenio a remover
     * @return Alergenio removido
     */
    public String removerAlergenio(String alergenio){
        String res = this.getAlergenio(alergenio);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Alergenio WHERE alergenio=?")) {
            pstm.setString(1, alergenio);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return res;
    }

    /** 
     * Método que devolve um alergenio da base de dados
     * 
     * @param alergenio Alergenio a procurar
     * @return Alergenio encontrado, ou null se não existir
     */
    public String getAlergenio(String alergenio){
        String res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT alergenio FROM Alergenio WHERE alergenio=?")) {
            pstm.setString(1, alergenio);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    res = rs.getString("alergenio");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    /** 
     * Método que devolve a lista de alergenios da base de dados
     * 
     * @return Lista de alergenios da base de dados
     */
    public List<String> getAlergenios(){
        List<String> alergenios = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT alergenio FROM Alergenio")) {
            while (rs.next()) {
                String alergenio = rs.getString("alergenio");
                alergenios.add(alergenio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return alergenios;
    }

    /** 
     * Método que adiciona um ingrediente à base de dados
     * 
     * @param nome Nome do ingrediente a adicionar
     * @param preco Preço do ingrediente
     */
    public void addIngrediente(String nome, double preco){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO Ingrediente (nome, preco) VALUES (?, ?)")) {
            pstm.setString(1, nome);
            pstm.setDouble(2, preco);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /** 
     * Método que adiciona um ingrediente e os seus alergenios à base de dados
     * 
     * @param nome Nome do ingrediente a adicionar
     * @param alergenios Lista de alergenios do ingrediente a adicionar
     * @param preco Preço do ingrediente
     */
    public void addIngredienteEAlergenios(String nome, List<String> alergenios, double preco){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO Ingrediente (nome, preco) VALUES (?, ?)")) {
            pstm.setString(1, nome);
            pstm.setDouble(2, preco);
            pstm.executeUpdate();
            for(String alergenio : alergenios){
                Statement  stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Alergenio WHERE alergenio='" + alergenio.trim() + "'");
                if(!rs.next()) continue;
                try (PreparedStatement pstmAlergenio = conn.prepareStatement("INSERT INTO AlergenioIngrediente (alergenio_FK, nomeIngrediente_FK) VALUES (?, ?)")) {
                    pstmAlergenio.setString(1, alergenio.trim());
                    pstmAlergenio.setString(2, nome);
                    pstmAlergenio.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /** 
     * Método que remove um ingrediente da base de dados
     * 
     * @param key Nome do ingrediente a remover
     * @return Ingrediente removido
     */
    public Ingrediente removerIngrediente(Object key) {
        Ingrediente i = this.getIngredientePorNome(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM AlergenioIngrediente WHERE nomeIngrediente_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Ingrediente WHERE nome='" + key.toString() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    /** 
     * Método que devolve a lista de ingredientes da base de dados
     * 
     * @return Lista de ingredientes da base de dados
     */
    public List<Ingrediente> getIngredientes() {
        List<Ingrediente> ingredientes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT nome FROM Ingrediente")) {
            while (rs.next()) {
                String nome = rs.getString("nome");
                Ingrediente ingrediente = this.getIngredientePorNome(nome);
                if (ingrediente != null) {
                    ingredientes.add(ingrediente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return ingredientes;
    }

    /** 
     * Método que devolve a lista de propostas da base de dados
     * 
     * @param codPropostas Coleção de códigos das propostas a procurar
     * @return Lista de propostas encontradas
     */
    public List<Proposta> getPropostas(Collection<String> codPropostas) {///Pensar numa solucao que nao tenha
        List<Proposta> propostas = new ArrayList<>();              //// que estar a chamar o get varias vezes
        for (String cod : codPropostas) {
            Proposta proposta = this.get(cod);
            if (proposta != null) {
                propostas.add(proposta);
            }
        }
        return propostas;
    }

    /** 
     * Método que devolve um ingrediente da base de dados
     * 
     * @param key Nome do ingrediente a procurar
     * @return Ingrediente encontrado
     */
    public Ingrediente getIngredientePorNome(Object key) {
        Ingrediente i = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rss = stm.executeQuery("SELECT * FROM Ingrediente WHERE nome='" + key.toString() + "'")) {
            if (!rss.next()) {
                return null; // Ingrediente não encontrado
            }
            
            double preco = rss.getDouble("preco");
             
            ResultSet rsAlergenios = stm.executeQuery("SELECT alergenio_FK FROM AlergenioIngrediente WHERE nomeIngrediente_FK='" + key.toString() + "'");
            List<String> alergenios = new ArrayList<>();
            while (rsAlergenios.next()) {
                String alergenio = rsAlergenios.getString("alergenio_FK");
                alergenios.add(alergenio);
            }
            i = new Ingrediente(key.toString(), alergenios, preco);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    /** 
     * Método que devolve a lista de ingredientes da base de dados
     * 
     * @param nomes Coleção de nomes dos ingredientes a procurar
     * @return Lista de ingredientes encontrados
     */
    public List<Ingrediente> getIngredientesPorNomes(Collection<String> nomes) {///Pensar numa solucao que nao tenha
        List<Ingrediente> ingredientes = new ArrayList<>();             //// que estar a chamar o get varias vezes  
        for (String nome : nomes) {
            Ingrediente ingrediente = this.getIngredientePorNome(nome);
            if (ingrediente != null) {
                ingredientes.add(ingrediente);
            }
        }
        return ingredientes;
    }
    
    // Implementação dos métodos obrigatórios de Map
    
    /** 
     * Método que devolve o número de Propostas na base de dados
     * 
     * @return Número de Propostas na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Proposta")) {
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
     * Método que verifica se a base de dados de Propostas está vazia
     * 
     * @return true se a base de dados estiver vazia, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    /** 
     * Método que verifica se um determinado ID de Proposta existe na base de dados
     * 
     * @param key ID da Proposta
     * @return true se o ID existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT idProposta FROM Proposta WHERE idProposta=?")) {
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
     * Método que verifica se uma determinada Proposta existe na base de dados
     * 
     * @param value Proposta a verificar
     * @return true se a Proposta existir na base de dados, false caso contrário
     */
    @Override
    public boolean containsValue(Object value) {
        Proposta f = (Proposta) value;
        return this.containsKey(f.getIdProposta());
    }
    
    /** 
     * Método que devolve uma Proposta a partir do seu ID
     * 
     * @param key ID do Proposta
     * @return Proposta correspondente ao ID
     */
    @Override
    public Proposta get(Object key) {
        Proposta a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             Statement stmt = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Proposta WHERE idProposta='" + key + "'")) {
            if (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");

                ResultSet rsIngredientes = stmt.executeQuery("SELECT nomeIngrediente_FK FROM IngredienteProposta WHERE idProposta_FK='" + key + "'");
                List<Ingrediente> ingredientes = new ArrayList<>();
                while (rsIngredientes.next()) {
                    String nomeIngrediente = rsIngredientes.getString("nomeIngrediente_FK");
                    Ingrediente ingrediente = this.getIngredientePorNome(nomeIngrediente);
                    if (ingrediente != null) {
                        ingredientes.add(ingrediente);
                    }
                }

                ResultSet rsEtapas = stmt.executeQuery("SELECT etapa FROM EtapasProposta WHERE idProposta_FK='" + key + "' ORDER BY ordem");
                List<String> etapas = new ArrayList<>();
                while (rsEtapas.next()) {
                    String etapa = rsEtapas.getString("etapa");
                    etapas.add(etapa);
                }

                a = new Proposta(key.toString(), nome, preco, ingredientes, etapas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }
    
    /** 
     * Método que insere ou atualiza uma Proposta na base de dados
     * 
     * @param key ID da Proposta
     * @param value Proposta a inserir ou atualizar
     * @return Proposta previamente associada ao ID, ou null se não existia
     */
    @Override
    public Proposta put(String key, Proposta value) {
        Proposta res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Verificar se já existe
            res = this.get(key);
            
            if (res != null) {
                // Atualizar
                try (PreparedStatement pstm = conn.prepareStatement("UPDATE Proposta SET nome=?, preco=? WHERE idProposta=?")) {
                    pstm.setString(1, value.getNome());
                    pstm.setDouble(2, value.getPreco());
                    pstm.setString(3, value.getIdProposta());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();
                    stm.executeUpdate("DELETE FROM IngredienteProposta WHERE idProposta_FK='" + value.getIdProposta() + "'");
                    for (Ingrediente ingrediente : value.getIngredientes()) {
                        stm.executeUpdate("INSERT INTO IngredienteProposta (nomeIngrediente_FK, idProposta_FK) VALUES ('" + ingrediente.getNome() + "', '" + value.getIdProposta() + "')");
                    }

                    stm.executeUpdate("DELETE FROM EtapasProposta WHERE idProposta_FK='" + value.getIdProposta() + "'");
                    for (String s : value.getEtapas()) {
                        int ordem = value.getEtapas().indexOf(s) + 1;
                        stm.executeUpdate("INSERT INTO EtapasProposta (etapa, idProposta_FK, ordem) VALUES ('" + s + "', '" + value.getIdProposta() + "', " + ordem + ")");
                    }
                }
            } else {
                // Inserir novo
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO Proposta (idProposta, nome, preco) VALUES (?, ?, ?)")) {
                    pstm.setString(1, value.getIdProposta());
                    pstm.setString(2, value.getNome());
                    pstm.setDouble(3, value.getPreco());
                    pstm.executeUpdate();

                    Statement stm = conn.createStatement();
                    for (Ingrediente ingrediente : value.getIngredientes()) {
                        stm.executeUpdate("INSERT INTO IngredienteProposta (nomeIngrediente_FK, idProposta_FK) VALUES ('" + ingrediente.getNome() + "', '" + value.getIdProposta() + "')");
                    }

                    for (String s : value.getEtapas()) {
                        int ordem = value.getEtapas().indexOf(s) + 1;
                        stm.executeUpdate("INSERT INTO EtapasProposta (etapa, idProposta_FK, ordem) VALUES ('" + s + "', '" + value.getIdProposta() + "', " + ordem + ")");
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
     * Método que remove uma Proposta da base de dados
     * 
     * @param key ID da Proposta a remover
     * @return Proposta removida, ou null se não existia
     */
    @Override
    public Proposta remove(Object key) {
        Proposta t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM IngredienteProposta WHERE idProposta_FK='" + key.toString() + "'");
            stm.executeUpdate("DELETE FROM Proposta WHERE idProposta='" + key.toString() + "'");
            
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
    
    /** 
     * Método que insere todos as Propostas de um mapa na base de dados
     * 
     * @param m Mapa de Propostas a inserir
     */
    @Override
    public void putAll(Map<? extends String, ? extends Proposta> m) {
        for(Proposta p : m.values()) {
            this.put(p.getIdProposta(), p);
        }
    }
    
    /** 
     * Método que remove todos as Propostas da base de dados
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM IngredienteProposta");
            stm.executeUpdate("DELETE FROM Proposta");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /** 
     * Método que devolve o conjunto de IDs das Propostas na base de dados
     * 
     * @return Conjunto de IDs das Propostas na base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT idProposta FROM Proposta")) {
            while (rs.next()) {
                res.add(rs.getString("idProposta"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
    
    /** 
     * Método que devolve a coleção de Propostas na base de dados
     * 
     * @return Coleção de Propostas na base de dados
     */
    @Override
    public Collection<Proposta> values() {
        Proposta a = null;
        Collection<Proposta> novo = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             Statement stmt = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Proposta")) {
            while (rs.next()) {
                String idProposta = rs.getString("idProposta");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");

                ResultSet rsIngredientes = stmt.executeQuery("SELECT nomeIngrediente_FK FROM IngredienteProposta WHERE idProposta_FK='" + idProposta + "'");
                List<Ingrediente> ingredientes = new ArrayList<>();
                while (rsIngredientes.next()) {
                    String nomeIngrediente = rsIngredientes.getString("nomeIngrediente_FK");
                    Ingrediente ingrediente = this.getIngredientePorNome(nomeIngrediente);
                    if (ingrediente != null) {
                        ingredientes.add(ingrediente);
                    }
                }
                ResultSet rsEtapas = stmt.executeQuery("SELECT etapa FROM EtapasProposta WHERE idProposta_FK='" + idProposta + "' ORDER BY ordem");
                List<String> etapas = new ArrayList<>();
                while (rsEtapas.next()) {
                    String etapa = rsEtapas.getString("etapa");
                    etapas.add(etapa);
                }

                a = new Proposta(idProposta, nome, preco, ingredientes, etapas);

                novo.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
    }
    
    /** 
     * Método que devolve o conjunto de entradas (ID, Proposta) na base de dados
     * 
     * @return Conjunto de entradas (ID, Proposta) na base de dados
     */
    @Override
    public Set<Entry<String, Proposta>> entrySet() {
        Proposta a = null;
        Set<Entry<String, Proposta>> novo = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             Statement stmt = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Proposta")) {
            while (rs.next()) {
                String idProposta = rs.getString("idProposta");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");

                ResultSet rsIngredientes = stmt.executeQuery("SELECT nomeIngrediente_FK FROM IngredienteProposta WHERE idProposta_FK='" + idProposta + "'");
                List<Ingrediente> ingredientes = new ArrayList<>();
                while (rsIngredientes.next()) {
                    String nomeIngrediente = rsIngredientes.getString("nomeIngrediente_FK");
                    Ingrediente ingrediente = this.getIngredientePorNome(nomeIngrediente);
                    if (ingrediente != null) {
                        ingredientes.add(ingrediente);
                    }
                }

                ResultSet rsEtapas = stmt.executeQuery("SELECT etapa FROM EtapasProposta WHERE idProposta_FK='" + idProposta + "' ORDER BY ordem");
                List<String> etapas = new ArrayList<>();
                while (rsEtapas.next()) {
                    String etapa = rsEtapas.getString("etapa");
                    etapas.add(etapa);
                }

                a = new Proposta(idProposta, nome, preco, ingredientes, etapas);

                novo.add(new AbstractMap.SimpleEntry<>(idProposta, a));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return novo;
    }
}
