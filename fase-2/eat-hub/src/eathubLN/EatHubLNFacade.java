package eathubLN;

import eathubLN.ssCadeia.*;
import eathubLN.ssComercial.*;
import eathubLN.ssPedidos.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Exceptions.PedidoNaoExisteException;
import Exceptions.PedidoVaiAtrasarException;

/** Facade da camada da lógica de negócio do EatHub */
public class EatHubLNFacade implements IEatHubLN {
    /** Interface do subsistema comercial */
    private ISSComercial ssComercial;
    /** Interface do subsistema de pedidos */
    private ISSPedidos ssPedidos;  
    /** Interface do subsistema de cadeia */
    private ISSCadeia ssCadeia;

    /**
     * Construtor vazio do Facade da camada da lógica de negócio do EatHub
     */
    public EatHubLNFacade() {
        this.ssComercial = new SSComercialFacade();
        this.ssPedidos = new SSPedidosFacade();
        this.ssCadeia = new SSCadeiaFacade();
    }

    /**
     * Método que obtém os códigos dos pedidos pendentes de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Conjunto com os códigos dos pedidos pendentes
     */
    public Set<String> obterIds_CodsPedidos_Pendentes(String idRestaurante) {
        return ssPedidos.obterIds_CodsPedidos_Pendentes(idRestaurante);
    }

    /**
     * Método que verifica se o pagamento de um pedido foi confirmado
     * 
     * @param codPedido Código do pedido
     * @return true se o pagamento foi confirmado, false caso contrário
     */
    public boolean pagamento_confirmado(String codPedido) {
        return ssComercial.pagamento_confirmado(codPedido);
    }

    /** 
     * Método que verifica se um pedido está pendente
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @return true se o pedido estiver pendente, false caso contrário
     */
    public boolean ePendente(String codPedido, String idRestaurante) {
        return ssPedidos.ePendente(codPedido, idRestaurante);
    }

    /** 
     * Método que obtém um pedido pendente
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @return Pedido pendente
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public Pedido getPedidoPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        return ssPedidos.getPedidoPendente(codPedido, idRestaurante);
    }

    /** 
     * Método que obtém um pedido da base de dados
     * 
     * @param codPedido Código do pedido
     * @return Pedido da base de dados
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public Pedido getPedidoBD(String codPedido) throws PedidoNaoExisteException {
        return ssPedidos.getPedidoBD(codPedido);
    }

    /** 
     * Método que obtém a ementa
     * 
     * @return Ementa
     */
    public Ementa obter_ementa() {
        return ssPedidos.obter_ementa();
    }

    /** 
     * Método que obtém os alérgenios de um pedido na base de dados
     * 
     * @param codPedido Código do pedido
     * @return Coleção de pares com o código do alérgenio e o ingrediente
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public Collection<Pair<String,Ingrediente>> obter_alergenios_vBD(String codPedido) throws PedidoNaoExisteException {
        return ssPedidos.obter_alergenios_vBD(codPedido);
    }

    /** 
     * Método que obtém os alérgenios de um pedido pendente
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @return Coleção de pares com o código do alérgenio e o ingrediente
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public Collection<Pair<String,Ingrediente>> obter_alergenios_vPendente(String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        return ssPedidos.obter_alergenios_vPendente(codPedido, idRestaurante);
    }

    /** 
     * Método que regista um pedido
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public void regista_pedido(String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        ssPedidos.regista_pedido(codPedido, idRestaurante);
    }

    /** 
     * Método que regista uma nota num pedido
     * 
     * @param nota Nota a registar
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public void regista_nota(String nota, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        ssPedidos.regista_nota(nota, codPedido, idRestaurante);
    }

    /** 
     * Método que regista o tipo de um pedido
     * 
     * @param tipo Tipo do pedido
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public void registar_tipo_pedido(String tipo, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        ssPedidos.registar_tipo_pedido(tipo, codPedido, idRestaurante);
    }

    /** 
     * Método que cancela um pedido
     * 
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     */
    public void cancelar_pedido(String codPedido, String idRestaurante) {
        ssPedidos.cancelar_pedido(codPedido, idRestaurante);
    }

