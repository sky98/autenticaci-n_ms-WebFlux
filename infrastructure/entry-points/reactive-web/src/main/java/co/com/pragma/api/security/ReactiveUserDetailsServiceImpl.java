package co.com.pragma.api.security;

import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return usuarioRepository.obtenerPorCorreo(username)
                .map(usuario -> User.withUsername(usuario.getCorreoElectronico())
                        .password(usuario.getContrasena())
                        .roles(String.valueOf(usuario.getRolId()))
                        .build())
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuario no encontrado :"+ username)))
                ;
    }

}
