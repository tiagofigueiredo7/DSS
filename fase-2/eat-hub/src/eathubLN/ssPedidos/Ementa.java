package eathubLN.ssPedidos;

import eathubDL.EstruturasDAO.MenuDAO;
import eathubDL.EstruturasDAO.PropostaDAO;
import java.util.Collection;

/** A Ementa do EatHub */
public class Ementa {

    /** Instância singleton da Ementa */
    private static Ementa singleton = null;

    /** Instância do PropostaDAO */
    private PropostaDAO propostasEmenta;

    /** Instância do MenuDAO */
    private MenuDAO menusEmenta;

    /** Coleção de Propostas da Ementa */
    private Collection<Proposta> propostas;

    /** Coleção de Menus da Ementa */
    private Collection<Menu> menus;

    /** 
     * Método que devolve a instância singleton da Ementa
     * 
     * @return Instância singleton da Ementa
     */
    public static Ementa getInstance() {
        if (singleton == null) {
            singleton = new Ementa();
        }
        return singleton;
    }

    /** 
     * Construtor Vazio
     * 
     * Cria uma Ementa consoante o que está guardado no PropostaDAO e MenuDAO
     * 
     * @return Uma nova instância de Ementa
     */
    public Ementa() {
        this.propostasEmenta = PropostaDAO.getInstance();
        this.menusEmenta = MenuDAO.getInstance();
        this.propostas = this.propostasEmenta.values();
        this.menus = this.menusEmenta.values();
    }

    /** 
     * Método que devolve a instância do PropostaDAO da Ementa
     * 
     * @return Instância do PropostaDAO da Ementa
     */
    public PropostaDAO getPropostaDAO() {
        return propostasEmenta;
    }

    /** 
     * Método que devolve a instância do MenuDAO da Ementa
     * 
     * @return Instância do MenuDAO da Ementa
     */
    public MenuDAO getMenuDAO() {
        return menusEmenta;
    }

    /** 
     * Método que devolve um Menu específico da Ementa
     * 
     * @param codMenu Código do Menu a ser devolvido
     * @return Menu específico da Ementa
     */
    public Menu getMenu(String codMenu) {
        return menusEmenta.get(codMenu);
    }

    /** 
     * Método que devolve uma Proposta específica da Ementa
     * 
     * @param codProposta Código da Proposta a ser devolvida
     * @return Proposta específica da Ementa
     */
    public Proposta getProposta(String codProposta) {
        return propostasEmenta.get(codProposta);
    }

    /** 
     * Método que devolve uma representação em String da Ementa
     * 
     * @return Representação em String da Ementa
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌──────────────────────────────────────────────────\n");
        sb.append("│                  E M E N T A                     \n");
        sb.append("├──────────────────────────────────────────────────\n");
        sb.append("│ PROPOSTAS:\n");
        if (propostas.isEmpty()) {
            sb.append("│   (nenhuma proposta disponível)\n");
        } else {
            for (Proposta p : propostas) {
                sb.append("│   » ").append(p.getIdProposta());
                sb.append(" - ").append(p.getNome());
                sb.append(" - €").append(String.format("%.2f", p.getPreco())).append("\n");
            }
        }
        sb.append("├──────────────────────────────────────────────────\n");
        sb.append("│ MENUS:\n");
        if (menus.isEmpty()) {
            sb.append("│   (nenhum menu disponível)\n");
        } else {
            for (Menu m : menus) {
                sb.append("│   » ").append(m.getIdMenu());
                sb.append(" - ").append(m.getNome());
                sb.append(" - €").append(String.format("%.2f", m.getPrecoTotal())).append("\n");
                sb.append("│      Propostas:\n");
                for (Proposta p : m.getPropostasMenu()) {
                    sb.append("│       • ").append(p.getIdProposta());
                    sb.append(" - ").append(p.getNome()).append("\n");
                }
            }
        }
        sb.append("└──────────────────────────────────────────────────");
        return sb.toString();
    }
    
}
