package com.bolsadeideas.spbackapirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bolsadeideas.spbackapirest.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

}
