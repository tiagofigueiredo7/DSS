package eathubUI.UIs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import eathubLN.IEatHubLN;
import eathubLN.ssCadeia.Estatistica;
import eathubLN.ssCadeia.Funcionario;
import eathubLN.ssCadeia.Historico;
import eathubLN.ssCadeia.Restaurante;
import eathubLN.ssPedidos.Ementa;
import eathubLN.ssPedidos.Menu;
import eathubLN.ssPedidos.Proposta;
import eathubUI.Menus.MenuOpcao;
import eathubUI.Menus.MenuUI;

/**
 * Interface do GestorUI
 */
public class GestorUI {
    /** Instância do Facade da Lógica de Negócio */
    private IEatHubLN lnFacade;
    /** Scanner para entrada do utilizador */
    private Scanner scanner;

    /**
     * Construtor da classe GestorUI
     * 
     * @param lnFacade Instância do Facade da Lógica de Negócio
     * @param scanner  Scanner para entrada do utilizador
     */
    public GestorUI(IEatHubLN lnFacade, Scanner scanner) {
        this.lnFacade = lnFacade;
        this.scanner = scanner;
    }

    /** 
     * Método principal para executar a interface do Gestor
     */
    public void run() {
        this.menuGestor();
    }

    /** 
     * Método que cria o menu do Gestor
     */
    private void menuGestor() {
        String idGestor = lnFacade.getGestor().getIDFunc();
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║        INTERFACE DO GESTOR                ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Ver Perfil", () -> this.verPerfil(idGestor)));
        opcoes.add(new MenuOpcao("Ver Estatísticas de um Restaurante", () -> {
            System.out.print("ID do Restaurante: ");
            String idRestaurante = scanner.nextLine().trim();
            if (!lnFacade.obter_IDs_restaurantes().contains(idRestaurante)) {
                System.out.println("[Erro: Restaurante não existe!]");
                pausar();
                return;
            }
            this.verEstatisticasRestaurante(idRestaurante);
        }));
        opcoes.add(new MenuOpcao("Ver Estatísticas da Cadeia", () -> this.verEstatisticasCadeia()));
        opcoes.add(new MenuOpcao("Listar Restaurantes", () -> this.listarRestaurantes()));
        opcoes.add(new MenuOpcao("Registar Funcionário", () -> {
            
            System.out.print("ID do Restaurante do qual o funcionário fará parte: ");
            String idRestaurante = scanner.nextLine().trim();
            
            this.criarFuncionario(idRestaurante);
        }));
        opcoes.add(new MenuOpcao("Gerir Ementa", () -> this.gerirEmenta()));
        opcoes.add(new MenuOpcao("Enviar mensagem aos Funcionários", () -> this.enviarMensagemFuncionarios(idGestor)));
        opcoes.add(new MenuOpcao("Listar Histórico de Pedidos da Cadeia", () -> this.listarHistoricoPedidosCadeia()));
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
     * Método que envia uma mensagem a todos os funcionários
     * 
     * @param idGestor ID do Gestor
     */
    private void enviarMensagemFuncionarios(String idGestor) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   ENVIAR MENSAGEM AOS FUNCIONÁRIOS       ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        System.out.print("Mensagem a enviar: ");
        String mensagem = scanner.nextLine().trim();
        
        lnFacade.enviar_mensagem_gestor(mensagem, idGestor);
        System.out.println("[Mensagem enviada a todos os funcionários.]");
        
        pausar();
    }

    /** 
     * Método que lista todos os restaurantes da cadeia
     */
    private void listarRestaurantes() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE RESTAURANTES                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Restaurante> restaurantes = lnFacade.restauranteValues();
        
