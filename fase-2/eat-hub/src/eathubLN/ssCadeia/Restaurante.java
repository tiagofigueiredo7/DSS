package eathubLN.ssCadeia;

import eathubDL.EstruturasDAO.FuncionarioDAO;
import eathubDL.EstruturasDAO.StockDAO;
import java.util.ArrayList;
import java.util.List;

/** Restaurante do EatHub */
public class Restaurante {

    /** Identificador único do restaurante */
    private String idRestaurante;

    /** Nome do restaurante */
    private String nome;

    /** Chefe do restaurante */
    private ChefeRestaurante chefeRestaurante;

    /** Códigos dos ID's dos funcionários do restaurante */
    private List<String> codsFuncionarios;

    /** Instância do DAO dos funcionários do restaurante */
    private FuncionarioDAO funcionariosRestaurante;

    /** Instância do DAO do stock do restaurante */
    private StockDAO stockRestaurante;

    /** 
     * Construtor parametrizado de um restaurante
     * 
     * @param id Identificador único do restaurante
     * @param nome Nome do restaurante
     * @param chefeRestaurante Chefe do restaurante
     * @param codsFuncionarios Códigos dos ID's dos funcionários do restaurante
     * @return Uma instância de Restaurante
     */
    public Restaurante(String id, String nome, ChefeRestaurante chefeRestaurante, List<String> codsFuncionarios) {
        this.idRestaurante = id;
        this.nome = nome;
        this.chefeRestaurante = chefeRestaurante;
        this.codsFuncionarios = new ArrayList<>(codsFuncionarios);
        this.funcionariosRestaurante = FuncionarioDAO.getInstance();
        this.stockRestaurante = StockDAO.getInstance();
    }

    /** 
     * Método que retorna o identificador do restaurante
     * 
     * @return Identificador do restaurante
     */
    public String getIdRestaurante() {
        return idRestaurante;
    }

    /** 
     * Método que retorna o nome do restaurante
     * 
     * @return Nome do restaurante
     */
    public String getNome() {
        return nome;
    }

    /** 
     * Método que retorna o chefe do restaurante
     * 
     * @return Chefe do restaurante
     */
    public ChefeRestaurante getChefeRestaurante() {
        return chefeRestaurante;
    }

    /** 
     * Método que retorna os códigos dos ID's dos funcionários do restaurante
     * 
     * @return Códigos dos ID's dos funcionários do restaurante
     */
    public List<String> getCodsFuncionarios() {
        return codsFuncionarios;
    }

    /** 
     * Método que retorna a instância do DAO dos funcionários do restaurante
     * 
     * @return Instância do DAO dos funcionários do restaurante
     */
    public FuncionarioDAO getFuncionariosRestaurante() {
        return funcionariosRestaurante;
    }

    /** 
     * Método que retorna a instância do DAO do stock do restaurante
     * 
     * @return Instância do DAO do stock do restaurante
     */
    public StockDAO getStockRestaurante() {
        return stockRestaurante;
    }

    /** 
     * Método que retorna um funcionário do restaurante dado o seu código (ID)
     * 
     * @param codFuncionario Código (ID) do funcionário
     * @return Funcionário do restaurante correspondente ao código (ID) fornecido
     */
    public Funcionario getFuncionario(String codFuncionario) {
        return this.funcionariosRestaurante.get(codFuncionario);
    }

    /** 
     * Método que retorna a quantidade de um ingrediente específico no stock do restaurante
     * 
     * @param codIngrediente Código do ingrediente
     * @return Quantidade do ingrediente no stock do restaurante
     */
    public int getQuantidadeIngrediente(String codIngrediente) {
        return this.stockRestaurante.get(new Pair<>(this.idRestaurante, codIngrediente));
    }

    /**
     * Método toString que representa o Restaurante como String
     * 
     * @return Representação em String do Restaurante
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────────────────\n");
        sb.append("│ ID: ").append(idRestaurante).append("\n");
        sb.append("│ Nome: ").append(nome).append("\n");
        sb.append("│ Chefe: ").append(chefeRestaurante != null ? chefeRestaurante.getNomeFunc() + " - ID: " + chefeRestaurante.getIDFunc() : "(nenhum)").append("\n");
        sb.append("│ Funcionários:\n");
        for (String codFunc : codsFuncionarios) {
            Funcionario f = this.funcionariosRestaurante.get(codFunc);
            sb.append("│   • ").append(f.getNomeFunc() + " - ID: " + f.getIDFunc()).append("\n");
        }
        sb.append("│ Nº Funcionários: ").append(codsFuncionarios.size()).append("\n");
        sb.append("└───────────────────────────────────────────");
        return sb.toString();
    }
    
}
