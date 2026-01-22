package eathubLN.ssComercial;

/** Talão do EatHub */
public class Talao {

    /** Identificador único do talão */
    private String idTalao;

    /** Código do pedido associado ao talão */
    private String codPedido;

    /** 
     * Construtor de um talão
     * 
     * @param id Identificador do talão
     * @param codPedido Código do pedido associado ao talão
     * @return Uma instância de Talao
     */
    public Talao(String id, String codPedido) {
        this.idTalao = id;
        this.codPedido = codPedido;
    }

    /** 
     * Método que retorna o identificador do talão
     * 
     * @return Identificador do talão
     */
    public String getIdTalao() {
        return idTalao;
    }

    /** 
     * Método que retorna o código do pedido associado ao talão
     * 
     * @return Código do pedido associado ao talão
     */
    public String getCodPedidoTalao() {
        return codPedido;
    }
    
    /** 
     * Método que retorna a representação em string do talão
     * 
     * @return Representação em string do talão
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n┌───────────────────────────────────────────\n");
        sb.append("│            T A L Ã O                     \n");
        sb.append("├───────────────────────────────────────────\n");
        sb.append("│ Talão ID: ").append(this.idTalao).append("\n");
        sb.append("│ Código do Pedido: ").append(this.codPedido).append("\n");
        sb.append("└───────────────────────────────────────────");
        return sb.toString();
    }
    
}
