package Exceptions;

/**
 * Exceção lançada quando um Pedido não existe.
 */
public class PedidoNaoExisteException extends Exception {
    /**
     * Construtor da exceção.
     *
     * @param mensagem Mensagem de erro a ser exibida
     */
    public PedidoNaoExisteException(String codPedido) {
        super("O pedido com o código " + codPedido + " não existe.");
    }
}