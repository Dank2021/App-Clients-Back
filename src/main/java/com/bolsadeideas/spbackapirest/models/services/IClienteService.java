package com.bolsadeideas.spbackapirest.models.services;


import java.util.List;
import com.bolsadeideas.spbackapirest.models.entity.Cliente;
import com.bolsadeideas.spbackapirest.models.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClienteService {

	List<Cliente> findAll();
	Page<Cliente> findAllPage(Pageable pageable);

	Cliente findById(Long id);

	Cliente save(Cliente cliente);

	void delete(Long id);

	public List<Region> findAllRegions();
		
}
