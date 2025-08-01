package adria.mayo.proyectopersonal.UsuarisTest;

import adria.mayo.proyectopersonal.controller.adminUsuaris;
import adria.mayo.proyectopersonal.dto.UsuariRespuestaDTO;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.UsuariService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestWebMVCUsuaris {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuariService usuariService;

    @Test
    public void testListaUsu() throws Exception{
        when(usuariService.buscarUsuarisAvançat(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(),
                any(Pageable.class)))
                .thenReturn(DataUsuaris.listaUsuariPage());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin/listaUsu")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("ListaUsu"))
                .andExpect(model().attributeExists("usu","page","estats","pais"))
                .andReturn();

        ModelMap modelMap = mvcResult.getModelAndView().getModelMap();

        @SuppressWarnings("unchecked")
        List<UsuariRespuestaDTO> usuList = (List<UsuariRespuestaDTO>) modelMap.get("usu");

        // Obtener usuario original
        Usuari usuariOriginal = DataUsuaris.listaUsuariPage().getContent().get(0);

        // Validar que se mapeó correctamente
        assertEquals(1, usuList.size());
        UsuariRespuestaDTO dto = usuList.get(0);

        assertEquals(usuariOriginal.getDni(), dto.getDni());
        assertEquals(usuariOriginal.getNom(), dto.getNom());
        assertEquals(usuariOriginal.getCognoms(), dto.getCognoms());
        assertEquals(usuariOriginal.getEmail(), dto.getEmail());
        assertEquals(usuariOriginal.getNomUsuari(), dto.getNomUsuari());
        assertEquals(usuariOriginal.getEstat(), dto.getEstat());
        assertEquals(usuariOriginal.getPais(), dto.getPais());
        assertEquals(usuariOriginal.getNumContacte(), dto.getNumContacte());
        assertEquals(usuariOriginal.getCodiPostal(), dto.getCodiPostal());
        assertEquals(usuariOriginal.getDireccio(), dto.getDireccio());
        assertEquals(usuariOriginal.getPoblacio(), dto.getPoblacio());
        assertEquals(usuariOriginal.getRol(), dto.getRol());
    }

    @Test
    public void adminDashboard() throws Exception {
        Usuari usuariMock = new Usuari();
        usuariMock.setNomUsuari("admin");
        usuariMock.setRol(Rol.ADMINISTRADOR);

        try (MockedStatic<UserUtils> userUtilsMock = mockStatic(UserUtils.class)) {
            userUtilsMock.when(() -> UserUtils.getUsuariDetalls(any(Model.class))).thenAnswer(invocation -> {
                Model model = invocation.getArgument(0);
                // También simular que añade los atributos al modelo
                model.addAttribute("nomUsuari", usuariMock.getNomUsuari());
                model.addAttribute("isLogged", true);
                model.addAttribute("isAdmin", true);
                model.addAttribute("isAgent", false);
                model.addAttribute("isClient", false);
                return usuariMock;
            });

            mockMvc.perform(MockMvcRequestBuilders.get("/admin/adminDashboard"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("adminDashboard"))
                    .andExpect(MockMvcResultMatchers.model().attributeExists("isAdmin"))
                    .andExpect(MockMvcResultMatchers.model().attribute("isAdmin", true));
        }
    }


    @Test
    public void eliminarUsuari() throws Exception {
        Usuari usuari = DataUsuaris.crearUsuari();

        doNothing().when(usuariService).eliminarUsuari(usuari.getNomUsuari());

        mockMvc.perform(post("/admin/eliminar/{nomUsuari}", usuari.getNomUsuari())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection()) // redirección esperada
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/listaUsu"));

        // Verificar que se llamó al servicio con el parámetro correcto
        verify(usuariService, times(1)).eliminarUsuari(usuari.getNomUsuari());

    }

    @Test
    public void activarUsuari() throws Exception {
        Usuari usuari = DataUsuaris.crearUsuari();

        doNothing().when(usuariService).activarUsuari(usuari.getNomUsuari());

        mockMvc.perform(post("/admin/activar/{nomUsuari}", usuari.getNomUsuari())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection()) // redirección esperada
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/listaUsu"));

        verify(usuariService, times(1)).activarUsuari(usuari.getNomUsuari());

    }

    @Test
    public void crearUsuariForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/creaUsuariAdmin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("CrearUsuari"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("usu"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pais"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("rol"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("estat"))
                .andExpect(MockMvcResultMatchers.model().attribute("isEdit", false));
    }


    @Test
    public void crearUsuariPost() throws Exception {
        Usuari usuari = DataUsuaris.crearUsuari();

        when(usuariService.findByEmailOptional("pepe@gmail.com"))
                .thenReturn(Optional.empty());

        when(usuariService.findByDniOptional("49828550Q"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/newUsuari")
                        .with(csrf())
                        .flashAttr("usu", usuari))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/listaUsu"));

        Mockito.verify(usuariService).crearUsuari(usuari, usuari.getRol(), usuari.getEstat());
    }

    @Test
    public void crearUsuarisAdmin_conErroresDeValidacion() throws Exception {
        mockMvc.perform(post("/admin/newUsuari")
                        .param("email", "") // campo vacío para provocar error de validación
                        .param("dni", "")
                        .with(csrf())
                        .flashAttr("usu", new Usuari())) // puede usar uno vacío si hay validaciones en la clase
                .andExpect(status().isOk())
                .andExpect(view().name("CrearUsuari"))
                .andExpect(model().attributeHasFieldErrors("usu", "email", "dni"));
    }

    @Test
    public void crearUsuarisAdmin_conEmailDuplicado() throws Exception {
        Usuari usu = new Usuari();
        usu.setEmail("test");
        usu.setDni("12345678X");

        Mockito.when(usuariService.findByEmailOptional("test"))
                .thenReturn(Optional.of(new Usuari()));

        mockMvc.perform(post("/admin/newUsuari")
                        .with(csrf())
                        .flashAttr("usu", usu))
                .andExpect(status().isOk())
                .andExpect(view().name("CrearUsuari"))
                .andExpect(model().attributeHasFieldErrors("usu", "email"));
    }

    @Test
    public void crearUsuarisAdmin_conDniDuplicado() throws Exception {
        Usuari usu = new Usuari();
        usu.setEmail("nuevo@example.com");
        usu.setDni("87654321Z");

        Mockito.when(usuariService.findByEmailOptional("nuevo@example.com"))
                .thenReturn(Optional.empty());
        Mockito.when(usuariService.findByDniOptional("87654321Z"))
                .thenReturn(Optional.of(new Usuari()));

        mockMvc.perform(post("/admin/newUsuari")
                        .with(csrf())
                        .flashAttr("usu", usu))
                .andExpect(status().isOk())
                .andExpect(view().name("CrearUsuari"))
                .andExpect(model().attributeHasFieldErrors("usu", "dni"));
    }

    @Test
    public void actualizarUsuariForm() throws Exception {
        Usuari usuari = DataUsuaris.crearUsuari();
        when(usuariService.findBynomUsuari(usuari.getNomUsuari())).thenReturn(usuari);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/editarUsuari/{nomUsuari}", usuari.getNomUsuari()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("CrearUsuari"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("usu"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pais"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("rol"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("estat"))
                .andExpect(MockMvcResultMatchers.model().attribute("isEdit", true));
    }

    @Test
    public void actualizarUsuariPost() throws Exception {
        Usuari usuari = DataUsuaris.crearUsuari();

        when(usuariService.findByEmailOptional(usuari.getNomUsuari()))
                .thenReturn(Optional.empty());

        usuari.setNom("luis");
        usuari.setCodiPostal("99999");

        mockMvc.perform(post("/admin/editarUsuari")
                        .with(csrf())
                        .flashAttr("usu", usuari))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/listaUsu"));

        Mockito.verify(usuariService).actualizarUsuari(usuari);
        assertEquals("luis", usuari.getNom());
        assertEquals("99999", usuari.getCodiPostal());
    }

    @Test
    public void actualizarUsuarisAdmin_conErroresDeValidacion() throws Exception {
        mockMvc.perform(post("/admin/editarUsuari")
                        .param("email", "") // campo vacío para provocar error de validación
                        .with(csrf())
                        .flashAttr("usu", new Usuari())) // puede usar uno vacío si hay validaciones en la clase
                .andExpect(status().isOk())
                .andExpect(view().name("CrearUsuari"))
                .andExpect(model().attributeHasFieldErrors("usu", "email"));
    }





}
