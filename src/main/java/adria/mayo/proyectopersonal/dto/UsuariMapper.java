package adria.mayo.proyectopersonal.dto;

import adria.mayo.proyectopersonal.entity.Usuari;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuariMapper {

    UsuariMapper INSTANCE = Mappers.getMapper(UsuariMapper.class);

    UsuariRespuestaDTO usuariToUsuariRespuestaDTO(Usuari usuari);
}
