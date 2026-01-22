package eathubLN.ssCadeia;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import eathubLN.ssPedidos.Ingrediente;
import eathubLN.ssPedidos.Pedido;

public interface ISSCadeia {

    public Estatistica obter_estatisticas_restaurante(String idRestaurante, String periodo);

    public Estatistica obter_estatisticas_cadeia(String periodo);

    public Collection<String> obter_IDs_restaurantes();
    
    public String criar_Funcionario(String nome, String posto, String tarefa, String idRestaurante, String tipo, String password);

    public String criar_Historico(Pedido pedido, String idRestaurante);

    public boolean adicionar_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade);

    public boolean remover_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade);

    public String criar_Restaurante(String nome, String chefe, List<String> funcionarios);

    public boolean eFuncionario(String id);

    public boolean eChefeRestaurante(String id);

    public boolean eGestor(String id);

    public Collection<MensagemGestor> obter_mensagens_Gestor();

    public boolean enviar_mensagem_gestor(String conteudo, String idGestor);

    public MensagemGestor remover_mensagem_gestor(int idMensagem);

    public Set<String> get_Cods_Restaurantes();

    public String getRestauranteDeFuncionario(String idFuncionario);

    public String obterIdFuncionarioTarefa_Igual_Etapa(String idRestaurante, String nomeEtapa);

    public Funcionario funcionarioGet(String idFuncionario);

    public Funcionario funcionarioRemove(String idFuncionario);

    public Collection<Funcionario> funcionarioValues();

    public Collection<String> funcionarioGetIdsFuncionariosRestaurante(String idRestaurante);

    public Restaurante restauranteRemove(String idRestaurante);

    public Collection<Restaurante> restauranteValues();

    public Set<Entry<Pair<String, String>, Integer>> stockEntrySetRestaurante(String idRestaurante);

    public Collection<Historico> getHistoricoPedidosCadeia();

    public Collection<Historico> getHistoricoPedidosRestaurante(String idRestaurante);

    public Historico remover_historico_pedidos(String idHistorico);

    public List<String> verificaIngredientesEmFaltaRestaurante(String idRestaurante, Collection<Ingrediente> ingredientes);

    public boolean registar_Ingredientes_Utilizados_Proposta(String idRestaurante, List<Ingrediente> lista);

    public String getChefeRestaurante(String idRestaurante);

    public boolean autenticarFuncionario(String id, String password);

    public boolean autenticarChefeRestaurante(String id, String password);

    public boolean autenticarGestor(String id, String password);

    public Gestor getGestor();
}
