import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
public class Gerenciador {
    Scanner sc;
    private List<Usuario> listadeUsuarios;
    private static final String users = "usuarios.data";
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Gerenciador (Scanner sc){
    this.listadeUsuarios = new ArrayList<>();
    this.sc = sc;
    carregarUsuariosDeArquivo();
    }

    public void CadastroUsuario() {
        System.out.println("Cadastro de novo usuário");

        System.out.print("\nDigite seu nome completo: ");
        String nomeCompleto = sc.nextLine();

        System.out.print("Digite seu email: ");
        String email = sc.nextLine();

        LocalDate dataNascimento = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Digite sua data de nascimento (no formato DD/MM/AAAA): ");
            String dataNascimentoInput = sc.nextLine();
            try{
                dataNascimento = LocalDate.parse(dataNascimentoInput, FORMATADOR_DATA);
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
        salvarUsuariosEmArquivo();
        System.out.println("\nUsuário cadastrado com sucesso!");
    }

    public void listarTodosUsuarios() {
        if (this.listadeUsuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado até o momento.");
            return;
        }
        System.out.println("\n--- Lista de Usuários Cadastrados ---");
        for (int i = 0; i < this.listadeUsuarios.size(); i++) {
            System.out.printf("\nUsuário nº%d: \n", (i + 1));
            this.listadeUsuarios.get(i).info();
        }
    }

    public void salvarUsuariosEmArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(users))) {
            for (Usuario usuario : listadeUsuarios) {
                String linha = String.join(";",
                        usuario.getUser(),
                        usuario.getSenha(),
                        usuario.getDataNascimento().format(FORMATADOR_DATA),
                        usuario.getNomeCompleto(),
                        usuario.getEmail());
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Usuários salvos em " + users);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários no arquivo: " + e.getMessage());
        }
    }

    public void carregarUsuariosDeArquivo() {
        File arquivo = new File(users);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de usuários (" + users + ") não encontrado. Começando com lista vazia.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(users))) {
            String linha;
            this.listadeUsuarios.clear();

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(";", -1);
                if (dados.length == 5) {
                    try {
                        String login = dados[0];
                        String senha = dados[1];
                        LocalDate dataNascimento = LocalDate.parse(dados[2], FORMATADOR_DATA);
                        String nomeCompleto = dados[3];
                        String email = dados[4];

                        Usuario usuario = new Usuario(login, senha, dataNascimento, nomeCompleto, email);
                        this.listadeUsuarios.add(usuario);
                    } catch (DateTimeParseException e) {
                        System.err.println("Erro ao converter data para o usuário na linha: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha do arquivo de usuários: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    }
                } else {
                    System.err.println("Linha malformada no arquivo de usuários (esperava 5 campos, obteve " + dados.length + "): '" + linha + "'. Pulando.");
                }
            }
            System.out.println(this.listadeUsuarios.size() + " usuários carregados de " + users);
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários do arquivo: " + e.getMessage());
        }
    }
}