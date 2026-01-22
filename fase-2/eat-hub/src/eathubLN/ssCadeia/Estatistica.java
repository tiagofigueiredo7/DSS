package eathubLN.ssCadeia;

import java.time.LocalDate;

/** Classe que contém estatísticas de um restaurante */
public class Estatistica {

    /** Data de início do período estatístico */
    private LocalDate dataInicio;

    /** Data de fim do período estatístico */
    private LocalDate dataFim;

    /** Lucro */
    private double lucro;

    /** Número de pedidos vendidos */
    private int numeroPedidosVendidos;

    /** Número de itens inidividuais vendidos */
    private int numeroItensVendidos;

    /** Tempo médio de entrega */
    private double tempoMedioEntrega;

    /** 
     * Construtor parametrizado
     * 
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @param lucro Lucro
     * @param numeroPedidosVendidos Número de pedidos vendidos
     * @param numeroItensVendidos Número de itens vendidos
     * @param tempoMedioEntrega Tempo médio de entrega
     * @return Uma nova instância de Estatistica
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public Estatistica(LocalDate dataInicio, LocalDate dataFim, 
                       double lucro, int numeroPedidosVendidos, int numeroItensVendidos, double tempoMedioEntrega) {
        // Validação
        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início não pode ser nula");
        }
        if (dataFim == null) {
            throw new IllegalArgumentException("Data de fim não pode ser nula");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início");
        }
        if (lucro < 0) {
            throw new IllegalArgumentException("Lucro não pode ser negativo");
        }
        if (numeroPedidosVendidos < 0) {
            throw new IllegalArgumentException("Número de pedidos vendidos não pode ser negativo");
        }
        if (numeroItensVendidos < 0) {
            throw new IllegalArgumentException("Número de itens vendidos não pode ser negativo");
        }
        if (numeroItensVendidos < numeroPedidosVendidos) {
            throw new IllegalArgumentException("Número de itens vendidos não pode ser menor que o número de pedidos vendidos");
        }
        if (tempoMedioEntrega < 0) {
            throw new IllegalArgumentException("Tempo médio de entrega não pode ser negativo");
        }

        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.lucro = lucro;
        this.numeroPedidosVendidos = numeroPedidosVendidos;
        this.numeroItensVendidos = numeroItensVendidos;
        this.tempoMedioEntrega = tempoMedioEntrega;
    }

    /** 
     * Construtor vazio
     * 
     * @return Uma nova instância de Estatistica com valores padrão
     */
    public Estatistica() {
        this.dataInicio = LocalDate.now();
        this.dataFim = LocalDate.now();
        this.lucro = 0.0;
        this.numeroPedidosVendidos = 0;
        this.numeroItensVendidos = 0;
        this.tempoMedioEntrega = 0.0;
    }

    /** 
     * Método que retorna a data de início do período
     * 
     * @return Data de início
     */
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    /** 
     * Método que retorna a data de fim do período
     * 
     * @return Data de fim
     */
    public LocalDate getDataFim() {
        return dataFim;
    }

    /** 
     * Método que retorna o lucro de uma estatística
     * 
     * @return Lucro
     */
    public double getLucro() {
        return lucro;
    }

    /** 
     * Método que retorna o número de pedidos de uma estatística
     * 
     * @return Número de pedidos vendidos
     */
    public int getNumeroPedidosVendidos() {
        return numeroPedidosVendidos;
    }

    /** 
     * Método que retorna o número de itens vendidos de uma estatística
     * 
     * @return Número de itens vendidos
     */
    public int getNumeroItensVendidos() {
        return numeroItensVendidos;
    }

    /** 
     * Método que retorna o tempo médio de entrega de uma estatística
     * 
     * @return Tempo médio de entrega
     */
    public double getTempoMedioEntrega() {
        return tempoMedioEntrega;
    }
    
    /** 
     * Método que clona uma estatística
     * 
     * @return Uma nova instância de Estatistica com os mesmos valores
     */
    public Estatistica clone() {
        return new Estatistica(this.dataInicio, this.dataFim, 
                               this.lucro, this.numeroPedidosVendidos, this.numeroItensVendidos, this.tempoMedioEntrega);
    }

    /**
     * Método que devolve a representação em String da estatística
     * 
     * @return Representação em String da estatística
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────────────────\n");
        sb.append("│ Período: ").append(this.dataInicio).append(" a ").append(this.dataFim).append("\n");
        sb.append("│ Lucro Total: €").append(String.format("%.2f", this.lucro)).append("\n");
        sb.append("│ Número de Pedidos Vendidos: ").append(this.numeroPedidosVendidos).append("\n");
        sb.append("│ Número de Itens Vendidos: ").append(this.numeroItensVendidos).append("\n");
        sb.append("│ Tempo Médio de Entrega: ").append(String.format("%.1f", this.tempoMedioEntrega)).append(" min\n");
        sb.append("└───────────────────────────────────────────");
        return sb.toString();
    }
}
