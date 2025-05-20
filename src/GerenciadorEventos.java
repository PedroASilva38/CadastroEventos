import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GerenciadorEventos {
    Scanner sc;
    private List<Eventos> listaDeEventos;
    private static final String events = "events.data";
    private static final DateTimeFormatter FORMATADOR_DATA_HORARIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private ConsultaViaCep consultaViaCepService;

    public GerenciadorEventos (Scanner sc){
        this.listaDeEventos = new ArrayList<>();
        this.sc = sc;
        this.consultaViaCepService = new ConsultaViaCep();
        carregarEventosDeArquivo();
        }

public void CadastroEvento() {
        System.out.println("Cadastro de novo eventos");

        System.out.print("\nDigite o nome do eventos: ");
        String nome = sc.nextLine();

        System.out.print("A qual categoria ele pertence? ");
        Eventos.exibirCategorias();
        int escolhaC = 0;
        String categoria = null;
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

        LocalDateTime dataHorario = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Digite a data do eventos (no formato 'DD/MM/AAAA HH:mm'): ");
            String dataHorarioInput = sc.nextLine().trim();
            try{
                dataHorario = LocalDateTime.parse(dataHorarioInput, FORMATADOR_DATA_HORARIO);
                dataValida = true;
            } catch(DateTimeParseException e){
                System.out.println("Formato de data inválido. Por favor, use 'DD/MM/AAAA HH:mm. Tente novamente.");
            }
        }
        
        System.out.println("\n--- Cadastro do Local do Evento ---");
        String local = "Local não informado";
        boolean localDefinidoComSucesso = false;

        System.out.print("Deseja cadastrar o local usando o CEP? (S/N): ");
        boolean continuarPerguntandoOpcaoCep = true;

        while (continuarPerguntandoOpcaoCep) {
            System.out.print("Deseja cadastrar o local usando o CEP? (S/N): ");
            String opcaoUsarCep = sc.nextLine().trim().toUpperCase();

            switch (opcaoUsarCep) {
                case "S":
                    boolean tentarNovamenteCep = true;
                    while (tentarNovamenteCep && !localDefinidoComSucesso) {
                        System.out.print("Digite o CEP (apenas 8 números): ");
                        String cepInput = sc.nextLine().replaceAll("[^0-9]", "");

                        if (cepInput.length() == 8) {
                            Map<String, String> dadosEndereco = consultaViaCepService.buscarDadosPorCep(cepInput);

                            if (dadosEndereco != null) {
                                System.out.println("\n--- Dados do Endereço Encontrado ---");
                                System.out.println("Logradouro: " + dadosEndereco.getOrDefault("logradouro", "N/A"));
                                System.out.println("Bairro: " + dadosEndereco.getOrDefault("bairro", "N/A"));
                                System.out.println("Cidade: " + dadosEndereco.getOrDefault("localidade", "N/A"));
                                System.out.println("UF: " + dadosEndereco.getOrDefault("uf", "N/A"));

                                System.out.print("\nDigite o número da residência/complemento: ");
                                String numeroComplemento = sc.nextLine();

                                local = dadosEndereco.getOrDefault("logradouro", "") + ", " + numeroComplemento +
                                        ", " + dadosEndereco.getOrDefault("bairro", "") +
                                        ", " + dadosEndereco.getOrDefault("localidade", "") +
                                        " - " + dadosEndereco.getOrDefault("uf", "");
                                System.out.println("Local definido como: " + local);
                                localDefinidoComSucesso = true;
                                tentarNovamenteCep = false;
                            } else {
                                System.out.println("CEP não encontrado na base de dados ou falha na API.");
                                System.out.print("Deseja tentar digitar outro CEP? (S/N): ");
                                if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                                    tentarNovamenteCep = false;
                                }
                            }
                        } else {
                            System.out.println("CEP digitado não possui 8 números.");
                            System.out.print("Deseja tentar digitar o CEP novamente? (S/N): ");
                            if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                                tentarNovamenteCep = false;
                            }
                        }
                    }
                    continuarPerguntandoOpcaoCep = false;
                    break;

                case "N":
                    continuarPerguntandoOpcaoCep = false;
                    break;

                default:
                    System.out.println("Opção inválida. Por favor, digite 'S' para Sim ou 'N' para Não.");
                    break;
            }
        }

        if (!localDefinidoComSucesso) {
            System.out.print("Digite o local do evento manualmente: ");
            local = sc.nextLine();
        }

        if (local == null || local.trim().isEmpty()) {
            local = "Local não informado";
        }
        
        Eventos novoEvento = new Eventos(nome, categoria, local, dataHorario);
        this.listaDeEventos.add(novoEvento);
        salvarEventosEmArquivo();
        System.out.println("\nEvento cadastrado com sucesso!");
    }
    

    public void salvarEventosEmArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(events))) {
            for (Eventos eventos : listaDeEventos) {
                String linha = String.join(";",
                        eventos.getNome(),
                        eventos.getCategoria(),
                        eventos.getLocal(),
                        eventos.getDataHorario().format(FORMATADOR_DATA_HORARIO));
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Usuários salvos em " + events);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários no arquivo: " + e.getMessage());
        }
    }

    public void carregarEventosDeArquivo() {
        File arquivo = new File(events);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de usuários (" + events + ") não encontrado. Começando com lista vazia.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(events))) {
            String linha;
            this.listaDeEventos.clear();

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(";", -1);
                if (dados.length == 4) {
                    try {
                        String nome = dados[0];
                        String categoria = dados[1];
                        String local = dados[2];
                        LocalDateTime dataHorario = LocalDateTime.parse(dados[3], FORMATADOR_DATA_HORARIO);

                        Eventos eventos = new Eventos(nome, categoria, local, dataHorario);
                        this.listaDeEventos.add(eventos);
                    } catch (DateTimeParseException e) {
                        System.err.println("Erro ao converter data para o evento na linha: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha do arquivo de usuários: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    }
                } else {
                    System.err.println("Linha malformada no arquivo de usuários (esperava 4 campos, obteve " + dados.length + "): '" + linha + "'. Pulando.");
                }
            }
            System.out.println(this.listaDeEventos.size() + " eventos carregados de " + events);
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários do arquivo: " + e.getMessage());
        }
    }
}
