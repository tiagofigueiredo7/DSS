package eathubLN.ssCadeia;

/** Chefe de um restaurante do sistema Eat Hub */
public class ChefeRestaurante extends Funcionario {

    /** 
     * Construtor vazio
     * 
     * @param id ID do chefe de restaurante
     * @return Uma nova instância de ChefeRestaurante
     */
    public ChefeRestaurante(String id) {
        super(id);
    }

    /** 
     * Construtor parametrizado
     * 
     * @param id ID do chefe de restaurante
     * @param nome Nome do chefe de restaurante
     * @param posto Posto do chefe de restaurante
     * @param tarefa Tarefa do chefe de restaurante
     * @return Uma nova instância de ChefeRestaurante
     */
    public ChefeRestaurante(String id, String nome, String posto, String tarefa) {
        super(id, nome, posto, tarefa);
    }

    /** 
     * Construtor de cópia
     * 
     * @param f Funcionário a partir do qual se cria o chefe de restaurante
     * @return Uma nova instância de ChefeRestaurante
     */
    public ChefeRestaurante(Funcionario f) {
        super(f.getIDFunc(), f.getNomeFunc(), f.getPostoFunc(), f.getTarefaFunc());
    }
    
}
