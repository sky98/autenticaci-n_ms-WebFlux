package co.com.pragma.api.security;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.GestorCredencialesPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GestorCredencialesAdapter implements GestorCredencialesPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario encryptarContrasena(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuario;
    }
}
