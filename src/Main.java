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

    public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    Gerenciador gerenciador = new Gerenciador(sc);    
    
    int escolha = 0;
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
        
                break;
        
            case 2:
                gerenciador.CadastroUsuario();
                break;

            case 3:
                System.out.println("Salvando dados e encerrando o programa...");
                break;   

            case 10:
                gerenciador.listarTodosUsuarios();
                break;     

            default:
            System.out.println("Opção inválida. Tente novamente.");
                break;
        }
    } while(escolha != 4);
    sc.close();
    System.out.println("Programa encerrado.");
    }
}