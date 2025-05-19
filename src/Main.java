import java.util.Scanner;

public class Main{
    
    private static void exibirMenuInicial() {
        System.out.println("\n --- Eventos Mossoró ---");
        System.out.println("Escolha uma opção: ");
        System.out.println("1: Entrar no sistema");
        System.out.println("2: Cadastrar-se no sistema");
        System.out.println("3: Sair");
        System.out.print("Sua opção: ");
    }

    private static void exibirMenuEventos() {
    System.out.println("\n--- Eventos Mossoró ---");
    System.out.println("Bem-vindo(a)!");
    System.out.println("\nEscolha uma opção:");
    System.out.println("1. Ver Todos os Eventos Disponíveis");
    System.out.println("2. Meus Eventos Confirmados");
    System.out.println("3. Ver Eventos Ocorrendo Agora");
    System.out.println("4. Ver Eventos Futuros");
    System.out.println("5. Ver Histórico de Eventos Passados");
    System.out.println("6. Cadastrar Novo Evento");
    System.out.println("7. Meu Perfil");
    System.out.println("8. Logout");
    System.out.println("-------------------------");
    }

    public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    GerenciadorUser gerenciadorUser = new GerenciadorUser(sc);    
    GerenciadorEventos gerenciadorEventos = new GerenciadorEventos(sc);

    int escolha = 0;
    int escolhaE = 0;
    do {
        exibirMenuInicial();
        if (sc.hasNextInt()){
            escolha = sc.nextInt();
            sc.nextLine();
        } else {
            System.out.println("Opção inválida. Tente novamente.");
        }
        switch (escolha) {
            case 1:
                gerenciadorUser.login();
                if (gerenciadorUser.login() == true){
                    do {
                        exibirMenuEventos();
                        if (sc.hasNextInt()){
                            escolhaE = sc.nextInt();
                            sc.nextLine();
                        } else {
                            System.out.println("Opção inválida. Tente novamente.");
                        }
                        switch (escolhaE) {
                            case 1:
                                
                                break;
                        
                            default:
                                break;
                        }
                    } while (escolhaE != 8);
                }
                break;
        
            case 2:
                gerenciadorUser.CadastroUsuario();
                break;

            case 3:
                System.out.println("Salvando dados e encerrando o programa...");
                break;   

            case 10:
                gerenciadorUser.listarTodosUsuarios();
                break;     

            default:
            System.out.println("Opção inválida. Tente novamente.");
                break;
        }
    } while(escolha != 3);
    sc.close();
    System.out.println("Programa encerrado.");
    }
}