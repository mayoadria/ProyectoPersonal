package adria.mayo.proyectopersonal.UsuarisTest;

import adria.mayo.proyectopersonal.Excepciones.Usuari.AdminException;
import adria.mayo.proyectopersonal.Excepciones.Usuari.EncontrarUsuarioException;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.repository.UsuarioRepo;
import adria.mayo.proyectopersonal.service.UsuariService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestUsuarisService {

    @Mock
    private UsuarioRepo usuarioRepo;

    @InjectMocks
    private UsuariService usuariService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void testFindAll() {
        when(usuarioRepo.findAll()).thenReturn(DataUsuaris.listaUsuari());
        this.usuariService.findAll();

        assertNotNull(DataUsuaris.listaUsuari());
        assertEquals(4, DataUsuaris.listaUsuari().size());
        verify(usuarioRepo, times(1)).findAll();
    }

    @Test
    public void crearUsuaris() {
        Usuari usuari = DataUsuaris.crearUsuari();

        when(usuarioRepo.save(usuari)).thenReturn(usuari);
        this.usuariService.crearUsuari(usuari,usuari.getRol(),usuari.getEstat());

        ArgumentCaptor<Usuari> captor = ArgumentCaptor.forClass(Usuari.class);
        verify(usuarioRepo).save(any(Usuari.class));
        verify(usuarioRepo, times(1)).save(captor.capture());
        assertNotNull(usuari);
        assertEquals("49828550Q",usuari.getDni());
        assertEquals(Pais.ESPANYA,usuari.getPais());
    }

    @Test
    public void findUsuarisByDni() {
        Usuari usuari = DataUsuaris.crearUsuari();

        when(usuarioRepo.findById(usuari.getDni())).thenReturn(Optional.of(usuari));
        this.usuariService.findByDni(usuari.getDni());

        assertNotNull(usuari);
        assertEquals("04444",usuari.getCodiPostal());
        verify(usuarioRepo, times(1)).findById(usuari.getDni());
    }
    @Test
    public void findUsuarisByNomUsuari() {
        Usuari usuari = DataUsuaris.crearUsuari();

        when(usuarioRepo.findBynomUsuari(usuari.getNomUsuari())).thenReturn(Optional.of(usuari));
        this.usuariService.findBynomUsuari(usuari.getNomUsuari());

        assertNotNull(usuari);
        assertEquals("pepe",usuari.getNomUsuari());
        assertEquals("04444",usuari.getCodiPostal());
        verify(usuarioRepo, times(1)).findBynomUsuari(usuari.getNomUsuari());
    }

    @Test
    public void findUsuarisByEmailUsuari() {
        Usuari usuari = DataUsuaris.crearUsuari();

        when(usuarioRepo.findByEmail(usuari.getEmail())).thenReturn(Optional.of(usuari));
        this.usuariService.findByEmail(usuari.getEmail());

        assertNotNull(usuari);
        assertEquals("pepe@gmail.com",usuari.getEmail());
        assertEquals("04444",usuari.getCodiPostal());
        verify(usuarioRepo, times(1)).findByEmail(usuari.getEmail());
    }


    @Test
    public void eliminarUsuaris() {
        Usuari usuari = DataUsuaris.crearUsuari();

        when(usuarioRepo.findBynomUsuari(usuari.getNomUsuari())).thenReturn(Optional.of(usuari));
        usuariService.crearUsuari(usuari,usuari.getRol(),usuari.getEstat());
        usuariService.eliminarUsuari(usuari.getNomUsuari());

        ArgumentCaptor<Usuari> captor = ArgumentCaptor.forClass(Usuari.class);
        verify(usuarioRepo).save(any(Usuari.class));
        verify(usuarioRepo, times(1)).save(captor.capture());

        ArgumentCaptor<Usuari> captorE = ArgumentCaptor.forClass(Usuari.class);
        verify(usuarioRepo, times(1)).findBynomUsuari(usuari.getNomUsuari());
        verify(usuarioRepo, times(1)).delete(captorE.capture());
    }


    @Test
    public void eliminarUsuariNotFound() {
        Usuari usuari = DataUsuaris.crearUsuari();
        when(usuarioRepo.findBynomUsuari(usuari.getNomUsuari())).thenReturn(Optional.empty());
        assertThrows(EncontrarUsuarioException.class, () -> usuariService.eliminarUsuari(usuari.getNomUsuari()));
    }
    @Test
    public void eliminarUsuariAdmin() {
        Usuari usuari = DataUsuaris.crearUsuari();
        usuari.setRol(Rol.ADMINISTRADOR);
        when(usuarioRepo.findBynomUsuari(usuari.getNomUsuari())).thenReturn(Optional.of(usuari));
        assertThrows(AdminException.class, () -> usuariService.eliminarUsuari(usuari.getNomUsuari()));
    }

    @Test
    public void actualizarUsuari() {
        Usuari usuari = DataUsuaris.crearUsuari();
        when(usuarioRepo.findById(usuari.getDni())).thenReturn(Optional.of(usuari));

        usuari.setRol(Rol.ADMINISTRADOR);
        usuari.setNom("pedro");

        usuariService.actualizarUsuari(usuari);

        ArgumentCaptor<Usuari> captor = ArgumentCaptor.forClass(Usuari.class);
        verify(usuarioRepo, times(1)).findById(usuari.getDni());
        verify(usuarioRepo, times(1)).save(captor.capture());
        assertEquals("pedro",captor.getValue().getNom());
        assertEquals(Rol.ADMINISTRADOR,captor.getValue().getRol());
    }

    @Test
    public void actualizarUsuarioError(){
        Usuari usuari = DataUsuaris.crearUsuari();
        when(usuarioRepo.findById(usuari.getDni())).thenReturn(Optional.empty());
        assertThrows(EncontrarUsuarioException.class, () -> usuariService.actualizarUsuari(usuari));
    }

    @Test
    public void activarUsuario(){
        Usuari usuari = DataUsuaris.crearUsuari();
        when(usuarioRepo.findBynomUsuari(usuari.getNomUsuari())).thenReturn(Optional.of(usuari));
        this.usuariService.activarUsuari(usuari.getNomUsuari());

        assertNotNull(usuari);
        assertEquals(EstatUsuari.INACTIVO,usuari.getEstat());
    }
    @Test
    public void desactivarUsuario(){
        Usuari usuari = DataUsuaris.crearUsuariInactivo();
        when(usuarioRepo.findBynomUsuari(usuari.getNomUsuari())).thenReturn(Optional.of(usuari));
        this.usuariService.activarUsuari(usuari.getNomUsuari());
        assertNotNull(usuari);
        assertEquals(EstatUsuari.ACTIVO,usuari.getEstat());
    }

    @Test
    public void desactivarUsuarioError(){
        Usuari usuari = DataUsuaris.crearUsuari();
        usuari.setRol(Rol.ADMINISTRADOR);
        when(usuarioRepo.findBynomUsuari(usuari.getNomUsuari())).thenReturn(Optional.of(usuari));
        assertThrows(AdminException.class, () -> this.usuariService.activarUsuari(usuari.getNomUsuari()));
    }


    @Test
    public void listaPage(){
        Pageable pageable = PageRequest.of(0, 10);

        when(usuarioRepo.buscarUsuarisAvançat(
                eq("12345678A"),
                eq("prueba1"),
                eq("prueba1C"),
                eq("pepe@"),
                eq("pepe"),
                eq("123456789"),
                eq("04444"),
                eq("prueba"),
                eq("prueba"),
                eq(EstatUsuari.ACTIVO),
                eq(Pais.ESPANYA),
                eq(pageable)
        )).thenReturn(DataUsuaris.listaUsuariPage());

        Page<Usuari> pageUsuari = usuariService.buscarUsuarisAvançat("12345678A","prueba1","prueba1C","pepe@",
        "pepe","123456789","04444","prueba","prueba",EstatUsuari.ACTIVO,Pais.ESPANYA,pageable
        );

        assertNotNull(pageUsuari);
        assertEquals(1,pageUsuari.getTotalElements());
        assertEquals("12345678A", pageUsuari.getContent().get(0).getDni());

        verify(usuarioRepo).buscarUsuarisAvançat(eq("12345678A"),
                eq("prueba1"),
                eq("prueba1C"),
                eq("pepe@"),
                eq("pepe"),
                eq("123456789"),
                eq("04444"),
                eq("prueba"),
                eq("prueba"),
                eq(EstatUsuari.ACTIVO),
                eq(Pais.ESPANYA),
                eq(pageable));
    }
}
