package eathubLN.ssCadeia;

import eathubDL.EstruturasDAO.FuncionarioDAO;

/** Funcionário do EatHub */
public class Funcionario {

    /** Identificador único do funcionário */
    private String idFuncionario;

    /** Nome do funcionário */
    private String nome;

    /** Posto do funcionário */
    private String posto;

    /** Tarefa do funcionário */
    private String tarefa;

    /** 
     * Construtor parametrizado de um funcionário
     * 
     * @param id Identificador único do funcionário
     * @return Uma instância de Funcionario
     */
    public Funcionario(String id) {
        this.idFuncionario = id;
        this.nome = "";
        this.posto = "";
        this.tarefa = "";
    }

    /** 
     * Construtor parametrizado de um funcionário
     * 
     * @param id Identificador único do funcionário
     * @param nome Nome do funcionário
     * @param posto Posto do funcionário
     * @param tarefa Tarefa do funcionário
     * @return Uma instância de Funcionario
     */
    public Funcionario(String id, String nome, String posto, String tarefa) {
        this.idFuncionario = id;
        this.nome = nome;
        this.posto = posto;
        this.tarefa = tarefa;
    }

    /** 
     * Método que retorna o identificador do funcionário
     * 
     * @return Identificador do funcionário
     */
    public String getIDFunc() {
        return idFuncionario;
    }

    /** 
     * Método que retorna o nome do funcionário
     * 
     * @return Nome do funcionário
     */
    public String getNomeFunc() {
        return nome;
    }

    /** 
     * Método que define o nome do funcionário
     * 
     * @param nome Nome do funcionário
     */
    public void setNomeFunc(String nome) {
        this.nome = nome;
    }

    /** 
     * Método que retorna o posto do funcionário
     * 
     * @return Posto do funcionário
     */
    public String getPostoFunc() {
        return posto;
    }

    /** 
     * Método que define o posto do funcionário
     * 
     * @param posto Posto do funcionário
     */
    public void setPostoFunc(String posto) {
        this.posto = posto;
    }

    /** 
     * Método que retorna a tarefa do funcionário
     * 
     * @return Tarefa do funcionário
     */
    public String getTarefaFunc() {
        return tarefa;
    }

    /** 
     * Método que define a tarefa do funcionário
     * 
     * @param tarefa Tarefa do funcionário
     */
    public void setTarefaFunc(String tarefa) {
        this.tarefa = tarefa;
    }

    /** 
     * Método que retorna o tipo do funcionário
     * 
     * @return Tipo do funcionário
     */
    public String getTipoFunc(){
        if (this instanceof Gestor) {
            return "Gestor";
        } else if (this instanceof ChefeRestaurante) {
            return "ChefeRestaurante";
        } else return "Funcionario";
    }

    /**
     * Método toString que representa o Funcionário como String
     * 
     * @return Representação em String do Funcionário
     */
    @Override
    public String toString() {
        String idRestaurante = FuncionarioDAO.getInstance().getRestauranteFuncionario(this.idFuncionario);
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────────────────────────────────────\n");
        sb.append("│ ID: ").append(idFuncionario).append("\n");
        if (idRestaurante != null && !idRestaurante.isEmpty()) sb.append("│ Restaurante: ").append(idRestaurante).append("\n");
        sb.append("│ Nome: ").append(nome).append("\n");
        sb.append("│ Posto: ").append(posto).append("\n");
        sb.append("│ Tarefa: ").append(tarefa).append("\n");
        sb.append("│ Tipo: ").append(getTipoFunc()).append("\n");
        sb.append("└─────────────────────────────────────────");
        return sb.toString();
    }

}
