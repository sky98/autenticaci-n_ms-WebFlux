package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.UsuarioEstado;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.model.usuario.errores.ErrorPersistencia;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        String,
        UsuarioReactiveRepository
> implements UsuarioRepository {

    private final TransactionalOperator transactionalOperator;

    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, UsuarioEntity -> mapper.map(UsuarioEntity, Usuario.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Usuario> guardar(Usuario usuario) {
        return transactionalOperator.execute(
                status -> super.save(usuario)
                ).singleOrEmpty()
                .onErrorResume(e ->
                        Mono.error(new ErrorPersistencia("Ocurrio un error al guardar usuario ", Set.of(e.getMessage())))
                );
    }

    @Override
    public Mono<Boolean> existeCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronico(correoElectronico)
                .doOnNext(existe -> log.info("Existe correo : {} en el sistema : {}", correoElectronico, existe));
    }

    @Override
    public Mono<Boolean> existeUsuarioPorDocumentoActivo(Long documentoId) {
        return repository.existsByDocumentoIdAndEstado(documentoId, UsuarioEstado.ACTIVO)
                .doOnNext(resp ->log.info("Existe y esta activo usuario con documentoId : {} : {}", documentoId, resp))
                .onErrorResume(e -> {
                    log.error("Error al consultar usuario activo por documento , error : {}", e.getMessage());
                    return Mono.error(new ErrorPersistencia("Error al consultar en la tabla usuarios", Set.of("usuario:existeUsuarioPorDocumentoActivo")));
                });
    }

    @Override
    public Mono<Boolean> existeUsuarioPorDocumento(Long documentoId) {
        return repository.existsByDocumentoId(documentoId)
                .doOnNext(resp -> log.info("Existe usuario con documentoId {} : {}", documentoId, resp));
    }


}
