package eathubLN.ssPedidos;

import java.util.Collection;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import Exceptions.PedidoNaoExisteException;

import eathubLN.ssCadeia.Pair;

import eathubDL.EstruturasDAO.PedidoDAO;
import eathubDL.EstruturasDAO.MenuDAO;
import eathubDL.EstruturasDAO.PropostaDAO;

/** Facade do subsistema de pedidos */
public class SSPedidosFacade implements ISSPedidos {

    /** Instância do DAO de pedidos */
    private PedidoDAO pedidos;
    /** Instância do DAO de menus */
    private MenuDAO menuDAO;
    /** Instância do DAO de propostas */
    private PropostaDAO propostaDAO;
    private Map<String, Map<String, Pedido>> pedidosPendentes; // Map<codRestaurante, Map<codPedido, Pedido>>
    private Map<String, Queue<String>> ordemDosPedidos;

    private Map<String, ProgressoPedido> progressoPedidoAtual; // Map<IdRestaurante, Progresso pedido atual>

    private Map<String, List<ProgressoPedido>> pedidosAtrasadosPorRestaurante; // Map<IdRestaurante, List<ProgressoPedido>>

    /**
     * Construtor vazio do Facade do subsistema de pedidos
     */
    public SSPedidosFacade() {

        this.pedidos = PedidoDAO.getInstance();
        this.menuDAO = MenuDAO.getInstance();
        this.propostaDAO = PropostaDAO.getInstance();
        this.pedidosPendentes = new HashMap<>();
        this.ordemDosPedidos = new HashMap<>();
        this.progressoPedidoAtual = new HashMap<>();
        this.pedidosAtrasadosPorRestaurante = new HashMap<>();

        for (String idRestaurante : this.pedidos.getIdsRestaurantes()) {
            this.pedidosPendentes.put(idRestaurante,new HashMap<>());
            this.ordemDosPedidos.put(idRestaurante, new PriorityQueue<>(Comparator.comparingDouble(codPedido -> {
                try {
                    return getPedidoBD(codPedido).getTempoEspera();
                } catch (PedidoNaoExisteException e) {
                    return Double.MAX_VALUE; // Pedidos não encontrados vão para o fim
                }
            })));
            this.progressoPedidoAtual.put(idRestaurante, null);
            this.pedidosAtrasadosPorRestaurante.put(idRestaurante, new ArrayList<>());

        }
    }

    /**
     * Construtor parameterizado do Facade do subsistema de pedidos
     * 
     * @param pedidos Instância do DAO de pedidos
     * @param menuDAO Instância do DAO de menus
     * @param propostaDAO Instância do DAO de propostas
     * @param pedidosPendentes Mapa de pedidos pendentes
     * @param ordemDosPedidos Mapa da ordem dos pedidos
     */
    public SSPedidosFacade(PedidoDAO pedidos, MenuDAO menuDAO, PropostaDAO propostaDAO, 
                           Map<String, Map<String, Pedido>> pedidosPendentes,
                           Map<String, Queue<String>> ordemDosPedidos) {
        this.pedidos = pedidos;
        this.menuDAO = menuDAO;
        this.propostaDAO = propostaDAO;
        this.pedidosPendentes = pedidosPendentes;
        this.ordemDosPedidos = ordemDosPedidos;
    }

    /** 
     * Método que insere as estruturas necessárias para um novo restaurante
     * 
     * @param idRestaurante Id do restaurante
     */
    public void insert_Maps_Novo_Restaurante(String idRestaurante){
        this.pedidosPendentes.put(idRestaurante,new HashMap<>());
        this.ordemDosPedidos.put(idRestaurante, new PriorityQueue<>(Comparator.comparingDouble(codPedido -> {
            try {
                return getPedidoBD(codPedido).getTempoEspera();
            } catch (PedidoNaoExisteException e) {
                return Double.MAX_VALUE; // Pedidos não encontrados vão para o fim
            }
        })));
        this.progressoPedidoAtual.put(idRestaurante, null);
        this.pedidosAtrasadosPorRestaurante.put(idRestaurante, new ArrayList<>());
    }

