package eathubLN.ssPedidos;

/** O tipo de serviço de um pedido */
public enum TipoServico {
    /**
     * Takeaway
     */
    TAKEWAY, 

    /**
     * Serviço à mesa
     */
    SERVICOMESA;

    /** 
     * Método que converte uma string num TipoServico
     * 
     * @param tipo A string a converter
     * @return O TipoServico correspondente à string
     * @throws IllegalArgumentException Se a string não corresponder a nenhum TipoServico válido
     */
    public static TipoServico fromString(String tipo) {
        if (tipo.equalsIgnoreCase("TAKEWAY")) {
            return TAKEWAY;
        } else if (tipo.equalsIgnoreCase("SERVICOMESA")) {
            return SERVICOMESA;
        } else {
            throw new IllegalArgumentException("Tipo de serviço inválido: " + tipo);
        }
    }

    /**
     * Método que converte um TipoServico numa string
     * 
     * @return A string correspondente ao TipoServico
     */
    @Override
    public String toString() {
        switch(this) {
            case TAKEWAY:
                return "TAKEWAY";
            case SERVICOMESA:
                return "SERVICOMESA";
            default:
                return "";
        }
    }   


    
}
