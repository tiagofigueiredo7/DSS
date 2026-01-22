package eathubLN.ssPedidos;

import java.util.Collection;
import java.util.List;

import eathubLN.ssCadeia.Pair;
import java.util.ArrayList;

/** Um Menu do EatHub */
public class Menu {

    /** ID único do Menu */
    private String idMenu;

    /** Nome do Menu */
    private String nome;
    
    /** Preço total do Menu */
    private double precoTotal;

    /** Lista de propostas que compõem o Menu */
    private List<Proposta> propostasMenu;

    /** 
     * Construtor parametrizado
     * 
     * Cria um Menu com os dados fornecidos.
     * 
     * @param id ID do Menu
     * @param nome Nome do Menu
     * @param precoTotal Preço total do Menu
     * @param propostasMenu Lista de Propostas do Menu
     * @return Uma nova instância de Menu
     */
    public Menu(String id, String nome, double precoTotal, List<Proposta> propostasMenu) {
        this.idMenu = id;
        this.nome = nome;
        this.precoTotal = precoTotal;
        this.propostasMenu = propostasMenu;
    }

    /** 
     * Método que devolve o ID do Menu
     * 
     * @return ID do Menu
     */
    public String getIdMenu() {
        return idMenu;
    }

    /** 
     * Método que devolve o nome do Menu
     * 
     * @return Nome do Menu
     */
    public String getNome() {
        return nome;
    }

    /** 
     * Método que devolve o preço total do Menu
     * 
     * @return Preço total do Menu
     */
    public double getPrecoTotal() {
        return precoTotal;
    }

    /** 
     * Método que devolve a lista de Propostas do Menu
     * 
     * @return Lista de Propostas do Menu
     */
    public List<Proposta> getPropostasMenu() {
        return propostasMenu;
    }

    /** 
     * Método que devolve a coleção de Alergénios presentes nas propostas do Menu
     * 
     * @return Coleção de Alergénios presentes no Menu
     */
    public Collection<Pair<String,Ingrediente>> getAlergenios() {
        Collection<Pair<String,Ingrediente>> alergenicos = new ArrayList<>();
        for (Proposta proposta : propostasMenu) {
            alergenicos.addAll(proposta.getAlergenios());
        }
        return alergenicos;
    }

    /** 
     * Método que verifica se uma Proposta está presente no Menu
     * 
     * @param codProposta Código da Proposta a verificar
     * @return true se a Proposta estiver no Menu, false caso contrário
     */
    public boolean containsProposta(String codProposta) {
        for (Proposta p : propostasMenu) {
            if (p.getIdProposta().equalsIgnoreCase(codProposta)) {
                return true;
            }
        }
        return false;
    }

    /** 
     * Método que devolve a representação em String do Menu
     * 
     * @return Representação em String do Menu
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────────────────\n");
        sb.append("│ Menu: ").append(idMenu).append("\n");
        sb.append("│ Nome: ").append(nome).append("\n");
        sb.append("│ Preço Total: €").append(String.format("%.2f", precoTotal)).append("\n");
        sb.append("├───────────────────────────────────────────\n");
        sb.append("│ Propostas no Menu:\n");
        if (propostasMenu.isEmpty()) {
            sb.append("│   (nenhuma)\n");
        } else {
            for (Proposta proposta : propostasMenu) {
                sb.append("│   • ").append(proposta.getIdProposta());
                sb.append(" - ").append(proposta.getNome()).append("\n");
            }
        }
        sb.append("└───────────────────────────────────────────");
        return sb.toString();
    }
    
}
