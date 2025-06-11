/**
 * Enumeració que representa el nombre de portes disponibles en un vehicle.
 * Cada opció està associada a un valor numèric que indica el nombre de portes.
 */
package adria.mayo.proyectopersonal.entity.enums.enumsVehiculo;

public enum Portes {

    /**
     * Vehicle amb tres portes.
     */
    TRES(3),

    /**
     * Vehicle amb cinc portes.
     */
    CINC(5);

    /**
     * Valor numèric associat al nombre de portes.
     */
    private final int valor;

    /**
     * Constructor de l'enumeració.
     *
     * @param valor el valor numèric associat al nombre de portes.
     */
    Portes(int valor) {
        this.valor = valor;
    }

    /**
     * Retorna el valor numèric associat al nombre de portes.
     *
     * @return el valor numèric.
     */
    public int getValor() {
        return valor;
    }
}
