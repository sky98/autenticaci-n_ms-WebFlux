package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.CrearUsuarioDTO;
import co.com.pragma.api.dto.response.UsuarioDTO;
import co.com.pragma.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioDTOMapper {
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "documentoId", source = "documentoId")
    @Mapping(target = "estado", constant = "ACTIVO")
    @Mapping(target = "rolId", constant = "3L")
    Usuario toModel(CrearUsuarioDTO crearUsuarioDTO);
    UsuarioDTO toResponse(Usuario usuario);

}