    /** 
     * Método que obtém os IDs dos pedidos pendentes de um restaurante
     * 
     * @param idRestaurante Id do restaurante
     * @return Conjunto de IDs dos pedidos pendentes
     */
    public Set<String> obterIds_CodsPedidos_Pendentes(String idRestaurante) {
        return this.pedidosPendentes.get(idRestaurante).keySet();
    }

    /** 
     * Método que verifica se um pedido está pendente
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @return true se o pedido estiver pendente, false caso contrário
     */
    public boolean ePendente(String codPedido, String idRestaurante) {
        return this.pedidosPendentes.get(idRestaurante).containsKey(codPedido);
    }

    /** 
     * Método que obtém um pedido pendente
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @return Pedido pendente
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public Pedido getPedidoPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        Pedido p = this.pedidosPendentes.get(idRestaurante).get(codPedido);
        if (p == null) {
            throw new PedidoNaoExisteException(codPedido);
        }
        return p;
    }

    /** 
     * Método que obtém um pedido da base de dados
     * 
     * @param codPedido Código do pedido
     * @return Pedido da base de dados
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public Pedido getPedidoBD(String codPedido) throws PedidoNaoExisteException {
        Pedido p = this.pedidos.get(codPedido);
        if (p == null) {
            throw new PedidoNaoExisteException(codPedido);
        }
        return p;
    }

    /** 
     * Método que obtém a ementa
     * 
     * @return Ementa
     */
    public Ementa obter_ementa() {
        return Ementa.getInstance();
    }

    /** 
     * Método que obtém os alérgenios de um pedido na base de dados
     * 
     * @param codPedido Código do pedido
     * @return Coleção de pares (nome do alérgenio, ingrediente)
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public Collection<Pair<String,Ingrediente>> obter_alergenios_vBD(String codPedido) throws PedidoNaoExisteException {
        Pedido p = getPedidoBD(codPedido);
        return p.getAlergenios();
    }

    /** 
     * Método que obtém os alérgenios de um pedido pendente
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @return Coleção de pares (nome do alérgenio, ingrediente)
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public Collection<Pair<String,Ingrediente>> obter_alergenios_vPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        Pedido p = getPedidoPendente(codPedido, idRestaurante);
        return p.getAlergenios();
    }

    /** 
     * Método que regista um pedido
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public void regista_pedido(String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        Pedido p = getPedidoPendente(codPedido, idRestaurante);
        double tempoEspera = calcularTempoEsperaInicial(p);// Ao registar o pedido, calcular o tempo de espera consoante a complexidade do pedido
        p.alterar_TempoEspera(tempoEspera);
        this.pedidos.put(codPedido, p);
        this.pedidosPendentes.get(idRestaurante).remove(codPedido);
    }

    /** 
     * Método que regista uma nota num pedido
     * 
     * @param nota Nota a registar
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public void regista_nota(String nota, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        Pedido p = getPedidoPendente(codPedido, idRestaurante);
        p.addNota(nota);

    }

    /** 
     * Método que regista o tipo de um pedido
     * 
     * @param tipo Tipo do pedido
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public void registar_tipo_pedido(String tipo, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        Pedido p = getPedidoPendente(codPedido, idRestaurante);
        p.setTipoPedido(tipo);

    }

    /** 
     * Método que cancela um pedido
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     */
    public void cancelar_pedido(String codPedido, String idRestaurante) {
        this.pedidosPendentes.get(idRestaurante).remove(codPedido);

    }

    /** 
     * Método que determina o próximo pedido a ser processado
     * 
     * @param idRestaurante Id do restaurante
     * @return Código do próximo pedido
     */
    public String determina_proximo_pedido(String idRestaurante) {
        return this.ordemDosPedidos.get(idRestaurante).poll();
    }

