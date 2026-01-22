package eathubLN.ssComercial;

import java.util.Collection;

import eathubDL.EstruturasDAO.FaturaDAO;
import eathubDL.EstruturasDAO.TalaoDAO;
import eathubDL.EstruturasDAO.PagamentoDAO;

import eathubLN.ssPedidos.Pedido;

/** Facade do subsistema comercial */
public class SSComercialFacade implements ISSComercial {
    /** Instância do DAO de faturas */
    private FaturaDAO faturas;
    /** Instância do DAO de talões */
    private TalaoDAO taloes;
    /** Instância do DAO de pagamentos */
    private PagamentoDAO pagamentos;

    /**
     * Construtor do Facade do subsistema comercial
     */
    public SSComercialFacade() {
        this.faturas = FaturaDAO.getInstance();
        this.taloes = TalaoDAO.getInstance();
        this.pagamentos = PagamentoDAO.getInstance();
    }

    /**
     * Método que regista um pagamento
     * 
     * @param valor          Valor do pagamento
     * @param codPedido      Código do pedido associado ao pagamento
     * @param metodoPagamento Método de pagamento utilizado
     * @return ID do pagamento registado
     */
    public String regista_pagamento(double valor, String codPedido, String metodoPagamento) {
        Pagamento pagamento = new Pagamento(pagamentos.generateNewId(), valor,codPedido,metodoPagamento);
        String idPag = pagamento.getIdPagamento();
        pagamentos.put(idPag, pagamento);
        return idPag;
    }

    /**
     * Método que gera uma fatura
     * 
     * @param idPagamento ID do pagamento associado à fatura
     * @param pedido      Pedido associado à fatura
     * @return Fatura gerada
     */
    public Fatura gera_fatura(String idPagamento, Pedido pedido) {
        Pagamento pagamento = pagamentos.get(idPagamento);
        Fatura fatura = new Fatura(faturas.generateNewId(),idPagamento, pedido, pagamento.getValorPagamento(),pedido.getNrContribuinte());
        this.faturas.put(fatura.getIdFatura(), fatura);
        return fatura;
    }

    /**
     * Método que gera um talão
     * 
     * @param codPedido Código do pedido associado ao talão
     * @return Talão gerado
     */
    public Talao gera_talao(String codPedido) {
        Talao talao = new Talao(taloes.generateNewId(),codPedido);
        String idTalao = talao.getIdTalao();
        taloes.put(idTalao, talao);
        return talao;
    }

    /**
     * Método que verifica se o pagamento de um pedido foi confirmado
     * 
     * @param codPedido Código do pedido
     * @return true se o pagamento foi confirmado, false caso contrário
     */
    public boolean pagamento_confirmado(String codPedido) {
        for (Pagamento pagamento : pagamentos.values()) {
            if (pagamento.getIdPedido().equals(codPedido)) {
                return true;
            }
        }
        return false;
    }

    // ==================== MÉTODOS DE ACESSO AOS DAOs ====================

    /**
     * Método que retorna a coleção de faturas
     * @return Coleção de faturas
     */
    public Collection<Fatura> faturaValues() {
        return this.faturas.values();
    }

    /** Método que remove uma fatura pelo seu ID
     * @param idFatura ID da fatura a remover
     * @return Fatura removida
     */
    public Fatura faturaRemove(String idFatura) {
        return this.faturas.remove(idFatura);
    }

    /**
     * Método que remove um pagamento pelo seu ID
     * @param idPagamento ID do pagamento a remover
     * @return Pagamento removido
     */
    public Pagamento pagamentoRemove(String idPagamento) {
        return this.pagamentos.remove(idPagamento);
    }

    /**
     * Método que retorna a coleção de pagamentos
     * @return Coleção de pagamentos
     */
    public Collection<Pagamento> pagamentoValues() {
        return this.pagamentos.values();
    }

    /** Método que remove um talão pelo seu ID
     * @param idTalao ID do talão a remover
     * @return Talão removido
     */
    public Talao talaoRemove(String idTalao) {
        return this.taloes.remove(idTalao);
    }
    /**
     * Método que retorna a coleção de talões
     * @return Coleção de talões
     */
    public Collection<Talao> talaoValues() {
        return this.taloes.values();
    }
}
