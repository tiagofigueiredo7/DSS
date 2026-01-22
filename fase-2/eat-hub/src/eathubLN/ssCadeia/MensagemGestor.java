package eathubLN.ssCadeia;

import java.util.Date;

/** Classe que representa uma mensagem enviada por um gestor */
public class MensagemGestor {

    /** Conteúdo da mensagem */
    private String conteudo;

    /** Data de envio da mensagem */
    private Date dataEnvio;

    /** Identificador do gestor que enviou a mensagem */
    private String idGestor;

    /** Identificador único da mensagem */
    private int id;

    /**
     * Construtor parametrizado da classe MensagemGestor
     * 
     * @param id
     * @param conteudo
     * @param dataEnvio
     * @param idGestor
     * @return Uma instância de MensagemGestor
     */
    public MensagemGestor(int id, String conteudo, Date dataEnvio, String idGestor) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.idGestor = idGestor;
    }

    /** 
     * Método que retorna o conteúdo da mensagem
     * 
     * @return conteudo da mensagem
     */
    public String getConteudo() {
        return conteudo;
    }

    /** 
     * Método que define o conteúdo da mensagem
     * 
     * @param conteudo da mensagem
     */
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    /** 
     * Método que retorna a data de envio da mensagem
     * 
     * @return data de envio da mensagem
     */
    public Date getDataEnvio() {
        return dataEnvio;
    }

    /** 
     * Método que define a data de envio da mensagem
     * 
     * @param dataEnvio da mensagem
     */
    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    /** 
     * Método que retorna o ID do gestor que enviou a mensagem
     * 
     * @return ID do gestor que enviou a mensagem
     */
    public String getIdGestor() {
        return idGestor;
    }

    /** 
     * Método que define o ID do gestor que enviou a mensagem
     * 
     * @param idGestor do gestor que enviou a mensagem
     */
    public void setIdGestor(String idGestor) {
        this.idGestor = idGestor;
    }

    /** 
     * Método que retorna o ID da mensagem
     * 
     * @return ID da mensagem
     */
    public int getId() {
        return id;
    }

    /** 
     * Método que define o ID da mensagem
     * 
     * @param id da mensagem
     */
    public void setId(int id) {
        this.id = id;
    }
}