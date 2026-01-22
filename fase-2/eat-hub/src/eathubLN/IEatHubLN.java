package eathubLN;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import eathubLN.ssCadeia.*;
import eathubLN.ssComercial.*;
import eathubLN.ssPedidos.*;

import Exceptions.PedidoNaoExisteException;

import Exceptions.PedidoVaiAtrasarException;

public interface IEatHubLN {

    public Pedido getPedidoPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public Pedido getPedidoBD(String codPedido) throws PedidoNaoExisteException;

    public Set<String> obterIds_CodsPedidos_Pendentes(String idRestaurante);

    public boolean ePendente(String codPedido, String idRestaurante);

    public boolean pagamento_confirmado(String codPedido);

    public Ementa obter_ementa();

    public Collection<Pair<String,Ingrediente>> obter_alergenios_vBD(String codPedido) throws PedidoNaoExisteException;

    public Collection<Pair<String,Ingrediente>> obter_alergenios_vPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void regista_pedido(String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void regista_nota(String nota, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void registar_tipo_pedido(String tipo, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public void cancelar_pedido(String codPedido, String idRestaurante);

    public String determina_proximo_pedido(String idRestaurante);

    public void regista_novo_tempo_espera(double tempo, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public String regista_pagamento(double valor, String codPedido, String metodoPagamento);

    public Fatura gera_fatura(String idPagamento, Pedido pedido);

    public Talao gera_talao(String codPedido);

    public void registar_contribuinte_Pedido(int nc, String codPedido, String idRestaurante) throws PedidoNaoExisteException;

    public Estatistica obter_estatisticas_restaurante(String idRestaurante, String periodo);

    public Estatistica obter_estatisticas_cadeia(String periodo);

    public Collection<String> obter_IDs_restaurantes();

    public String criar_Pedido(String idRestaurante);

    public void addPedidoQueue(String idPedido, String idRestaurante);

    public String peekPedidoQueue(String idRestaurante);
    
    public String criar_Pedido_Completo(double tempoEspera, int nContribuinte, String notas, 
                                        List<String> codMenus, List<String> codPropostas, 
                                        String tipoServico, String idRestaurante);

    public String criar_Menu(String nomeMenu, Collection<String> codPropostas, double preco);

    public String criar_Proposta(String nomeProposta, Collection<String> codIngredientes, double preco, List<String> etapas);

    public String criar_Funcionario(String nome, String posto, String tarefa, String idRestaurante, String tipo, String password);

    public String criar_Restaurante(String nome, String chefe, List<String> funcionarios);

    public String criar_Historico(Pedido pedido, String idRestaurante);

    public boolean adicionar_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade);

    public boolean remover_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade);

    public boolean eFuncionario(String id);

    public boolean eChefeRestaurante(String id);

    public boolean eGestor(String id);

    public Collection<MensagemGestor> obter_mensagens_Gestor();

    public boolean enviar_mensagem_gestor(String conteudo, String idGestor);

    public MensagemGestor remover_mensagem_gestor(int idMensagem);

    public Gestor getGestor();

    public Collection<String> obterIds_CodsPedidos_FilaEspera(String idRestaurante);

    public boolean menuExiste(String codMenu);

    public boolean propostaExiste(String codProposta);

    public Set<String> get_Cods_Restaurantes();

    public String getRestauranteDeFuncionario(String idFuncionario);

    public ProgressoPedido obterProgressoPedidoAtual(String idRestaurante);

    public void definirProgressoPedidoAtual(String idRestaurante, ProgressoPedido progresso) ;

    public PropostaEtapa obterProximaPropostaParaConfecionar(String idRestaurante);

    public boolean processarEtapaAtualProposta(String idRestaurante, String idFuncionario, PropostaEtapa propostaEtapa, String idPedido) throws PedidoVaiAtrasarException;

    public String obterIdFuncionarioTarefa_Igual_Etapa(String idRestaurante, String nomeEtapa);

    public ProgressoPedido obterProgressoAnteriorAtrasado(String idRestaurante, String idPedido);

    public Collection<Fatura> faturaValues();

    public Fatura faturaRemove(String idFatura);

    public Funcionario funcionarioGet(String idFuncionario);

    public Funcionario funcionarioRemove(String idFuncionario);

    public Collection<Funcionario> funcionarioValues();

    public Collection<String> funcionarioGetIdsFuncionariosRestaurante(String idRestaurante);

    public Menu menuRemove(String codMenu);

    public Collection<Menu> menuValues();

    public Proposta propostaRemove(String codProposta);

    public Collection<Proposta> propostaValues();

    public Proposta propostaGet(String codProposta);

    public Pagamento pagamentoRemove(String idPagamento);

    public Collection<Pagamento> pagamentoValues();

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

    public Restaurante restauranteRemove(String idRestaurante);

    public Collection<Restaurante> restauranteValues();

    public Set<Entry<Pair<String, String>, Integer>> stockEntrySetRestaurante(String idRestaurante);

    public Talao talaoRemove(String idTalao);

    public Collection<Talao> talaoValues();

    public Collection<Historico> getHistoricoPedidosCadeia();

    public Collection<Historico> getHistoricoPedidosRestaurante(String idRestaurante);

    public Historico remover_historico_pedidos(String idHistorico);

    public boolean ingredienteExiste(String nomeIngrediente);

    public Menu menuGet(String codMenu);

    public boolean registar_Ingredientes_Utilizados_Proposta(String idRestaurante, String codProposta);

    public String getChefeRestaurante(String idRestaurante);
    
    public boolean autenticarFuncionario(String id, String password);

    public boolean autenticarChefeRestaurante(String id, String password);

    public boolean autenticarGestor(String id, String password);

    public String listPropostaIngredientes(String codProposta);

    public void alterarIngredientesProposta(Pedido pedido, String codProposta, List<String> lista, boolean adicionar);

    public void alterarIngredientesPropostaMenu(Pedido pedido, String codMenu, String codProposta, List<String> lista, boolean adicionar);

    public String listMenuPropostas(String codMenu);
    
}
