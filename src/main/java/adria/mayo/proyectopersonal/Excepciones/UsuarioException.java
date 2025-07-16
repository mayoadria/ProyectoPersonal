package adria.mayo.proyectopersonal.Excepciones;

public abstract class UsuarioException extends RuntimeException {
    public UsuarioException(String mensaje) {
        super(mensaje);
    }
}


