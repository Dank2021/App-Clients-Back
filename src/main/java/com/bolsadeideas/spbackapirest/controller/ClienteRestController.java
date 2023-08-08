package com.bolsadeideas.spbackapirest.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.bolsadeideas.spbackapirest.models.entity.Cliente;
import com.bolsadeideas.spbackapirest.models.services.ClienteServiceImpl;
import org.springframework.web.multipart.MultipartFile;

//Se utiliza para habilitar el acceso a recursos de un servidor web desde dominios distintos al dominio del servidor.
// En este caso, se le pasa la IP del servidor de Angular permitiendole a dicho dominio, enviar y recibir datos.
@CrossOrigin(origins = {"http://localhost:4200/"})
//Anotación utilizada para definir un controlador REST que maneja solicitudes HTTP
@RestController
//Anotación utilizada para mapear una solicitud HTTP a un controlador o método de servicio.
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private ClienteServiceImpl clienteService;

    private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/clientes/page/{page}")    //En point que pagina los elementos del arreglo, y devuelve los de la pagina solicitada como parametro
    public Page<Cliente> getPaginacion(@PathVariable Integer page) {        //Pedimos el numero de la pagina deseadaa como parametro.
        return clienteService.findAll(PageRequest.of(page, 4)); //Devolvemos un tipo Page de 4 elementos por pagina.
    }
    @GetMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.OK)  //Es el estado por defecto, cuando una peticion sale exitosa, se le asigna un 200 automaticamente.
    public ResponseEntity<?> indexId(@PathVariable Long id) {
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();
        try {
            cliente = clienteService.findById(id);
        }catch (DataAccessException e ){ //Si el JSON del cliente a editar incumple con alguna normas de dicho objeto en la BDD
            response.put("mensaje: ", "Error al realizar la consulta en la base de datos!");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (cliente == null) { //Si el cliente con el Id dado no existe
            response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PostMapping("clientes")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {    //@Valid: Para que lo escrito en el entity se valide.
        Cliente clienteNew;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            /*Forma vieja de manejar errores.
            List<String> errors = new ArrayList<>();
            for (FieldError err:
                 result.getFieldErrors()) {
                errors.add("El campo \'"+err.getField()+"\' "+err.getDefaultMessage());
            }Forma con Streams de manejar errores:*/

//Toma una lista de errores de validación de campos (FieldError) y crea una lista de cadenas de texto que describen cada error.
//El resultado final se almacena en la variable 'errors', que es una lista de cadenas (List<String>).
        List<String> errors = result.getFieldErrors() // Obtiene la lista de errores de validación de campos del objeto 'result'
            .stream() // Convierte la lista en un flujo de elementos para facilitar su manipulación.
            .map(err -> "El campo \'" + err.getField() + "\' " + err.getDefaultMessage()) //Mapea cada error a una cadena de texto que describe el campo y su mensaje de error
            .collect(Collectors.toList()); // Recolecta las cadenas de texto en una lista nuevamente y las asigna a la variable 'errors'

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            clienteNew = clienteService.save(cliente);
        }catch (DataAccessException e){ //Si el JSON del cliente a editar incumple con alguna normas de dicho objeto en la BDD
            response.put("mensaje", "Error al agregar a la base de datos!");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "creado con exito");
        response.put("cliente", clienteNew);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("clientes/{id}")
    public ResponseEntity<?> put(@Valid @RequestBody Cliente clienteNuevo, BindingResult result, @PathVariable Long id) {
        Cliente editable = clienteService.findById(id);
        Cliente clienteUpdated = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            /*Forma vieja de manejar errores.
            List<String> errors = new ArrayList<>();
            for (FieldError err:
                 result.getFieldErrors()) {
                errors.add("El campo \'"+err.getField()+"\' "+err.getDefaultMessage());
            }Forma con Streams de manejar errores:*/

        //Toma una lista de errores de validación de campos (FieldError) y crea una lista de cadenas de texto que describen cada error.
        //El resultado final se almacena en la variable 'errors', que es una lista de cadenas (List<String>).
            List<String> errors = result.getFieldErrors() // Obtiene la lista de errores de validación de campos del objeto 'result'
                    .stream() // Convierte la lista en un flujo de elementos para facilitar su manipulación.
                    .map(err -> "El campo \'" + err.getField() + "\' " + err.getDefaultMessage()) //Mapea cada error a una cadena de texto que describe el campo y su mensaje de error
                    .collect(Collectors.toList()); // Recolecta las cadenas de texto en una lista nuevamente y las asigna a la variable 'errors'

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (editable == null) { //Si el cliente con el Id dado no existe
            response.put("mensaje", "No se puede editar cliente con Id: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            editable.setNombre(clienteNuevo.getNombre());
            editable.setApellido(clienteNuevo.getApellido());
            editable.setEmail(clienteNuevo.getEmail());
            editable.setCreteAt(clienteNuevo.getCreateAt());

            clienteUpdated = clienteService.save(editable);
        }catch (DataAccessException e){ //Si el JSON del cliente a editar incumple con alguna normas de dicho objeto en la BDD
            response.put("mensaje", "Error al editar en la base de datos!");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido editado con exito");
        response.put("cliente", clienteUpdated);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {//Endpoint para eliminar cliente y su foto.
        Map<String, Object> response = new HashMap<>();
        try {
            Cliente cliente = clienteService.findById(id);
            //Revisamos si el cliente ya tiene una foto subida, para borrarla y mantener siempre una foto por cliente:
            String fotoAnterior = cliente.getFoto();
            if (fotoAnterior != null && fotoAnterior.length() > 0 ) {   //Si el cliente tiene ya una imagen
                Path rutaFotoAnterior = Paths.get("uploads").resolve(fotoAnterior).toAbsolutePath();//Construimos de nuevo la ruta de la imagen existente
                File archivoFotoAnterior = rutaFotoAnterior.toFile();
                if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {    //Si la imagen ahora en formato archivo existe y se puede leer:
                    archivoFotoAnterior.delete();   //Eliminela
                }
            }

            clienteService.delete(id);

        }catch (DataAccessException e){ //Si el JSON del cliente a editar incumple con alguna normas de dicho objeto en la BDD
            response.put("mensaje: ", "Error al eliminar de la base de datos!");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje: ", "El cliente ha sido eliminado con exito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("clientes/upload") //Endopoint para subir foto.
    public ResponseEntity<?> upload(@RequestParam("archivo")MultipartFile archivo, @RequestParam("id") Long id_cliente){
        Map<String, Object> response = new HashMap<>(); //Creamos el Map en el que guardamos lo que se responde desde el back.
        Cliente cliente = clienteService.findById(id_cliente);  //Obtenemos el cliente al que se enlazara la imagen.

        if (!archivo.isEmpty()) {   //Si el archivo no esta vacio:
            String nombreArchivo = UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ",""); //>1
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
            //Creamos una ruta con el nombre del archivo, donde haga parte de la carpeta "uploads" del proyecto.
            log.info(rutaArchivo.toString());   //Es como el System.out.println(); de Spring Boot. Obvio tiene un monton mas de caracteristicas
            try {
                Files.copy(archivo.getInputStream(), rutaArchivo);  //Guardamos el archivo en la ruta que le creamos antes.
            } catch (IOException e) {
                response.put("mensaje: ", "Error al subir la imagen del cliente: "+nombreArchivo);
                response.put("error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            //Revisamos si el cliente ya tiene una foto subida, para borrarla y mantener siempre una foto por cliente:
            String fotoAnterior = cliente.getFoto();
            if (fotoAnterior != null && fotoAnterior.length() > 0 ) {   //Si el cliente tiene ya una imagen
                Path rutaFotoAnterior = Paths.get("uploads").resolve(fotoAnterior).toAbsolutePath();//Construimos de nuevo la ruta de la imagen existente
                File archivoFotoAnterior = rutaFotoAnterior.toFile();
                if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {    //Si la imagen ahora en formato archivo existe y se puede leer:
                    archivoFotoAnterior.delete();   //Eliminela.
                }
            }

            cliente.setFoto(nombreArchivo);

            clienteService.save(cliente);

            response.put("cliente", cliente);
            response.put("mensaje: ", "Foto subida correctamente: "+ nombreArchivo);

        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("uploads/img/{nombreFoto:.+}")//Endpoint que recibe el nombre de la imagen y la descarga automáticamente.
    // img/{nombreFoto:.+} Expresion regular que indica que el parametro tendra un punto y algo mas .jpg,.png,.jpeg
    public ResponseEntity<Resource> descargarFoto(@PathVariable String nombreFoto){
        Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();//Construimos de nuevo la ruta de la imagen existente
        log.info(rutaFotoAnterior.toString());//Imprimir la ruta reconstruida
        Resource recurso = null;

        try {
            recurso = new UrlResource(rutaFotoAnterior.toUri());//>2
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if (!recurso.exists() && !recurso.isReadable()) {  //Si no existe y no es legible.
            //Como no existe el archivo/imagen, le asignamos por defecto la ruta de la imagen por defecto not_user.png.
            rutaFotoAnterior = Paths.get("src/main/resources/static/images").resolve("not_user.png").toAbsolutePath();//Construimos de nuevo la ruta de la imagen existente
            try {
                recurso = new UrlResource(rutaFotoAnterior.toUri());//>2
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            log.error("No se pudo cargar la imagen: "+nombreFoto);

        }
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"");//>3
        return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
    }

}

/*
* >1: Como se van todas las imagenes subidas por los usuarios en la carpeta "uploads", pueed existir la posibilidad de que
*     se suban dos archivos con el mismo nombre, lo que generaria conflicto y problemas. Para eso apoyandonos en la clase
*     UUID y su metodo .randomUUID() nos aseguramos de darle al nombre del archivo un random unico y solucionar ese problema.
*
* >2: En esta línea, desde el Path de la imagen/archivo la convertimos a una URI (Se utiliza para identificar de manera única un recurso)
*
* URI: Es una secuencia de caracteres que utilizada para identificar de manera única un recurso en Internet o en un sistema de archivos.
*  Tienen este formato: scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
*  Se ven de esta manera: http://www.ejemplo.com:8080/carpeta/archivo.html?param1=valor1#seccion2
*
* >3: Se agrega una cabecera a la respuesta HTTP que indica que el contenido debe ser tratado como una descarga adjunta
*    ("attachment") y se especifica el nombre del archivo.
*    HttpHeaders.CONTENT_DISPOSITION: Es una constante definida en la clase HttpHeaders y representa el nombre del encabezado HTTP que queremos configurar.
*    "attachment; filename=\"" + recurso.getFilename() + "\"": Es el valor del encabezado Content-Disposition que se asignará a la cabecera. Aquí es donde se especifica la disposición del contenido y el nombre del archivo para la descarga adjunta.
*    attachment: Es una indicación para el cliente de que el contenido debe ser tratado como una descarga adjunta, lo que significa que el cliente debería descargar el contenido como un archivo en lugar de mostrarlo directamente en el navegador.
*    filename=\"...\": Esta parte se utiliza para especificar el nombre del archivo que se descargará. En este caso, se utiliza el método getFilename() del recurso para obtener el nombre del archivo y se coloca dentro de comillas dobles
*    (esto es una buena práctica para manejar espacios y caracteres especiales en el nombre del archivo).
 * */
