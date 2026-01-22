package eathubLN.ssCadeia;

import eathubLN.ssPedidos.Pedido;
import java.time.LocalDate;

/** Classe que representa o histórico de um pedido, mais ainda,
 *  regista as informações relevantes sobre a finalização do pedido */
public class Historico {

    /** Identificador único do histórico */
    private String idHistorico;

    /** Pedido associado ao histórico */
    private Pedido pedido;

    /** Identificador do restaurante associado ao histórico */
    private String idRestaurante;

    /** Data de finalização do pedido */
    private LocalDate dataFinalizacao;

    /** 
     * Construtor parametrizado da classe Historico
     * 
     * @param idHistorico Identificador único do histórico
     * @param pedido Pedido associado ao histórico
     * @param idRestaurante Identificador do restaurante associado ao histórico
     * @param dataFinalizacao Data de finalização do pedido
     * @return Uma nova instância Historico
     */
    public Historico(String idHistorico, Pedido pedido, String idRestaurante, LocalDate dataFinalizacao) {
        this.idHistorico = idHistorico;
        this.pedido = pedido;
        this.idRestaurante = idRestaurante;
        this.dataFinalizacao = dataFinalizacao;
    }

    /**
     * Construtor parametrizado alternativo da classe Historico sem data de finalização
     * 
     * @param idHistorico Identificador único do histórico
     * @param pedido Pedido associado ao histórico
     * @param idRestaurante Identificador do restaurante associado ao histórico
     * @return Uma nova instância Historico
     */
    public Historico(String idHistorico, Pedido pedido, String idRestaurante) {
        this.idHistorico = idHistorico;
        this.pedido = pedido;
        this.idRestaurante = idRestaurante;
        this.dataFinalizacao = null;
    }

    /** 
     * Método que retorna o identificador único do histórico
     * 
     * @return idHistorico Identificador único do histórico
     */
    public String getIdHistorico() {
        return idHistorico;
    }

    /** 
     * Método que retorna o pedido associado ao histórico
     * 
     * @return pedido Pedido associado ao histórico
     */
    public Pedido getPedido() {
        return pedido;
    }

    /** 
     * Método que retorna o identificador do restaurante associado ao histórico
     * 
     * @return idRestaurante Identificador do restaurante associado ao histórico
     */
    public String getIdRestaurante() {
        return idRestaurante;
    }

    /** 
     * Método que retorna a data de finalização do pedido
     * 
     * @return dataFinalizacao Data de finalização do pedido
     */
    public LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }

    /** 
     * Método que define a data de finalização do pedido
     * 
     * @param dataFinalizacao Data de finalização do pedido
     */
    public void setDataFinalizacao(LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    /**
     * Método que define o pedido associado ao histórico
     * 
     * @param pedido Pedido associado ao histórico
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Método que define o identificador do restaurante associado ao histórico
     * 
     * @param idRestaurante Identificador do restaurante associado ao histórico
     */
    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    /**
     * Método que define o identificador único do histórico
     * 
     * @param idHistorico Identificador único do histórico
     */
    public void setIdHistorico(String idHistorico) {
        this.idHistorico = idHistorico;
    }

    /**
     * Método que retorna uma representação em String do histórico
     * 
     * @return String Representação em String do histórico
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────────────────\n");
        sb.append("│ ID Histórico: ").append(idHistorico).append("\n");
        sb.append("│ ID Restaurante: ").append(idRestaurante).append("\n");
        sb.append("│ Data Finalização: ").append(dataFinalizacao != null ? dataFinalizacao.toString() : "N/A").append("\n");
        sb.append("│ Pedido:\n");
        sb.append("│   - ID: ").append(pedido.getCodPedido()).append("\n");
        sb.append("│   - Valor: €").append(String.format("%.2f", pedido.calculaValorTotalPedido())).append("\n");
        sb.append("└───────────────────────────────────────────");
        return sb.toString();
    }
    
}
