import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GerenciadorEventos {
    Scanner sc;
    private List<Evento> listaDeEventos;
    private static final String events = "events.data";
    private static final DateTimeFormatter FORMATADOR_DATA_HORARIO = DateTimeFormatter.ofPattern("dd/MM/yyyy às HH:mm");
    public GerenciadorEventos (Scanner sc){
        this.listaDeEventos = new ArrayList<>();
        this.sc = sc;
        carregarEventosDeArquivo();
        }





public void CadastroEvento() {
        System.out.println("Cadastro de novo evento");

        System.out.print("\nDigite o nome do evento: ");
        String nomeCompleto = sc.nextLine();

        System.out.print("A qual categoria ele pertence? ");
        Eventos.exibirCategorias();
        int escolhaC = 0;
        String categoria;
        if (sc.hasNextInt()){
            escolhaC = sc.nextInt();
            sc.nextLine();
        } else {
            System.out.println("Opção inválida. Tente novamente.");
        }
        switch (escolhaC) {
            case 1:
                categoria = "Shows e Concertos";
                break;
            
            case 2:
                categoria = "Festas e Celebrações Tradicionais";
                break;

            case 3:
                categoria = "Eventos Esportivos e de Lazer Ativo";
                break;
                
            case 4:
                categoria = "Cultura, Arte e Educação";
                break;
            
            case 5:
                categoria = "Feiras, Exposições e Eventos Comunitários";
                break;

            case 6:
                categoria = "Outros";

            default:
                System.out.println("Categoria não encontrada");
                break;
        }
        String email = sc.nextLine();

        LocalDate dataNascimento = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Digite a data do evento (no formato 'DD/MM/AAAA às HH:MM'): ");
            String dataHorarioInput = sc.nextLine();
            try{
                dataNascimento = LocalDate.parse(dataHorarioInput, FORMATADOR_DATA_HORARIO);
                dataValida = true;
            } catch(DateTimeParseException e){
                System.out.println("Formato de data inválido. Por favor, use 'DD/MM/AAAA às HH:MM. Tente novamente.");
            }
        }
        
        System.out.print("Cadastre seu nome de usuário: ");
        String login = sc.nextLine();

        System.out.print("Cadastre sua senha: ");
        String senha = sc.nextLine();

        Evento novoEvento = new Evento(login, senha, dataNascimento, nomeCompleto, email);
        this.listadeEventos.add(novoEvento);
        salvarEventosEmArquivo();
        System.out.println("\nUsuário cadastrado com sucesso!");
    }

    public void carregarEventosDeArquivo() {
        File arquivo = new File(users);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de usuários (" + users + ") não encontrado. Começando com lista vazia.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(users))) {
            String linha;
            this.listadeEventos.clear();

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

                        Evento evento = new Evento(login, senha, dataNascimento, nomeCompleto, email);
                        this.listadeEventos.add(evento);
                    } catch (DateTimeParseException e) {
                        System.err.println("Erro ao converter data para o usuário na linha: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha do arquivo de usuários: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    }
                } else {
                    System.err.println("Linha malformada no arquivo de usuários (esperava 5 campos, obteve " + dados.length + "): '" + linha + "'. Pulando.");
                }
            }
            System.out.println(this.listadeEventos.size() + " usuários carregados de " + users);
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários do arquivo: " + e.getMessage());
        }
    }
}
