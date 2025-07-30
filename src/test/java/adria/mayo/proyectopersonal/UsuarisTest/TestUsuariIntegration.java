package adria.mayo.proyectopersonal.UsuarisTest;

import adria.mayo.proyectopersonal.Excepciones.Usuari.EncontrarUsuarioException;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.repository.UsuarioRepo;
import adria.mayo.proyectopersonal.service.UsuariService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TestUsuariIntegration {

    @Autowired
    private UsuariService userService;

    @Autowired
    private UsuarioRepo userRepository;


    private Usuari testUsuari;
    @Autowired
    private UsuariService usuariService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // Asegura que cada test comienza limpio
        testUsuari = new Usuari();
        testUsuari.setDni("12345678Z");
        testUsuari.setNom("Adria");
        testUsuari.setCognoms("Mayo");
        testUsuari.setEmail("adria@test.com");
        //testUsuari.setNomUsuari("adria");
        testUsuari.setEstat(EstatUsuari.ACTIVO);
        testUsuari.setPais(Pais.ESPANYA);
        testUsuari.setNumContacte("600000000");
        testUsuari.setCodiPostal("08001");
        testUsuari.setDireccio("Carrer Fictici 1");
        testUsuari.setPoblacio("Barcelona");
        testUsuari.setRol(Rol.ADMINISTRADOR);
        testUsuari.setContrasenya("123456");

    }

    @Test
    public void testCrearUsuari(){

        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Optional<Usuari> result = userService.findByEmailOptional("adria@test.com");
        assertTrue(result.isPresent());
        assertEquals("adria", result.get().getNomUsuari());
        System.out.println(result);
    }

    @Test
    public void testCrearUsuariEmailDuplicado() {
        // 1. Creamos el primer usuario (original)
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        // 2. Creamos un segundo usuario con el mismo email
        Usuari usuarioDuplicado = new Usuari();
        usuarioDuplicado.setDni("87654321X");
        usuarioDuplicado.setNom("Otro");
        usuarioDuplicado.setCognoms("Usuario");
        usuarioDuplicado.setEmail("adria@test.com"); // mismo email
        usuarioDuplicado.setNomUsuari("otroUsuario");
        usuarioDuplicado.setEstat(EstatUsuari.ACTIVO);
        usuarioDuplicado.setPais(Pais.ESPANYA);
        usuarioDuplicado.setNumContacte("699999999");
        usuarioDuplicado.setCodiPostal("08002");
        usuarioDuplicado.setDireccio("Otra calle 123");
        usuarioDuplicado.setPoblacio("Barcelona");
        usuarioDuplicado.setRol(Rol.CLIENTE);
        usuarioDuplicado.setContrasenya("12345");

        // 3. Esperamos una excepción o error al crear el duplicado
        assertThrows(RuntimeException.class, () -> {
            userService.crearUsuari(usuarioDuplicado, usuarioDuplicado.getRol(), usuarioDuplicado.getEstat());
        });
    }

    @Test
    public void findUsuariByDniOptional() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Optional<Usuari> user = userService.findByDniOptional(testUsuari.getDni());
        assertTrue(user.isPresent());
        assertEquals("adria", user.get().getNomUsuari());
        System.out.println(user);
    }

    @Test
    public void findUsuariByDni() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari user = userService.findByDni(testUsuari.getDni());
        assertNotNull(user);
        assertEquals("adria", user.getNomUsuari());
        System.out.println(user);
    }
    @Test
    public void findUsuariByDniNotFound() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari usuari = new Usuari();
        usuari.setDni("121212");
        assertThrows(EncontrarUsuarioException.class, () -> usuariService.findByDni(usuari.getDni()));
    }

    @Test
    public void findUsuariByEmailOptional() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Optional<Usuari> user = userService.findByEmailOptional(testUsuari.getEmail());
        assertTrue(user.isPresent());
        assertEquals("adria", user.get().getNomUsuari());
        System.out.println(user);
    }

    @Test
    public void findUsuariByEmail() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari user = userService.findByEmail(testUsuari.getEmail());
        assertNotNull(user);
        assertEquals("adria", user.getNomUsuari());
        System.out.println(user);
    }
    @Test
    public void findUsuariByEmailNotFound() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari usuari = new Usuari();
        usuari.setEmail("121212@asas.com");
        assertThrows(EncontrarUsuarioException.class, () -> usuariService.findByEmail(usuari.getEmail()));
    }


    @Test
    public void findByNomUsuari() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari user = userService.findBynomUsuari(testUsuari.getNomUsuari());
        assertNotNull(user);
        assertEquals("adria", user.getNomUsuari());
        System.out.println(user);
    }

    @Test
    public void findUsuariByNomUsuariNotFound() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari usuari = new Usuari();
        usuari.setNomUsuari("121212");
        assertThrows(EncontrarUsuarioException.class, () -> usuariService.findBynomUsuari(usuari.getNomUsuari()));
    }




}
