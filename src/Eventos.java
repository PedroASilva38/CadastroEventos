import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Eventos {
    String nome;
    String categoria;
    String local;
    LocalDateTime dataHorario;
    String descricao;

    public Eventos(String nome, String categoria, String local, LocalDateTime dataHorario, String descricao){
        this.nome = nome;
        this.categoria = categoria;
        this.local = local;
        this.dataHorario = dataHorario;
        this.descricao = descricao;
    }

    public String getNome(){
        return this.nome;
    }

    public String getCategoria(){
        return this.categoria;
    }

    public String getLocal(){
        return this.local;
    }

    public String getDataHorarioFormatada(){ 
        if (this.dataHorario == null) {
            return "Data não informada";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        return this.dataHorario.format(formatter);
    }

    public LocalDateTime getLocalDateTime(){
        return this.dataHorario;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public void info() {
        System.out.println("  Evento: " + this.nome);
        System.out.println("  Categoria: " + this.categoria);
        System.out.println("  Data do evento: " + getDataHorarioFormatada());
        System.out.println("  Local: " + this.local);
        System.out.println("  Descrição: " + this.descricao);
    }

    public static void exibirCategorias(){
        System.out.println("Categorias Disponíveis:");
        System.out.println("1: Show / Concerto");
        System.out.println("2: Festa");
        System.out.println("3: Evento Esportivo");
        System.out.println("4: Cultura, Arte e Educação");
        System.out.println("5: Feira / Evento Comunitário");
        System.out.println("6: Outros");
    }
}