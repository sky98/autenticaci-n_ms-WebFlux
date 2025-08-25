package co.com.pragma.model.usuario.errores;

import java.util.Set;

public class ErrorDominio extends RuntimeException {

    private Set<String> campos;
    public ErrorDominio(String mensaje, Set<String> campos) {
        super(mensaje);
        this.campos = campos;
    }
    public Set<String> getCampos() {
        return campos;
    }

}
