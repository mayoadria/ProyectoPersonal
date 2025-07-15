package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuari, String> {
    Usuari findBynomUsuari(String nomUsuari);
    Usuari findByEmail(String email);

    @Query("SELECT u FROM Usuari u " +
            "WHERE (:dni IS NULL OR LOWER(u.dni) LIKE LOWER(CONCAT('%', :dni, '%'))) " +
            "AND (:nom IS NULL OR LOWER(u.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) " +
            "AND (:cognom IS NULL OR LOWER(u.cognoms) LIKE LOWER(CONCAT('%', :cognom, '%'))) " +
            "AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:nomUsuari IS NULL OR LOWER(u.nomUsuari) LIKE LOWER(CONCAT('%', :nomUsuari, '%'))) " +
            "AND (:telf IS NULL OR u.numContacte LIKE CONCAT('%', :telf, '%')) " +
            "AND (:codiPostal IS NULL OR u.codiPostal LIKE CONCAT('%', :codiPostal, '%')) " +
            "AND (:direccio IS NULL OR LOWER(u.direccio) LIKE LOWER(CONCAT('%', :direccio, '%'))) " +
            "AND (:poblacio IS NULL OR LOWER(u.poblacio) LIKE LOWER(CONCAT('%', :poblacio, '%'))) " +
            "AND (:estat IS NULL OR u.estat = :estat) " +
            "AND (:pais IS NULL OR u.pais = :pais)")
    List<Usuari> buscarUsuarisAvançat(
            @Param("dni") String dni,
            @Param("nom") String nom,
            @Param("cognom") String cognom,
            @Param("email") String email,
            @Param("nomUsuari") String nomUsuari,
            @Param("telf") String telf,
            @Param("codiPostal") String codiPostal,
            @Param("direccio") String direccio,
            @Param("poblacio") String poblacio,
            @Param("estat") EstatUsuari estat,
            @Param("pais") Pais pais
    );
}
