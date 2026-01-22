package eathubUI.UIs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import Exceptions.PedidoNaoExisteException;
import Exceptions.PedidoVaiAtrasarException;
import eathubLN.IEatHubLN;
import eathubLN.ssCadeia.Funcionario;
import eathubLN.ssCadeia.MensagemGestor;
import eathubLN.ssPedidos.Pedido;
import eathubLN.ssPedidos.ProgressoPedido;
import eathubLN.ssPedidos.PropostaEtapa;
import eathubUI.Menus.MenuOpcao;
import eathubUI.Menus.MenuUI;

/**
 * Interface de Utilizador para Funcionários
 */
public class FuncionarioUI {
    /** Instância do Facade da Lógica de Negócio */
    private IEatHubLN lnFacade;
    /** Scanner para entrada do utilizador */
    private Scanner scanner;

    /**
     * Construtor da classe FuncionarioUI
     * 
     * @param lnFacade Instância do Facade da Lógica de Negócio
     * @param scanner  Scanner para entrada do utilizador
     */
    public FuncionarioUI(IEatHubLN lnFacade, Scanner scanner) {
        this.lnFacade = lnFacade;
        this.scanner = scanner;
    }

    /**
     * Inicia a interface do funcionário com o ID fornecido
     * 
     * @param idFuncionario ID do Funcionário
     */
    public void run(String idFuncionario) {
        this.menuFuncionario(idFuncionario);
    }
    
