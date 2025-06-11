/**
 * Enumeració que representa el nombre de marxes disponibles per a un vehicle.
 * Cada opció està associada a un valor numèric que indica el nombre de marxes.
 */
package adria.mayo.proyectopersonal.entity.enums.enumsVehiculo;

public enum Marxes {

    /**
     * Vehicle amb cinc marxes.
     */
    CINC(5),

    /**
     * Vehicle amb sis marxes.
     */
    SIS(6);

    /**
     * Valor numèric associat al nombre de marxes.
     */
    private final int valor;

    /**
     * Constructor de l'enumeració.
     *
     * @param valor el valor numèric associat al nombre de marxes.
     */
    Marxes(int valor) {
        this.valor = valor;
    }

    /**
     * Retorna el valor numèric associat al nombre de marxes.
     *
     * @return el valor numèric.
     */
    public int getValor() {
        return valor;
    }
}
