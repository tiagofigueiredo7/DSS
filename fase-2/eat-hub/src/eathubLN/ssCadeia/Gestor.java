package eathubLN.ssCadeia;

/** Gestor de um restaurante do EatHub */
public class Gestor extends ChefeRestaurante{

    /** 
     * Construtor vazio de Gestor
     * 
     * @param id Identificador do gestor
     * @return Uma nova instância de Gestor
     */
    public Gestor(String id) {
        super(id);
    }

    /** 
     * Construtor parametrizado de Gestor
     * 
     * @param id Identificador do gestor
     * @param nome Nome do gestor
     * @param posto Posto do gestor
     * @param tarefa Tarefa do gestor
     * @return Uma nova instância de Gestor
     */
    public Gestor(String id, String nome, String posto, String tarefa) {
        super(id, nome, posto, tarefa);
    }
    
}