    /**
     * Método que cria o menu do funcionário
     * 
     * @param idFuncionario ID do Funcionário
     */
    private void menuFuncionario(String idFuncionario) {

        String idRestaurante = lnFacade.getRestauranteDeFuncionario(idFuncionario);

        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Ver Perfil", () -> this.verPerfil(idFuncionario)));
        opcoes.add(new MenuOpcao("Ver Próximo Pedido", () -> this.verProximoPedido(idRestaurante)));
        opcoes.add(new MenuOpcao("Ver Fila de Pedidos", () -> this.verFilaPedidos(idRestaurante)));
        opcoes.add(new MenuOpcao("Sinalizar início do próximo pedido", () -> lnFacade.obterProgressoPedidoAtual(idRestaurante) == null, () -> this.iniciarProximoPedido(idRestaurante)));
        opcoes.add(new MenuOpcao("Processar Pedido", () -> lnFacade.obterProgressoPedidoAtual(idRestaurante) != null, () -> this.processarPedido(idRestaurante)));
        opcoes.add(new MenuOpcao("Consultar Detalhes de um Pedido Pendente", () -> this.consultarDetalhesPedidoPendente(idRestaurante)));
        opcoes.add(new MenuOpcao("Consultar Detalhes de um Pedido Finalizado", () -> this.consultarDetalhesPedidoFinalizado()));
        opcoes.add(new MenuOpcao("Ver mensagens do Gestor", () -> this.verMensagensGestor()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método que apresenta as mensagens do gestor
     */
    private void verMensagensGestor() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       MENSAGENS DO GESTOR                 ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<MensagemGestor> mensagens = lnFacade.obter_mensagens_Gestor();
        
        if (mensagens.isEmpty()) {
            System.out.println("[Não há mensagens do gestor.]");
        } else {
            System.out.println("Lista de mensagens:\n");
            for (MensagemGestor msg : mensagens) {
                System.out.println("┌───────────────────────────────────────────────────────────────────────────────┐");
                System.out.println("│ ID: " + msg.getId());
                System.out.println("│ Gestor: " + msg.getIdGestor());
                System.out.println("│ Data: " + msg.getDataEnvio());
                System.out.println("│ Conteúdo: " + msg.getConteudo());
                System.out.println("└───────────────────────────────────────────────────────────────────────────────┘\n");
            }
        }
        
        pausar();
    }

    /** 
     * Método que apresenta o próximo pedido na fila
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void verProximoPedido(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║      PRÓXIMO PEDIDO NA FILA               ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        String proximoPedido = lnFacade.peekPedidoQueue(idRestaurante);
        
        if (proximoPedido != null && !proximoPedido.isEmpty()) {
            try {
                System.out.println("Próximo pedido a ser preparado: " + proximoPedido);
                System.out.println("Detalhes do pedido:");
                System.out.println(lnFacade.getPedidoBD(proximoPedido).toString());
            } catch (PedidoNaoExisteException e) {
                System.out.println("[Erro: " + e.getMessage() + "]");
                pausar();
                return;
            }
        } else {
            System.out.println("[Não há pedidos na fila.]");
        }
        
        pausar();
    }

    /** 
     * Método que apresenta a fila de pedidos pendentes
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void verFilaPedidos(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║         FILA DE PEDIDOS                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<String> pedidosFilaEspera = lnFacade.obterIds_CodsPedidos_FilaEspera(idRestaurante);
        
        if (pedidosFilaEspera.isEmpty()) {
            System.out.println("[Não há pedidos pendentes.]");
        } else {
            System.out.println("Pedidos aguardando preparação:\n");
            int i = 1;
            for (String id : pedidosFilaEspera) {
                System.out.println("  " + i++ + ". " + id);
            }
            System.out.println("\n───────────────────────────────────────────");
            System.out.println("Total: " + pedidosFilaEspera.size() + " pedido(s)");
        }
        
        pausar();
    }

    /** 
     * Método que inicia o próximo pedido na fila
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void iniciarProximoPedido(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║      INICIAR PRÓXIMO PEDIDO               ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        ProgressoPedido progresso = lnFacade.obterProgressoPedidoAtual(idRestaurante);
        if (progresso != null) {
            System.out.println("[Atenção: Já existe um pedido em progresso neste restaurante.]");
            System.out.println("[Por favor, finalize o pedido atual antes de iniciar outro.]");
        } else {
            try {
                String proximoPedido = lnFacade.peekPedidoQueue(idRestaurante);
                if (proximoPedido == null || proximoPedido.isEmpty()) {
                    System.out.println("[Não há pedidos na fila para iniciar.]");
                    pausar();
                    return;
                }

                ProgressoPedido pedidoAntigo = lnFacade.obterProgressoAnteriorAtrasado(idRestaurante, proximoPedido);
                if (pedidoAntigo == null) {
                    ProgressoPedido novoProgresso = new ProgressoPedido(lnFacade.getPedidoBD(lnFacade.determina_proximo_pedido(idRestaurante)));
                    lnFacade.definirProgressoPedidoAtual(idRestaurante, novoProgresso);

                } else {
                    lnFacade.determina_proximo_pedido(idRestaurante); // Remove o pedido da fila
                    lnFacade.definirProgressoPedidoAtual(idRestaurante, pedidoAntigo); 
                }

                System.out.println("[Iniciado o tratamento do próximo pedido: " + proximoPedido + "]");

            } catch (Exception e) {
                System.out.println("[Erro ao obter o próximo pedido: " + e.getMessage() + "]");
                pausar();
                return;
            }
        }

        pausar();
        return;
    }

    /** 
     * Método que processa o pedido em progresso
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void processarPedido(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║         PROCESSAR PEDIDO                  ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        ProgressoPedido progresso = lnFacade.obterProgressoPedidoAtual(idRestaurante);
        if (progresso == null) {
            System.out.println("[Não há nenhum pedido em progresso neste restaurante.]");
            pausar();
            return;
        }
        
        // Verificar se já está concluído
        if (progresso.isConcluido()) {
            System.out.println("[Este pedido já está completamente confecionado!]");
            System.out.println("Prima Enter para finalizar o pedido.");
            scanner.nextLine();
            
            // Remover pedido em progresso
            lnFacade.definirProgressoPedidoAtual(idRestaurante, null);
            System.out.println("[Pedido finalizado!]");
            pausar();
            return;
        }

        Pedido pedido = progresso.getPedido();

        while(!progresso.isConcluido()) {
            
            // Mostrar próxima proposta a confecionar
            PropostaEtapa proximaProposta = lnFacade.obterProximaPropostaParaConfecionar(idRestaurante);
            
            if (proximaProposta != null) {

                String idFuncionario = lnFacade.obterIdFuncionarioTarefa_Igual_Etapa(idRestaurante, proximaProposta.getNomeEtapa());
                if (idFuncionario == null) {
                    System.out.println("\n[Erro: Não há funcionário disponível para a etapa " + proximaProposta.getNomeEtapa() + ".]");
                    System.out.print("Abortar processamento do pedido(S/N): ");
                    String escolha = scanner.nextLine().trim().toUpperCase();
                    if (escolha.equals("S")) {
                        System.out.println("[Processamento do pedido abortado pelo funcionário.]");
                        lnFacade.definirProgressoPedidoAtual(idRestaurante, null);
                    }
                    pausar();//Não deve acontecer nunca se o sistema estiver bem configurado
                    return;
                }

                boolean sucesso = false;

                try {
                    sucesso = lnFacade.processarEtapaAtualProposta(idRestaurante, idFuncionario, proximaProposta, pedido.getCodPedido());
                } catch (PedidoVaiAtrasarException e) {
                    System.out.println("\n[Aviso: " + e.getMessage() + "]");
                    pausar();
                    return;//Volta ao menu principal para o funcionário decidir o que fazer
                }

                if (sucesso) {
                    if (proximaProposta.getCodMenu() != null) {// O proximaProposta.getIndiceEtapa() + 1 deve-se a que o resultado sem o + 1 é sem contar a etapa atual feita
                        System.out.println("\n[Funcionário " + idFuncionario + " inicia a " + (proximaProposta.getIndiceEtapa() + 1) + "º etapa: " + proximaProposta.getNomeEtapa() + " da proposta " + proximaProposta.getCodProposta() + " do menu " + proximaProposta.getCodMenu() + ".]");
                    } else {
                        System.out.println("\n[Funcionário " + idFuncionario + " inicia a " + (proximaProposta.getIndiceEtapa() + 1) + "º etapa: " + proximaProposta.getNomeEtapa() + " da proposta " + proximaProposta.getCodProposta() + ".]");
                    }

                    try {
                        if (progresso.isPropostaCompleta(proximaProposta.getCodProposta(), proximaProposta.getCodMenu())) {
                            if (proximaProposta.getCodMenu() != null) {
                                System.out.println("\n[Proposta " + proximaProposta.getCodProposta() + " do menu " + proximaProposta.getCodMenu() + " completamente confecionada!]");
                            } else {
                                System.out.println("\n[Proposta " + proximaProposta.getCodProposta() + " completamente confecionada!]");
                            }
                            boolean sucessoRegisto = lnFacade.registar_Ingredientes_Utilizados_Proposta(idRestaurante, proximaProposta.getCodProposta());
                            if (!sucessoRegisto) {
                                System.out.println("[Erro ao registar os ingredientes utilizados no stock do restaurante.]");
                                System.out.println("[Pedido eliminado do progresso atual.]");
                                lnFacade.definirProgressoPedidoAtual(idRestaurante, null);
                                pausar();
                                return;
                            }
                        }
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (IllegalArgumentException e) {
                        System.out.println("[Erro ao verificar o estado da proposta: " + e.getMessage() + "]");
                    }

                } else {// O proximaProposta.getIndiceEtapa() + 1 deve-se a que o resultado sem o + 1 é sem contar a etapa atual feita
                    System.out.println("[Erro ao processar a a " + (proximaProposta.getIndiceEtapa() + 1) + "º etapa: " + proximaProposta.getNomeEtapa() + ".");
                    System.out.println("[Pedido eliminado do progresso atual.]");
                    lnFacade.definirProgressoPedidoAtual(idRestaurante, null);
                    pausar();
                    return;
                }

            } else {
                System.out.println("\n[Todas os itens do pedido foram processados.]");
                break;
            }
            
        }

        System.out.println("\nPrima Enter para finalizar o pedido.");
        scanner.nextLine();
        limparEcra();

        lnFacade.definirProgressoPedidoAtual(idRestaurante, null);
        System.out.println("[Pedido finalizado e prestes a ser entregue por um funcionário!]");

        lnFacade.criar_Historico(progresso.getPedido(), idRestaurante);//Pedido finalizado e guardado no histórico

        pausar();
    }

    /** 
     * Método que consulta os detalhes de um pedido pendente
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void consultarDetalhesPedidoPendente(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║    CONSULTAR DETALHES DO PEDIDO           ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        String codPedido = obterCodigoPedido();

        if (lnFacade.ePendente(codPedido, idRestaurante) == false) {
            System.out.println("[Erro: O pedido " + codPedido + " não está pendente neste restaurante.]");
            pausar();
            return;
        }
        
        try {
            Pedido pedido = lnFacade.getPedidoPendente(codPedido, idRestaurante);
            System.out.println(pedido.toString());
        } catch (PedidoNaoExisteException e) {
            System.out.println("[Erro : Pedido não existe.]");
        }
        
        pausar();
    }

    /** 
     * Método que consulta os detalhes de um pedido finalizado
     * 
     */
    private void consultarDetalhesPedidoFinalizado() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║    CONSULTAR DETALHES DO PEDIDO           ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        String codPedido = obterCodigoPedido();
        
        try {
            Pedido pedido = lnFacade.getPedidoBD(codPedido);
            System.out.println(pedido.toString());
        } catch (PedidoNaoExisteException e) {
            System.out.println("[Erro : Pedido não existe.]");
        }
        
        pausar();
    }

    /** 
     * Método que apresenta o perfil do funcionário
     * 
     * @param idFuncionario ID do Funcionário
     */
    private void verPerfil(String idFuncionario) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║           PERFIL DO FUNCIONÁRIO           ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
            
        Funcionario perfil = lnFacade.funcionarioGet(idFuncionario);
        System.out.println(perfil.toString());
            
        pausar();
    }

    ///////////////////////////// Métodos auxiliares /////////////////////////// 
    
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
