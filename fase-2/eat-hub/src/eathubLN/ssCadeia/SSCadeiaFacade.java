package eathubLN.ssCadeia;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import eathubDL.EstruturasDAO.FuncionarioDAO;
import eathubDL.EstruturasDAO.RestauranteDAO;
import eathubDL.EstruturasDAO.StockDAO;
import eathubDL.EstruturasDAO.HistoricoDAO;
import eathubLN.ssPedidos.Ingrediente;
import eathubLN.ssPedidos.Pedido;

/**
 * Facade do subsistema da cadeia de restaurantes
 */
public class SSCadeiaFacade implements ISSCadeia {
    /** Instância do gestor da cadeia */
    private Gestor gestor;
    /** Instância do DAO de funcionários */
    private FuncionarioDAO funcionarios;
    /** Instância do DAO de restaurantes */
    private RestauranteDAO restaurantes;
    /** Instância do DAO de stock */
    private StockDAO stock;
    /** Instância do DAO de históricos */
    private HistoricoDAO historicos;

    /**
     * Construtor do Facade do subsistema da cadeia de restaurantes
     */
    public SSCadeiaFacade() {
        this.funcionarios = FuncionarioDAO.getInstance();
        this.restaurantes = RestauranteDAO.getInstance();
        this.stock = StockDAO.getInstance();
        this.historicos = HistoricoDAO.getInstance();
        this.gestor = this.funcionarios.getGestor();
    }

    /**
     * Método que obtém as estatísticas de um restaurante num determinado período
     * 
     * @param idRestaurante ID do restaurante
     * @param periodo       Período para o qual se querem as estatísticas (formato
     *                      "YYYY-MM-DD" ou "total" para todas as estatísticas)
     * @return Estatísticas do restaurante no período especificado
     * @throws IllegalArgumentException Se o restaurante não existir ou se o período
     *                                  for inválido
     */
    public Estatistica obter_estatisticas_restaurante(String idRestaurante, String periodo){
        if (!this.restaurantes.containsKey(idRestaurante)) {
            throw new IllegalArgumentException("O restaurante especificado não existe.");
        } else if (periodo == null || periodo.trim().isEmpty()) {
            throw new IllegalArgumentException("O período especificado é null ou está vazio.");
        }

        LocalDate dataLimite = null;
        if (!periodo.equalsIgnoreCase("total")) {
            try {
                dataLimite = LocalDate.parse(periodo);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("O formato do período é inválido. Use 'YYYY-MM-DD' ou 'total'.");
            }
        }

        return historicos.calcularEstatisticasRestaurante(idRestaurante, dataLimite);
    }

    /**
     * Método que obtém as estatísticas da cadeia num determinado período
     * 
     * @param periodo Período para o qual se querem as estatísticas (formato
     *                "YYYY-MM-DD" ou "total" para todas as estatísticas)
     * @return Estatísticas da cadeia no período especificado
     * @throws IllegalArgumentException Se o período for inválido
     */
    public Estatistica obter_estatisticas_cadeia(String periodo){
        if (periodo == null || periodo.trim().isEmpty()) {
            throw new IllegalArgumentException("O período especificado é null ou está vazio.");
        }

        LocalDate dataLimite = null;
        if (!periodo.equalsIgnoreCase("total")) {
            try {
                dataLimite = LocalDate.parse(periodo);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("O formato do período é inválido. Use 'YYYY-MM-DD' ou 'total'.");
            }
        }

        return historicos.calculaEstatisticasCadeia(dataLimite);
    }

    /** Método que obtém os IDs de todos os restaurantes da cadeia
     * 
     * @return Coleção de IDs dos restaurantes
     */
    public Collection<String> obter_IDs_restaurantes(){
        return this.restaurantes.keySet();
    }

