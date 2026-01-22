package eathubLN.ssCadeia;

/** Classe que representa um par de valores genéricos */
public class Pair<F, S> {
    
    /** Primeiro valor do par */
    private F first;


    /** Segundo valor do par */
    private S second;

    /** 
     * Construtor parametrizado de um par de valores
     * 
     * @param first Primeiro valor do par
     * @param second Segundo valor do par
     * @return Uma instância de Pair<F, S>
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /** 
     * Método que retorna o primeiro valor do par
     * 
     * @return Primeiro valor do par
     */
    public F getFirst() {
        return first;
    }

    /** 
     * Método que retorna o segundo valor do par
     * 
     * @return Segundo valor do par
     */
    public S getSecond() {
        return second;
    }
}
