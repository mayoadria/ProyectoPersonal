package adria.mayo.proyectopersonal.UsuarisTest;

import adria.mayo.proyectopersonal.Excepciones.Usuari.AdminException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        testUsuari.setRol(Rol.CLIENTE);
        testUsuari.setContrasenya("123456");

    }

    @Test
    public void testCrearUsuari(){

        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Optional<Usuari> result = userService.findByEmailOptional("adria@test.com");
        assertTrue(result.isPresent());
        assertEquals("adria", result.get().getNomUsuari());
    }

    @Test
    public void testCrearUsuariEmailDuplicado() {
        // 1. Creamos el primer usuario (original)
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        // 2. Creamos un segundo usuario con el mismo email
        Usuari usuarioDuplicado = DataUsuaris.crearUsuari();
        usuarioDuplicado.setEmail("adria@test.com");

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
        //System.out.println(user);
    }

    @Test
    public void findUsuariByDni() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari user = userService.findByDni(testUsuari.getDni());
        assertNotNull(user);
        assertEquals("adria", user.getNomUsuari());

    }
    @Test
    public void findUsuariByDniNotFound() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari usuari = new Usuari();
        usuari.setDni("121212");
        assertThrows(EncontrarUsuarioException.class, () -> userService.findByDni(usuari.getDni()));
    }

    @Test
    public void findUsuariByEmailOptional() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Optional<Usuari> user = userService.findByEmailOptional(testUsuari.getEmail());
        assertTrue(user.isPresent());
        assertEquals("adria", user.get().getNomUsuari());

    }

    @Test
    public void findUsuariByEmail() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari user = userService.findByEmail(testUsuari.getEmail());
        assertNotNull(user);
        assertEquals("adria", user.getNomUsuari());

    }
    @Test
    public void findUsuariByEmailNotFound() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari usuari = new Usuari();
        usuari.setEmail("121212@asas.com");
        assertThrows(EncontrarUsuarioException.class, () -> userService.findByEmail(usuari.getEmail()));
    }


    @Test
    public void findByNomUsuari() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari user = userService.findBynomUsuari(testUsuari.getNomUsuari());
        assertNotNull(user);
        assertEquals("adria", user.getNomUsuari());

    }

    @Test
    public void findUsuariByNomUsuariNotFound() {
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Usuari usuari = new Usuari();
        usuari.setNomUsuari("121212");
        assertThrows(EncontrarUsuarioException.class, () -> userService.findBynomUsuari(usuari.getNomUsuari()));
    }


    @Test
    public void testEliminarUsuari() {
        // Crear usuario
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        // Verificar que el usuario fue creado
        Usuari user = userService.findBynomUsuari(testUsuari.getNomUsuari());
        assertNotNull(user, "El usuario debería existir después de crearlo");

        // Eliminar usuario
        userService.eliminarUsuari(user.getNomUsuari());

    }


    @Test
    public void testEliminarUsuariNotFound() {
        Usuari usuari = new Usuari();
        usuari.setNomUsuari("121212");

        assertThrows(EncontrarUsuarioException.class, () -> userService.eliminarUsuari(usuari.getNomUsuari()));
    }

    @Test
    public void eliminarUsuariAdmin(){
        Usuari usuari = testUsuari;
        usuari.setRol(Rol.ADMINISTRADOR);
        userService.crearUsuari(usuari, usuari.getRol(), usuari.getEstat());

        assertThrows(AdminException.class,()-> userService.eliminarUsuari(usuari.getNomUsuari()));
    }

    @Test
    public void findAll(){
        Usuari usuari = DataUsuaris.crearUsuari();

        userService.crearUsuari(usuari, usuari.getRol(), usuari.getEstat());
        userService.crearUsuari(testUsuari,testUsuari.getRol(), testUsuari.getEstat());

        userService.findAll();

        assertEquals(2, userService.findAll().size());
        assertEquals("pepe@gmail.com", userService.findAll().get(0).getEmail());
        assertEquals("adria@test.com", userService.findAll().get(1).getEmail());
    }


    @Test
    public void findAllEmpty(){
        assertThrows(EncontrarUsuarioException.class, () -> userService.findAll());
    }

    @Test
    public void actualizarUsuari(){
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        testUsuari.setNomUsuari("pepe");
        testUsuari.setCognoms("pedro");
        userService.actualizarUsuari(testUsuari);

        assertEquals("pepe", testUsuari.getNomUsuari());
        assertEquals("pedro", testUsuari.getCognoms());
    }


    @Test
    public void actualizarUsuariNotFound(){
        Usuari usuari = new Usuari();
        usuari.setDni("49828550Q");

        assertThrows(EncontrarUsuarioException.class, () -> userService.actualizarUsuari(usuari));
    }

    @Test
    public void activarUsuari(){
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());


        userService.activarUsuari(testUsuari.getNomUsuari());
        Usuari usuari = userService.findBynomUsuari(testUsuari.getNomUsuari());
        assertEquals(EstatUsuari.INACTIVO, usuari.getEstat());
    }

    @Test
    public void desactivarUsuari(){
        testUsuari.setEstat(EstatUsuari.INACTIVO);
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());


        userService.activarUsuari(testUsuari.getNomUsuari());
        Usuari usuari = userService.findBynomUsuari(testUsuari.getNomUsuari());
        assertEquals(EstatUsuari.ACTIVO, usuari.getEstat());
    }

    @Test
    public void desactivarUsuariAdmin(){
        testUsuari.setRol(Rol.ADMINISTRADOR);
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        assertThrows(AdminException.class,()-> userService.activarUsuari(testUsuari.getNomUsuari()));
    }

    @Test
    public void testPage(){
        userService.crearUsuari(testUsuari, testUsuari.getRol(), testUsuari.getEstat());

        Pageable pageable = PageRequest.of(0, 10);

        Page<Usuari> resultado = userService.buscarUsuarisAvançat(
                null,              // dni
                "Adria",            // nom
                null,              // cognom
                null,              // email
                null,              // nomUsuari
                null,              // telf
                null,              // codiPostal
                null,              // direccio
                null,              // poblacio
                EstatUsuari.ACTIVO, // estat
                null,              // pais
                pageable
        );
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "La búsqueda debería retornar al menos un usuario");
        assertEquals("Adria", resultado.getContent().get(0).getNom());
    }







}
