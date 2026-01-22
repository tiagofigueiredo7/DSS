package eathubUI.UIs;

import eathubLN.IEatHubLN;
import eathubLN.ssCadeia.Funcionario;
import eathubLN.ssCadeia.Historico;
import eathubLN.ssCadeia.MensagemGestor;
import eathubLN.ssCadeia.Pair;
import eathubLN.ssCadeia.Restaurante;
import eathubLN.ssComercial.Fatura;
import eathubLN.ssComercial.Pagamento;
import eathubLN.ssComercial.Talao;
import eathubLN.ssPedidos.Ingrediente;
import eathubLN.ssPedidos.Menu;
import eathubLN.ssPedidos.Pedido;
import eathubLN.ssPedidos.Proposta;
import eathubUI.Menus.MenuOpcao;
import eathubUI.Menus.MenuUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 * Interface para a Gestão da BD
 */
public class GestaoBdUI {
    /** Instância do Facade da Lógica de Negócio */
    private IEatHubLN lnFacade;
    /** Scanner para entrada do utilizador */
    private Scanner scanner;

    /**
     * Construtor da classe GestaoBdUI
     * 
     * @param lnFacade Instância do Facade da Lógica de Negócio
     * @param scanner  Scanner para entrada do utilizador
     */
    public GestaoBdUI(IEatHubLN lnFacade, Scanner scanner) {     
        this.lnFacade = lnFacade;
        this.scanner = scanner;
    }

    /** 
     * Método principal para executar a interface de Gestão da BD
     */
    public void run() {
        this.menuModoCriativo();
    }

