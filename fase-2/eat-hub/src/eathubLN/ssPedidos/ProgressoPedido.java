package eathubLN.ssPedidos;

import eathubDL.EstruturasDAO.MenuDAO;
import eathubDL.EstruturasDAO.PropostaDAO;
import java.util.HashMap;
import java.util.Map;

/** Classe responsável por gerir e acompanhar o progresso de confeção de um Pedido */
public class ProgressoPedido {

    /** Pedido associado ao progresso */
    private Pedido pedido;
    
    /** Progresso das propostas individuais pertencentes ao pedido */
    private Map<String, Integer> progressoPropostas;
    
    /** Progresso das propostas pertencentes aos menus do pedido */
    private Map<String, Map<String, Integer>> progressoMenus;
    
    /** Número total de etapas por proposta. */
    private Map<String, Integer> totalEtapasPorProposta;

    /** 
     * Construtor parametrizado
     * 
     * @param pedido Pedido cujo progresso será acompanhado
     * @return Uma nova instância de ProgressoPedido 
     */
    public ProgressoPedido(Pedido pedido) {
        this.pedido = pedido;
        this.progressoPropostas = new HashMap<>();
        this.progressoMenus = new HashMap<>();
        this.totalEtapasPorProposta = new HashMap<>();
        
        inicializarProgressos();
    }

    /**
     * Método que inicializa as estruturas de progresso para todas as propostas
     * (individuais e pertencentes a menus) associadas ao pedido.
     * Cada proposta começa na etapa 0 e é registado o total de etapas
     * que a mesma possui.
     * 
     */
    private void inicializarProgressos() {
        PropostaDAO propostaDAO = PropostaDAO.getInstance();
        MenuDAO menuDAO = MenuDAO.getInstance();
        
        // Inicializar progresso para propostas individuais
        for (String codProposta : pedido.getCodPropostas()) {
            Proposta proposta = propostaDAO.get(codProposta);
            if (proposta != null && proposta.getEtapas() != null) {
                progressoPropostas.put(codProposta, 0);
                totalEtapasPorProposta.put(codProposta, proposta.getEtapas().size());
            }
        }
        
        // Inicializar progresso para propostas dentro dos menus
        for (String codMenu : pedido.getCodMenus()) {
            Menu menu = menuDAO.get(codMenu);
            if (menu != null) {
                Map<String, Integer> progressoPropostasDoMenu = new HashMap<>();
                
                for (Proposta proposta : menu.getPropostasMenu()) {
                    if (proposta.getEtapas() != null) {
                        progressoPropostasDoMenu.put(proposta.getIdProposta(), 0);
                        totalEtapasPorProposta.put(proposta.getIdProposta(), proposta.getEtapas().size());
                    }
                }
                
                progressoMenus.put(codMenu, progressoPropostasDoMenu);
            }
        }
    }

    /** 
     * Método que retorna o pedido associado a este progresso 
     *
     * @return Pedido associado ao progresso 
     */
    public Pedido getPedido() {
        return pedido;
    }

    /** 
     * Obtém a próxima proposta e respetiva etapa que deve ser confeccionada.
     * A pesquisa é feita primeiro nas propostas individuais e,
     * caso todas estejam concluídas, nas propostas pertencentes a menus.
     * 
     * @return A próxima PropostaEtapa a confeccionar, ou null se todas estiverem concluídas
     */
    public PropostaEtapa obterProximaPropostaParaConfecionar() {
        PropostaDAO propostaDAO = PropostaDAO.getInstance();
        
        // Verificar propostas individuais
        for (String codProposta : pedido.getCodPropostas()) {
            Integer etapaAtual = progressoPropostas.get(codProposta);
            Integer totalEtapas = totalEtapasPorProposta.get(codProposta);
            
            if (etapaAtual != null && totalEtapas != null && etapaAtual < totalEtapas) {
                Proposta proposta = propostaDAO.get(codProposta);
                String proximaEtapa = proposta.getEtapas().get(etapaAtual);
                return new PropostaEtapa(codProposta, null, proximaEtapa, etapaAtual);
            }
        }
        
        // Verificar propostas dos menus
        MenuDAO menuDAO = MenuDAO.getInstance();
        for (String codMenu : pedido.getCodMenus()) {
            Menu menu = menuDAO.get(codMenu);
            if (menu != null) {
                Map<String, Integer> progressoPropostasDoMenu = progressoMenus.get(codMenu);
                
                if (progressoPropostasDoMenu != null) {
                    for (Proposta proposta : menu.getPropostasMenu()) {
                        String codProposta = proposta.getIdProposta();
                        Integer etapaAtual = progressoPropostasDoMenu.get(codProposta);
                        Integer totalEtapas = totalEtapasPorProposta.get(codProposta);
                        
                        if (etapaAtual != null && totalEtapas != null && etapaAtual < totalEtapas) {
                            String proximaEtapa = proposta.getEtapas().get(etapaAtual);
                            return new PropostaEtapa(codProposta, codMenu, proximaEtapa, etapaAtual);
                        }
                    }
                }
            }
        }
        
        return null; // Todas as propostas foram completadas
    }

