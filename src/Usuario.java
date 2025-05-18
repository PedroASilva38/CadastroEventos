import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Usuario {
    String user;
    String senha;
    LocalDate dataNascimento;
    String nomeCompleto;
    String email;
    int diaN;
    int mesN;
    int anoN;

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
}