    /** 
     * Método que determina o próximo pedido a ser processado num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Código do próximo pedido
     */
    public String determina_proximo_pedido(String idRestaurante) {
        return ssPedidos.determina_proximo_pedido(idRestaurante);
    }

    /** 
     * Método que regista um novo tempo de espera para um pedido
     * 
     * @param tempo Novo tempo de espera
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public void regista_novo_tempo_espera(double tempo, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        ssPedidos.regista_novo_tempo_espera(tempo, codPedido, idRestaurante);
    }

    /** 
     * Método que regista um pagamento
     * 
     * @param valor Valor do pagamento
     * @param codPedido Código do pedido
     * @param metodoPagamento Método de pagamento
     * @return Identificador do pagamento
     */
    public String regista_pagamento(double valor, String codPedido, String metodoPagamento) {
        return ssComercial.regista_pagamento(valor, codPedido, metodoPagamento);
    }

    /** 
     * Método que gera uma fatura
     * 
     * @param idPagamento Identificador do pagamento
     * @param pedido Pedido associado à fatura
     * @return Fatura gerada
     */
    public Fatura gera_fatura(String idPagamento, Pedido pedido) {
        return ssComercial.gera_fatura(idPagamento, pedido);
    }

    /** 
     * Método que gera um talão
     * 
     * @param codPedido Código do pedido
     * @return Talão gerado
     */
    public Talao gera_talao(String codPedido) {
        return ssComercial.gera_talao(codPedido);
    }

    /** 
     * Método que regista um contribuinte num pedido
     * 
     * @param nc Número de contribuinte
     * @param codPedido Código do pedido
     * @param idRestaurante Identificador do restaurante
     * @throws PedidoNaoExisteException Exceção lançada caso o pedido não exista
     */
    public void registar_contribuinte_Pedido(int nc, String codPedido, String idRestaurante) throws PedidoNaoExisteException {
        ssPedidos.registar_contribuinte_Pedido(nc, codPedido, idRestaurante);
    }

    /** 
     * Método que obtém as estatísticas de um restaurante num determinado período
     * 
     * @param idRestaurante Identificador do restaurante
     * @param periodo Período para o qual se querem as estatísticas
     * @return Estatísticas do restaurante no período indicado
     */
    public Estatistica obter_estatisticas_restaurante(String idRestaurante, String periodo) {
        return ssCadeia.obter_estatisticas_restaurante(idRestaurante, periodo);
    }

    /** 
     * Método que obtém as estatísticas da cadeia num determinado período
     * 
     * @param periodo Período para o qual se querem as estatísticas
     * @return Estatísticas da cadeia no período indicado
     */
    public Estatistica obter_estatisticas_cadeia(String periodo) {
        return ssCadeia.obter_estatisticas_cadeia(periodo);
    }

    /** 
     * Método que obtém os identificadores dos restaurantes
     * 
     * @return Coleção de identificadores dos restaurantes
     */
    public Collection<String> obter_IDs_restaurantes() {
        return ssCadeia.obter_IDs_restaurantes();
    }

    /** 
     * Método que cria um pedido
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Identificador do pedido criado
     */
    public String criar_Pedido(String idRestaurante) {
        return ssPedidos.criar_Pedido(idRestaurante);
    }

    /** 
     * Método que adiciona um pedido à fila de espera de um restaurante
     * 
     * @param idPedido Identificador do pedido
     * @param idRestaurante Identificador do restaurante
     */
    public void addPedidoQueue(String idPedido, String idRestaurante) {
        ssPedidos.addPedidoQueue(idPedido, idRestaurante);
    }

