package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.UsuarioEstado;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, String>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByDocumentoIdAndEstado(Long documentoId, UsuarioEstado usuarioEstado);
    Mono<Boolean> existsByDocumentoId(Long documentoId);
}
