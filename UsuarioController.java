import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final SessaoService     sessaoService;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             SessaoService sessaoService) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoService     = sessaoService;
    }

    // ── POST /cadastrar → salva usuário no banco ───────────
    @PostMapping("/cadastrar")
    public ResponseEntity<Map<String, String>> cadastrar(@RequestBody Usuario usuario) {
        usuarioRepository.save(usuario);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Map.of("mensagem", "Usuário " + usuario.getNome() + " cadastrado com sucesso!"));
    }

    // ── POST /login → valida e gera token de sessão ────────
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        Optional<Usuario> usuario = usuarioRepository.findByEmailAndSenha(email, senha);

        if (usuario.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensagem", "Email ou senha inválidos."));
        }

        String token = sessaoService.criarSessao(email);

        return ResponseEntity.ok(Map.of(
            "mensagem", "Login realizado! Sua sessão expira em 1 minuto.",
            "token",    token
        ));
    }

    // ── GET /acesso → recurso protegido por sessão ─────────
    @GetMapping("/acesso")
    public ResponseEntity<Map<String, String>> acesso(
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || token.isBlank()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensagem", "Você não fez login ou sua sessão expirou."));
        }

        if (!sessaoService.validar(token)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensagem", "Você não fez login ou sua sessão expirou."));
        }

        return ResponseEntity.ok(Map.of("mensagem", "Acesso garantido! Bem-vindo à área restrita. ✔"));
    }
}
