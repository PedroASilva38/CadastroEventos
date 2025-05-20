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

    private static void exibirMenuEventosLogado() {
        System.out.println("\n--- Eventos Mossoró ---");
        System.out.println("Escolha uma opção:");
        System.out.println("1. Meus Eventos Confirmados");
        System.out.println("2. Eventos Futuros Disponíveis");
        System.out.println("3. Histórico de Eventos Passados");
        System.out.println("4. Cadastrar Novo Evento");
        System.out.println("5. Confirmar Presença em Evento");
        System.out.println("6. Cancelar Presença em Evento");
        System.out.println("7. Logout");
        System.out.println("-------------------------");
        System.out.print("Sua opção: ");
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        GerenciadorUser gerenciadorUser = new GerenciadorUser(sc);    
        GerenciadorEventos gerenciadorEventos = new GerenciadorEventos(sc);
        Usuario usuarioLogado = null; 

        int escolha = 0;
        
        do {
            if (usuarioLogado != null && gerenciadorUser.getUsuarioLogado() == null) {
                usuarioLogado = null;
            }

            exibirMenuInicial();
            if (sc.hasNextInt()){
                escolha = sc.nextInt();
                sc.nextLine(); 
            } else {
                System.out.println("Opção inválida. Por favor, digite um número.");
                sc.nextLine(); 
                escolha = 0; 
            }

            switch (escolha) {
                case 1: 
                    if (gerenciadorUser.login()) { 
                        usuarioLogado = gerenciadorUser.getUsuarioLogado(); 
                        int escolhaMenuLogado = 0; 
                        do {
                            exibirMenuEventosLogado();
                            if (sc.hasNextInt()){
                                escolhaMenuLogado = sc.nextInt();
                                sc.nextLine(); 
                            } else {
                                System.out.println("Opção inválida. Por favor, digite um número.");
                                sc.nextLine(); 
                                escolhaMenuLogado = 0; 
                            }
                            switch (escolhaMenuLogado) {
                                case 1: 
                                    gerenciadorEventos.listarEventosConfirmadosDoUsuario(usuarioLogado);
                                    break;
                                case 2: 
                                    gerenciadorEventos.listarEventosNaoConfirmadosPeloUsuario(usuarioLogado);
                                    break;
                                case 3:
                                    gerenciadorEventos.listarEventosPassados(usuarioLogado);
                                    break;
                                case 4:
                                    gerenciadorEventos.CadastroEvento();
                                    gerenciadorEventos.salvarEventosEmArquivo(); 
                                    break;
                                case 5:
                                    gerenciadorEventos.confirmarPresencaEvento(usuarioLogado);
                                    gerenciadorUser.salvarUsuariosEmArquivo(); 
                                    break;
                                case 6:
                                    gerenciadorEventos.cancelarPresencaEvento(usuarioLogado);
                                    gerenciadorUser.salvarUsuariosEmArquivo(); 
                                    break;
                                case 7:
                                    gerenciadorUser.logout(); 
                                    usuarioLogado = null; 
                                    System.out.println("Logout realizado.");
                                    break;
                                default:
                                    if (escolhaMenuLogado != 7) { 
                                        System.out.println("Opção inválida no menu de eventos. Tente novamente.");
                                    }
                                    break;
                            }
                        } while (escolhaMenuLogado != 7); 
                    }
                    break; 
            
                case 2: 
                    gerenciadorUser.CadastroUsuario();
                    break;

                case 3: 
                    System.out.println("Salvando todos os dados e encerrando o programa...");
                    gerenciadorUser.salvarUsuariosEmArquivo(); 
                    gerenciadorEventos.salvarEventosEmArquivo(); 
                    break;   
                default:
                if (escolha != 3) { 
                    System.out.println("Opção inválida no menu inicial. Tente novamente.");
                }
                    break;
            }
        } while(escolha != 3); 

        sc.close();
        System.out.println("Programa encerrado.");
    }
}