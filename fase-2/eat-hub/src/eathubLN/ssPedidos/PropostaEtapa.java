package eathubLN.ssPedidos;

/** Uma etapa da confeção de uma Proposta */
public class PropostaEtapa {

    /** ID único da Proposta a que pertence a Etapa */
    private String codProposta;

    /** ID único do Menu a que pertence a Etapa, ou null se for proposta individual */
    private String codMenu; // null se for proposta individual

    /** Nome da Etapa */
    private String nomeEtapa;

    /** Índice da Etapa na ordem de preparação */
    private int indiceEtapa;

    /** Construtor parametrizado
     * 
     * Cria uma PropostaEtapa com o código da proposta, código do menu, nome da etapa e índice da etapa fornecidos.
     * 
     * @param codProposta ID único da Proposta a que pertence a Etapa
     * @param codMenu ID único do Menu a que pertence a Etapa, ou null se for proposta individual
     * @param nomeEtapa Nome da Etapa
     * @param indiceEtapa Índice da Etapa na ordem de preparação
     * @return Uma nova instância de PropostaEtapa
     */
    public PropostaEtapa(String codProposta, String codMenu, String nomeEtapa, int indiceEtapa) {
        this.codProposta = codProposta;
        this.codMenu = codMenu;
        this.nomeEtapa = nomeEtapa;
        this.indiceEtapa = indiceEtapa;
    }

    /** 
     * Método que devolve o código da Proposta
     * 
     * @return ID único da Proposta a que pertence a Etapa
     */
    public String getCodProposta() {
        return codProposta;
    }

    /** 
     * Método que devolve o código do Menu
     * 
     * @return ID único do Menu a que pertence a Etapa, ou null se for proposta individual
     */
    public String getCodMenu() {
        return codMenu;
    }

    /** 
     * Método que devolve o nome da Etapa
     * 
     * @return Nome da Etapa
     */
    public String getNomeEtapa() {
        return nomeEtapa;
    }

    /** 
     * Método que devolve o índice da Etapa na ordem de preparação
     * 
     * @return Índice da Etapa na ordem de preparação
     */
    public int getIndiceEtapa() {
        return indiceEtapa;
    }
}
