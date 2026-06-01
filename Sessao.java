import java.time.LocalDateTime;

public class Sessao {
    private String        token;
    private String        email;
    private LocalDateTime expiracao;

    public Sessao(String token, String email) {
        this.token     = token;
        this.email     = email;
        this.expiracao = LocalDateTime.now().plusMinutes(1);
    }

    public String        getToken()     { return token; }
    public String        getEmail()     { return email; }
    public LocalDateTime getExpiracao() { return expiracao; }

    public boolean isValida() {
        return LocalDateTime.now().isBefore(expiracao);
    }
}