    /** 
     * Método que avança a etapa de confeção da proposta especificada.
     * Se a proposta pertencer a um menu, o código do menu deve ser fornecido.
     * 
     * @param codProposta Código da proposta a avançar
     * @param codMenu Código do menu ao qual a proposta pertence (ou null se for individual)
     * @return true se a etapa foi avançada com sucesso, false caso contrário
     */
    public boolean avancarEtapaProposta(String codProposta, String codMenu) {
        boolean flag = false;
        if (codMenu == null) {
            // Proposta individual
            Integer etapaAtual = progressoPropostas.get(codProposta);
            Integer totalEtapas = totalEtapasPorProposta.get(codProposta);
            
            if (etapaAtual != null && totalEtapas != null && etapaAtual < totalEtapas) {
                progressoPropostas.put(codProposta, etapaAtual + 1);
                flag = true;
                if (etapaAtual + 1 == totalEtapas) {
                    progressoPropostas.remove(codProposta);
                }
            }
        } else {
            // Proposta dentro de um menu
            Map<String, Integer> progressoPropostasDoMenu = progressoMenus.get(codMenu);
            if (progressoPropostasDoMenu != null) {
                Integer etapaAtual = progressoPropostasDoMenu.get(codProposta);
                Integer totalEtapas = totalEtapasPorProposta.get(codProposta);
                
                if (etapaAtual != null && totalEtapas != null && etapaAtual < totalEtapas) {
                    progressoPropostasDoMenu.put(codProposta, etapaAtual + 1);
                    flag = true;
                    if (etapaAtual + 1 == totalEtapas) {
                        progressoPropostasDoMenu.remove(codProposta);
                    }
                }

                if (progressoPropostasDoMenu.isEmpty()) {/////////////////////////////////////////////////////////////
                    progressoMenus.remove(codMenu);
                }
            }
        }
        
        return flag;
    }

    /** 
     * Método que verifica se uma determinada proposta já foi concluída.
     * 
     * @param codProposta Código da proposta a verificar
     * @param codMenu Código do menu ao qual a proposta pertence (ou null se for individual)
     * @return true se a proposta estiver concluída, false caso contrário
     */
    public boolean isPropostaCompleta(String codProposta, String codMenu) {

        if (codMenu != null && !pedido.containsMenu(codMenu)) {
            throw new IllegalArgumentException("Menu não pertence ao pedido.");
        } else if (codMenu == null && !pedido.containsProposta(codProposta)) {
            throw new IllegalArgumentException("Proposta não pertence ao pedido.");
        } else if (codMenu != null && !(pedido.getMenu(codMenu).containsProposta(codProposta))) {
            throw new IllegalArgumentException("Proposta não pertence ao menu do pedido.");
        }

        if (codMenu == null) {
            return !progressoPropostas.containsKey(codProposta);
        } else {
            Map<String, Integer> progressoPropostasDoMenu = progressoMenus.get(codMenu);
            return progressoPropostasDoMenu == null || !progressoPropostasDoMenu.containsKey(codProposta);
        }
    }

    /** 
     * Método que verifica se todas as propostas do pedido foram concluídas.
     * 
     * @return true se todas as propostas estiverem concluídas, false caso contrário
     */
    public boolean isConcluido() {
        if (!progressoPropostas.isEmpty() || !progressoMenus.isEmpty()) {
            return false;
        }
        return true;
    }

}