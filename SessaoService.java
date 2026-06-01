import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessaoService {

    // token → Sessao
    private final Map<String, Sessao> sessoes = new ConcurrentHashMap<>();

    public String criarSessao(String email) {
        // Remove sessão anterior do mesmo email se existir
        sessoes.values().removeIf(s -> s.getEmail().equals(email));

        String token = UUID.randomUUID().toString();
        sessoes.put(token, new Sessao(token, email));
        return token;
    }

    public boolean validar(String token) {
        Sessao sessao = sessoes.get(token);
        if (sessao == null) return false;

        if (!sessao.isValida()) {
            sessoes.remove(token); // limpa sessão expirada
            return false;
        }
        return true;
    }
}