    /** Método que cria um novo funcionário na cadeia
     * 
     * @param nome          Nome do funcionário
     * @param posto         Posto do funcionário
     * @param tarefa        Tarefa do funcionário
     * @param idRestaurante ID do restaurante associado (pode ser null para gestor)
     * @param tipo          Tipo de funcionário ("ChefeRestaurante", "Gestor", "Funcionario")
     * @param password      Password do funcionário
     * @return ID do novo funcionário criado
     * @throws IllegalArgumentException Se o restaurante não existir, se já existir um chefe ou gestor, ou se o tipo for inválido
     */
    public String criar_Funcionario(String nome, String posto, String tarefa, String idRestaurante, String tipo, String password) {
        String novoCod = this.funcionarios.generateNewId();
        if (idRestaurante != null && !idRestaurante.trim().isEmpty()){ 
            if (this.restaurantes.get(idRestaurante) == null) {
                throw new IllegalArgumentException("O restaurante especificado não existe.");
            }
        }

        if (tipo.equalsIgnoreCase("ChefeRestaurante")) {

            if (idRestaurante != null && !idRestaurante.trim().isEmpty()){
                if (this.restaurantes.getChefeRestaurante(idRestaurante) != null) {
                    throw new IllegalArgumentException("Já existe um chefe de restaurante neste restaurante.");
                }

                ChefeRestaurante novoChefe = new ChefeRestaurante(novoCod, nome, posto, tarefa);
                this.funcionarios.putCompleto(novoCod, novoChefe, idRestaurante);
                this.funcionarios.associarFuncionarioPassword(novoCod, password);
                this.restaurantes.atualizaChefeRestaurante(idRestaurante, novoChefe.getIDFunc());
                return novoCod;
            } else {
                throw new IllegalArgumentException("Um chefe de restaurante deve estar associado a um restaurante.");
            }

            
        } else if (tipo.equalsIgnoreCase("Gestor")) {

            if (this.funcionarios.getGestor() != null) {
                throw new IllegalArgumentException("Já existe um gestor na cadeia.");
            }

            Gestor novoGestor = new Gestor(novoCod, nome, posto, tarefa);
            this.funcionarios.putCompleto(novoCod, novoGestor, idRestaurante);
            this.funcionarios.associarFuncionarioPassword(novoCod, password);
            this.gestor = novoGestor;
            return novoCod;
        } else if (tipo.equalsIgnoreCase("Funcionario")) {
            Funcionario novoFunc = new Funcionario(novoCod, nome, posto, tarefa);
            this.funcionarios.putCompleto(novoCod, novoFunc, idRestaurante);
            this.funcionarios.associarFuncionarioPassword(novoCod, password);
            return novoCod;
        } else {
            throw new IllegalArgumentException("Tipo de funcionário inválido.");
        }
    }

    /** 
     * Método que autentica um funcionário
     * 
     * @param id       ID do funcionário
     * @param password Password do funcionário (não utilizada nesta implementação)
     * @return true se o funcionário existir, false caso contrário
     */
    public boolean autenticarFuncionario(String id, String password) {
        Funcionario f = this.funcionarios.get(id);
        if (f != null) {
            return this.funcionarios.validarPassword(id, password);
        }
        return false;
    }

    /** 
     * Método que autentica um chefe de restaurante
     * 
     * @param id       ID do chefe de restaurante
     * @param password Password do chefe de restaurante (não utilizada nesta implementação)
     * @return true se o chefe de restaurante existir, false caso contrário
     */
    public boolean autenticarChefeRestaurante(String id, String password) {
        Funcionario f = this.funcionarios.get(id);
        if (f != null && f instanceof ChefeRestaurante) {
            return this.funcionarios.validarPassword(id, password);
        }
        return false;
    }

    /** 
     * Método que autentica um gestor
     * 
     * @param id       ID do gestor
     * @param password Password do gestor (não utilizada nesta implementação)
     * @return true se o gestor existir, false caso contrário
     */
    public boolean autenticarGestor(String id, String password) {
        Funcionario f = this.funcionarios.get(id);
        if (f != null && f instanceof Gestor) {
            return this.funcionarios.validarPassword(id, password);
        }
        return false;
    }

