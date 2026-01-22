package eathubLN.ssPedidos;

import eathubDL.EstruturasDAO.MenuDAO;
import eathubDL.EstruturasDAO.PropostaDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eathubLN.ssCadeia.Pair;

/** Um Pedido do EatHub */
public class Pedido {

    /** ID único do Pedido (gerado pelo PedidoDAO) */
    private String idPedido;

    /** Tempo de espera estimado para o pedido */
    private double tempoEspera;

    /** Número de contribuinte do cliente que fez o pedido */
    private int nContribuinte;

    /** Notas adicionais que o cliente pode adicionar ao pedido */
    private String notas;

    /** Códigos (ID's) dos menus incluídos no pedido */
    private List<String> codMenus;

    /** Códigos (ID's) das propostas incluídas no pedido */
    private List<String> codPropostas;

    /** Instância do PropostaDAO */
    private PropostaDAO propostas;

    /** Instância do MenuDAO */
    private MenuDAO menus;

    /** Tipo de Serviço do Pedido */
    private TipoServico tipoServico;

    /** 
     * Construtor Parametrizado
     * 
     * Cria um Pedido com o id, tempo de espera, número de contribuinte, notas,
     * códigos de menus, códigos de propostas e tipo de serviço fornecidos.
     * 
     * @param idPedido ID único do Pedido
     * @param tempoEspera Tempo de espera estimado para o Pedido
     * @param nContribuinte Número de contribuinte do cliente que fez o Pedido
     * @param notas Notas adicionais que o cliente pode adicionar ao Pedido
     * @param codMenus Códigos (ID's) dos menus incluídos no Pedido
     * @param codPropostas Códigos (ID's) das propostas incluídas no Pedido
     * @param tipoServico Tipo de Serviço do Pedido
     * @return Uma nova instância de Pedido
     */
    public Pedido(String idPedido, double tempoEspera, int nContribuinte, String notas, 
                    List<String> codMenus, List<String> codPropostas, 
                    String tipoServico) {
        this.idPedido = idPedido;
        this.tempoEspera = tempoEspera;
        this.nContribuinte = nContribuinte;
        this.notas = notas;
        this.codMenus = new ArrayList<>(codMenus);
        this.codPropostas = new ArrayList<>(codPropostas);
        this.propostas = PropostaDAO.getInstance();
        this.menus = MenuDAO.getInstance();
        this.tipoServico = TipoServico.fromString(tipoServico);
    }

    /** 
     * Construtor Parametrizado
     * 
     * Cria um Pedido com o id fornecido e valores padrão para os outros atributos.
     * 
     * @param idPedido ID único do Pedido
     * @return Uma nova instância de Pedido
     */
    public Pedido(String idPedido) {
        this.idPedido = idPedido;
        this.tempoEspera = 0.0;
        this.nContribuinte = 0;
        this.notas = "";
        this.codMenus = new ArrayList<>();
        this.codPropostas = new ArrayList<>();
        this.propostas = PropostaDAO.getInstance();
        this.menus = MenuDAO.getInstance();
        this.tipoServico = null;
    }

    /** 
     * Método que devolve o tipo de serviço do Pedido
     * 
     * @return Tipo de Serviço do Pedido
     */
    public TipoServico getTipoPedido() {
        return tipoServico;
    }

    /**
     * Método que define o tipo de serviço do Pedido
     * 
     * @param tipo Tipo de Serviço do Pedido
     */
    public void setTipoPedido(String tipo) {
        this.tipoServico = TipoServico.fromString(tipo);
    }

    /**
     * Método que devolve o ID do Pedido
     * 
     * @return ID do Pedido
     */
    public String getCodPedido() {
        return idPedido;
    }

    /**
     * Método que devolve o tempo de espera estimado para o Pedido
     * 
     * @return Tempo de espera estimado para o Pedido
     */
    public double getTempoEspera() {
        return tempoEspera;
    }

    /**
     * Método que devolve o número de contribuinte do cliente que fez o Pedido
     * 
     * @return Número de contribuinte do cliente que fez o Pedido
     */
    public int getNrContribuinte() {
        return nContribuinte;
    }

    /** 
     * Método que devolve as notas adicionais do Pedido
     * 
     * @return Notas adicionais do Pedido
     */
    public String getNotas() {
        return notas;
    }

    /** 
     * Método que devolve os códigos (ID's) dos menus incluídos no Pedido
     * 
     * @return Códigos (ID's) dos menus incluídos no Pedido
     */
    public List<String> getCodMenus() {
        return codMenus;
    }

    /** 
     * Método que devolve os códigos (ID's) das propostas incluídas no Pedido
     * 
     * @return Códigos (ID's) das propostas incluídas no Pedido
     */
    public List<String> getCodPropostas() {
        return codPropostas;
    }

    /**
     * Método que altera o tempo de espera estimado para o Pedido
     * 
     * @param novoTempo Novo tempo de espera estimado para o Pedido
     */
    public void alterar_TempoEspera(double novoTempo) {
        this.tempoEspera = novoTempo;
    }

    /**
     * Método que adiciona uma nota ao Pedido 
     * Caso já existam notas, a nova nota é adicionada ao final, separada por "; "
     * 
     * @param nota Nota a ser adicionada ao Pedido
     */
    public void addNota(String nota) {
        if (this.notas == null || this.notas.isEmpty()) {
            this.notas = nota;
        } else {
            this.notas += ";" + nota;
        }
    }

