package eathubUI.UIs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import Exceptions.PedidoNaoExisteException;
import eathubLN.IEatHubLN;
import eathubLN.ssComercial.Fatura;
import eathubLN.ssComercial.Talao;
import eathubLN.ssPedidos.Ementa;
import eathubLN.ssPedidos.Ingrediente;
import eathubLN.ssPedidos.Pedido;
import eathubUI.Menus.MenuOpcao;
import eathubUI.Menus.MenuUI;
import eathubLN.ssPedidos.Proposta;
import eathubLN.ssPedidos.Menu;
import eathubLN.ssCadeia.Pair;
import eathubLN.ssCadeia.Restaurante;

/** Interface do Cliente */
public class ClienteUI {
    /** Instância do Facade da Lógica de Negócio */
    private IEatHubLN lnFacade;
    /** Scanner para entrada do utilizador */
    private Scanner scanner;
    /** Guarda o ID do pedido que está a ser criado/editado */
    private String pedidoAtual;
    /** Guarda o ID do restaurante do pedido atual */
    private String restauranteAtual;

    /** 
     * Construtor da interface do Cliente
     * 
     * @param lnFacade Instância do Facade da Lógica de Negócio
     * @param scanner Scanner para entrada do utilizador
     */
    public ClienteUI(IEatHubLN lnFacade, Scanner scanner) {
        this.lnFacade = lnFacade;
        this.scanner = scanner;
        this.pedidoAtual = null;
        this.restauranteAtual = null;
    }

    /** 
     * Método principal para executar a interface do Cliente
     */
    public void run() {
        this.menuCliente();
    }

    /** 
     * Método que apresenta o menu do Cliente
     */
    private void menuCliente() {

        Collection<Restaurante> restaurantes = lnFacade.restauranteValues();
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║      ESCOLHA O RESTAURANTE                ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.println("Restaurantes disponíveis:\n");
        for(Restaurante r : restaurantes){
            System.out.println(" > " + r.getIdRestaurante() + ": " + r.getNome());
        }
        System.out.print("\nID Restaurante: ");
        String idRestaurante = scanner.nextLine().trim();
        boolean restauranteValido = false;
        for (Restaurante r : restaurantes) {
            if (r.getIdRestaurante().equals(idRestaurante)) {
                restauranteValido = true;
                break;
            }
        }
        if (!restauranteValido) {
            System.out.println("\nRestaurante inválido!");
            pausar();
            return;
        }


        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Fazer Novo Pedido", () -> this.fazerPedido(idRestaurante)));
        opcoes.add(new MenuOpcao("Consultar Ementa", () -> this.verEmentaDisponivel()));
        opcoes.add(new MenuOpcao("Consultar Pedidos Pendentes", () -> this.verPedidosPendentesExistentes(idRestaurante)));
        opcoes.add(new MenuOpcao("Pagar Pedido Pendente", () -> this.registarPagamento(idRestaurante, false)));
        opcoes.add(new MenuOpcao("Ver Estado de um Pedido Pendente", () -> this.verEstadoPedido(idRestaurante)));
        opcoes.add(new MenuOpcao("Cancelar Pedido Pendente", () -> this.cancelarPedido(idRestaurante)));
        new MenuUI(opcoes).run();
    }

