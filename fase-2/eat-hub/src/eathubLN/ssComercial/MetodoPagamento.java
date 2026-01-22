package eathubLN.ssComercial;

/** Métodos de pagamento disponíveis no sistema EatHub */
public enum MetodoPagamento {

    /** 
     * Dinheiro 
    */
    DINHEIRO,

    /** 
     * Multibanco 
    */
    MULTIBANCO,

    /** 
     * MBWAY 
    */
    MBWAY;

    /** 
     * Método que converte uma string num MetodoPagamento
     * 
     * @param metodo A string a converter
     * @return O MetodoPagamento correspondente à string
     */
    public static MetodoPagamento qualMetodo(String metodo) {
        switch(metodo.toUpperCase()) {
            case "DINHEIRO":
                return DINHEIRO;
            case "MULTIBANCO":
                return MULTIBANCO;
            case "MBWAY":
                return MBWAY;
            default:
                return null;
        }
    }

    /**
     * Método que converte um MetodoPagamento numa string
     * 
     * @return A string correspondente ao MetodoPagamento
     */
    @Override
    public String toString() {
        switch(this) {
            case DINHEIRO:
                return "DINHEIRO";
            case MULTIBANCO:
                return "MULTIBANCO";
            case MBWAY:
                return "MBWAY";
            default:
                return "";
        }
    }
}
