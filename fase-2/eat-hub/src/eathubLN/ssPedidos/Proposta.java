package eathubLN.ssPedidos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eathubLN.ssCadeia.Pair;

/** Uma Proposta do EatHub */
public class Proposta {
    
    /** ID único da Proposta */
    private String idProposta;

    /** Nome da Proposta */
    private String nome;

    /** Preço da Proposta */
    private double preco;

    /** Lista de Ingredientes da Proposta */
    private List<Ingrediente> ingredientes;

    /** Lista ordenada de Etapas de preparação da Proposta */
    private List<String> etapas;//tem de estar ordenado pela ordem de preparação

    /**
     * Construtor Parametrizado
     * 
     * Cria uma Proposta com o id, nome e preço fornecidos.
     * As listas de ingredientes e etapas são inicializadas como vazias.
     * 
     * @param id ID único da Proposta
     * @param nome Nome da Proposta
     * @param preco Preço da Proposta
     * @return Uma nova instância de Proposta
     */
    public Proposta(String id, String nome, double preco) {
        this.idProposta = id;
        this.nome = nome;
        this.preco = preco;
        this.ingredientes = new ArrayList<>();
        this.etapas = new ArrayList<>();
    }

    /**
     * Construtor Parametrizado Completo
     * 
     * Cria uma Proposta com o id, nome, preço, ingredientes e etapas fornecidos.
     * 
     * @param id ID único da Proposta
     * @param nome Nome da Proposta
     * @param preco Preço da Proposta
     * @param ingredientes Lista de Ingredientes da Proposta
     * @param etapas Lista ordenada de Etapas de preparação da Proposta
     * @return Uma nova instância de Proposta
     */
    public Proposta(String id, String nome, double preco, List<Ingrediente> ingredientes, List<String> etapas) {
        this.idProposta = id;
        this.nome = nome;
        this.preco = preco;
        this.ingredientes = new ArrayList<>(ingredientes);
        this.etapas = new ArrayList<>(etapas);
    }

    /** 
     * Método que devolve o ID da Proposta
     * 
     * @return ID único da Proposta
     */
    public String getIdProposta() {
        return idProposta;
    }

    /**
     * Método que devolve o nome da Proposta
     * 
     * @return Nome da Proposta
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método que devolve o preço da Proposta
     * 
     * @return Preço da Proposta
     */
    public double getPreco() {
        return preco;
    }

    /** 
     * Método que devolve a lista de ingredientes da Proposta
     * 
     * @return Lista de Ingredientes da Proposta
     */
    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    /** 
     * Método que devolve a lista de etapas da Proposta
     * 
     * @return Lista ordenada de Etapas de preparação da Proposta
     */
    public List<String> getEtapas() {
        return etapas;
    }

    /** 
     * Método que devolve a coleção de alergénios presentes nos ingredientes da Proposta
     * 
     * @return Coleção de alergenios presentes na Proposta
     */
    public Collection<Pair<String,Ingrediente>> getAlergenios() {
        Collection<Pair<String,Ingrediente>> alergenicos = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            alergenicos.addAll(ingrediente.getAlergeniosIngrediente());
        }
        return alergenicos;
    }

    /** 
     * Método que devolve a representação em String da Proposta
     * 
     * @return Representação em String da Proposta
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────────────────\n");
        sb.append("│ Proposta: ").append(idProposta).append("\n");
        sb.append("│ Nome: ").append(nome).append("\n");
        sb.append("│ Preço: €").append(String.format("%.2f", preco)).append("\n");
        sb.append("├───────────────────────────────────────────\n");
        sb.append("│ Ingredientes: \n");
        if (ingredientes.isEmpty()) {
            sb.append("│   (nenhum)\n");
        } else {
            for (Ingrediente ingrediente : ingredientes) {
                sb.append("│   • ").append(ingrediente.getNome()).append("\n");
            }
        }
        sb.append("├───────────────────────────────────────────\n");
        sb.append("│ Etapas: \n");
        if (etapas.isEmpty()) {
            sb.append("│   (nenhuma)\n");
        } else {
            int i = 1;
            for (String etapa : etapas) {
                sb.append("│   ").append(i++).append(". ").append(etapa).append("\n");
            }
        }
        sb.append("└───────────────────────────────────────────");
        return sb.toString();
    }
    
}
