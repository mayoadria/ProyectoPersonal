package adria.mayo.proyectopersonal.UsuarisTest;

import adria.mayo.proyectopersonal.Config.RateLimit;
import adria.mayo.proyectopersonal.entity.Token;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.EnviarCorreo;
import adria.mayo.proyectopersonal.service.TokenService;
import adria.mayo.proyectopersonal.service.UsuariService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import java.time.Duration;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestWebMVCPerfilUsuari {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuariService usuariService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private EnviarCorreo enviarCorreo;

    @MockBean
    private RateLimit rateLimit;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testMostrarPerfil() throws Exception {
        Usuari usuari = new Usuari();
        usuari.setNomUsuari("testUser");
        usuari.setEmail("testUser@example.com");
        usuari.setDni("12345678A");

        // Simula que el servicio devuelve el usuario
        when(usuariService.findBynomUsuari("testUser")).thenReturn(usuari);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuari, null, usuari.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil/mostrarPerfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("Perfil"))
                .andExpect(model().attribute("cliente", usuari))
                .andExpect(model().attribute("pais", Pais.values()))
                .andExpect(model().attribute("nomUsuari", "testUser"))
                .andExpect(model().attribute("isLogged", true));
    }


    @Test
    public void testEditarUsuario_correctamenteSinImagenNiCambioNombre() throws Exception {
        Usuari usuariMock = new Usuari();
        usuariMock.setNomUsuari("testUser");
        usuariMock.setDni("12345678A");
        usuariMock.setEmail("testUser@example.com");

        // Usuario logueado
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuariMock, null, usuariMock.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(usuariService.findBynomUsuari("testUser")).thenReturn(usuariMock);

        mockMvc.perform(MockMvcRequestBuilders.post("/perfil/editar")
                        .param("nom", "Test")
                        .param("cognoms", "Usuario")
                        .param("direccio", "Calle 1")
                        .param("codiPostal", "08001")
                        .param("numContacte", "665582953")
                        .param("poblacio", "Barcelona")
                        .param("email", "testUser@example.com")
                        .param("dni", "49828550Q")// sin cambio
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED).with(csrf()))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil/mostrarPerfil"));

        verify(usuariService).actualizarUsuari(any(Usuari.class));
    }

    @Test
    public void testEnviarContra_conExito() throws Exception {
        Usuari usuariMock = new Usuari();
        usuariMock.setNomUsuari("testUser");
        usuariMock.setEmail("mayoadria@gmail.com");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuariMock, null, usuariMock.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Bucket bucketMock = Bucket4j.builder().addLimit(Bandwidth.simple(10, Duration.ofMinutes(1))).build();
        when(usuariService.findBynomUsuari("testUser")).thenReturn(usuariMock);
        when(rateLimit.resolveBucket("testUser")).thenReturn(bucketMock);

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil/changeContra"))
                .andExpect(status().isOk())
                .andExpect(view().name("canviarContra"));

        verify(enviarCorreo).enviarCorreo(eq("mayoadria@gmail.com"), anyString());
        verify(tokenService).saveToken(any(Token.class));
    }

    @Test
    public void testNewContra_tokenValidoYPasswordsCoinciden() throws Exception {
        Usuari usuariMock = new Usuari();
        usuariMock.setNomUsuari("testUser");
        usuariMock.setDni("49828550Q");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuariMock, null, usuariMock.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Token tokenMock = new Token("12345678", usuariMock);
        when(usuariService.findBynomUsuari("testUser")).thenReturn(usuariMock);
        when(tokenService.getByToken("12345678")).thenReturn(Optional.of(tokenMock));
        when(tokenService.isExpired(tokenMock)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/perfil/procesar-token")
                        .param("token", "12345678")
                        .param("password", "nueva123")
                        .param("confirmPassword", "nueva123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil/mostrarPerfil"));

        verify(usuariService).actualizarUsuari(usuariMock);
        //verify(tokenService).deleteToken(tokenMock);
    }
}
