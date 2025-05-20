import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    String user;
    String senha;
    LocalDate dataNascimento;
    String nomeCompleto;
    String email;
    private List<String> nomesEventosConfirmados;

    public Usuario(String user, String senha, LocalDate dataNascimento, String nomeCompleto, String email){
        this.user = user;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.nomesEventosConfirmados = new ArrayList<>();
    }

    public String getDataNascimentoFormatada() {
        if (this.dataNascimento == null) {
            return "Data não informada";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataNascimento.format(formatter);
    }
    
    public String getUser(){
            return this.user;
    }

    public String getSenha() {
        return this.senha;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public String getNomeCompleto() {
        return this.nomeCompleto;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void info() {
        System.out.println("Nome: " + this.nomeCompleto);
        System.out.println("Email: " + this.email);
        System.out.println("Data de Nascimento: " + getDataNascimentoFormatada());
        System.out.println("Login: " + this.user);
        if (!nomesEventosConfirmados.isEmpty()) {
            System.out.println("Eventos Confirmados (Nomes): " + String.join(", ", nomesEventosConfirmados));
        } else {
            System.out.println("Nenhum evento confirmado.");
        }
    }

    public List<String> getNomesEventosConfirmados() {
        return new ArrayList<>(this.nomesEventosConfirmados);
    }

    public void confirmarPresenca(String nomeEvento) {
        if (nomeEvento != null && !nomeEvento.isEmpty() && !this.nomesEventosConfirmados.contains(nomeEvento)) {
            this.nomesEventosConfirmados.add(nomeEvento);
        }
    }

    public void cancelarPresenca(String nomeEvento) {
        this.nomesEventosConfirmados.remove(nomeEvento);
    }

    public boolean estaConfirmado(String nomeEvento) {
        return this.nomesEventosConfirmados.contains(nomeEvento);
    }

    void setNomesEventosConfirmados(List<String> nomesEventos) {
        this.nomesEventosConfirmados = new ArrayList<>(nomesEventos);
    }
}