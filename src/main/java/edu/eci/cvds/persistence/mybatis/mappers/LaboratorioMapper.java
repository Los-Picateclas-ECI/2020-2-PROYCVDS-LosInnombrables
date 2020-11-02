package edu.eci.cvds.persistence.mybatis.mappers;

import edu.eci.cvds.entities.Laboratorio;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaboratorioMapper {

    List<Laboratorio> consultarLaboratorios();

    Laboratorio consultarLaboratorioNombre(@Param("nombre") String nombre);
}
