package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        String,
        MyReactiveRepository
> implements UsuarioRepository {

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, UsuarioEntity -> mapper.map(UsuarioEntity, Usuario.class));
    }

    @Override
    public Mono<Usuario> guardar(Usuario usuario) {
        return super.save(usuario)
                .doOnNext(modelo -> log.info("Usuario registrado con exito : {}", modelo));
    }

    @Override
    public Mono<Boolean> existeCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronico(correoElectronico)
                .doOnNext(existe -> log.info("Existe correo : {} en el sistema : {}", correoElectronico, existe));
    }


}
