package com.bolsadeideas.spbackapirest.dao;

import com.bolsadeideas.spbackapirest.models.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bolsadeideas.spbackapirest.models.entity.Cliente;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

    @Query("from Region")   // Consulta personalizada para seleccionar todas las regiones.
    public List<Region> findAllRegions();

}