    /** 
     * Método que cria o menu de Gestão da BD
     */
    private void menuModoCriativo() { 
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Gerir Propostas", () -> this.gerirPropostas()));
        opcoes.add(new MenuOpcao("Gerir Menus", () -> this.gerirMenus()));
        opcoes.add(new MenuOpcao("Gerir Ingredientes", () -> this.gerirIngredientes()));
        opcoes.add(new MenuOpcao("Gerir Pedidos", () -> this.gerirPedidos()));
        opcoes.add(new MenuOpcao("Gerir Faturas", () -> this.gerirFaturas()));
        opcoes.add(new MenuOpcao("Gerir Talões", () -> this.gerirTaloes()));
        opcoes.add(new MenuOpcao("Gerir Pagamentos", () -> this.gerirPagamentos()));
        opcoes.add(new MenuOpcao("Gerir Funcionários", () -> this.gerirFuncionarios()));
        opcoes.add(new MenuOpcao("Gerir Restaurantes", () -> this.gerirRestaurantes()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir ingredientes na BD
     */
    private void gerirIngredientes() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Criar Ingrediente", () -> this.criarIngrediente()));
        opcoes.add(new MenuOpcao("Remover Ingrediente", () -> this.removerIngrediente()));
        opcoes.add(new MenuOpcao("Listar Ingredientes", () -> this.listarIngredientes()));
        opcoes.add(new MenuOpcao("Adicionar Ingrediente a Stock de Restaurante", () -> this.adicionarStockIngrediente()));
        opcoes.add(new MenuOpcao("Remover Ingrediente do Stock de Restaurante", () -> this.removerStockIngrediente()));
        opcoes.add(new MenuOpcao("Listar Stock de Ingredientes de Restaurante", () -> this.listarStockIngredientesRestaurante()));
        opcoes.add(new MenuOpcao("Gerir Alergénios", () -> this.gerirAlergenios()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir alergénios na BD
     */
    private void gerirAlergenios() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Criar Alergénio", () -> this.criarAlergenio()));
        opcoes.add(new MenuOpcao("Remover Alergénio", () -> this.removerAlergenio()));
        opcoes.add(new MenuOpcao("Listar Alergénios existentes", () -> this.listarAlergenios()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir propostas na BD
     */
    private void gerirPropostas() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Criar Proposta", () -> this.criarProposta()));
        opcoes.add(new MenuOpcao("Remover Proposta", () -> this.removerProposta()));
        opcoes.add(new MenuOpcao("Listar Propostas", () -> this.listarPropostas()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir menus na BD
     */
    private void gerirMenus() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Criar Menu", () -> this.criarMenu()));
        opcoes.add(new MenuOpcao("Remover Menu", () -> this.removerMenu()));
        opcoes.add(new MenuOpcao("Listar Menus", () -> this.listarMenus()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir pedidos na BD
     */
    private void gerirPedidos() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Remover Pedido", () -> this.removerPedidoBD()));
        opcoes.add(new MenuOpcao("Listar Pedidos", () -> this.listarPedidosBD()));
        opcoes.add(new MenuOpcao("Gerir Histórico de Pedidos", () -> this.gerirHistoricoPedidos()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir  histórico de pedidos na BD
     */
    private void gerirHistoricoPedidos() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Listar Histórico de Pedidos da Cadeia", () -> this.listarHistoricoPedidosCadeia()));
        opcoes.add(new MenuOpcao("Listar Histórico de Pedidos de um Restaurante", () -> {
            System.out.print("ID do Restaurante: ");
            String idRestaurante = scanner.nextLine().trim();
            this.listarHistoricoPedidosRestaurante(idRestaurante);}));
        opcoes.add(new MenuOpcao("Remover Histórico de Pedidos", () -> this.removerHistoricoPedidos()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir faturas na BD
     */
    private void gerirFaturas() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Remover Fatura", () -> this.removerFatura()));
        opcoes.add(new MenuOpcao("Listar Faturas", () -> this.listarFaturas()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir talões na BD
     */
    private void gerirTaloes() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Remover Talão", () -> this.removerTalao()));
        opcoes.add(new MenuOpcao("Listar Talões", () -> this.listarTaloes()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir pagamentos na BD
     */
    private void gerirPagamentos() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Remover Pagamento", () -> this.removerPagamento()));
        opcoes.add(new MenuOpcao("Listar Pagamentos", () -> this.listarPagamentos()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir funcionários na BD
     */
    private void gerirFuncionarios() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Criar Funcionário", () -> {
            System.out.print("ID do Restaurante do qual o funcionário fará parte(Pressione Enter para nenhum): ");
            String idRestaurante = scanner.nextLine();
            if (idRestaurante.trim().isEmpty()) {
                idRestaurante = null;
            }
            this.criarFuncionario(idRestaurante);
        }));
        opcoes.add(new MenuOpcao("Remover Funcionário", () -> this.removerFuncionario()));
        opcoes.add(new MenuOpcao("Listar Funcionários", () -> this.listarFuncionariosCompleto()));
        opcoes.add(new MenuOpcao("Gerir Mensagens enviadas pelo Gestor", () -> this.gerirMensagensGestor()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir mensagens de Gestor na BD
     */
    private void gerirMensagensGestor() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Remover Mensagem do Gestor", () -> this.removerMensagemGestor()));
        opcoes.add(new MenuOpcao("Listar Mensagens do Gestor", () -> this.verMensagensGestor()));
        new MenuUI(opcoes).run();
    }

    /** 
     * Método para gerir restaurantes na BD
     */
    private void gerirRestaurantes() {
        List<MenuOpcao> opcoes = new ArrayList<>();
        opcoes.add(new MenuOpcao("Criar Restaurante", () -> this.criarRestaurante()));
        opcoes.add(new MenuOpcao("Remover Restaurante", () -> this.removerRestaurante()));
        opcoes.add(new MenuOpcao("Listar Restaurantes", () -> this.listarRestaurantesCompleto()));
        new MenuUI(opcoes).run();
    }

    // ==================== MÉTODOS (CRIAR/REMOVER/LISTAR) ====================

    /** 
     * Método para remover mensagem de Gestor da BD
     */
    private void removerMensagemGestor() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER MENSAGEM DO GESTOR              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("ID da mensagem a remover: ");
        String idMensagem = scanner.nextLine().trim();
        int id = Integer.parseInt(idMensagem);

        if (idMensagem.isEmpty()) {
            System.out.println("[Erro: ID inválido!]");
            pausar();
            return;
        }

        MensagemGestor msg = lnFacade.remover_mensagem_gestor(id);
        if (msg != null) {
            System.out.println("[Mensagem com ID " + idMensagem + " removida com sucesso!]");
        } else {
            System.out.println("[Mensagem com ID " + idMensagem + " não encontrada!]");
        }
        pausar();
    }

    /** 
     * Método para adicionar alergénio à BD
     */
    private void criarAlergenio() {

        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   CRIAR ALERGÉNIO                         ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("Nome do alergénio: ");
        String nome = scanner.nextLine();

        lnFacade.propostaAddAlergenio(nome);
        System.out.println("[Alergénio " + nome + " criado com sucesso!]");

    }

    /** 
     * Método para remover alergénio da BD
     */
    private void removerAlergenio() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER ALERGÉNIO                       ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Nome do alergénio a remover: ");
        String nomeAlergenio = scanner.nextLine().trim();
        
        if (nomeAlergenio.isEmpty()) {
            System.out.println("[Erro: Nome inválido!]");
            pausar();
            return;
        }
        
        String res = lnFacade.propostaRemoveAlergenio(nomeAlergenio);
        if (res != null) {
            System.out.println("[Alergénio " + nomeAlergenio + " removido com sucesso!]");
        } else {
            System.out.println("[Alergénio " + nomeAlergenio + " não encontrado!]");
        }
        pausar();
    }

    /** 
     * Método para listar alergénios na BD
     */
    private void listarAlergenios() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE ALERGÉNIOS                     ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<String> alergenios = lnFacade.propostaGetAlergenios();
        
        if (alergenios.isEmpty()) {
            System.out.println("Não há alergénios cadastrados.");
        } else {
            System.out.println("Alergénios disponíveis:\n");
            for (String a : alergenios) {
                System.out.println("┌─────────────────────────────────────────┐");
                System.out.println("│ " + String.format("%-41s", a));
                System.out.println("└─────────────────────────────────────────┘");
            }
        }
        
        pausar();
    }

    /** 
     * Método para adicionar ingrediente à BD
     */
    private void criarIngrediente() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   CRIAR INGREDIENTE                       ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("Nome do ingrediente: ");
        String nome = scanner.nextLine();

        System.out.print("Preço do ingrediente (€): ");
        double preco = 0.0;
        try {
            preco = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[Erro: Preço inválido!]");
            pausar();
            return;
        }

        System.out.print("Possui alergénios? (separados por vírgula, ou deixe vazio): ");
        String alergicos = scanner.nextLine().trim();
        if (!alergicos.isEmpty()) {
            List<String> alergicosList = Arrays.asList(alergicos.split(","));
            lnFacade.propostaAddIngredienteEAlergenios(nome, alergicosList, preco);
        } else {
            lnFacade.propostaAddIngrediente(nome, preco);
        }
        System.out.println("[Ingrediente " + nome + " criado com sucesso!]");

    }

    /** 
     * Método para remover ingrediente da BD
     */
    private void removerIngrediente() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER INGREDIENTE                     ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Nome do ingrediente a remover: ");
        String codIngrediente = scanner.nextLine().trim();
        
        if (codIngrediente.isEmpty()) {
            System.out.println("[Erro: Nome inválido!]");
            pausar();
            return;
        }
        
        Ingrediente i = lnFacade.propostaRemoveIngrediente(codIngrediente);
        if (i!=null) {
            System.out.println("[Ingrediente " + codIngrediente + " removido com sucesso!]");
        } else {
            System.out.println("[Ingrediente " + codIngrediente + " não encontrado!]");
        }
        pausar();
    }

    /** 
     * Método para listar ingredientes na BD
     */
    private void listarIngredientes() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE INGREDIENTES                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Ingrediente> ingredientes = lnFacade.propostaGetIngredientes();
        
        if (ingredientes.isEmpty()) {
            System.out.println("[Não há ingredientes cadastrados.]");
        } else {
            for (Ingrediente i : ingredientes) {
                System.out.println(i.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para adicionar ingrediente ao stock de um restaurante na BD
     */
    public void adicionarStockIngrediente() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   ADICIONAR INGREDIENTE AO STOCK          ║");
        System.out.println("║   DO RESTAURANTE                          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("ID do Restaurante: ");
        String idRestaurante = scanner.nextLine().trim();

        System.out.print("Código do Ingrediente: ");
        String codIngrediente = scanner.nextLine().trim();

        if (!lnFacade.ingredienteExiste(codIngrediente)) {
            System.out.println("[Erro: Ingrediente não existe!]");
            pausar();
            return;
        }

        System.out.print("Quantidade a adicionar: ");
        int quantidade;
        try {
            quantidade = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Erro: Quantidade inválida!]");
            pausar();
            return;
        }

        boolean sucesso = lnFacade.adicionar_ingrediente_stock_restaurante(idRestaurante, codIngrediente, quantidade);
        if (sucesso) {
            System.out.println("[Ingrediente " + codIngrediente + " adicionado ao stock do restaurante " + idRestaurante + " com sucesso!]");
        } else {
            System.out.println("[Erro ao adicionar ingrediente. Verifique os dados fornecidos.]");
        }

        pausar();
    }

    /** 
     * Método para remover ingrediente do stock de um restaurante na BD
     */
    private void removerStockIngrediente() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER INGREDIENTE DO STOCK            ║");
        System.out.println("║   DO RESTAURANTE                          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("ID do Restaurante: ");
        String idRestaurante = scanner.nextLine().trim();

        System.out.print("Código do Ingrediente: ");
        String codIngrediente = scanner.nextLine().trim();

        if (!lnFacade.ingredienteExiste(codIngrediente)) {
            System.out.println("[Erro: Ingrediente não existe!]");
            pausar();
            return;
        }

        System.out.print("Quantidade a remover: ");
        int quantidade;

        try {
            quantidade = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Erro: Quantidade inválida!]");
            pausar();
            return;
        }

        boolean sucesso = lnFacade.remover_ingrediente_stock_restaurante(idRestaurante, codIngrediente, quantidade);
        if (sucesso) {
            System.out.println("[Ingrediente " + codIngrediente + " removido do stock do restaurante " + idRestaurante + " com sucesso!]");
        } else {
            System.out.println("[Erro ao remover ingrediente. Verifique os dados fornecidos.]");
        }

        pausar();
    }

    /** 
     * Método para listar stock de ingredientes num restaurante
     */
    private void listarStockIngredientesRestaurante() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE STOCK DE INGREDIENTES          ║");
        System.out.println("║   DO RESTAURANTE                          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.print("ID do Restaurante: ");
        String idRestaurante = scanner.nextLine().trim();

        Set<Entry<Pair<String, String>, Integer>> stock = lnFacade.stockEntrySetRestaurante(idRestaurante);

        if (stock.isEmpty()) {
            System.out.println("[O restaurante " + idRestaurante + " não possui ingredientes em stock ou não existe.]");
        } else {
            System.out.println("\nStock de ingredientes do restaurante " + idRestaurante + ":\n");
        }

        for (Entry<Pair<String, String>, Integer> entry : stock) {
            Pair<String, String> key = entry.getKey();
            Integer quantidade = entry.getValue();
            System.out.println("Restaurante ID: " + key.getFirst() + 
                               " | Ingrediente Código: " + key.getSecond() + 
                               " | Quantidade: " + quantidade);
        }

        pausar();
    }

    /** 
     * Método para adicionar proposta à BD
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
     * Método para adicionar menu à BD
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
     * Método para remover proposta da BD
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
     * Método para listar propostas na BD
     */
    private void listarPropostas() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE PROPOSTAS DISPONÍVEIS          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Proposta> propostas = lnFacade.propostaValues();
        
        if (propostas.isEmpty()) {
            System.out.println("[Não há propostas cadastradas.]");
        } else {
            for (Proposta p : propostas) {
                System.out.println(p.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para remover menu da BD
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
     * Método para listar menus na BD
     */
    private void listarMenus() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE MENUS DISPONÍVEIS              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Menu> menus = lnFacade.menuValues();
        
        if (menus.isEmpty()) {
            System.out.println("[Não há menus cadastrados.]");
        } else {
            for (Menu m : menus) {
                System.out.println(m.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para remover pedido da BD
     */
    private void removerPedidoBD() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER PEDIDO                          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Código do pedido a remover: ");
        String codPedido = scanner.nextLine().trim();
        
        if (codPedido.isEmpty()) {
            System.out.println("[Erro: Código inválido!]");
            pausar();
            return;
        }
        
        Pedido p = lnFacade.pedidoRemove(codPedido);
        if (p != null) {
            System.out.println("[Pedido " + codPedido + " removido com sucesso!]");
        } else {
            System.out.println("[Pedido " + codPedido + " não encontrado!]");
        }
        
        pausar();
    }

    /** 
     * Método para listar pedidos na BD
     */
    private void listarPedidosBD() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE PEDIDOS                        ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        // Pedidos registados
        Collection<Pedido> registados = lnFacade.pedidoValues();
        System.out.println("Pedidos Registados (" + registados.size() + "):");
        if (registados.isEmpty()) {
            System.out.println("  (nenhum)");
        } else {
            for (Pedido p : registados) {
                System.out.println(p.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para remover fatura da BD
     */
    private void removerFatura() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER FATURA                          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Código da fatura a remover: ");
        String codFatura = scanner.nextLine().trim();
        
        if (codFatura.isEmpty()) {
            System.out.println("[Erro: Código inválido!]");
            pausar();
            return;
        }
        
        Fatura f = lnFacade.faturaRemove(codFatura);
        if (f != null) {
            System.out.println("[Fatura " + codFatura + " removida com sucesso!]");
        } else {
            System.out.println("[Fatura " + codFatura + " não encontrada!]");
        }
        pausar();
    }

    /** 
     * Método para listar faturas na BD
     */
    private void listarFaturas() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE FATURAS                        ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Fatura> faturas = lnFacade.faturaValues();
        
        if (faturas.isEmpty()) {
            System.out.println("[Não há faturas cadastradas.]");
        } else {
            System.out.println("Faturas registadas:");
            for (Fatura f : faturas) {
                System.out.println(f.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para remover talão da BD
     */
    private void removerTalao() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER TALÃO                           ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Código do talão a remover: ");
        String codTalao = scanner.nextLine().trim();
        
        if (codTalao.isEmpty()) {
            System.out.println("[Erro: Código inválido!]");
            pausar();
            return;
        }
        
        lnFacade.talaoRemove(codTalao);
        System.out.println("[Talão " + codTalao + " removido com sucesso!]");
        pausar();
    }

    /** 
     * Método para listar talões na BD
     */
    private void listarTaloes() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE TALÕES                         ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Talao> taloes = lnFacade.talaoValues();
        
        if (taloes.isEmpty()) {
            System.out.println("[Não há talões cadastrados.]");
        } else {
            System.out.println("Talões registados:");
            for (Talao t : taloes) {
                System.out.println(t.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para remover pagamento da BD
     */
    private void removerPagamento() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER PAGAMENTO                       ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("Código do pagamento a remover: ");
        String codPagamento = scanner.nextLine().trim();
        
        if (codPagamento.isEmpty()) {
            System.out.println("[Erro: Código inválido!]");
            pausar();
            return;
        }
        
        Pagamento p = lnFacade.pagamentoRemove(codPagamento);
        if (p != null) {
            System.out.println("[Pagamento " + codPagamento + " removido com sucesso!]");
        } else {
            System.out.println("[Pagamento " + codPagamento + " não encontrado!]");
        }
        pausar();
    }

    /** 
     * Método para listar pagamentos na BD
     */
    private void listarPagamentos() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE PAGAMENTOS                     ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Pagamento> pagamentos = lnFacade.pagamentoValues();
        
        if (pagamentos.isEmpty()) {
            System.out.println("[Não há pagamentos cadastrados.]");
        } else {
            System.out.println("Pagamentos registados:");
            for (Pagamento p : pagamentos) {
                System.out.println(p.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para remover funcionário da BD
     */
    private void removerFuncionario() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER FUNCIONÁRIO                     ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("ID do funcionário a remover: ");
        String idFunc = scanner.nextLine().trim();
        
        if (idFunc.isEmpty()) {
            System.out.println("[Erro: ID inválido!]");
            pausar();
            return;
        }
        
        Funcionario f = lnFacade.funcionarioRemove(idFunc);
        if (f != null) {
            System.out.println("[Funcionário " + idFunc + " removido com sucesso!]");
        } else {
            System.out.println("[Funcionário " + idFunc + " não encontrado!]");
        }
        pausar();
    }

    /** 
     * Método para listar funcionários na BD
     */
    private void listarFuncionariosCompleto() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE FUNCIONÁRIOS                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Funcionario> funcionarios = lnFacade.funcionarioValues();
        
        if (funcionarios.isEmpty()) {
            System.out.println("[Não há funcionários cadastrados.]");
        } else {
            for (Funcionario f : funcionarios) {
                System.out.println(f.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para adicionar restaurante à BD
     */
    private void criarRestaurante() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   CRIAR RESTAURANTE DE TESTE              ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        System.out.print("Nome do restaurante: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Chefe de Restaurante(Id do Funcionário): ");
        String chefe = scanner.nextLine().trim();

        if (!lnFacade.eFuncionario(chefe)) {
            System.out.println("[Erro: Chefe de Restaurante não encontrado!]");
            pausar();
            return;
        } else if (lnFacade.eChefeRestaurante(chefe)){
            System.out.println("[Erro: O funcionário já é chefe de restaurante!]");
            pausar();
            return;
        } else if (lnFacade.eGestor(chefe)){
            System.out.println("[Erro: O funcionário é um gestor e não pode ser chefe de restaurante!]");
            pausar();
            return;
        }

        System.out.print("Adicionar Funcionários a Restaurante(S/N): ");
        String resposta = scanner.nextLine().trim();

        List<String> lista = new ArrayList<>();
        if (resposta.equalsIgnoreCase("S")) {
            while (true) {
                System.out.print("ID do Funcionário (ou 'sair' para terminar): ");
                String idFunc = scanner.nextLine().trim();
                if (idFunc.equalsIgnoreCase("sair")) {
                    break;
                }
                if (lnFacade.eFuncionario(idFunc) && !lnFacade.eChefeRestaurante(idFunc) && !lnFacade.eGestor(idFunc)) lista.add(idFunc);
                else if (!lnFacade.eFuncionario(idFunc)) System.out.println("[Funcionário não encontrado!]");
                else System.out.println("[Funcionário já é chefe de restaurante ou gestor!]");
            }
        }

        String idRest = lnFacade.criar_Restaurante(nome, chefe, lista);
        System.out.println("[Restaurante criado com ID: " + idRest + "]");
        pausar();
    }

    /** 
     * Método para remover restaurante da BD
     */
    private void removerRestaurante() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER RESTAURANTE                     ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.print("ID do restaurante a remover: ");
        String idRest = scanner.nextLine().trim();
        
        if (idRest.isEmpty()) {
            System.out.println("[Erro: ID inválido!]");
            pausar();
            return;
        }
        
        Restaurante r = lnFacade.restauranteRemove(idRest);
        if (r != null) {
            System.out.println("[Restaurante " + idRest + " removido com sucesso!]");
        } else {
            System.out.println("[Restaurante " + idRest + " não encontrado!]");
        }
        pausar();
    }

    /** 
     * Método para listar restaurantes na BD
     */
    private void listarRestaurantesCompleto() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE RESTAURANTES                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Restaurante> restaurantes = lnFacade.restauranteValues();
        
        if (restaurantes.isEmpty()) {
            System.out.println("[Não há restaurantes cadastrados.]");
        } else {
            for (Restaurante r : restaurantes) {
                System.out.println(r.toString());
            }
        }
        
        pausar();
    }

    /** 
     * Método para ver mensagens de Gestor da BD
     */
    private void verMensagensGestor() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   MENSAGENS DO GESTOR                     ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<MensagemGestor> mensagens = lnFacade.obter_mensagens_Gestor();
        
        if (mensagens.isEmpty()) {
            System.out.println("[Não há mensagens do gestor.]");
        } else {
            System.out.println("Mensagens do gestor:");
            for (MensagemGestor msg : mensagens) {
                System.out.println(">>> ID: " + msg.getId() + " | Conteudo: " + msg.getConteudo() 
                + " | Data de envio: " + msg.getDataEnvio() +  " | Gestor: " + msg.getIdGestor());
            }
        }
        
        pausar();
    }

    /** 
     * Método para adicionar funcionário à BD
     * @param idRestaurante Id do restaurante a que o funcionário será adicionado
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
     * Método para remover histórico de pedidos da BD
     */
    private void removerHistoricoPedidos() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   REMOVER HISTÓRICO DE PEDIDOS            ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        System.out.println("ID de Histórico a remover: \n");
        String idHistorico = scanner.nextLine().trim();
        
        Historico h = lnFacade.remover_historico_pedidos(idHistorico);
        if (h == null) {
            System.out.println("[Não há histórico de pedidos para remover.]");
            pausar();
            return;
        }
        System.out.println("[Histórico de pedidos removido com sucesso!]");
        
        pausar();
    }

    /** 
     * Método para listar histórico de pedidos na BD
     */
    private void listarHistoricoPedidosCadeia() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE HISTÓRICO DE PEDIDOS           ║");
        System.out.println("║   DA CADEIA                               ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Historico> historicos = lnFacade.getHistoricoPedidosCadeia();
        
        if (historicos.isEmpty()) {
            System.out.println("[Não há histórico de pedidos.]");
        } else {
            System.out.println("Histórico de pedidos da cadeia:\n");
            for (Historico h : historicos) {
                System.out.println(h.toString() + "\n");
            }
        }
        
        pausar();
    }

    /** 
     * Método para listar histórico de pedidos de um restaurante
     * @param idRestaurante ID do respetivo restaurante
     */
    private void listarHistoricoPedidosRestaurante(String idRestaurante) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   LISTA DE HISTÓRICO DE PEDIDOS           ║");
        System.out.println("║   DO RESTAURANTE                          ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        Collection<Historico> historicos = lnFacade.getHistoricoPedidosRestaurante(idRestaurante);
        
        if (historicos.isEmpty()) {
            System.out.println("[Não há histórico de pedidos para o restaurante " + idRestaurante + ".");
        } else {
            System.out.println("Histórico de pedidos do restaurante " + idRestaurante + ":\n");
            for (Historico h : historicos) {
                System.out.println(h.toString() + "\n");
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
