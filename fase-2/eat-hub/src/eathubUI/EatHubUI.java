package eathubUI;

import eathubLN.EatHubLNFacade;
import eathubLN.IEatHubLN;
import eathubUI.Menus.MenuUI;
import eathubUI.UIs.ClienteUI;
import eathubUI.UIs.FuncionarioUI;
import eathubUI.UIs.ChefeRestauranteUI;
import eathubUI.UIs.GestorUI;
import eathubUI.UIs.GestaoBdUI;
import eathubUI.Menus.MenuOpcao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal da interface de utilizador do EatHub
 */
public class EatHubUI {
    /** Instância do Facade da Lógica de Negócio */
    private IEatHubLN lnFacade;
    /** Scanner para entrada do utilizador */
    private Scanner scanner;

    /** Instância da interface do Cliente */
    private ClienteUI clienteUI;
    /** Instância da interface do Funcionário */
    private FuncionarioUI funcionarioUI;
    /** Instância da interface do Chefe de Restaurante */
    private ChefeRestauranteUI chefeRestauranteUI;
    /** Instância da interface do Gestor */
    private GestorUI gestorUI;
    /** Instância da interface de Gestão de BD */
    private GestaoBdUI gestaoBdUI;
    
    /** 
     * Construtor da classe EatHubUI 
     */
    public EatHubUI() {
        this.lnFacade = new EatHubLNFacade();
        this.scanner = new Scanner(System.in);

        this.clienteUI = new ClienteUI(lnFacade, scanner);
        this.funcionarioUI = new FuncionarioUI(lnFacade, scanner);
        this.chefeRestauranteUI = new ChefeRestauranteUI(lnFacade, scanner);
        this.gestorUI = new GestorUI(lnFacade, scanner);
        this.gestaoBdUI = new GestaoBdUI(lnFacade, scanner);
    }

    /** 
     * Método que inicia a interface principal de utilizador do EatHub
     */
    public void run() {
        mensagemInicial();
        MenuPrincipal().run();
        mensagemFinal();
    }

    // ==================== MENUS ====================

    /** 
     * Método que cria o menu principal da aplicação
     * 
     * @return MenuUI Menu principal
     */
    private MenuUI MenuPrincipal() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Entrar como Cliente", () -> this.clienteUI.run()));
        opcoes.add(new MenuOpcao("Entrar como Funcionário", () -> this.menuAutenticacaoFuncionario()));
        opcoes.add(new MenuOpcao("Entrar como Chefe de Restaurante", () -> this.menuAutenticacaoChefeRestaurante()));
        opcoes.add(new MenuOpcao("Entrar como Gestor", () -> this.menuAutenticacaoGestor()));
        opcoes.add(new MenuOpcao("Aceder ao modo de Gestão da Base de Dados", () -> this.menuAutenticacaoGestaoBD()));
        return new MenuUI(opcoes);
    }

    // Extras
    /** 
     * Método que gere o menu de autenticação do Funcionário
     */
    private void menuAutenticacaoFuncionario() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║    AUTENTICAÇÃO DE FUNCIONÁRIO            ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Id: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        boolean autenticado = lnFacade.autenticarFuncionario(username, password);
        if (autenticado) {
            System.out.println("\nAutenticação bem-sucedida! Acedendo ao menu de funcionário...");
            pausar();
            this.funcionarioUI.run(username);
        } else {
            System.out.println("\nErro: Credenciais inválidas. Acesso negado.");
            pausar();
        }
    }

    /** 
     * Método que gere o menu de autenticação do Chefe de Restaurante
     */
    private void menuAutenticacaoChefeRestaurante() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   AUTENTICAÇÃO DE CHEFE DE RESTAURANTE    ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Id: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        boolean autenticado = lnFacade.autenticarChefeRestaurante(username, password);
        if (autenticado) {
            System.out.println("\nAutenticação bem-sucedida! Acedendo ao menu de chefe de restaurante...");

            pausar();
            this.chefeRestauranteUI.run(lnFacade.getRestauranteDeFuncionario(username));
        } else {
            System.out.println("\nErro: Credenciais inválidas. Acesso negado.");
            pausar();
        }
    }

    /** 
     * Método que gere o menu de autenticação do Gestor
     */
    private void menuAutenticacaoGestor() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║       AUTENTICAÇÃO DE GESTOR              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Id: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        boolean autenticado = lnFacade.autenticarGestor(username, password);
        if (autenticado) {
            System.out.println("\nAutenticação bem-sucedida! Acedendo ao menu de gestor...");
            pausar();
            this.gestorUI.run();
        } else {
            System.out.println("\nErro: Credenciais inválidas. Acesso negado.");
            pausar();
        }
    }

    /** 
     * Método que gere o menu de autenticação do modo de Gestão de BD
     */
    private void menuAutenticacaoGestaoBD() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║    AUTENTICAÇÃO DO MODO GESTÃO BD         ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Id: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        boolean autenticado;

        if (username.equals("admin") && password.equals("admin")) {
            autenticado = true;
        } else {
            autenticado = false;
        }
        if (autenticado) {
            System.out.println("\nAutenticação bem-sucedida! Acedendo ao menu de gestão BD...");
            pausar();
            this.gestaoBdUI.run();
        } else {
            System.out.println("\nErro: Credenciais inválidas. Acesso negado.");
            pausar();
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Limpa o ecrã
     */
    private void limparEcra() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Pausa a execução até o utilizador pressionar ENTER
     */
    private void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Exibe mensagem de boas-vindas
     */
    private void mensagemInicial() {
        limparEcra();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║          Bem-vindo ao EatHub!          ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        pausar();
    }

    /**
     * Exibe mensagem de despedida
     */
    private void mensagemFinal() {
        limparEcra();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║     Obrigado por comer no EatHub!!     ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
}
