/**
 * Enumeració que representa el nombre de places disponibles en un vehicle.
 * Cada opció està associada a un valor numèric que indica el nombre de places.
 */
package adria.mayo.proyectopersonal.entity.enums.enumsVehiculo;

public enum Places {

    /**
     * Vehicle amb dues places.
     */
    DOS(2),

    /**
     * Vehicle amb tres places.
     */
    TRES(3),

    /**
     * Vehicle amb quatre places.
     */
    QUATRE(4),

    /**
     * Vehicle amb cinc places.
     */
    CINC(5),

    /**
     * Vehicle amb set places.
     */
    SET(7);

    /**
     * Valor numèric associat al nombre de places.
     */
    private final int valor;

    /**
     * Constructor de l'enumeració.
     *
     * @param valor el valor numèric associat al nombre de places.
     */
    Places(int valor) {
        this.valor = valor;
    }

    /**
     * Retorna el valor numèric associat al nombre de places.
     *
     * @return el valor numèric.
     */
    public int getValor() {
        return valor;
    }
}
