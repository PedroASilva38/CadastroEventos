import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Usuario {
    String user;
    String senha;
    LocalDate dataNascimento;
    String nomeCompleto;
    String email;

    public Usuario(String user, String senha, LocalDate dataNascimento, String nomeCompleto, String email){
        this.user = user;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
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
        System.out.println("Data de Nascimento: " + this.dataNascimento);
        System.out.println("Login: " + this.user);
        System.out.println("Senha: " + this.senha);
    }
}