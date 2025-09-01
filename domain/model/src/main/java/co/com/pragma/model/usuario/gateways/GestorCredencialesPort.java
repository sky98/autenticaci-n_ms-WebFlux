package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;

public interface GestorCredencialesPort {
    Usuario encryptarContrasena(Usuario usuario);
}