    /**
     * Método para fazer um novo pedido
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void fazerPedido(String idRestaurante) {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║             CRIAR NOVO PEDIDO             ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        String novoPedido = lnFacade.criar_Pedido(idRestaurante);
        System.out.println("[Pedido criado com ID: " + novoPedido + "]");
        this.pedidoAtual = novoPedido;
        this.restauranteAtual = idRestaurante;

        if (!adicionarItensPedido()) {//1
            return; // Pedido foi cancelado durante a adição de itens
        }
        configurarDetalhesPedido();//2
        confirmarERegistarPedido();//5

    }

    /** 
     * Método para adicionar itens ao pedido atual
     * 
     * @return true se os itens foram adicionados com sucesso, false se o pedido foi cancelado
     */
    private boolean adicionarItensPedido() {
        boolean frstTime = true;
        boolean continuarAdicionando = true;
        Pedido p;
        try {
            p = lnFacade.getPedidoPendente(this.pedidoAtual, this.restauranteAtual);
        } catch (PedidoNaoExisteException e) {
            System.out.println("\n[Erro: " + e.getMessage() + "]");
            return false;
        }
        List<String> menusEscolhidos = p.getCodMenus();
        List<String> propostasEscolhidas = p.getCodPropostas();

        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║              Adicionar Itens              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        while (continuarAdicionando) {

            if (!frstTime) {
                limparEcra();
            } else {
                frstTime = false;
            }
            
            System.out.println("Pedido: " + this.pedidoAtual);
            System.out.println("Menus adicionados: " + (menusEscolhidos.isEmpty() ? "Nenhum" : menusEscolhidos.size()));
            System.out.println("Propostas adicionadas: " + (propostasEscolhidas.isEmpty() ? "Nenhum" : propostasEscolhidas.size()));
            System.out.println("\n" + "=".repeat(45) + "\n");
            
            // Mostrar ementa
            this.verEmentaDisponivel();
            
            System.out.println("\n" + "=".repeat(45));
            System.out.println("\nO que deseja adicionar?");
            System.out.println("1 - Adicionar Menu");
            System.out.println("2 - Adicionar Proposta");
            System.out.println("3 - Não adicionar mais itens");
            System.out.println("0 - Cancelar pedido");
            System.out.print("\nEscolha: ");
            
            String escolha = scanner.nextLine();
            
            switch (escolha) {
                case "1":
                    System.out.print("\nCódigo do menu (ex: M1): ");
                    String codMenu = scanner.nextLine();
                    boolean flagM = lnFacade.menuExiste(codMenu);
                    if (!codMenu.trim().isEmpty() && flagM) {
                        menusEscolhidos.add(codMenu);
                        System.out.println("[Menu " + codMenu + " adicionado!]\n");
                    } else if (!flagM) {
                        System.out.println("[Menu " + codMenu + " não existe!]\n");
                    }
                    break;
                    
                case "2":
                    System.out.print("\nCódigo da proposta (ex: PROP1): ");
                    String codProposta = scanner.nextLine();
                    boolean flagP = lnFacade.propostaExiste(codProposta);
                    if (!codProposta.trim().isEmpty() && flagP) {
                        propostasEscolhidas.add(codProposta);
                        System.out.println("[Proposta " + codProposta + " adicionada!]\n");
                    } else if (!flagP) {
                        System.out.println("[Proposta " + codProposta + " não existe!]\n");
                    }
                    break;
                    
                case "3":
                    if (menusEscolhidos.isEmpty() && propostasEscolhidas.isEmpty()) {
                        System.out.println("\nAdicione pelo menos um item ao pedido!");
                        pausar();
                    } else {
                        continuarAdicionando = false;
                    }
                    break;
                    
                case "0":
                    System.out.print("\nTem certeza que deseja cancelar? (S/N): ");
                    if (scanner.nextLine().equalsIgnoreCase("S")) {
                        lnFacade.cancelar_pedido(this.pedidoAtual, this.restauranteAtual);
                        this.pedidoAtual = null;
                        System.out.println("[Pedido cancelado.]");
                        pausar();
                        return false;
                    }
                    break;
                    
                default:
                    System.out.println("Opção inválida!");
                    pausar();
            }
        }
        return true;

    }

    /** 
     * Método para configurar os detalhes do pedido atual
     */
    private void configurarDetalhesPedido() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║            Informações Adicionais         ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("\nVer alergénios presentes no pedido? (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            verAlergeniosPedido(this.pedidoAtual, this.restauranteAtual);
        }

        registarTipoServico(this.pedidoAtual, this.restauranteAtual);//3

        realizarAlteracoesPedido(this.pedidoAtual);//4 
        
        System.out.print("Deseja inserir uma nota? (S/N): ");

        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            boolean continuar = true;