    /** 
     * Método que define o número de contribuinte do cliente que fez o Pedido
     * 
     * @param novoContribuinte Novo número de contribuinte do cliente que fez o Pedido
     */
    public void setNrContribuinte(int novoContribuinte) {
        this.nContribuinte = novoContribuinte;
    }

    /** 
     * Método que devolve a coleção de alergénios presentes nas listas dos menus e propostas do Pedido
     * 
     * @return Coleção de alergénios presentes no Pedido
     */
    public Collection<Pair<String,Ingrediente>> getAlergenios() {
        Collection<Pair<String,Ingrediente>> alergenicos = new ArrayList<>();

        for (String codMenu : codMenus) {
            Menu menu = menus.get(codMenu);
            alergenicos.addAll(menu.getAlergenios());
        }

        for (String codProposta : codPropostas) {
            Proposta proposta = propostas.get(codProposta);
            alergenicos.addAll(proposta.getAlergenios());
        }

        return alergenicos;
    }

    /** 
     * Método que calcula o valor total do Pedido, somando os preços dos menus e propostas incluídos nele
     * 
     * @return Valor total do Pedido
     */
    public double calculaValorTotalPedido() {
        double total = 0.0;

        for (String codMenu : codMenus) {
            Menu menu = menus.get(codMenu);
            total += menu.getPrecoTotal();
        }

        for (String codProposta : codPropostas) {
            Proposta proposta = propostas.get(codProposta);
            total += proposta.getPreco();
        }

        return total;
    }

    /** 
     * Método que verifica se o Pedido contém um Menu específico
     * 
     * @param codMenu Código (ID) do Menu a ser verificado
     * @return true se o Menu estiver presente na lista de menus do Pedido, false caso contrário
     */
    public boolean containsMenu(String codMenu) {
        return codMenus.contains(codMenu);
    }

    /** 
     * Método que verifica se o Pedido contém uma Proposta específica
     * 
     * @param codProposta Código (ID) da Proposta a ser verificada
     * @return true se a Proposta estiver presente na lista de propostas do Pedido, false caso contrário
     */
    public boolean containsProposta(String codProposta) {
        return codPropostas.contains(codProposta);
    }

    /** 
     * Método que devolve um Menu específico do Pedido
     * 
     * @param codMenu Código (ID) do Menu a ser devolvido
     * @return Menu específico do Pedido
     */
    public Menu getMenu(String codMenu) {
        if (!codMenus.contains(codMenu)) {
            throw new IllegalArgumentException("Menu não pertence ao pedido.");
        }
        return menus.get(codMenu);
    }

    /** 
     * Método que devolve uma Proposta específica do Pedido
     * 
     * @param codProposta Código (ID) da Proposta a ser devolvida
     * @return Proposta específica do Pedido
     */
    public Proposta getProposta(String codProposta) {
        if (!codPropostas.contains(codProposta)) {
            throw new IllegalArgumentException("Proposta não pertence ao pedido.");
        }
        return propostas.get(codProposta);
    }

    /** 
     * Método que devolve uma lista detalhada dos itens (menus e propostas) do Pedido
     * 
     * @return Lista detalhada dos itens do Pedido
     */
    public String listarItensPedido() {
        StringBuilder sb = new StringBuilder();

        sb.append("Menus no Pedido:");
        if (codMenus.isEmpty()) {
            sb.append(" Nenhum\n\n");
        } else {
            sb.append("\n\n");
        }

        for (String codMenu : codMenus) {
            Menu menu = menus.get(codMenu);
            sb.append(menu.toString()).append("\n");
        }

        sb.append("Propostas no Pedido:");
        if (codPropostas.isEmpty()) {
            sb.append(" Nenhum\n\n");
        } else {
            sb.append("\n\n");
        }

        for (String codProposta : codPropostas) {
            Proposta proposta = propostas.get(codProposta);
            sb.append(proposta.toString()).append("\n");
        }

        return sb.toString();
    }
    
    /**
     * Método que devolve uma representação em String do Pedido
     * 
     * @return Representação em String do Pedido
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────────────────────────────────────────────────────────────────────────\n");
        sb.append("│ Pedido ID: ").append(idPedido).append("\n");
        sb.append("│ Tipo de Serviço: ").append(tipoServico.toString()).append("\n");
        if (tempoEspera <= 0) {
            sb.append("│ Tempo de Espera: Indisponível\n");
        } else {
            sb.append("│ Tempo de Espera: ").append(String.format("%.0f", tempoEspera)).append(" min\n");
        }
        if (nContribuinte != 0) {
            sb.append("│ Nº Contribuinte: ").append(nContribuinte).append("\n");
        }
        if (notas != null && !notas.isEmpty()) {
            sb.append("│ Notas:\n");
            String[] notasArray = notas.split(";");
            int i = 1;
            for (String nota : notasArray) {
                sb.append("│    ").append(i++).append(". ").append(nota.trim()).append("\n");
            }
        }
        sb.append("├─────────────────────────────────────────────────────────────────────────────\n");
        sb.append("│ Menus: ");
        for (String codMenu : codMenus) {
            sb.append(codMenu).append(" ");
        }
        sb.append("\n");
        sb.append("│ Propostas: ");
        for (String codProposta : codPropostas) {
            sb.append(codProposta).append(" ");
        }
        sb.append("\n");
        sb.append("├─────────────────────────────────────────────────────────────────────────────\n");
        sb.append("│ VALOR TOTAL: €").append(String.format("%.2f", calculaValorTotalPedido())).append("\n");
        sb.append("└─────────────────────────────────────────────────────────────────────────────");
        return sb.toString();
    }

    
    
}
