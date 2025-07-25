package adria.mayo.proyectopersonal.UsuarisTest;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class DataUsuaris {


    public static List<Usuari> listaUsuari() {
        return List.of(
                new Usuari("12345678A", "prueba1", "prueba1C", "123456789", "pepe@", "1234", "pepe", Pais.ESPANYA, "04444", "prueba", "prueba", Rol.CLIENTE, EstatUsuari.ACTIVO, null, null, null, null),
                new Usuari("12345678A", "prueba1", "prueba1C", "123456789", "pepe@", "1234", "pepe", Pais.ESPANYA, "04444", "prueba", "prueba", Rol.CLIENTE, EstatUsuari.ACTIVO, null, null, null, null),
                new Usuari("12345678A", "prueba1", "prueba1C", "123456789", "pepe@", "1234", "pepe", Pais.ESPANYA, "04444", "prueba", "prueba", Rol.CLIENTE, EstatUsuari.ACTIVO, null, null, null, null),
                new Usuari("12345678A", "prueba1", "prueba1C", "123456789", "pepe@", "1234", "pepe", Pais.ESPANYA, "04444", "prueba", "prueba", Rol.CLIENTE, EstatUsuari.ACTIVO, null, null, null, null)
        );
    }

    public static Usuari crearUsuari() {
        return new Usuari("12345678A", "prueba1", "prueba1C", "123456789", "pepe@",
                "1234", "pepe", Pais.ESPANYA, "04444", "prueba", "prueba",
                Rol.CLIENTE, EstatUsuari.ACTIVO, null, null, null, null);

    }

    public static Usuari crearUsuariInactivo() {
        return new Usuari("12345678A", "prueba1", "prueba1C", "123456789", "pepe@",
                "1234", "pepe", Pais.ESPANYA, "04444", "prueba", "prueba",
                Rol.CLIENTE, EstatUsuari.INACTIVO, null, null, null, null);

    }

    public static Page<Usuari> listaUsuariPage() {
        List<Usuari> listaUsuari = List.of(
                new Usuari("12345678A", "prueba1", "prueba1C", "123456789", "pepe@", "1234",
                        "pepe", Pais.ESPANYA, "04444", "prueba", "prueba", Rol.CLIENTE, EstatUsuari.ACTIVO,
                        null, null, null, null));
        Page<Usuari> page = new PageImpl<>(listaUsuari);
        return page;
    }
}