    /** 
     * Método que regista um novo tempo de espera para um pedido
     * 
     * @param tempo Novo tempo de espera
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public void regista_novo_tempo_espera(double tempo, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        Pedido p = getPedidoBD(codPedido);
        p.alterar_TempoEspera(tempo);
        this.pedidos.put(codPedido, p);
        // Reordenar a fila após alterar o tempo de espera
        // Quando se altera o tempo de espera, é porque ele ja nao estava mais na fila de espera
        ordemDosPedidos.get(idRestaurante).offer(codPedido);
    }

    /** 
     * Método que cria um pedido vazio
     * 
     * @param idRestaurante Id do restaurante
     * @return Código do pedido criado
     */
    public String criar_Pedido(String idRestaurante) {
        String id = this.pedidos.generateNewId();
        Pedido p = new Pedido(id);
        pedidosPendentes.get(idRestaurante).put(p.getCodPedido(), p);
        return p.getCodPedido();
    }

    /** 
     * Método que regista o número de contribuinte num pedido
     * 
     * @param nc Número de contribuinte
     * @param codPedido Código do pedido
     * @param idRestaurante Id do restaurante
     * @throws PedidoNaoExisteException Se o pedido não existir
     */
    public void registar_contribuinte_Pedido(int nc, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        Pedido p = getPedidoPendente(codPedido, idRestaurante);
        p.setNrContribuinte(nc);

    }

    /** 
     * Método que adiciona um pedido à fila de espera
     * 
     * @param idPedido Id do pedido
     * @param idRestaurante Id do restaurante
     */
    public void addPedidoQueue(String idPedido, String idRestaurante) {
        ordemDosPedidos.get(idRestaurante).offer(idPedido);

    }

    /** 
     * Método que cria um pedido completo
     * 
     * @param tempoEspera Tempo de espera
     * @param nContribuinte Número de contribuinte
     * @param notas Notas do pedido
     * @param codMenus Códigos dos menus
     * @param codPropostas Códigos das propostas
     * @param tipoServico Tipo de serviço
     * @param idRestaurante Id do restaurante
     * @return Código do pedido criado
     */
    public String criar_Pedido_Completo(double tempoEspera, int nContribuinte, String notas, 
                                        List<String> codMenus, List<String> codPropostas, 
                                        String tipoServico, String idRestaurante) {
        Pedido p = new Pedido(this.pedidos.generateNewId(), 
                                tempoEspera, 
                                nContribuinte, 
                                notas, 
                                codMenus, 
                                codPropostas, 
                                tipoServico);

        pedidosPendentes.get(idRestaurante).put(p.getCodPedido(), p);
        return p.getCodPedido();
    }

    /** 
     * Método que cria um menu
     * 
     * @param nomeMenu Nome do menu
     * @param codPropostas Códigos das propostas
     * @param preco Preço do menu
     * @return Código do menu criado
     */
    public String criar_Menu(String nomeMenu, Collection<String> codPropostas, double preco) {
        Menu m = new Menu(this.menuDAO.generateNewId(), nomeMenu, preco, this.propostaDAO.getPropostas(codPropostas));
        this.menuDAO.put(m.getIdMenu(), m);
        return m.getIdMenu();
    }

    /** 
     * Método que cria uma proposta
     * 
     * @param nomeProposta Nome da proposta
     * @param codIngredientes Códigos dos ingredientes
     * @param preco Preço da proposta
     * @param etapas Etapas da proposta
     * @return Código da proposta criada
     */
    public String criar_Proposta(String nomeProposta, Collection<String> codIngredientes, double preco, List<String> etapas) {
        Proposta pm = new Proposta(this.propostaDAO.generateNewId(), nomeProposta, preco, this.propostaDAO.getIngredientesPorNomes(codIngredientes), etapas);
        this.propostaDAO.put(pm.getIdProposta(), pm);
        return pm.getIdProposta();
    }

    /** 
     * Método que espreita o próximo pedido na fila de espera
     * 
     * @param idRestaurante Id do restaurante
     * @return Código do próximo pedido
     */
    public String peekPedidoQueue(String idRestaurante) {
        return ordemDosPedidos.get(idRestaurante).peek();
    }