        if (restaurantes.isEmpty()) {
            System.out.println("Não há restaurantes cadastrados.");
        } else {
            for (Restaurante r : restaurantes) {
                System.out.println(r.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para gerir a ementa da cadeia
     */
    private void gerirEmenta() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║            GERIR EMENTA                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        while(true) {
            System.out.println("O que deseja fazer?");
            System.out.println("1. Adicionar novo item à ementa");
            System.out.println("2. Remover item da ementa");
            System.out.println("3. Ver ementa atual");
            System.out.println("4. Voltar");
            System.out.print("Escolha uma opção (1-4): ");
            String escolha = scanner.nextLine().trim();

            switch (escolha) {
                case "1":
                    System.out.println("Que tipo de item deseja criar e adicionar à ementa?");
                    System.out.println("1. Proposta");
                    System.out.println("2. Menu");
                    System.out.print("Escolha uma opção (1-2): ");
                    String tipoCriacao = scanner.nextLine().trim();

                    if (tipoCriacao.equals("1")) {
                        this.criarProposta();
                    } else if (tipoCriacao.equals("2")) {
                        this.criarMenu();
                    } else {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                    pausar();
                    break;
                case "2":
                    System.out.println("Que tipo de item deseja remover?");
                    System.out.println("1. Proposta");
                    System.out.println("2. Menu");
                    System.out.print("Escolha uma opção (1-2): ");
                    String tipoRemocao = scanner.nextLine().trim();

                    if (tipoRemocao.equals("1")) {
                        this.removerProposta();
                    } else if (tipoRemocao.equals("2")) {
                        this.removerMenu();
                    } else {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                    pausar();
                    break;
                case "3":
                    this.verEmentaDisponivel();
                    break;
                case "4":
                    return; // Voltar ao menu anterior
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    pausar();
            }
        }
    }

    /** 
     * Método que cria uma nova proposta
     */
    private void criarProposta() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   CRIAR PROPOSTA                          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        System.out.print("Nome da proposta: ");
        String nome = scanner.nextLine();
        
        System.out.print("Preço: ");
        double preco;
        try {
            preco = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[Erro: Preço inválido!]");
            pausar();
            return;
        }
        
        System.out.print("Códigos dos ingredientes (separados por vírgula): ");
        String ingredientesStr = scanner.nextLine();
        Collection<String> codIngredientes = Arrays.asList(ingredientesStr.split(","));

        System.out.print("Etapas (separadas por vírgula): ");
        String etapasStr = scanner.nextLine();
        List<String> etapas = Arrays.asList(etapasStr.split(","));
        
        String idProposta = lnFacade.criar_Proposta(nome, codIngredientes, preco, etapas);
        System.out.println("[Proposta criada com ID: " + idProposta + "]");
        
        pausar();
    }

    /** 
     * Método que cria um novo menu (conjunto de propostas)
     */
    private void criarMenu() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   CRIAR MENU                              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        System.out.print("Nome do menu: ");
        String nome = scanner.nextLine();
        
        System.out.print("Preço: ");
        double preco;
        try {
            preco = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[Erro: Preço inválido!]");
            pausar();
            return;
        }
        
        System.out.print("Códigos das propostas (separados por vírgula): ");
        String propostasStr = scanner.nextLine();
        Collection<String> codPropostas = Arrays.asList(propostasStr.split(","));
        
        String idMenu = lnFacade.criar_Menu(nome, codPropostas, preco);
        System.out.println("[Menu criado com ID: " + idMenu + "]");
        
        pausar();
    }

    /** 
     * Método que remove um menu da ementa
     */
    private void removerMenu() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER MENU                            ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Código do menu a remover: ");
        String codMenu = scanner.nextLine().trim();
        
        if (codMenu.isEmpty()) {
            System.out.println("[Erro: Código inválido!]");
            pausar();
            return;
        }
        
        Menu m = lnFacade.menuRemove(codMenu);
        if (m!=null) {
            System.out.println("[Menu " + codMenu + " removido com sucesso!]");
        } else {
            System.out.println("[Menu " + codMenu + " não encontrado!]");
        }
        pausar();
    }

    /** 
     * Método que remove uma proposta da ementa
     */
    private void removerProposta() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER PROPOSTA                        ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Código da proposta a remover: ");
        String codProposta = scanner.nextLine().trim();
        
        if (codProposta.isEmpty()) {
            System.out.println("[Erro: Código inválido!]");
            pausar();
            return;
        }
        
        Proposta p = lnFacade.propostaRemove(codProposta);
        if (p!=null) {
            System.out.println("[Proposta " + codProposta + " removida com sucesso!]");
        } else {
            System.out.println("[Proposta " + codProposta + " não encontrada!]");
        }
        pausar();
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
            System.out.println("Não há ementa disponível no momento.");
        }
        
        pausar();
    }
    /** 
     * Método que apresenta as estatísticas da cadeia
     */
    private void verEstatisticasCadeia() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       ESTATÍSTICAS DA CADEIA              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        System.out.print("Até que período deseja obter as estatísticas?");
        System.out.print("\nEx: 2024-01-10 para obter desde 10 de janeiro de 2024 até agora, ou 'total' para todas as estatísticas): ");
        String periodo = scanner.nextLine();

        Estatistica stats = null;

        try {
            stats = lnFacade.obter_estatisticas_cadeia(periodo);
        } catch (IllegalArgumentException e) {
            System.out.println("[Erro ao obter estatísticas: " + e.getMessage() + "]");
            pausar();
            return;
        }
        
        if (stats != null) {
            System.out.println("\n" + stats.toString());
        } else {
            System.out.println("[Restaurante não encontrado.]");
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
     * Método que apresenta as estatísticas de um restaurante
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
     * Método que lista o histórico de pedidos da cadeia
     */
    private void listarHistoricoPedidosCadeia() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║     HISTÓRICO DE PEDIDOS DA CADEIA        ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Historico> historicos = lnFacade.getHistoricoPedidosCadeia();
        
        if (historicos.isEmpty()) {
            System.out.println("  Não há histórico de pedidos.");
        } else {
            System.out.println("Histórico de pedidos da cadeia:\n");
            for (Historico h : historicos) {
                System.out.println(h.toString());
                System.out.println("\n");
            }
        }
        
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

    
}
