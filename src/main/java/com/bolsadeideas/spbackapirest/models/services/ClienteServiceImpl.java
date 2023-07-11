package com.bolsadeideas.spbackapirest.models.services;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bolsadeideas.spbackapirest.dao.IClienteDao;
import com.bolsadeideas.spbackapirest.models.entity.Cliente;

@Service	//Decoramos y marcamos la clase como un componente de servicio en Spring
public class ClienteServiceImpl implements IClienteService{
	
	@Autowired		//Inyeccion de dependencias automaticamente en un componente.
	private IClienteDao clienteDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll(){				
		return clienteDao.findAll();
	}
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {	//Con este metodo definimos la cantidad de elementos por pagina
		return clienteDao.findAll(pageable);
	}
	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);	//Si encuentra, retorna el objeto, si no, retorna un null
	}
	@Override
	public Cliente save(Cliente cliente) {
		// TODO Auto-generated method stub
		return clienteDao.save(cliente);
	}
	@Override
	public void delete(Long id) {
		clienteDao.deleteById(id);;

	}
}



/*
 * @Transactional
 * Marcar un método o una clase que realiza operaciones de base de datos transaccionales. 
 * Una transacción es un conjunto de operaciones que se deben realizar de manera atómica (es decir, todas o ninguna)
 * y en forma aislada del resto de las operaciones realizadas en la base de datos.
 * 
 * Spring intercepta las llamadas a ese método o clase y crea una transacción antes de que se realice la operación. 
 * Si la operación se realiza correctamente, se confirma la transacción; si hay algún error, la transacción se anula. 
 * La anulación de la transacción asegura que todas las operaciones realizadas hasta ese momento se reviertan y se vuelvan 
 * a su estado original.
 *
 *
 * Estructura y por que del ClienteServiceImpl:
 * Se debe tener en cuenta, que por ahora (7/6/23) hay 1 clases y 2 interfaces que causan cierta confusion.
 * Por un lado esta la IClienteDao que es muy importante porque hereda directamente de CRUDRepository, que es la clase
 * de la cual extraemos los metodos ya hechos para ahorrarnos trabajo.
 *
 * Por otro lado IClienteService no le rinde cuentas a nadie y simplemente funciona como plantilla donde agregamos
 * los metodos que requiramos para la API. Esta al ser una plantilla, es la que implemeta la clase ClienteServiceImpl
 * que ya propiamente es la que sobreescribe los metodos de su plantilla o interfaz. Para sobreescribirlos se apoya de los
 * metodos ya hechos inyectando un objeto IClienteDao que a su vez permite llamar a los de CRUDRepository.
 *
 * En resumen. IClienteService plantilla de IClienteServiceImpl.
 * IClienteDao implementa CRUDRepository (Los metodos ya hechos)
 * IClienteServiceImpl usa CRUDRepository(Los metodos ya hechos) para hacer funcionar los metodos dictados por su plantilla IClienteService
 *
 * IClienteService <- IClienteServiceImpl(CRUDRepository <- IClienteDao)
 * IClienteService <da la plantilla a- IClienteServiceImpl (se apoya de) (CRUDRepository <permite acceder a sus metodos a- IClienteDao)
 * */
