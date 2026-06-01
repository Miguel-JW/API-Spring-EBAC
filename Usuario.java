import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {}

    public Usuario(String nome, String email, String senha) {
        this.nome  = nome;
        this.email = email;
        this.senha = senha;
    }

    public Long   getId()    { return id; }
    public String getNome()  { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}
