package Exceptions;

/**
 * Exceção lançada quando um Pedido vai atrasar.
 */
public class PedidoVaiAtrasarException extends Exception {
    /**
     * Construtor da exceção.
     *
     * @param codPedido Código do pedido
     * @param novoTempoEspera Novo tempo de espera estimado
     * @param ingrEmFalta Ingrediente que está em falta
     */
    public PedidoVaiAtrasarException(String codPedido, double novoTempoEspera, String ingrEmFalta) {
        super("Ingrediente em falta: " + ingrEmFalta + ", pedido " + codPedido + " inserido na fila com novo tempo de espera: " + novoTempoEspera + " minutos.");
    }
}