    /** 
     * Método que cria um pedido completo
     * 
     * @param tempoEspera Tempo de espera do pedido
     * @param nContribuinte Número de contribuinte associado ao pedido
     * @param notas Notas do pedido
     * @param codMenus Códigos dos menus do pedido
     * @param codPropostas Códigos das propostas do pedido
     * @param tipoServico Tipo de serviço do pedido
     * @param idRestaurante Identificador do restaurante
     * @return Identificador do pedido criado
     */
    public String criar_Pedido_Completo(double tempoEspera, int nContribuinte, String notas, 
                                        List<String> codMenus, List<String> codPropostas, 
                                        String tipoServico, String idRestaurante) {
        return ssPedidos.criar_Pedido_Completo(tempoEspera, nContribuinte, notas, codMenus, codPropostas, tipoServico, idRestaurante);
    }

    /** 
     * Método que cria um menu
     * 
     * @param nomeMenu Nome do menu
     * @param codPropostas Códigos das propostas do menu
     * @param preco Preço do menu
     * @return Código do menu criado
     */
    public String criar_Menu(String nomeMenu, Collection<String> codPropostas, double preco) {
        return ssPedidos.criar_Menu(nomeMenu, codPropostas, preco);
    }

    /** 
     * Método que cria uma proposta
     * 
     * @param nomeProposta Nome da proposta
     * @param codIngredientes Códigos dos ingredientes da proposta
     * @param preco Preço da proposta
     * @param etapas Etapas da proposta
     * @return Código da proposta criada
     */
    public String criar_Proposta(String nomeProposta, Collection<String> codIngredientes, double preco, List<String> etapas) {
        return ssPedidos.criar_Proposta(nomeProposta, codIngredientes, preco, etapas);
    }

    /** 
     * Método que cria um funcionário
     * 
     * @param nome Nome do funcionário
     * @param posto Posto do funcionário
     * @param tarefa Tarefa do funcionário
     * @param idRestaurante Identificador do restaurante
     * @param tipo Tipo do funcionário
     * @return Identificador do funcionário criado
     */
    public String criar_Funcionario(String nome, String posto, String tarefa, String idRestaurante, String tipo, String password) {
        return ssCadeia.criar_Funcionario(nome, posto, tarefa, idRestaurante, tipo, password);
    }

    /** 
     * Método que cria um restaurante
     * 
     * @param nome Nome do restaurante
     * @param chefe Chefe do restaurante
     * @param funcionarios Lista de funcionários do restaurante
     * @return Identificador do restaurante criado
     */
    public String criar_Restaurante(String nome, String chefe, List<String> funcionarios) {
        String res = ssCadeia.criar_Restaurante(nome, chefe, funcionarios);
        ssPedidos.insert_Maps_Novo_Restaurante(res);
        return res;
    }

    /** 
     * Método que verifica se um identificador pertence a um funcionário, chefe de restaurante ou gestor
     * 
     * @param id Identificador a verificar
     * @return true se o identificador pertencer a um funcionário, chefe de restaurante ou gestor, false caso contrário
     */
    public boolean eFuncionario(String id) {
        return ssCadeia.eFuncionario(id);
    }

    /** 
     * Método que verifica se um identificador pertence a um chefe de restaurante
     * 
     * @param id Identificador a verificar
     * @return true se o identificador pertencer a um chefe de restaurante, false caso contrário
     */
    public boolean eChefeRestaurante(String id) {
        return ssCadeia.eChefeRestaurante(id);
    }

    /** 
     * Método que verifica se um identificador pertence a um gestor
     * 
     * @param id Identificador a verificar
     * @return true se o identificador pertencer a um gestor, false caso contrário
     */
    public boolean eGestor(String id) {
        return ssCadeia.eGestor(id);
    }

