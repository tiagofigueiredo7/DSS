package eathubLN.ssComercial;

import java.util.Collection;

import eathubLN.ssPedidos.Pedido;

public interface ISSComercial {

    public String regista_pagamento(double valor, String codPedido, String metodoPagamento);

    public Fatura gera_fatura(String idPagamento, Pedido pedido);

    public Talao gera_talao(String codPedido);

    public boolean pagamento_confirmado(String codPedido);

    public Collection<Fatura> faturaValues();

    public Fatura faturaRemove(String idFatura);

    public Pagamento pagamentoRemove(String idPagamento);

    public Collection<Pagamento> pagamentoValues();

    public Talao talaoRemove(String idTalao);

    public Collection<Talao> talaoValues();
    
}
