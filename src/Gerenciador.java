import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
public class Gerenciador {
    Scanner sc;
    private List<Usuario> listadeUsuarios;

    public Gerenciador (Scanner sc){
    this.listadeUsuarios = new ArrayList<>();
    this.sc = sc;
    }

    public void CadastroUsuario() {
        System.out.println("Cadastro de novo usuário");

        System.out.print("\nDigite seu nome completo: ");
        String nomeCompleto = sc.nextLine();

        System.out.print("Digite seu email: ");
        String email = sc.nextLine();

        LocalDate dataNascimento = null;
        String dataNascimentoString;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Digite sua data de nascimento (no formato DD/MM/AAAA): ");
            dataNascimentoString = sc.nextLine();
            try{
                DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataNascimento = LocalDate.parse(dataNascimentoString, formatterEntrada);
                dataValida = true;
            } catch(DateTimeParseException e){
                System.out.println("Formato de data inválido. Por favor, use DD/MM/AAAA. Tente novamente.");
            }
        }
        
        System.out.print("Cadastre seu login: ");
        String login = sc.nextLine();

        System.out.print("Cadastre sua senha: ");
        String senha = sc.nextLine();

        Usuario novoUsuario = new Usuario(login, senha, dataNascimento, nomeCompleto, email);
        this.listadeUsuarios.add(novoUsuario);
        System.out.println("\nUsuário cadastrado com sucesso!");
    }

    public int ListarUsers(){
        int i = 0;
        int escolha;

        if (this.listadeUsuarios.isEmpty()) {
            System.out.println("Nenhum produto cadastrado até o momento.");
            return -1;
        }
        for (Usuario usuario : listadeUsuarios) {
                System.out.printf("%d: %s\n", (i + 1), usuario.getUser());
                i++;
            }
            System.out.printf("%d: voltar\n",(this.listadeUsuarios.size()+1));
            if (sc.hasNextInt()) {
                escolha = sc.nextInt();
                sc.nextLine();
            } else {
                System.out.println("Opção inválida.");
                sc.nextLine();
                return -1;
            }
            if (escolha == this.listadeUsuarios.size() + 1) {
                System.out.println("Retornando ao menu principal");
                return -1;
            }
            return escolha;
    }
}