    /** 
     * Método que adiciona ingredientes ao stock de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @param codIngrediente Código do ingrediente
     * @param quantidade Quantidade a adicionar
     * @return true se a operação foi bem sucedida, false caso contrário
     */
    public boolean adicionar_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade) {
        return ssCadeia.adicionar_ingrediente_stock_restaurante(idRestaurante, codIngrediente, quantidade);
    }

    /** 
     * Método que remove ingredientes do stock de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @param codIngrediente Código do ingrediente
     * @param quantidade Quantidade a remover
     * @return true se a operação foi bem sucedida, false caso contrário
     */
    public boolean remover_ingrediente_stock_restaurante(String idRestaurante, String codIngrediente, int quantidade) {
        return ssCadeia.remover_ingrediente_stock_restaurante(idRestaurante, codIngrediente, quantidade);
    }

    /** 
     * Método que espreita o próximo pedido na fila de espera de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Identificador do próximo pedido na fila de espera
     */
    public String peekPedidoQueue(String idRestaurante) {
        return ssPedidos.peekPedidoQueue(idRestaurante);
    }

    /** 
     * Método que autentica um funcionário
     * 
     * @param id Identificador do funcionário
     * @param password Senha do funcionário
     * @return true se a autenticação foi bem sucedida, false caso contrário
     */
    public boolean autenticarFuncionario(String id, String password) {
        return ssCadeia.autenticarFuncionario(id, password);
    }

    /** 
     * Método que autentica um chefe de restaurante
     * 
     * @param id Identificador do chefe de restaurante
     * @param password Senha do chefe de restaurante
     * @return true se a autenticação foi bem sucedida, false caso contrário
     */
    public boolean autenticarChefeRestaurante(String id, String password) {
        return ssCadeia.autenticarChefeRestaurante(id, password);
    }

    /** 
     * Método que autentica um gestor
     * 
     * @param id Identificador do gestor
     * @param password Senha do gestor
     * @return true se a autenticação foi bem sucedida, false caso contrário
     */
    public boolean autenticarGestor(String id, String password) {
        return ssCadeia.autenticarGestor(id, password);
    }

    /** 
     * Método que obtém as mensagens do gestor
     * 
     * @return Coleção de mensagens do gestor
     */
    public Collection<MensagemGestor> obter_mensagens_Gestor() {
        return ssCadeia.obter_mensagens_Gestor();
    }

    /** 
     * Método que envia mensagem de um gestor
     * 
     * @param conteudo Conteúdo da mensagem
     * @param idGestor Identificador do gestor
     * @return true se a mensagem foi enviada com sucesso, false caso contrário
     */
    public boolean enviar_mensagem_gestor(String conteudo, String idGestor) {
        return ssCadeia.enviar_mensagem_gestor(conteudo, idGestor);
    }

    /** 
     * Método que remove uma mensagem do gestor
     * 
     * @param idMensagem Identificador da mensagem
     * @return Mensagem removida
     */
    public MensagemGestor remover_mensagem_gestor(int idMensagem) {
        return ssCadeia.remover_mensagem_gestor(idMensagem);
    }

    /** 
     * Método que obtém o gestor
     * 
     * @return Gestor
     */
    public Gestor getGestor() {
        return ssCadeia.getGestor();
    }

    /** 
     * Método que obtém os códigos dos pedidos na fila de espera de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Coleção de códigos dos pedidos na fila de espera
     */
    public Collection<String> obterIds_CodsPedidos_FilaEspera(String idRestaurante) {
        return ssPedidos.obterIds_CodsPedidos_FilaEspera(idRestaurante);
    }

    /** 
     * Método que verifica se um menu existe
     * 
     * @param codMenu Código do menu
     * @return true se o menu existir, false caso contrário
     */
    public boolean menuExiste(String codMenu) {
        return ssPedidos.menuExiste(codMenu);
    }

    /** 
     * Método que verifica se uma proposta existe
     * 
     * @param codProposta Código da proposta
     * @return true se a proposta existir, false caso contrário
     */
    public boolean propostaExiste(String codProposta) {
        return ssPedidos.propostaExiste(codProposta);
    }

    /** 
     * Método que obtém os códigos dos restaurantes
     * 
     * @return Conjunto com os códigos dos restaurantes
     */
    public Set<String> get_Cods_Restaurantes() {
        return ssCadeia.get_Cods_Restaurantes();
    }

    /** 
     * Método que obtém o restaurante de um funcionário
     * 
     * @param idFuncionario Identificador do funcionário
     * @return Identificador do restaurante do funcionário
     */
    public String getRestauranteDeFuncionario(String idFuncionario) {
        return ssCadeia.getRestauranteDeFuncionario(idFuncionario);
    }

    /** 
     * Método que cria o histórico de um pedido
     * 
     * @param pedido Pedido a adicionar ao histórico
     * @param idRestaurante Identificador do restaurante
     * @return Identificador do histórico criado
     */
    public String criar_Historico(Pedido pedido, String idRestaurante) {
        return ssCadeia.criar_Historico(pedido, idRestaurante);
    }

    /** 
     * Método que obtém o progresso atual de um pedido num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Progresso atual do pedido
     */
    public ProgressoPedido obterProgressoPedidoAtual(String idRestaurante) {
        return ssPedidos.obterProgressoPedidoAtual(idRestaurante);
    }

    /** 
     * Método que define o progresso de um pedido atual num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @param progresso Progresso a definir
     */
    public void definirProgressoPedidoAtual(String idRestaurante, ProgressoPedido progresso) {
        ssPedidos.definirProgressoPedidoAtual(idRestaurante, progresso);
    }

    /** 
     * Método que obtém a próxima proposta a confeccionar num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Próxima proposta a confeccionar
     */
    public PropostaEtapa obterProximaPropostaParaConfecionar(String idRestaurante) {
        return ssPedidos.obterProximaPropostaParaConfecionar(idRestaurante);
    }

    /** 
     * Método que processa a etapa atual de uma proposta num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @param idFuncionario Identificador do funcionário
     * @param propostaEtapa Proposta e etapa a processar
     * @param idPedido Identificador do pedido
     * @return true se a etapa foi processada com sucesso, false caso contrário
     * @throws PedidoVaiAtrasarException Exceção lançada caso o pedido vá atrasar
     */
    public boolean processarEtapaAtualProposta(String idRestaurante, String idFuncionario, PropostaEtapa propostaEtapa, String idPedido) throws PedidoVaiAtrasarException {
        if (propostaEtapa != null && propostaEtapa.getIndiceEtapa() == 0){//No inicio da proposta
            Proposta proposta = this.propostaGet(propostaEtapa.getCodProposta());
            List<String> ingredientesEmFalta = ssCadeia.verificaIngredientesEmFaltaRestaurante(idRestaurante, proposta.getIngredientes());

            if (ingredientesEmFalta == null) return false;
            else if (ingredientesEmFalta.size() > 0){
                for(String s : ingredientesEmFalta){
                    ssCadeia.adicionar_ingrediente_stock_restaurante(idRestaurante, s, 5);
                }

                String str = ingredientesEmFalta.toString();
                
                try {
                    Pedido pedido = ssPedidos.getPedidoBD(idPedido);
                    double tempoAtual = pedido.getTempoEspera();
                    double tempoAdicional = 5.0; // Tempo adicional
                    double novoTempoEspera = tempoAtual + (tempoAdicional * ingredientesEmFalta.size());

                    ssPedidos.guardarProgressoPedidoAtual(idRestaurante);

                    ssPedidos.regista_novo_tempo_espera(novoTempoEspera, idPedido, idRestaurante);

                    throw new PedidoVaiAtrasarException(idPedido, novoTempoEspera, str);
                } catch (PedidoNaoExisteException e) {
                    return false;
                }

            }
        }
        return ssPedidos.processarEtapaAtualProposta(idRestaurante, idFuncionario, propostaEtapa);
    }

    /** 
     * Método que regista os ingredientes utilizados numa proposta num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @param codProposta Código da proposta
     * @return true se o registo foi bem sucedido, false caso contrário
     */
    public boolean registar_Ingredientes_Utilizados_Proposta(String idRestaurante, String codProposta) {
        List<Ingrediente> ingredientesUtilizados = ssPedidos.propostaGet(codProposta).getIngredientes();
        return ssCadeia.registar_Ingredientes_Utilizados_Proposta(idRestaurante, ingredientesUtilizados);
    }

    /** 
     * Método que obtém o progresso anterior atrasado de um pedido num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @param idPedido Identificador do pedido
     * @return Progresso anterior atrasado do pedido
     */
    public ProgressoPedido obterProgressoAnteriorAtrasado(String idRestaurante, String idPedido) {
        return ssPedidos.obterProgressoAnteriorAtrasado(idRestaurante, idPedido);
    }

    /** 
     * Método que obtém o identificador do funcionário responsável por uma tarefa igual a uma etapa num restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @param nomeEtapa Nome da etapa
     * @return Identificador do funcionário responsável pela tarefa
     */
    public String obterIdFuncionarioTarefa_Igual_Etapa(String idRestaurante, String nomeEtapa) {
        return ssCadeia.obterIdFuncionarioTarefa_Igual_Etapa(idRestaurante, nomeEtapa);
    }
    
    /**
     * Método que retorna uma coleção de faturas
     * 
     * @return Coleção de faturas
     */
    public Collection<Fatura> faturaValues() {
        return ssComercial.faturaValues();
    }

    /** 
     * Método que remove uma fatura
     * 
     * @param idFatura Identificador da fatura
     * @return Fatura removida
     */
    public Fatura faturaRemove(String idFatura) {
        return ssComercial.faturaRemove(idFatura);
    }

    /**     
     * Método que remove um pagamento
     * 
     * @param idPagamento Identificador do pagamento
     * @return Pagamento removido
     */
    public Pagamento pagamentoRemove(String idPagamento) {
        return ssComercial.pagamentoRemove(idPagamento);
    }

    /** 
     * Método que retorna uma coleção de pagamentos
     * 
     * @return Coleção de pagamentos
     */
    public Collection<Pagamento> pagamentoValues() {
        return ssComercial.pagamentoValues();
    }

    /**
     * Método que remove um talão
     * 
     * @param codTalao Código do talão
     * @return Talão removido
     */
    public Talao talaoRemove(String codTalao) {
        return ssComercial.talaoRemove(codTalao);
    }

    /**
     * Método que retorna uma coleção de talões
     * 
     * @return Coleção de talões
     */
    public Collection<Talao> talaoValues() {
        return ssComercial.talaoValues();
    }

    // DAO Access Methods - ssPedidos

    /**
     * Método que remove um menu
     * 
     * @param codMenu Código do menu
     * @return Menu removido
     */
    public Menu menuRemove(String codMenu) {
        return ssPedidos.menuRemove(codMenu);
    }

    /** 
     * Método que retorna uma coleção de menus
     * 
     * @return Coleção de menus
     */
    public Collection<Menu> menuValues() {
        return ssPedidos.menuValues();
    }

    /** 
     * Método que obtém um menu
     * 
     * @param codMenu Código do menu
     * @return Menu obtido
     */
    public Menu menuGet(String codMenu) {
        return ssPedidos.menuGet(codMenu);
    }

    /** 
     * Método que remove uma proposta
     * 
     * @param codProposta Código da proposta
     * @return Proposta removida
     */
    public Proposta propostaRemove(String codProposta) {
        return ssPedidos.propostaRemove(codProposta);
    }

    /** 
     * Método que obtém uma proposta
     * 
     * @param codProposta Código da proposta
     * @return Proposta obtida
     */
    public Proposta propostaGet(String codProposta) {
        return ssPedidos.propostaGet(codProposta);
    }

    /** 
     * Método que retorna uma coleção de propostas
     * 
     * @return Coleção de propostas
     */
    public Collection<Proposta> propostaValues() {
        return ssPedidos.propostaValues();
    }

    /** 
     * Método que remove um pedido
     * 
     * @param codPedido Código do pedido
     * @return Pedido removido
     */
    public Pedido pedidoRemove(String codPedido) {
        return ssPedidos.pedidoRemove(codPedido);
    }

    /** 
     * Método que retorna uma coleção de pedidos
     * 
     * @return Coleção de pedidos
     */
    public Collection<Pedido> pedidoValues() {
        return ssPedidos.pedidoValues();
    }

    /** 
     * Método que obtém um pedido
     * 
     * @param codPedido Código do pedido
     * @return Pedido obtido
     */
    public Pedido pedidoGet(String codPedido) {
        return ssPedidos.pedidoGet(codPedido);
    }

    /** 
     * Método que adiciona um alérgenio à base de dados
     * 
     * @param alergenio Alérgenio a adicionar
     */
    public void propostaAddAlergenio(String alergenio) {
        ssPedidos.propostaAddAlergenio(alergenio);
    }

    /** 
     * Método que remove um alérgenio da base de dados
     * 
     * @param alergenio Alérgenio a remover
     * @return Código do alérgenio removido
     */
    public String propostaRemoveAlergenio(String alergenio) {
        return ssPedidos.propostaRemoveAlergenio(alergenio);
    }

    /** 
     * Método que retorna uma lista de alérgenios
     * 
     * @return Lista de alérgenios
     */
    public List<String> propostaGetAlergenios() {
        return ssPedidos.propostaGetAlergenios();
    }

    /** 
     * Método que adiciona um ingrediente com alérgenios à base de dados
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @param alergenios Lista de alérgenios do ingrediente
     * @param preco Preço do ingrediente
     */
    public void propostaAddIngredienteEAlergenios(String nomeIngrediente, List<String> alergenios, double preco) {
        ssPedidos.propostaAddIngredienteEAlergenios(nomeIngrediente, alergenios, preco);
    }

    /** 
     * Método que adiciona um ingrediente
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @param preco Preço do ingrediente
     */
    public void propostaAddIngrediente(String nomeIngrediente, double preco) {
        ssPedidos.propostaAddIngrediente(nomeIngrediente, preco);
    }

    /** 
     * Método que remove um ingrediente
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @return Ingrediente removido
     */
    public Ingrediente propostaRemoveIngrediente(String nomeIngrediente) {
        return ssPedidos.propostaRemoveIngrediente(nomeIngrediente);
    }

    /** 
     * Método que retorna uma lista de ingredientes
     * 
     * @return Lista de ingredientes
     */
    public List<Ingrediente> propostaGetIngredientes() {
        return ssPedidos.propostaGetIngredientes();
    }

    /**
     * Método que obtém um funcionário
     * 
     * @param idFuncionario Identificador do funcionário
     * @return Funcionário obtido
     */
    public Funcionario funcionarioGet(String idFuncionario) {
        return ssCadeia.funcionarioGet(idFuncionario);
    }

    /** 
     * Método que remove um funcionário
     * 
     * @param idFuncionario Identificador do funcionário
     * @return Funcionário removido
     */
    public Funcionario funcionarioRemove(String idFuncionario) {
        return ssCadeia.funcionarioRemove(idFuncionario);
    }

    /** 
     * Método que retorna uma coleção de funcionários
     * 
     * @return Coleção de funcionários
     */
    public Collection<Funcionario> funcionarioValues() {
        return ssCadeia.funcionarioValues();
    }

    /** 
     * Método que retorna uma coleção de identificadores de funcionários de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Coleção de identificadores de funcionários
     */
    public Collection<String> funcionarioGetIdsFuncionariosRestaurante(String idRestaurante) {
        return ssCadeia.funcionarioGetIdsFuncionariosRestaurante(idRestaurante);
    }

    /**
     * Método que remove um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Restaurante removido
     */
    public Restaurante restauranteRemove(String idRestaurante) {
        return ssCadeia.restauranteRemove(idRestaurante);
    }

    /**
     * Método que retorna uma coleção de restaurantes
     * 
     * @return Coleção de restaurantes
     */
    public Collection<Restaurante> restauranteValues() {
        return ssCadeia.restauranteValues();
    }

    /** 
     * Método que obtém o stock de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Mapa com o stock do restaurante
     */
    public Set<Map.Entry<Pair<String, String>, Integer>> stockEntrySetRestaurante(String idRestaurante) {
        return ssCadeia.stockEntrySetRestaurante(idRestaurante);
    }

    /** 
     * Método que obtém o histórico de pedidos da cadeia
     * 
     * @return Coleção do histórico de pedidos da cadeia
     */
    public Collection<Historico> getHistoricoPedidosCadeia() {
        return ssCadeia.getHistoricoPedidosCadeia();
    }

    /** 
     * Método que obtém o histórico de pedidos de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Coleção do histórico de pedidos do restaurante
     */
    public Collection<Historico> getHistoricoPedidosRestaurante(String idRestaurante) {
        return ssCadeia.getHistoricoPedidosRestaurante(idRestaurante);
    }

    /** 
     * Método que remove um histórico de pedidos
     * 
     * @param idHistorico Identificador do histórico
     * @return Histórico removido
     */
    public Historico remover_historico_pedidos(String idHistorico) {
        return ssCadeia.remover_historico_pedidos(idHistorico);
    }

    /** 
     * Método que lista os ingredientes de uma proposta
     * 
     * @param codProposta Código da proposta
     * @return Lista de ingredientes da proposta
     */
    public String listPropostaIngredientes(String codProposta) {
        return ssPedidos.listPropostaIngredientes(codProposta);
    }

    /** 
     * Método que altera os ingredientes de uma proposta num pedido
     * 
     * @param pedido Pedido a alterar
     * @param codProposta Código da proposta
     * @param lista Lista de ingredientes a adicionar ou remover
     * @param adicionar true para adicionar ingredientes, false para remover
     */
    public void alterarIngredientesProposta(Pedido pedido, String codProposta, List<String> lista, boolean adicionar) {
        ssPedidos.alterarIngredientesProposta(pedido, codProposta, lista, adicionar);
    }

    /** 
     * Método que altera os ingredientes de uma proposta num menu de um pedido
     * 
     * @param pedido Pedido a alterar
     * @param codMenu Código do menu
     * @param codProposta Código da proposta
     * @param lista Lista de ingredientes a adicionar ou remover
     * @param adicionar true para adicionar ingredientes, false para remover
     */
    public void alterarIngredientesPropostaMenu(Pedido pedido, String codMenu, String codProposta, List<String> lista, boolean adicionar) {
        ssPedidos.alterarIngredientesPropostaMenu(pedido, codMenu, codProposta, lista, adicionar);
    }

    /** 
     * Método que verifica se um ingrediente existe
     * 
     * @param nomeIngrediente Nome do ingrediente
     * @return true se o ingrediente existir, false caso contrário
     */
    public boolean ingredienteExiste(String nomeIngrediente) {
        return ssPedidos.ingredienteExiste(nomeIngrediente);
    }

    /** 
     * Método que lista as propostas de um menu
     * 
     * @param codMenu Código do menu
     * @return Lista de propostas do menu
     */
    public String listMenuPropostas(String codMenu){
        return ssPedidos.listMenuPropostas(codMenu);
    }

    /** 
     * Método que obtém o chefe de um restaurante
     * 
     * @param idRestaurante Identificador do restaurante
     * @return Nome do chefe do restaurante
     */
    public String getChefeRestaurante(String idRestaurante) {
        return ssCadeia.getChefeRestaurante(idRestaurante);
    }
}