    /** 
     * Método que adiciona ingredientes ao stock de um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @param codIngrediente Código do ingrediente
     * @param quantidade Quantidade a adicionar
     * @return true se a operação for bem-sucedida, false caso contrário
     */
    public boolean adicionar_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade) {
        Restaurante r = this.restaurantes.get(idRestaurante);
        if (r != null) {
            Integer quantidadeExistente = this.stock.get(new Pair<>(idRestaurante, codIngrediente));
            if (quantidadeExistente == null) {
                quantidadeExistente = 0;
            }
            this.stock.put(new Pair<>(idRestaurante, codIngrediente), quantidadeExistente + quantidade);
            return true;
        }
        return false;
    }

    /** 
     * Método que remove ingredientes do stock de um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @param codIngrediente Código do ingrediente
     * @param quantidade Quantidade a remover
     * @return true se a operação for bem-sucedida, false caso contrário
     */
    public boolean remover_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade) {
        Restaurante r = this.restaurantes.get(idRestaurante);
        if (r != null) {
            Integer quantidadeExistente = this.stock.get(new Pair<>(idRestaurante, codIngrediente));
            if (quantidadeExistente != null && quantidadeExistente >= quantidade) {
                this.stock.put(new Pair<>(idRestaurante, codIngrediente), quantidadeExistente - quantidade);
                return true;
            }
        }
        return false;
    }

    /** 
     * Método que verifica os ingredientes em falta num restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @param ingredientes Coleção de ingredientes a verificar
     * @return Lista de nomes dos ingredientes em falta
     */
    public List<String> verificaIngredientesEmFaltaRestaurante(String idRestaurante, Collection<Ingrediente> ingredientes) {
        Restaurante r = this.restaurantes.get(idRestaurante);
        if (r != null) {
            List<String> ingredientesEmFalta = new ArrayList<>();
            for (Ingrediente ing : ingredientes) {
                Integer quantidadeExistente = this.stock.get(new Pair<>(idRestaurante, ing.getNome()));
                if (quantidadeExistente == null || quantidadeExistente == 0) {
                    ingredientesEmFalta.add(ing.getNome());
                }
            }
            return ingredientesEmFalta;
        }
        return null;
        
    }
    
    /**
     * Método que verifica se um ID corresponde a um funcionário
     * 
     * @param id ID do funcionário
     * @return true se o funcionário existir, false caso contrário
     */
    public boolean eFuncionario(String id) {
        Funcionario f = this.funcionarios.get(id);
        return f != null;
    }

    /** 
     * Método que verifica se um ID corresponde a um chefe de restaurante
     * 
     * @param id ID do chefe de restaurante
     * @return true se o chefe de restaurante existir, false caso contrário
     */
    public boolean eChefeRestaurante(String id) {
        Funcionario f = this.funcionarios.get(id);
        return f != null && f instanceof ChefeRestaurante;
    }

    /** 
     * Método que verifica se um ID corresponde a um gestor
     * 
     * @param id ID do gestor
     * @return true se o gestor existir, false caso contrário
     */
    public boolean eGestor(String id) {
        Funcionario f = this.funcionarios.get(id);
        return f != null && f instanceof Gestor;
    }

    /** 
     * Método que cria um novo restaurante na cadeia
     * 
     * @param nome Nome do restaurante
     * @param chefe ID do chefe do restaurante
     * @param funcionarios Lista de IDs dos funcionários do restaurante
     * @return ID do novo restaurante criado
     */
    public String criar_Restaurante(String nome, String chefe, List<String> funcionarios) {
        Restaurante novoRest = new Restaurante(this.restaurantes.generateNewId(), nome, new ChefeRestaurante(this.funcionarios.get(chefe)), funcionarios);
        this.restaurantes.put(novoRest.getIdRestaurante(), novoRest);
        return novoRest.getIdRestaurante();
    }

    /** 
     * Método que obtém as mensagens do gestor
     * 
     * @return Coleção de mensagens do gestor
     */
    public Collection<MensagemGestor> obter_mensagens_Gestor() {
        return this.funcionarios.getMensagensGestor();
    }

    /** 
     * Método que envia uma mensagem para o gestor
     * 
     * @param conteudo Conteúdo da mensagem
     * @param idGestor ID do gestor
     * @return true se a mensagem for enviada com sucesso, false caso contrário
     */
    public boolean enviar_mensagem_gestor(String conteudo, String idGestor) {
        Gestor g = this.funcionarios.getGestor();
        if (g != null && g.getIDFunc().equals(idGestor)) {
            this.funcionarios.enviar_mensagem_gestor(conteudo, idGestor);
            return true;
        }
        return false;
    }

    /** 
     * Método que remove uma mensagem do gestor
     * 
     * @param idMensagem ID da mensagem a remover
     * @return Mensagem removida
     */
    public MensagemGestor remover_mensagem_gestor(int idMensagem) {
        MensagemGestor m = this.funcionarios.remover_mensagem_gestor(idMensagem);
        return m;
    }

    /** 
     * Método que obtém o gestor da cadeia
     * 
     * @return Instância do gestor
     */
    public Gestor getGestor() {
        return this.gestor;
    }

    /** 
     * Método que obtém os códigos dos restaurantes da cadeia
     * 
     * @return Conjunto de códigos dos restaurantes
     */
    public Set<String> get_Cods_Restaurantes() {
        return this.restaurantes.keySet();
    }

    /** 
     * Método que obtém o restaurante associado a um funcionário
     * 
     * @param idFuncionario ID do funcionário
     * @return ID do restaurante associado ao funcionário
     */
    public String getRestauranteDeFuncionario(String idFuncionario) {
        return this.funcionarios.getRestauranteFuncionario(idFuncionario);
    }

    /** 
     * Método que cria um novo histórico de pedido
     * 
     * @param pedido        Pedido a registar no histórico
     * @param idRestaurante ID do restaurante onde o pedido foi realizado
     * @return ID do novo histórico criado
     */
    public String criar_Historico(Pedido pedido, String idRestaurante) {
        Historico novoHistorico = new Historico(this.historicos.generateNewId(), pedido, idRestaurante);
        this.historicos.put(novoHistorico.getIdHistorico(), novoHistorico);
        return novoHistorico.getIdHistorico();
    }

    /** 
     * Método que obtém o ID do funcionário responsável por uma determinada etapa num restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @param nomeEtapa Nome da etapa
     * @return ID do funcionário responsável pela etapa, ou null se não existir
     */
    public String obterIdFuncionarioTarefa_Igual_Etapa(String idRestaurante, String nomeEtapa) {
        Restaurante r = this.restaurantes.get(idRestaurante);
        if (r != null) {
            for (String codFunc : r.getCodsFuncionarios()) {
                Funcionario f = r.getFuncionario(codFunc);
                if (f.getTarefaFunc().equalsIgnoreCase(nomeEtapa)) {
                    return f.getIDFunc();
                }
            }
        }
        return null;
    }

    // DAO Access Methods
    /**
     * Método que obtém um funcionário pelo seu ID
     * 
     * @param idFuncionario ID do funcionário
     * @return Instância do funcionário
     */
    public Funcionario funcionarioGet(String idFuncionario) {
        return this.funcionarios.get(idFuncionario);
    }

    /** 
     * Método que remove um funcionário pelo seu ID
     * 
     * @param idFuncionario ID do funcionário
     * @return Instância do funcionário removido
     */
    public Funcionario funcionarioRemove(String idFuncionario) {
        return this.funcionarios.remove(idFuncionario);
    }

    /** 
     * Método que obtém todos os funcionários da cadeia
     * 
     * @return Coleção de funcionários
     */
    public Collection<Funcionario> funcionarioValues() {
        return this.funcionarios.values();
    }

    /** 
     * Método que obtém os IDs dos funcionários de um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Coleção de IDs dos funcionários do restaurante
     */
    public Collection<String> funcionarioGetIdsFuncionariosRestaurante(String idRestaurante) {
        return this.funcionarios.getIdsFuncionariosRestaurante(idRestaurante);
    }

    /**
     * Método que remove um restaurante pelo seu ID
     * 
     * @param idRestaurante ID do restaurante
     * @return Instância do restaurante removido
     */
    public Restaurante restauranteRemove(String idRestaurante) {
        return this.restaurantes.remove(idRestaurante);
    }

    /** 
     * Método que obtém todos os restaurantes da cadeia
     * 
     * @return Coleção de restaurantes
     */
    public Collection<Restaurante> restauranteValues() {
        return this.restaurantes.values();
    }

    /** 
     * Método que obtém o conjunto de entradas de stock de um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Conjunto de entradas de stock do restaurante
     */
    public Set<Map.Entry<Pair<String, String>, Integer>> stockEntrySetRestaurante(String idRestaurante) {
        return this.stock.entrySetRestaurante(idRestaurante);
    }

    /** 
     * Método que obtém todos os históricos de pedidos da cadeia
     * 
     * @return Coleção de históricos de pedidos
     */
    public Collection<Historico> getHistoricoPedidosCadeia() {
        return this.historicos.values();
    }

    /** 
     * Método que obtém os históricos de pedidos de um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return Coleção de históricos de pedidos do restaurante
     */
    public Collection<Historico> getHistoricoPedidosRestaurante(String idRestaurante) {
        return this.historicos.getValuesRestaurante(idRestaurante);
    }

    /** 
     * Método que remove um histórico de pedidos pelo seu ID
     * 
     * @param idHistorico ID do histórico
     * @return Instância do histórico removido
     */
    public Historico remover_historico_pedidos(String idHistorico) {
        return this.historicos.remove(idHistorico);
    }
    
    /**
     * Método que regista os ingredientes utilizados numa proposta de pedido
     * 
     * @param idRestaurante ID do restaurante
     * @param lista Lista de ingredientes utilizados
     * @return true se o registo for bem-sucedido, false caso contrário
     */
    public boolean registar_Ingredientes_Utilizados_Proposta(String idRestaurante, List<Ingrediente> lista){
        return this.stock.registar_Uso_Ingredientes(idRestaurante, lista);
    }

    /** 
     * Método que obtém o ID do chefe de um restaurante
     * 
     * @param idRestaurante ID do restaurante
     * @return ID do chefe do restaurante, ou null se não existir
     */
    public String getChefeRestaurante(String idRestaurante) {
        Restaurante r = this.restaurantes.get(idRestaurante);
        if (r != null) {
            return r.getChefeRestaurante().getIDFunc();
        }
        return null;
    }
}
