import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GerenciadorEventos {
    Scanner sc;
    private List<Eventos> listaDeEventos;
    private static final String EVENTS_FILE_PATH = "events.data";
    private static final DateTimeFormatter FORMATADOR_DATA_HORARIO_ARQUIVO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter FORMATADOR_DATA_HORARIO_USUARIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private ConsultaViaCep consultaViaCepService;

    public GerenciadorEventos (Scanner sc){
        this.listaDeEventos = new ArrayList<>();
        this.sc = sc;
        this.consultaViaCepService = new ConsultaViaCep();
        carregarEventosDeArquivo();
    }

    public void CadastroEvento() {
        System.out.println("Cadastro de novo evento");

        System.out.print("\nDigite o nome do evento: ");
        String nome = sc.nextLine();
        for (Eventos e : listaDeEventos) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                System.out.println("Um evento com este nome já existe. Por favor, escolha outro nome.");
                return;
            }
        }

        System.out.println("A qual categoria ele pertence? ");
        Eventos.exibirCategorias();
        int escolhaC = 0;
        String categoria = "Outros"; 
        boolean categoriaValida = false;
        while(!categoriaValida){
            System.out.print("Escolha o número da categoria: ");
            if (sc.hasNextInt()){
                escolhaC = sc.nextInt();
                sc.nextLine(); 
                switch (escolhaC) {
                    case 1: categoria = "Shows e Concertos"; categoriaValida = true; break;
                    case 2: categoria = "Festas e Celebrações Tradicionais"; categoriaValida = true; break;
                    case 3: categoria = "Eventos Esportivos e de Lazer Ativo"; categoriaValida = true; break;
                    case 4: categoria = "Cultura, Arte e Educação"; categoriaValida = true; break;
                    case 5: categoria = "Feiras, Exposições e Eventos Comunitários"; categoriaValida = true; break;
                    case 6: categoria = "Outros"; categoriaValida = true; break;
                    default: System.out.println("Categoria inválida. Tente novamente."); break;
                }
            } else {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.nextLine(); 
            }
        }

        LocalDateTime dataHorario = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Digite a data e hora do evento (no formato 'DD/MM/AAAA HH:mm'): ");
            String dataHorarioInput = sc.nextLine().trim();
            try{
                dataHorario = LocalDateTime.parse(dataHorarioInput, FORMATADOR_DATA_HORARIO_USUARIO);
                if (dataHorario.isBefore(LocalDateTime.now())) {
                    System.out.println("A data do evento não pode ser no passado. Tente novamente.");
                } else {
                    dataValida = true;
                }
            } catch(DateTimeParseException e){
                System.out.println("Formato de data e hora inválido. Por favor, use 'DD/MM/AAAA HH:mm'. Tente novamente.");
            }
        }
        
        System.out.println("\n--- Cadastro do Local do Evento ---");
        String local = "Local não informado";
        boolean localDefinidoComSucesso = false;
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

                            if (dadosEndereco != null && !(dadosEndereco.containsKey("erro") && dadosEndereco.get("erro").equals("true"))) {
                                String logradouro = dadosEndereco.getOrDefault("logradouro", "");
                                String bairro = dadosEndereco.getOrDefault("bairro", "");
                                String cidade = dadosEndereco.getOrDefault("localidade", "");
                                String uf = dadosEndereco.getOrDefault("uf", "");

                                if (logradouro.isEmpty() && cidade.isEmpty() && uf.isEmpty()) {
                                     System.out.println("CEP não resultou em um endereço válido. Tente um CEP mais específico ou cadastre manualmente.");
                                     System.out.print("Deseja tentar digitar outro CEP? (S/N): ");
                                     if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                                         tentarNovamenteCep = false;
                                     }
                                     continue;
                                }

                                System.out.println("\n--- Dados do Endereço Encontrado ---");
                                System.out.println("Logradouro: " + (logradouro.isEmpty() ? "N/A" : logradouro));
                                System.out.println("Bairro: " + (bairro.isEmpty() ? "N/A" : bairro));
                                System.out.println("Cidade: " + (cidade.isEmpty() ? "N/A" : cidade));
                                System.out.println("UF: " + (uf.isEmpty() ? "N/A" : uf));

                                System.out.print("\nDigite o número da residência/complemento (ou deixe em branco): ");
                                String numeroComplemento = sc.nextLine().trim();

                                StringBuilder localBuilder = new StringBuilder();
                                if (!logradouro.isEmpty()) localBuilder.append(logradouro);
                                if (!numeroComplemento.isEmpty()) {
                                    if (localBuilder.length() > 0) localBuilder.append(", ");
                                    localBuilder.append(numeroComplemento);
                                }
                                if (!bairro.isEmpty()) {
                                    if (localBuilder.length() > 0) localBuilder.append(", ");
                                    localBuilder.append(bairro);
                                }
                                if (!cidade.isEmpty()) {
                                    if (localBuilder.length() > 0) localBuilder.append(", ");
                                    localBuilder.append(cidade);
                                }
                                if (!uf.isEmpty()) {
                                    if (localBuilder.length() > 0) localBuilder.append(" - ");
                                    localBuilder.append(uf);
                                }
                                
                                local = localBuilder.toString();
                                System.out.println("Local definido como: " + local);
                                localDefinidoComSucesso = true;
                                tentarNovamenteCep = false; 
                            } else {
                                System.out.println("CEP não encontrado ou falha na API.");
                                System.out.print("Deseja tentar digitar outro CEP? (S/N): ");
                                if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                                    tentarNovamenteCep = false;
                                }
                            }
                        } else {
                            System.out.println("CEP digitado não possui 8 números válidos.");
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
            local = sc.nextLine().trim();
        }
        if (local == null || local.trim().isEmpty()) {
            local = "Local não informado";
        }
        
        System.out.println("Dê uma descrição ao evento: ");
        String descricao = sc.nextLine();

        Eventos novoEvento = new Eventos(nome, categoria, local, dataHorario, descricao);
        this.listaDeEventos.add(novoEvento);
        System.out.println("\nEvento '" + nome + "' cadastrado com sucesso!");
    }
    
    public void listarTodosEventos() {
        if (this.listaDeEventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado até o momento.");
        return;
        }
        List<Eventos> listaOrdenada = new ArrayList<>(this.listaDeEventos);
        Collections.sort(listaOrdenada, new Comparator<Eventos>() {
            @Override
            public int compare(Eventos e1, Eventos e2) {
                if (e1.getLocalDateTime() == null && e2.getLocalDateTime() == null) return 0;
                if (e1.getLocalDateTime() == null) return 1; 
                if (e2.getLocalDateTime() == null) return -1; 
                return e1.getLocalDateTime().compareTo(e2.getLocalDateTime());
            }
        });
        System.out.println("\n--- Lista de Todos os Eventos Cadastrados (Ordenados por Data) ---");
        for (int i = 0; i < listaOrdenada.size(); i++) {
            System.out.printf("\nEvento nº%d: \n", (i + 1));
            listaOrdenada.get(i).info();
        }
    }

    public void listarEventosConfirmadosDoUsuario(Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            System.out.println("Nenhum usuário logado para verificar eventos confirmados.");
            return;
        }
        List<String> nomesEventosConfirmados = usuarioLogado.getNomesEventosConfirmados();
        if (nomesEventosConfirmados.isEmpty()) {
            System.out.println("\nVocê não possui eventos confirmados, " + usuarioLogado.getUser() + ".");
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        List<Eventos> eventosConfirmadosAtuaisOuFuturos = new ArrayList<>();
        for (String nomeEventoConfirmado : nomesEventosConfirmados) {
            this.listaDeEventos.stream()
                .filter(evento -> evento.getNome().equals(nomeEventoConfirmado) &&
                                 evento.getLocalDateTime() != null && 
                                 !evento.getLocalDateTime().isBefore(agora)) 
                .findFirst()
                .ifPresent(eventosConfirmadosAtuaisOuFuturos::add);
        }

        if (eventosConfirmadosAtuaisOuFuturos.isEmpty()) {
            System.out.println("\nVocê não possui eventos confirmados ocorrendo agora ou no futuro, " + usuarioLogado.getUser() + ".");
            System.out.println("Verifique a opção 'Eventos Passados' para ver eventos anteriores.");
            return;
        }
        
        Collections.sort(eventosConfirmadosAtuaisOuFuturos, Comparator.comparing(Eventos::getLocalDateTime, Comparator.nullsLast(Comparator.naturalOrder())));

        System.out.println("\n--- Seus Eventos Confirmados (Atuais e Futuros), " + usuarioLogado.getUser() + " (Ordenados por Data) ---");
        for (int i = 0; i < eventosConfirmadosAtuaisOuFuturos.size(); i++) {
            System.out.printf("\nEvento nº%d (Confirmado):\n", (i + 1));
            eventosConfirmadosAtuaisOuFuturos.get(i).info();
        }
    }

    public void listarEventosNaoConfirmadosPeloUsuario(Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            System.out.println("Nenhum usuário logado para verificar eventos não confirmados.");
            return;
        }
         if (this.listaDeEventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado no sistema para confirmar.");
            return;
        }
        LocalDateTime agora = LocalDateTime.now();
        List<Eventos> eventosNaoConfirmadosDisponiveis = this.listaDeEventos.stream()
            .filter(evento -> !usuarioLogado.estaConfirmado(evento.getNome()) && 
                              evento.getLocalDateTime() != null && 
                              evento.getLocalDateTime().isAfter(agora))
            .sorted(Comparator.comparing(Eventos::getLocalDateTime, Comparator.nullsLast(Comparator.naturalOrder())))
            .collect(Collectors.toList());

        if (eventosNaoConfirmadosDisponiveis.isEmpty()) {
            System.out.println("\nNão há novos eventos futuros disponíveis para confirmação ou você já confirmou todos os futuros disponíveis, " + usuarioLogado.getUser() + ".");
            return;
        }
        System.out.println("\n--- Eventos Futuros Disponíveis para Confirmação, " + usuarioLogado.getUser() + " (Ordenados por Data) ---");
        for (int i = 0; i < eventosNaoConfirmadosDisponiveis.size(); i++) {
            System.out.printf("\n%d. ", (i + 1)); 
            eventosNaoConfirmadosDisponiveis.get(i).info();
             System.out.println("   Status: Disponível para confirmar");
        }
    }
    
    public void listarEventosPassados(Usuario usuarioLogado) {
        if (this.listaDeEventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado no sistema para exibir no histórico.");
            return;
        }
        LocalDateTime agora = LocalDateTime.now();
        List<Eventos> eventosPassados = this.listaDeEventos.stream()
            .filter(evento -> evento.getLocalDateTime() != null && evento.getLocalDateTime().isBefore(agora))
            .sorted(Comparator.comparing(Eventos::getLocalDateTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
            .collect(Collectors.toList());

        if (eventosPassados.isEmpty()) {
            System.out.println("\nNenhum evento passado encontrado no histórico.");
            return;
        }
        System.out.println("\n--- Histórico de Eventos Passados (Mais recentes primeiro) ---");
        for (int i = 0; i < eventosPassados.size(); i++) {
            Eventos evento = eventosPassados.get(i);
            System.out.printf("\nEvento nº%d:\n", (i + 1));
            evento.info();
            if (usuarioLogado != null && usuarioLogado.estaConfirmado(evento.getNome())) {
                System.out.println("   Sua presença: Confirmada");
            } else if (usuarioLogado != null) {
                System.out.println("   Sua presença: Não confirmada / Ausente");
            } else {
                 System.out.println("   (Status de presença não disponível sem login)");
            }
        }
    }

    public void confirmarPresencaEvento(Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            System.out.println("Você precisa estar logado para confirmar presença.");
            return;
        }
        System.out.println("\n--- Confirmar Presença em Evento ---");
        
        LocalDateTime agora = LocalDateTime.now();
        List<Eventos> eventosDisponiveis = this.listaDeEventos.stream()
            .filter(e -> !usuarioLogado.estaConfirmado(e.getNome()) && 
                          e.getLocalDateTime()!=null && 
                          e.getLocalDateTime().isAfter(agora)) 
            .sorted(Comparator.comparing(Eventos::getLocalDateTime, Comparator.nullsLast(Comparator.naturalOrder())))
            .collect(Collectors.toList());

        if (eventosDisponiveis.isEmpty()) {
            System.out.println("Não há eventos futuros disponíveis para você confirmar no momento.");
            return;
        }
        System.out.println("Eventos futuros disponíveis para confirmação:");
        for (int i = 0; i < eventosDisponiveis.size(); i++) {
            System.out.printf("%d. %s (%s)\n", (i + 1), eventosDisponiveis.get(i).getNome(), eventosDisponiveis.get(i).getDataHorarioFormatada());
        }
        System.out.print("Digite o número do evento que deseja confirmar (ou 0 para cancelar): ");
        int escolhaEventoNum;
        if (sc.hasNextInt()) {
            escolhaEventoNum = sc.nextInt();
            sc.nextLine(); 
        } else {
            System.out.println("Entrada inválida.");
            sc.nextLine(); 
            return;
        }

        if (escolhaEventoNum > 0 && escolhaEventoNum <= eventosDisponiveis.size()) {
            Eventos eventoEscolhido = eventosDisponiveis.get(escolhaEventoNum - 1);
            usuarioLogado.confirmarPresenca(eventoEscolhido.getNome());
            System.out.println("Presença confirmada para o evento: " + eventoEscolhido.getNome());
        } else if (escolhaEventoNum == 0) {
            System.out.println("Operação de confirmação cancelada.");
        } else {
            System.out.println("Número do evento inválido.");
        }
    }

    public void cancelarPresencaEvento(Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            System.out.println("Você precisa estar logado para cancelar presença.");
            return;
        }
        System.out.println("\n--- Cancelar Presença em Evento ---");
        
        List<String> nomesEventosConfirmados = usuarioLogado.getNomesEventosConfirmados();
        if (nomesEventosConfirmados.isEmpty()) {
            System.out.println("Você não tem nenhuma presença confirmada para cancelar.");
            return;
        }
        LocalDateTime agora = LocalDateTime.now();
        List<Eventos> eventosCancelaveis = new ArrayList<>();
        for(String nomeEvento : nomesEventosConfirmados) {
            this.listaDeEventos.stream()
                .filter(e -> e.getNome().equals(nomeEvento) &&
                              e.getLocalDateTime() != null &&
                              !e.getLocalDateTime().isBefore(agora)) 
                .findFirst()
                .ifPresent(eventosCancelaveis::add);
        }
        
        Collections.sort(eventosCancelaveis, Comparator.comparing(Eventos::getLocalDateTime, Comparator.nullsLast(Comparator.naturalOrder())));

        if (eventosCancelaveis.isEmpty()) {
             System.out.println("Você não tem presenças confirmadas em eventos atuais ou futuros para cancelar.");
            return;
        }
        System.out.println("Seus eventos confirmados (atuais ou futuros) que podem ser cancelados:");
        for (int i = 0; i < eventosCancelaveis.size(); i++) {
            System.out.printf("%d. %s (%s)\n", (i + 1), eventosCancelaveis.get(i).getNome(), eventosCancelaveis.get(i).getDataHorarioFormatada());
        }
        System.out.print("Digite o número do evento que deseja cancelar a presença (ou 0 para voltar): ");
        int escolhaEventoNum;
        if (sc.hasNextInt()) {
            escolhaEventoNum = sc.nextInt();
            sc.nextLine(); 
        } else {
            System.out.println("Entrada inválida.");
            sc.nextLine(); 
            return;
        }

        if (escolhaEventoNum > 0 && escolhaEventoNum <= eventosCancelaveis.size()) {
            Eventos eventoEscolhido = eventosCancelaveis.get(escolhaEventoNum - 1);
            usuarioLogado.cancelarPresenca(eventoEscolhido.getNome());
            System.out.println("Presença cancelada para o evento: " + eventoEscolhido.getNome());
        } else if (escolhaEventoNum == 0) {
            System.out.println("Operação de cancelamento de presença cancelada.");
        } else {
            System.out.println("Número do evento inválido.");
        }
    }

    public void salvarEventosEmArquivo() {
        try (BufferedWriter writer = new BufferedWriter(
                                     new OutputStreamWriter(
                                     new FileOutputStream(EVENTS_FILE_PATH), StandardCharsets.UTF_8))) {
        for (Eventos evento : listaDeEventos) {
                String dataHorarioStr = "";
                if (evento.getLocalDateTime() != null) {
                    dataHorarioStr = evento.getLocalDateTime().format(FORMATADOR_DATA_HORARIO_ARQUIVO); 
                } else {
                    dataHorarioStr = "NULL"; 
                }
                String linha = String.join(";",
                        evento.getNome(),
                        evento.getCategoria(),
                        evento.getLocal(),
                        dataHorarioStr,
                        evento.getDescricao()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar eventos no arquivo: " + e.getMessage());
        }
    }

    public void carregarEventosDeArquivo() {
        File arquivo = new File(EVENTS_FILE_PATH);
        if (!arquivo.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(
                                     new InputStreamReader(
                                     new FileInputStream(EVENTS_FILE_PATH), StandardCharsets.UTF_8))) {
        String linha;
            this.listaDeEventos.clear();
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(";", -1);
                if (dados.length == 5) { 
                    try {
                        String nome = dados[0];
                        String categoria = dados[1];
                        String local = dados[2];
                        LocalDateTime dataHorario = null;
                        String descricao = dados[4];
                        if (dados[3] != null && !dados[3].trim().isEmpty() && !dados[3].equalsIgnoreCase("NULL")) { 
                           dataHorario = LocalDateTime.parse(dados[3], FORMATADOR_DATA_HORARIO_ARQUIVO); 
                        }
                        Eventos evento = new Eventos(nome, categoria, local, dataHorario, descricao); 
                        this.listaDeEventos.add(evento);
                    } catch (DateTimeParseException e) {
                        System.err.println("Erro ao converter data para o evento na linha: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha do arquivo de eventos: '" + linha + "'. Pulando. Detalhe: " + e.getMessage());
                    }
                } else {
                    System.err.println("Linha malformada no arquivo de eventos (esperava 5 campos, obteve " + dados.length + "): '" + linha + "'. Pulando.");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar eventos do arquivo: " + e.getMessage());
        }
    }
}