    /** 
     * Método que calcula o tempo de espera inicial de um pedido
     * 
     * @param pedido Pedido
     * @return Tempo de espera inicial
     */
    public double calcularTempoEsperaInicial(Pedido pedido) {
        double tempoBase = 5.0;
        
        // Adicionar tempo por cada menu/proposta com crescimento exponencialmente menor
        int numItens = pedido.getCodMenus().size() + pedido.getCodPropostas().size();
        // Usa função logarítmica: tempo = 5 * log(n+1) * n / (n+1)
        // O tempo adicional diminui exponencialmente à medida que n aumenta
        double tempoItens = numItens > 0 ? 5.0 * Math.log(numItens + 1) * numItens / (numItens + 1.0) : 0.0;
        
        // Adicionar tempo baseado na fila atual
        double tempoFila = ordemDosPedidos.size() * 2.0; // 2 min extra por pedido na fila
        
        return tempoBase + tempoItens + tempoFila;
    }

    /** 
     * Método que obtém os IDs dos pedidos na fila de espera de um restaurante
     * 
     * @param idRestaurante Id do restaurante
     * @return Coleção de IDs dos pedidos na fila de espera
     */
    public Collection<String> obterIds_CodsPedidos_FilaEspera(String idRestaurante) {
        return new ArrayList<String>(ordemDosPedidos.get(idRestaurante));
    }

    /** 
     * Método que verifica se um menu existe
     * 
     * @param codMenu Código do menu
     * @return true se o menu existir, false caso contrário
     */
    public boolean menuExiste(String codMenu) {
        return this.menuDAO.containsKey(codMenu);
    }

    /** 
     * Método que verifica se uma proposta existe
     * 
     * @param codProposta Código da proposta
     * @return true se a proposta existir, false caso contrário
     */
    public boolean propostaExiste(String codProposta) {
        return this.propostaDAO.containsKey(codProposta);
    }

    /** 
     * Método que obtém o progresso atual do pedido de um restaurante
     * 
     * @param idRestaurante Id do restaurante
     * @return Progresso atual do pedido
     */
    public ProgressoPedido obterProgressoPedidoAtual(String idRestaurante) {
        return this.progressoPedidoAtual.get(idRestaurante);
    }

    /** 
     * Método que define o progresso atual do pedido de um restaurante
     * 
     * @param idRestaurante Id do restaurante
     * @param progresso Progresso do pedido
     */
    public void definirProgressoPedidoAtual(String idRestaurante, ProgressoPedido progresso) {
        this.progressoPedidoAtual.put(idRestaurante, progresso);
    }

    /** 
     * Método que processa a etapa atual de uma proposta
     * 
     * @param idRestaurante Id do restaurante
     * @param idFuncionario Id do funcionário
     * @param propostaEtapa Etapa da proposta
     * @return true se a etapa foi processada com sucesso, false caso contrário
     */
    public boolean processarEtapaAtualProposta(String idRestaurante, String idFuncionario, PropostaEtapa propostaEtapa) {
        ProgressoPedido progresso = progressoPedidoAtual.get(idRestaurante);
        
        if (progresso == null) {
            return false;
        }
        
        // Avançar a etapa
        boolean avancou = progresso.avancarEtapaProposta(propostaEtapa.getCodProposta(), propostaEtapa.getCodMenu());
        
        return avancou;
    }
    
    /** 
     * Método que obtém a próxima proposta a confeccionar para um restaurante
     * 
     * @param idRestaurante Id do restaurante
     * @return Próxima proposta a confeccionar
     */
    public PropostaEtapa obterProximaPropostaParaConfecionar(String idRestaurante) {
        ProgressoPedido progresso = progressoPedidoAtual.get(idRestaurante);
        
        if (progresso == null) {
            return null;
        }
        
        return progresso.obterProximaPropostaParaConfecionar();
    }

