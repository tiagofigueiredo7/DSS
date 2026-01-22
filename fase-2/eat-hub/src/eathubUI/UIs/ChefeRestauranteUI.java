package eathubUI.UIs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import eathubLN.IEatHubLN;
import eathubLN.ssCadeia.Estatistica;
import eathubLN.ssCadeia.Funcionario;
import eathubLN.ssCadeia.Historico;
import eathubLN.ssCadeia.MensagemGestor;
import eathubLN.ssCadeia.Pair;
import eathubUI.Menus.MenuOpcao;
import eathubUI.Menus.MenuUI;

/**
 * Interface do Chefe de Restaurante
 */
public class ChefeRestauranteUI {
    /** Instância do Facade da Lógica de Negócio */
    private IEatHubLN lnFacade;
    /**  Scanner para entrada do utilizador */
    private Scanner scanner;

    /** 
     * Construtor da Interface do Chefe de Restaurante
     * 
     * @param lnFacade Instância do Facade da Lógica de Negócio
     * @param scanner Scanner para entrada do utilizador
     */
    public ChefeRestauranteUI(IEatHubLN lnFacade, Scanner scanner) {
        this.lnFacade = lnFacade;
        this.scanner = scanner;
    }

    /** 
     * Método que inicia a interface do Chefe de Restaurante
     * 
     * @param idRestaurante ID do Restaurante do Chefe
     */
    public void run(String idRestaurante) {
        this.menuChefeRestaurante(idRestaurante);
    }

    /** 
     * Método que apresenta o menu do Chefe de Restaurante
     * 
     * @param idRestaurante ID do Restaurante do Chefe
     */
    private void menuChefeRestaurante(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║    INTERFACE DO CHEFE DE RESTAURANTE      ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        String idFuncionario = lnFacade.getChefeRestaurante(idRestaurante);

        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Ver Perfil", () -> this.verPerfil(idFuncionario)));
        opcoes.add(new MenuOpcao("Ver Estatísticas do Restaurante", () -> this.verEstatisticasRestaurante(idRestaurante)));
        opcoes.add(new MenuOpcao("Listar Funcionarios", () -> this.listarFuncionarios(idRestaurante)));
        opcoes.add(new MenuOpcao("Ver Stock", () -> this.verStockRestaurante(idRestaurante)));
        opcoes.add(new MenuOpcao("Registar Funcionário", () -> this.criarFuncionario(idRestaurante)));
        opcoes.add(new MenuOpcao("Ver mensagens do Gestor", () -> this.verMensagensGestor()));
        opcoes.add(new MenuOpcao("Listar Histórico de Pedidos do Restaurante", () -> this.listarHistoricoPedidosRestaurante(idRestaurante)));
        new MenuUI(opcoes).run();
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

    /** 
     * Método que apresenta o stock do restaurante
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void verStockRestaurante(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       STOCK DO RESTAURANTE                ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.println("Restaurante: " + idRestaurante + "\n");

        Set<Entry<Pair<String, String>, Integer>> stock = lnFacade.stockEntrySetRestaurante(idRestaurante);

        System.out.println("Lista de ingredientes em stock:\n");
        for(Entry<Pair<String, String>, Integer> e : stock) {
            System.out.println("  » " + e.getKey().getSecond() + " - Quantidade: " + e.getValue());
        }
        
        pausar();
    }

    /** 
     * Método que lista os funcionários do restaurante
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void listarFuncionarios(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       LISTA DE FUNCIONÁRIOS               ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.println("Restaurante: " + idRestaurante + "\n");

        Collection<String> funcionarios = lnFacade.funcionarioGetIdsFuncionariosRestaurante(idRestaurante);

        System.out.println("Funcionários registados:\n");
        for (String f : funcionarios) {
            Funcionario func = lnFacade.funcionarioGet(f);
            System.out.println(func.toString());
        }

        pausar();
    }

    /** 
     * Método que cria um novo funcionário para o restaurante
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void criarFuncionario(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   CRIAR FUNCIONÁRIO                       ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        if (nome.trim().isEmpty()) {
            System.out.println("[Erro: Nome inválido!]");
            pausar();
            return;
        }
        
        System.out.print("Posto: ");
        String posto = scanner.nextLine();

        if (posto.trim().isEmpty()) {
            System.out.println("[Erro: Posto inválido!]");
            pausar();
            return;
        }
        
        System.out.print("Tarefa: ");
        String tarefa = scanner.nextLine();

        if (tarefa.trim().isEmpty()) {
            System.out.println("[Erro: Tarefa inválida!]");
            pausar();
            return;
        }

        System.out.print("Tipo(ChefeRestaurante/Funcionario/Gestor): ");
        String tipo = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (password.trim().isEmpty()) {
            System.out.println("[Erro: Password inválida!]");
            pausar();
            return;
        }

        String idFunc = null;
        
        try {
            idFunc = lnFacade.criar_Funcionario(nome, posto, tarefa, idRestaurante, tipo, password);
        } catch (IllegalArgumentException e) {
            System.out.println("[Erro ao criar funcionário: " + e.getMessage() + "]");
            pausar();
            return;
        }
        System.out.println("[Funcionário do tipo " + tipo + " criado com ID: " + idFunc + " no restaurante " + idRestaurante + "]");
        
        pausar();
    }

    /** 
     * Método que apresenta as estatísticas do restaurante
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void verEstatisticasRestaurante(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║    ESTATÍSTICAS DO RESTAURANTE " + idRestaurante + "         ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("Até que período deseja obter as estatísticas?");
        System.out.print("\nEx: 2024-01-10 para obter desde 10 de janeiro de 2024 até agora, ou 'total' para todas as estatísticas): ");
        String periodo = scanner.nextLine();
        
        Estatistica stats = lnFacade.obter_estatisticas_restaurante(idRestaurante, periodo);
        
        if (stats != null) {
            System.out.println("\n" + stats.toString());
        } else {
            System.out.println("[Restaurante não encontrado.]");
        }
        
        pausar();
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
     * Método que lista o histórico de pedidos do restaurante
     * 
     * @param idRestaurante ID do Restaurante
     */
    private void listarHistoricoPedidosRestaurante(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║    HISTÓRICO DE PEDIDOS DO RESTAURANTE    ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Historico> historicos = lnFacade.getHistoricoPedidosRestaurante(idRestaurante);
        
        if (historicos.isEmpty()) {
            System.out.println("  Não há histórico de pedidos para o restaurante " + idRestaurante + ".");
        } else {
            System.out.println("Histórico de pedidos do restaurante " + idRestaurante + ":\n");
            for (Historico h : historicos) {
                System.out.println(h.toString());
            }
        }
        
        pausar();
    }
    
    /**
     * Pausa a execução até o utilizador pressionar ENTER
     */
    private void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
}
