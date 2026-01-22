package eathubLN.ssPedidos;

import Exceptions.PedidoNaoExisteException;
import eathubLN.ssCadeia.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/** Interface funcional para a lógica de negócio dos Pedidos */
public interface ISSPedidos {

    /** 
     * Método que devolve um Pedido pendente a partir do seu código e do id do restaurante
     * 
     * @param codPedido Código do Pedido
     * @param idRestaurante Id do Restaurante
     * @return Pedido pendente
     * @throws PedidoNaoExisteException Se o Pedido não existir
     */
    public Pedido getPedidoPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    /** 
     * Método que devolve um Pedido a partir do seu código
     * 
     * @param codPedido Código do Pedido
     * @return Pedido
     * @throws PedidoNaoExisteException Se o Pedido não existir
     */
    public Pedido getPedidoBD(String codPedido) throws PedidoNaoExisteException;

    /** Método que devolve os IDs/Códigos dos Pedidos Pendentes de um restaurante 
     * 
     * @param idRestaurante Id do Restaurante
     * @return Set com os IDs/Códigos dos Pedidos Pendentes do restaurante
     * @throws PedidoNaoExisteException Se o Pedido não existir
    */
    public Set<String> obterIds_CodsPedidos_Pendentes(String idRestaurante);

    /** 
     * Método que verifica se um Pedido é pendente
     * 
     * @param codPedido Código do Pedido
     * @param idRestaurante Id do Restaurante
     * @return true se o Pedido for pendente, false caso contrário
     */
    public boolean ePendente(String codPedido, String idRestaurante);

    /**
     * Método que devolve a Ementa do EatHub
     * 
     * @return Ementa do EatHub
     */
    public Ementa obter_ementa();

    
    public Collection<Pair<String,Ingrediente>> obter_alergenios_vBD(String codPedido) throws PedidoNaoExisteException;

    public Collection<Pair<String,Ingrediente>> obter_alergenios_vPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void regista_pedido(String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void regista_nota(String nota, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void registar_tipo_pedido(String tipo, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void cancelar_pedido(String codPedido, String idRestaurante);

    public String determina_proximo_pedido(String idRestaurante);

    public void regista_novo_tempo_espera(double tempo, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public String criar_Pedido(String idRestaurante);

    public void registar_contribuinte_Pedido(int nc, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void addPedidoQueue(String idPedido, String idRestaurante);

    public String peekPedidoQueue(String idRestaurante);

    public double calcularTempoEsperaInicial(Pedido pedido);

    public String listPropostaIngredientes(String codProposta);

    public void alterarIngredientesProposta(Pedido pedido, String codProposta, List<String> lista, boolean adicionar);

    public void alterarIngredientesPropostaMenu(Pedido pedido, String codMenu, String codProposta, List<String> lista, boolean adicionar);

    public String listMenuPropostas(String codMenu);
    
    public String criar_Pedido_Completo(double tempoEspera, int nContribuinte, String notas, 
                                        List<String> codMenus, List<String> codPropostas, 
                                        String tipoServico, String idRestaurante);

    public String criar_Menu(String nomeMenu, Collection<String> codPropostas, double preco);

    public String criar_Proposta(String nomeProposta, Collection<String> codIngredientes, double preco, List<String> etapas);

    public Collection<String> obterIds_CodsPedidos_FilaEspera(String idRestaurante);

    public boolean menuExiste(String codMenu);

    public boolean propostaExiste(String codProposta);

    public void insert_Maps_Novo_Restaurante(String idRestaurante);

    public ProgressoPedido obterProgressoPedidoAtual(String idRestaurante);

    public void definirProgressoPedidoAtual(String idRestaurante, ProgressoPedido progresso);

    public PropostaEtapa obterProximaPropostaParaConfecionar(String idRestaurante);

    public boolean processarEtapaAtualProposta(String idRestaurante, String idFuncionario, PropostaEtapa propostaEtapa);

    public ProgressoPedido obterProgressoAnteriorAtrasado(String idRestaurante, String idPedido);

    public void guardarProgressoPedidoAtual(String idRestaurante);

    public Menu menuRemove(String codMenu);

    public Collection<Menu> menuValues();

    public Proposta propostaRemove(String codProposta);

    public Collection<Proposta> propostaValues();

    public Proposta propostaGet(String codProposta);

    public Pedido pedidoRemove(String idPedido);

    public Collection<Pedido> pedidoValues();

    public Pedido pedidoGet(String idPedido);

    public void propostaAddAlergenio(String alergenio);

    public String propostaRemoveAlergenio(String alergenio);

    public List<String> propostaGetAlergenios();

    public void propostaAddIngredienteEAlergenios(String nomeIngrediente, List<String> alergenios, double preco);

    public void propostaAddIngrediente(String nomeIngrediente, double preco);

    public Ingrediente propostaRemoveIngrediente(String nomeIngrediente);

    public List<Ingrediente> propostaGetIngredientes();

    public boolean ingredienteExiste(String nomeIngrediente);

    public Menu menuGet(String codMenu);
    
}