    /** 
     * Método que guarda o progresso atual do pedido como atrasado
     * 
     * @param idRestaurante Id do restaurante
     */
    public void guardarProgressoPedidoAtual(String idRestaurante) {
        ProgressoPedido progressoAtual = this.progressoPedidoAtual.get(idRestaurante);
        if (progressoAtual != null) {
            this.pedidosAtrasadosPorRestaurante.get(idRestaurante).add(progressoAtual);
            this.progressoPedidoAtual.put(idRestaurante, null);
        }
    }

    /** 
     * Método que obtém o progresso do pedido atrasado anterior de um restaurante
     * 
     * @param idRestaurante Id do restaurante
     * @param idPedido Id do pedido
     * @return Progresso do pedido atrasado
     */
    public ProgressoPedido obterProgressoAnteriorAtrasado(String idRestaurante, String idPedido) {
        List<ProgressoPedido> atrasados = this.pedidosAtrasadosPorRestaurante.get(idRestaurante);
        for (ProgressoPedido pp : atrasados) {
            if (pp.getPedido().getCodPedido().equals(idPedido)) {
                atrasados.remove(pp);
                return pp;
            }
        }
        return null;
    }

    // ==================== MÉTODOS DE ACESSO AOS DAOs ====================

    /** 
     * Método que remove um menu da base de dados
     * 
     * @param codMenu Código do menu
     * @return Menu removido
     */
    public Menu menuRemove(String codMenu) {
        return this.menuDAO.remove(codMenu);
    }

    /** 
     * Método que obtém todos os menus
     * 
     * @return Coleção de menus
     */
    public Collection<Menu> menuValues() {
        return this.menuDAO.values();
    }

    /** 
     * Método que remove uma proposta da base de dados
     * 
     * @param codProposta Código da proposta
     * @return Proposta removida
     */
    public Proposta propostaRemove(String codProposta) {
        return this.propostaDAO.remove(codProposta);
    }

    /** 
     * Método que obtém uma proposta da base de dados
     * 
     * @param codProposta Código da proposta
     * @return Proposta
     */
    public Proposta propostaGet(String codProposta) {
        return this.propostaDAO.get(codProposta);
    }

    /** 
     * Método que obtém todas as propostas
     * 
     * @return Coleção de propostas
     */
    public Collection<Proposta> propostaValues() {
        return this.propostaDAO.values();
    }

    /** 
     * Método que remove um pedido
     * 
     * @param idPedido Id do pedido
     * @return Pedido removido
     */
    public Pedido pedidoRemove(String idPedido) {
        return this.pedidos.remove(idPedido);
    }

    /** 
     * Método que obtém todos os pedidos
     * 
     * @return Coleção de pedidos
     */
    public Collection<Pedido> pedidoValues() {
        return this.pedidos.values();
    }

    /** 
     * Método que obtém um pedido
     * 
     * @param idPedido Id do pedido
     * @return Pedido
     */
    public Pedido pedidoGet(String idPedido) {
        return this.pedidos.get(idPedido);
    }

    /** 
     * Método que adiciona um alérgenio à base de dados
     * 
     * @param alergenio Nome do alérgenio
     */
    public void propostaAddAlergenio(String alergenio) {
        this.propostaDAO.addAlergenio(alergenio);
    }

    /** 
     * Método que remove um alérgenio da base de dados
     * 
     * @param alergenio Nome do alérgenio
     * @return Nome do alérgenio removido
     */
    public String propostaRemoveAlergenio(String alergenio) {
        return this.propostaDAO.removerAlergenio(alergenio);
    }

    /** 
     * Método que obtém todos os alérgenios
     * 
     * @return Lista de alérgenios
     */
    public List<String> propostaGetAlergenios() {
        return this.propostaDAO.getAlergenios();
    }

    /** 
     * Método que adiciona um ingrediente e os seus alérgenios à base de dados
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @param alergenios Lista de alérgenios
     * @param preco Preço do ingrediente
     */
    public void propostaAddIngredienteEAlergenios(String nomeIngrediente, List<String> alergenios, double preco) {
        this.propostaDAO.addIngredienteEAlergenios(nomeIngrediente, alergenios, preco);
    }

