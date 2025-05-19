import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Eventos {
    String nome;
    String categoria;
    String local;
    LocalDateTime dataHorario;

    public Eventos(String nome, String categoria, String local, LocalDateTime dataHorario){
        this.nome = nome;
        this.categoria = categoria;
        this.local = local;
        this.dataHorario = dataHorario;
    }

    public String getDataHorarioFormatada() {
        if (this.dataHorario == null) {
            return "Data não informada";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy às HH:mm");
        return this.dataHorario.format(formatter);
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

    public LocalDateTime getDataHorario(){
        return this.dataHorario;
    }

    public void info() {
        System.out.println("Evento: " + this.nome);
        System.out.println("Categoria: " + this.categoria);
        System.out.println("Data do evento: " + this.dataHorario);
        System.out.println("Local: " + this.local);
    }

    public static void exibirCategorias(){
        System.out.println("1: Shows e Concertos");
        System.out.println("2: Festas e Celebrações Tradicionais");
        System.out.println("3: Eventos Esportivos e de Lazer Ativo");
        System.out.println("4: Cultura, Arte e Educação");
        System.out.println("5: Feiras, Exposições e Eventos Comunitários");
        System.out.println("6: Outros");
    }
}
