package eathubLN.ssComercial;

/** Pagamento no sistema do EatHub */
public class Pagamento {

    /** Identificador único do pagamento */
    private String idPagamento;

    /** Valor do pagamento */
    private double valor;

    /** Identificador do pedido associado ao pagamento */
    private String idPedido;

    /** Método de pagamento utilizado */
    private MetodoPagamento metodoPagamento;

    /** 
     * Construtor parametrizado
     * 
     * @param id Identificador único do pagamento
     * @param valor Valor do pagamento
     * @param idPedido Identificador do pedido associado ao pagamento
     * @param metodoPagamento Método de pagamento utilizado
     * @return Uma nova instância de Pagamento
     */
    public Pagamento(String id, double valor, String idPedido, String metodoPagamento) {
        this.idPagamento = id;
        this.valor = valor;
        this.idPedido = idPedido;

        if (metodoPagamento == null) {
            this.metodoPagamento = null;//nao vai acontecer
        } else {
            this.metodoPagamento = MetodoPagamento.qualMetodo(metodoPagamento);
        }
    }

    /** 
     * Método que devolve o identificador único do pagamento
     * 
     * @return Identificador único do pagamento
     */
    public String getIdPagamento() {
        return idPagamento;
    }

    /** 
     * Método que devolve o valor do pagamento
     * 
     * @return Valor do pagamento
     */
    public double getValorPagamento() {
        return valor;
    }

    /** 
     * Método que devolve o identificador do pedido associado ao pagamento
     * 
     * @return Identificador do pedido associado ao pagamento
     */
    public String getIdPedido() {
        return idPedido;
    }

    /** 
     * Método que devolve o método de pagamento utilizado
     * 
     * @return Método de pagamento utilizado
     */
    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    /**
     * Método que devolve uma representação textual do pagamento
     * 
     * @return Representação textual do pagamento
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────────────────────────────────────\n");
        sb.append("│        P A G A M E N T O                \n");
        sb.append("├─────────────────────────────────────────\n");
        sb.append("│ Pagamento ID: ").append(idPagamento).append("\n");
        sb.append("│ Pedido ID: ").append(idPedido).append("\n");
        sb.append("│ Valor: €").append(String.format("%.2f", valor)).append("\n");
        sb.append("│ Método: ").append(metodoPagamento.toString()).append("\n");
        sb.append("└─────────────────────────────────────────");
        return sb.toString();
    }
    
}