    /** 
     * Método que adiciona um ingrediente à base de dados
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @param preco Preço do ingrediente
     */
    public void propostaAddIngrediente(String nomeIngrediente, double preco) {
        this.propostaDAO.addIngrediente(nomeIngrediente, preco);
    }

    /** 
     * Método que remove um ingrediente da base de dados
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @return Ingrediente removido
     */
    public Ingrediente propostaRemoveIngrediente(String nomeIngrediente) {
        return this.propostaDAO.removerIngrediente(nomeIngrediente);
    }

    /** 
     * Método que obtém todos os ingredientes
     * 
     * @return Lista de ingredientes
     */
    public List<Ingrediente> propostaGetIngredientes() {
        return this.propostaDAO.getIngredientes();
    }

    /** 
     * Método que verifica se um ingrediente existe
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @return true se o ingrediente existir, false caso contrário
     */
    public boolean ingredienteExiste(String nomeIngrediente) {
        if (this.propostaDAO.getIngredientePorNome(nomeIngrediente) != null) {
            return true;
        }
        return false;
    }

    /** 
     * Método que lista os ingredientes de uma proposta
     * 
     * @param codProposta Código da proposta
     * @return String com a lista de ingredientes
     */
    public String listPropostaIngredientes(String codProposta) {
        Proposta p = this.propostaDAO.get(codProposta);
        if (p == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Ingredientes da Proposta ").append(codProposta).append(":\n");
        for (Ingrediente ing : p.getIngredientes()) {
            sb.append("-> ").append(ing.getNome()).append("\n");
        }
        return sb.toString();
    }

    /** 
     * Método que altera os ingredientes de uma proposta num pedido
     * 
     * @param pedido Pedido
     * @param codProposta Código da proposta
     * @param lista Lista de ingredientes
     * @param adicionar true para adicionar, false para remover
     */
    public void alterarIngredientesProposta(Pedido pedido, String codProposta, List<String> lista, boolean adicionar) {
        if (adicionar) {
            for (String nomeIngrediente : lista) {
                pedido.addNota("Acrescentar " + nomeIngrediente + " à proposta " + codProposta + ".");
            }
        } else {
            for (String nomeIngrediente : lista) {
                pedido.addNota("Não usar " + nomeIngrediente + " na proposta " + codProposta + ".");
            }
        }
    }

    /** 
     * Método que altera os ingredientes de uma proposta de um menu num pedido
     * 
     * @param pedido Pedido
     * @param codMenu Código do menu
     * @param codProposta Código da proposta
     * @param lista Lista de ingredientes
     * @param adicionar true para adicionar, false para remover
     */
    public void alterarIngredientesPropostaMenu(Pedido pedido, String codMenu, String codProposta, List<String> lista, boolean adicionar) {
        if (adicionar) {
            for (String nomeIngrediente : lista) {
                pedido.addNota("Acrescentar " + nomeIngrediente + " à proposta " + codProposta + " do menu " + codMenu + ".");
            }
        } else {
            for (String nomeIngrediente : lista) {
                pedido.addNota("Não usar " + nomeIngrediente + " na proposta " + codProposta + " do menu " + codMenu + ".");
            }
        }
    }

    /** 
     * Método que obtém um menu da base de dados
     * 
     * @param codMenu Código do menu
     * @return Menu
     */
    public Menu menuGet(String codMenu) {
        return this.menuDAO.get(codMenu);
    }
    
    /** 
     * Método que lista as propostas de um menu
     * 
     * @param codMenu Código do menu
     * @return String com a lista de propostas
     */
    public String listMenuPropostas(String codMenu) {
        Menu m = this.menuDAO.get(codMenu);
        if (m == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Propostas do Menu ").append(codMenu).append(":\n");
        for (Proposta p : m.getPropostasMenu()) {
            sb.append("-> ").append(p.getIdProposta()).append(": ").append(p.getNome()).append("\n");
        }
        return sb.toString();
    }
}

