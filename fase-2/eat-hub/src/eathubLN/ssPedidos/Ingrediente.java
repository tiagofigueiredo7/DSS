package eathubLN.ssPedidos;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import eathubLN.ssCadeia.Pair;

/** Um Ingrediente do EatHub */
public class Ingrediente {

    /** Nome do Ingrediente */
    private String nome;
    
    /** Lista de Alergénios do Ingrediente */
    private List<String> alergenios;

    /** Preço do Ingrediente */
    private double preco;

    /**
     * Construtor parametrizado
     * 
     * Cria um Ingrediente com o nome fornecido e com a lista de alergénios vazia.
     * 
     * @param nome Nome do Ingrediente
     * @return Uma nova instância de Ingrediente
     */
    public Ingrediente(String nome) {
        this.nome = nome;
        this.alergenios = new ArrayList<>();
        this.preco = 0.0;
    }

    /**
     * Construtor parametrizado
     * 
     * Cria um Ingrediente com os dados fornecidos.
     * 
     * @param nome Nome do Ingrediente
     * @param alergenios Lista de Alergénios
     * @return Uma nova instância de Ingrediente
     */
    public Ingrediente(String nome, List<String> alergenios) {
        this.nome = nome;
        this.alergenios = new ArrayList<>(alergenios);
        this.preco = 0.0;
    }

    /**
     * Construtor parametrizado completo
     * 
     * Cria um Ingrediente com todos os dados fornecidos.
     * 
     * @param nome Nome do Ingrediente
     * @param alergenios Lista de Alergénios
     * @param preco Preço do Ingrediente
     * @return Uma nova instância de Ingrediente
     */
    public Ingrediente(String nome, List<String> alergenios, double preco) {
        this.nome = nome;
        this.alergenios = new ArrayList<>(alergenios);
        this.preco = preco;
    }

    /** 
     * Método que devolve o nome do Ingrediente
     * 
     * @return Nome do Ingrediente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método que devolve a lista de Alergénios do Ingrediente
     * 
     * @return Lista de Alergénios do Ingrediente
     */
    public Collection<Pair<String,Ingrediente>> getAlergeniosIngrediente() {
        Collection<Pair<String,Ingrediente>> alergenios = new ArrayList<>();
        for (String alerg : this.alergenios) {
            alergenios.add(new Pair<>(alerg, this));
        }
        return alergenios;
    }

    /**
     * Método que devolve o preço do Ingrediente
     * 
     * @return Preço do Ingrediente
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Método toString que representa o Ingrediente como String
     * 
     * @return Representação em String do Ingrediente
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────────────────────────────────────\n");
        sb.append("│ Ingrediente: ").append(nome).append("\n");
        sb.append("│ Preço: €").append(String.format("%.2f", preco)).append("\n");
        sb.append("│ Alergénios: ");
        if (alergenios.isEmpty()) {
            sb.append("(nenhum)");
        } else {
            sb.append(String.join(", ", alergenios));
        }
        sb.append("\n");
        sb.append("└─────────────────────────────────────────");
        return sb.toString();
    }
    
}
