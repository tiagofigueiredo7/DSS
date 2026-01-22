package eathubLN.ssComercial;

import eathubLN.ssPedidos.Pedido;

/** Uma Fatura do EatHub */
public class Fatura {

    /** Identificador único da fatura */
    private String idFatura;

    /** Identificador do pagamento associado à fatura */
    private String idPagamento;

    /** Valor total da fatura */
    private double valor;

    /** Número de contribuinte associado à fatura */
    private int contribuinte;

    /** Pedido associado à fatura */
    private Pedido pedido;

    /** 
     * Construtor parametrizado
     * 
     * @param id Identificador único da fatura
     * @param idPagamento Identificador do pagamento associado à fatura
     * @param pedido Pedido associado à fatura
     * @param valor Valor total da fatura
     * @param contribuinte Número de contribuinte associado à fatura
     * @return Uma nova instância de Fatura
     */
    public Fatura(String id, String idPagamento, Pedido pedido, double valor, int contribuinte) {
        this.idFatura = id;
        this.idPagamento = idPagamento;
        this.pedido = pedido;
        this.valor = valor;
        this.contribuinte = contribuinte;
    }

    /** 
     * Método que devolve o identificador único da fatura
     * 
     * @return Identificador único da fatura
     */
    public String getIdFatura() {
        return idFatura;
    }

    /** 
     * Método que devolve o identificador do pagamento associado à fatura
     * 
     * @return Identificador do pagamento associado à fatura
     */
    public String getIdPagamento() {
        return idPagamento;
    }

    /** 
     * Método que devolve o valor total da fatura
     * 
     * @return Valor total da fatura
     */
    public double getValor() {
        return valor;
    }

    /** 
     * Método que devolve o número de contribuinte associado à fatura
     * 
     * @return Número de contribuinte associado à fatura
     */
    public int getNrContribuinte() {
        return contribuinte;
    }

    /** 
     * Método que devolve o pedido associado à fatura
     * 
     * @return Pedido associado à fatura
     */
    public Pedido getPedido() {
        return pedido;
    }

    /** 
     * Método que devolve a representação em String da fatura
     * 
     * @return Representação em String da fatura
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n┌───────────────────────────────────────────\n");
        sb.append("│           F A T U R A                    \n");
        sb.append("├───────────────────────────────────────────\n");
        sb.append("│ Fatura ID: ").append(this.idFatura).append("\n");
        sb.append("│ Pagamento ID: ").append(this.idPagamento).append("\n");
        sb.append("│ Pedido ID: ").append(this.pedido.getCodPedido()).append("\n");
        if (this.contribuinte != 0) {
            sb.append("│ Nº Contribuinte: ").append(this.contribuinte).append("\n");
        }
        sb.append("├───────────────────────────────────────────\n");
        sb.append("│ VALOR TOTAL: €").append(String.format("%.2f", this.valor)).append("\n");
        sb.append("└───────────────────────────────────────────");
        return sb.toString();
    }
    
}