                System.out.print("Nota: ");
                String nota = scanner.nextLine().trim();
                try {
                    lnFacade.getPedidoPendente(this.pedidoAtual, this.restauranteAtual).addNota(nota);
                    System.out.println("[Nota registada.]");
                } catch (PedidoNaoExisteException e) {
                    System.out.println("\n[Erro: " + e.getMessage() + "]");
                }

            while(continuar){
                System.out.print("\nDeseja adicionar mais alguma nota?(S/N): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                    System.out.print("Nota: ");
                    nota = scanner.nextLine().trim();
                    try {
                        lnFacade.getPedidoPendente(this.pedidoAtual, this.restauranteAtual).addNota(nota);
                        System.out.println("[Nota registada]");
                    } catch (PedidoNaoExisteException e) {
                        System.out.println("[Erro: " + e.getMessage() + "]");
                    }
                } else {
                    continuar = false;
                }
            }
        }

        System.out.print("Deseja inserir número de contribuinte? (S/N): ");

        if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
            System.out.println("\n[Número de contribuinte não será inserido.]");
        } else {
            System.out.print("\nNúmero de Contribuinte: ");
            String nifStr = scanner.nextLine().trim();
            try {
                int nif = Integer.parseInt(nifStr);
                lnFacade.registar_contribuinte_Pedido(nif, this.pedidoAtual, this.restauranteAtual);
                System.out.println("[Contribuinte registado]");
            } catch (NumberFormatException e) {
                System.out.println("[Erro: NIF inválido]");
            } catch (PedidoNaoExisteException e) {
                System.out.println("\n[Erro: " + e.getMessage() + "]");
            }
        }
        
        pausar();

    }

    /** 
     * Método para confirmar e registar o pedido atual
     */
    private void confirmarERegistarPedido() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║              Finalizar Pedido             ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("Deseja finalizar e pagar o pedido? (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            String idPagamento = registarPagamento(this.restauranteAtual, true);
            if (idPagamento == null) {
                return;
            }

            try {
                System.out.println("[Tempo de espera estimado: " + lnFacade.getPedidoBD(pedidoAtual).getTempoEspera() + " minutos.]");
            } catch (PedidoNaoExisteException e) {
                System.out.println("\n[Erro: " + e.getMessage() + "]");
            }
        } else {
            System.out.println("[Pedido não finalizado. Pague para finalizar o pedido.]");
        }
        this.pedidoAtual = null; // Limpa o pedido atual
    }

    /**
     * Método que apresenta a ementa disponível
     */
    private void verEmentaDisponivel() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║         EMENTA DISPONÍVEL                 ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Ementa ementa = lnFacade.obter_ementa();
        
        if (ementa != null) {
            System.out.println(ementa.toString());
        } else {
            System.out.println("[Não há ementa disponível no momento.]");
        }
        
        pausar();
    }

    /**
     * Método que apresenta os pedidos pendentes existentes
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void verPedidosPendentesExistentes(String idRestaurante) {

        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       PEDIDOS PENDENTES                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.println("Restaurante: " + idRestaurante + "\n");
        Set<String> todosPedidosPendentes = lnFacade.obterIds_CodsPedidos_Pendentes(idRestaurante);
        
        System.out.println("Lista de pedidos pendentes:\n");
        int count = 0;
        for(String codPedido : todosPedidosPendentes){
            try {
                if (lnFacade.ePendente(codPedido, idRestaurante)) {
                    Pedido p = lnFacade.getPedidoPendente(codPedido, idRestaurante);
                    System.out.println("  » Pedido: " + codPedido + "\n    Valor: €" + String.format("%.2f", p.calculaValorTotalPedido()) + "\n");
                    count++;
                }
            } catch (PedidoNaoExisteException e) {
                // Pedido não existe, ignorar
            }
        }
        
        if (count == 0) {
            System.out.println("[Não há pedidos pendentes no momento.]");
        }

        pausar();
    }

    

    /**
     * Método que apresenta o estado de um pedido
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void verEstadoPedido(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║         ESTADO DO PEDIDO                  ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        String codPedido = obterCodigoPedido();
        if (codPedido == null) return;
        
        boolean isPendente = lnFacade.ePendente(codPedido, idRestaurante);
        
        if (isPendente) {
            try {
                System.out.println("O pedido " + codPedido + " está pendente. Detalhes:");
                System.out.println(lnFacade.getPedidoPendente(codPedido, idRestaurante).toString());
            } catch (PedidoNaoExisteException e) {
                System.out.println("[Erro: " + e.getMessage() + "]");
            }
        } else {
            System.out.println("[O pedido " + codPedido + " não está pendente ou não existe.]");
        }
        
        pausar();
    
    }

    /** 
     * Método para cancelar um pedido pendente
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void cancelarPedido(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║         CANCELAR PEDIDO                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        String codPedido = obterCodigoPedido();
        if (codPedido == null) return;

        boolean confirmado = lnFacade.pagamento_confirmado(codPedido);
        if (confirmado) {
            System.out.println("\n[Erro: O pagamento do pedido já foi concluído. Não é possível cancelar o pedido.]");
            pausar();
            return;
        }

        boolean isPendente = lnFacade.ePendente(codPedido, idRestaurante);

        if (!isPendente) {
            System.out.println("\n[Erro: O código do pedido não é válido ou o pedido já foi registado.]");
            pausar();
            return;
        }
        
        System.out.print("Tem certeza que deseja cancelar o pedido " + codPedido + "? (S/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S") && isPendente) {
            lnFacade.cancelar_pedido(codPedido, idRestaurante);
            System.out.println("\n[Pedido cancelado com sucesso!]");
            if (codPedido.equals(this.pedidoAtual)) {
                this.pedidoAtual = null;
            }
        } else {
            System.out.println("\n[Cancelamento abortado.]");
        }

        pausar();
    }

    //////////////////////////// Métodos auxiliares ///////////////////////////

    /**
     * Método que apresenta os alergénios de um pedido
     * 
     * @param codPedido Código do Pedido
     * @param idRestaurante ID do Restaurante
     */
    private void verAlergeniosPedido(String codPedido, String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       ALERGÉNIOS DO PEDIDO                ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        

        try {
            Collection<Pair<String,Ingrediente>> alergenios;
            if (lnFacade.ePendente(codPedido, idRestaurante)) {
                alergenios = lnFacade.obter_alergenios_vPendente(codPedido, idRestaurante);
            } else {
                alergenios = lnFacade.obter_alergenios_vBD(codPedido);
            }
            Set<Pair<String,Ingrediente>> alergSet = new HashSet<>(alergenios);
            
            System.out.println("Alergénios encontrados:\n");
            if (alergSet.isEmpty()) {
                System.out.println("[Nenhum alergénio identificado.]");
            } else {
                alergSet.forEach(a -> System.out.println("  ⚠ " + a.getFirst() + " (Ingrediente: " + a.getSecond().getNome() + ")"));
            }
        } catch (PedidoNaoExisteException e) {
            System.out.println("[Erro: " + e.getMessage() + "]");
        }
        
        pausar();
    }

    /**
     * Método para registar um pedido
     * 
     * @param codPedido Código do Pedido
     * @param idRestaurante ID do Restaurante
     */
    private void registarPedido(String codPedido, String idRestaurante) {
        
        if (codPedido == null) return;

        boolean confirmado = lnFacade.pagamento_confirmado(codPedido);
        if (!confirmado) {
            System.out.println("[Erro: O pagamento do pedido ainda não foi confirmado. Não é possível registar o pedido.]");
            pausar();
            return;
        }

        boolean isPendente = lnFacade.ePendente(codPedido, idRestaurante);
        if (!isPendente) {
            System.out.println("[Erro: O pedido já foi registado anteriormente.]");
            pausar();
            return;
        }
        
        try {
            lnFacade.regista_pedido(codPedido, idRestaurante);
            System.out.println("[Pedido confirmado e registado!]");
            lnFacade.addPedidoQueue(codPedido, idRestaurante);
            System.out.println("[Pedido " + codPedido + " registado e adicionado à fila!]");
        } catch (PedidoNaoExisteException e) {
            System.out.println("[Erro: " + e.getMessage() + "]");
        }
        
        pausar();
    }

    /**
     * Método para registar o tipo de serviço do pedido
     * 
     * @param novoPedido Código do Pedido
     * @param idRestaurante ID do Restaurante
     */
    private void registarTipoServico(String novoPedido, String idRestaurante) {
        System.out.println("\nEscolha o tipo de serviço para o seu pedido:");
        System.out.println("1 - Takeway");
        System.out.println("2 - Serviço de Mesa");
        System.out.print("\n>>> ");

        String tipoServico = scanner.nextLine();
        if (tipoServico.equals("1")) {
            tipoServico = "Takeway";
        } else if (tipoServico.equals("2")) {
            tipoServico = "ServicoMesa";
        } else {
            while(true){
                System.out.print("Opção inválida. Por favor, escolha 1 ou 2: ");
                tipoServico = scanner.nextLine();
                if (tipoServico.equals("1")) {
                    tipoServico = "Takeway";
                    break;
                } else if (tipoServico.equals("2")) {
                    tipoServico = "ServicoMesa";
                    break;
                }
            }
        }

        try {
            lnFacade.registar_tipo_pedido(tipoServico, novoPedido, idRestaurante);
        } catch (PedidoNaoExisteException e) {
            System.out.println("[Erro: " + e.getMessage() + "]");
            pausar();
            return;
        }
    }

    /** 
     * Método para realizar alterações no pedido antes de confirmar
     * 
     * @param codPedido Código do Pedido
     */
    private void realizarAlteracoesPedido(String codPedido){
        System.out.print("\nDeseja fazer alterações em algum dos itens do pedido antes de continuar? (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {

            Pedido p = null;

            try {
                p = lnFacade.getPedidoPendente(codPedido, this.restauranteAtual);
                System.out.println("\nItens atuais do pedido:\n");
                System.out.println(p.listarItensPedido());
            } catch (PedidoNaoExisteException e) {
                System.out.println("[Erro: " + e.getMessage() + "]");
                return;
            }

            boolean continuar = true;
            while(continuar){

                System.out.println("\nO que deseja alterar?");
                System.out.println("1 - Propostas");
                System.out.println("2 - Menus");
                System.out.println("0 - Terminar Alterações");
                System.out.print("\nEscolha: ");
                
                String escolha = scanner.nextLine();
                
                switch (escolha) {
                    case "1":

                        if (p.getCodPropostas().isEmpty()) {
                            System.out.println("[Não há propostas no pedido para alterar!]\n");
                            break;
                        }

                        System.out.print("\nCódigo do item a alterar (Proposta): ");
                        String codProposta = scanner.nextLine();

                        if (!p.containsProposta(codProposta)) {
                            System.out.println("[Proposta " + codProposta + " não está no pedido!]\n");
                            break;
                        }

                        this.alterar_Ingredientes_Proposta_Pedido(p, codProposta, null);
                        break;
                    case "2":

                        if (p.getCodMenus().isEmpty()) {
                            System.out.println("[Não há menus no pedido para alterar!]\n");
                            break;
                        }

                        System.out.print("\nCódigo do item a alterar (Menu): ");
                        String codMenu = scanner.nextLine();

                        if (!p.containsMenu(codMenu)) {
                            System.out.println("[Menu " + codMenu + " não está no pedido!]\n");
                            break;
                        }

                        this.alterar_Ingredientes_Menu_Pedido(p, codMenu);
                        break;
                    case "0":
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        }
        
    }

    /** 
     * Método para alterar ingredientes de um menu no pedido
     * 
     * @param p Pedido
     * @param codMenu Código do Menu
     */
    private void alterar_Ingredientes_Menu_Pedido(Pedido p, String codMenu) {

        Menu menu = lnFacade.menuGet(codMenu);

        if (menu == null) {
            System.out.println("[Menu " + codMenu + " não existe!]\n");
            return;
        }

        System.out.println("\n" +lnFacade.listMenuPropostas(codMenu));

        while(true){
            System.out.print("Código da proposta a alterar dentro do menu (ou 'sair' para terminar): ");
            String codProposta = scanner.nextLine();
            if (codProposta.equalsIgnoreCase("sair")) {
                break;
            }
            if (!menu.containsProposta(codProposta)) {
                System.out.println("[Proposta " + codProposta + " não está no menu " + codMenu + "!]\n");
                continue;
            }

            this.alterar_Ingredientes_Proposta_Pedido(p, codProposta, codMenu);
        }

    }

    /** 
     * Método para alterar ingredientes de uma proposta no pedido
     * 
     * @param p Pedido
     * @param codProposta Código da Proposta
     * @param codMenu Código do Menu (opcional, pode ser null)
     */
    private void alterar_Ingredientes_Proposta_Pedido(Pedido p, String codProposta, String codMenu) {

        Proposta prop = lnFacade.propostaGet(codProposta);

        if (prop == null) {
            System.out.println("[Proposta " + codProposta + " não existe!]\n");
            return;
        }

        System.out.println("\n" + lnFacade.listPropostaIngredientes(codProposta));

        System.out.println("Que tipo de alteração deseja fazer?");
        System.out.println("1 - Remover Ingredientes");
        System.out.println("2 - Adicionar Ingredientes");
        System.out.print("\nEscolha: ");

        String tipoAlteracao = scanner.nextLine();

        if (tipoAlteracao.equals("1")) {
            if (prop.getIngredientes().size() == 1) {
                System.out.println("[Não é possível remover ingredientes de uma proposta com apenas um ingrediente!]\n");
                return;
            }
            System.out.print("Ingredientes a remover (separados por vírgula): ");
            List<String> ingredientesList = Arrays.asList(scanner.nextLine().split(","));

            if (ingredientesList.size() == 0) {
                System.out.println("[Nenhum ingrediente especificado para remoção!]\n");
                return;
            } else if (ingredientesList.size() >= prop.getIngredientes().size()) {
                System.out.println("[Não é possível remover todos os ingredientes da proposta!]\n");
                return;
            }

            boolean allExist = true;

            for (String ing : ingredientesList) {
                boolean exists = false;
                for (Ingrediente i : prop.getIngredientes()) {
                    if (i.getNome().equalsIgnoreCase(ing)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    allExist = false;
                    System.out.println("[Ingrediente " + ing + " não existe na proposta " + codProposta + "!]\n");
                    System.out.println("[Operação de remoção abortada.]");
                    System.out.println("Tente novamente.\n");
                    break;
                }
            }

            if (!allExist) {
                return;
            }

            if (codMenu == null) {
                lnFacade.alterarIngredientesProposta(p, codProposta, ingredientesList, false);
            } else {
                lnFacade.alterarIngredientesPropostaMenu(p, codMenu, codProposta, ingredientesList, false);
            }

            System.out.println("[Operação de remoção concluída!]\n");

        } else if (tipoAlteracao.equals("2")) {
            System.out.print("Ingredientes a adicionar (separados por vírgula): ");
            List<String> ingredientesList = Arrays.asList(scanner.nextLine().split(","));

            if (ingredientesList.size() == 0) {
                System.out.println("[Nenhum ingrediente especificado para adição!]\n");
                return;
            } else {
                // Verificar se os ingredientes já existem na proposta
                boolean anyExists = false;

                for (String ing : ingredientesList) {
                    for (Ingrediente i : prop.getIngredientes()) {
                        if (i.getNome().equalsIgnoreCase(ing)) {
                            anyExists = true;
                            System.out.println("[Ingrediente " + ing + " já existe na proposta " + codProposta + "!]\n");
                            System.out.println("[Operação de adição abortada.]");
                            System.out.println("Tente novamente.\n");
                            break;
                        }
                    }
                    if (anyExists) {
                        break;
                    }
                }

                if (anyExists) {
                    return;
                }
            }

            boolean ingValid = true;

            for (String ing : ingredientesList) {
                boolean ingredienteValido = lnFacade.ingredienteExiste(ing);
                if (!ingredienteValido) {
                    System.out.println("[Ingrediente " + ing + " não é válido no sistema!]\n");
                    System.out.println("[Operação de adição abortada.]");
                    System.out.println("Tente novamente.\n");
                    ingValid = false;
                    break;
                }
            }

            if (!ingValid) {
                return;
            }

            if (codMenu == null) {
                lnFacade.alterarIngredientesProposta(p, codProposta, ingredientesList, true);
            } else {
                lnFacade.alterarIngredientesPropostaMenu(p, codMenu, codProposta, ingredientesList, true);
            }
            System.out.println("[Operação de adição concluída!]\n");
        } else {
            System.out.println("[Tipo de alteração inválido!]\n");
        }
    }

    /** 
     * Método para registar o pagamento de um pedido
     * 
     * @param idRestaurante ID do Restaurante
     * @param pagamentoImediato Indica se o pagamento é imediato (pedido atual) ou não
     * @return ID do pagamento registado, ou null em caso de erro
     */
    private String registarPagamento(String idRestaurante, boolean pagamentoImediato) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║             REGISTAR PAGAMENTO            ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        String codPedido;

        if (!pagamentoImediato) {
            System.out.print("Código do pedido a pagar: ");
            codPedido = scanner.nextLine();

            boolean isPendente = lnFacade.ePendente(codPedido, idRestaurante);
            if (!isPendente) {
                System.out.println("[Erro: O código do pedido não é válido ou o pedido já foi registado.]");
                pausar();
                return null;
            }
        } else {
            System.out.println("Pagamento do pedido atual: " + this.pedidoAtual);
            codPedido = this.pedidoAtual;
        }

        Pedido p = null;

        try {
            p =lnFacade.getPedidoPendente(codPedido, idRestaurante);
        } catch (PedidoNaoExisteException e) {
            System.out.println("[Erro: " + e.getMessage() + "]");
            pausar();
            return null;
        }

        double valorPedido = p.calculaValorTotalPedido();
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Valor a pagar: €" + String.format("%-23.2f", valorPedido));
        System.out.println("└─────────────────────────────────────────┘\n");
        
        System.out.println("Métodos de pagamento:");
        System.out.println("1 - Dinheiro");
        System.out.println("2 - MultiBanco");
        System.out.println("3 - MBWay");

        String opcao = null;

        while(true){
            System.out.print("\nEscolha o método: ");
            opcao = scanner.nextLine();

            if (Arrays.asList("1", "2", "3").contains(opcao)) {
                break;
            } else {
                System.out.println("[Erro: Método de pagamento inválido! Tente novamente.]");
            }
        }
        
        String metodoPagamento = switch (opcao) {
            case "1" -> "Dinheiro";
            case "2" -> "MultiBanco";
            case "3" -> "MBWay";
            default -> opcao;
        };
        
        String idPagamento = lnFacade.regista_pagamento(valorPedido, codPedido, metodoPagamento);

        if (idPagamento == null) {
            System.out.println("[Erro: Erro no pagamento. Pedido não finalizado.]");
            return null;
        }

        System.out.println("\n[Pagamento registado com sucesso! ID Pagamento: " + idPagamento + "]");
        registarPedido(codPedido, idRestaurante);

        Fatura f = null;

        try {
            f = lnFacade.gera_fatura(idPagamento, lnFacade.getPedidoBD(codPedido));
        } catch (PedidoNaoExisteException e) {
            System.out.println("[Erro: " + e.getMessage() + "]");
            pausar();
            return null;
        }
        Talao t = lnFacade.gera_talao(this.pedidoAtual);

        System.out.println("\n" + f.toString());
        System.out.println("\n" + t.toString());

        pausar();

        return idPagamento;
    }
    
    
    /**
     * Pausa a execução até o utilizador pressionar ENTER
     */
    private void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Obtém o código do pedido (usa o atual ou pede ao utilizador)
     */
    private String obterCodigoPedido() {
        System.out.print("Digite o código do pedido: ");
        String codigo = scanner.nextLine();
        if (codigo.trim().isEmpty()) {
            System.out.println("[Erro: Código do pedido não pode ser vazio.]");
            pausar();
            return null;
        }
        return codigo;
    }

    /**
     * Limpa o ecrã
     */
    private void limparEcra() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
}
