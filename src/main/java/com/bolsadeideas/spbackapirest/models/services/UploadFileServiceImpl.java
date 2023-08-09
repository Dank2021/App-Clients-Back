package com.bolsadeideas.spbackapirest.models.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService{

    private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
    private final static String CARPETA_UPLOADS = "uploads";
    @Override
    public Resource cargar(String nombreFoto) throws MalformedURLException { //Metodo que devuelve un Resource conteniendo una ruta Uri para el nombreFoto.

        Path rutaFotoAnterior = obternerURL(nombreFoto);//Agrega el nombre de la foto a una ruta donde hace parte de "uploads".
        log.info(rutaFotoAnterior.toString());//Imprimir la ruta construida.
        Resource recurso;

        recurso = new UrlResource(rutaFotoAnterior.toUri());//>2: Convertimos el Path con la ubicación de la foto a una URI.

        if (!recurso.exists() && !recurso.isReadable()) {  //Si no existe y no es legible:
            //Le asignamos la ruta de la imagen por defecto para todos los usuarios llamada not_user.png.
            rutaFotoAnterior = Paths.get("src/main/resources/static/images").resolve("not_user.png").toAbsolutePath();//Construimos de nuevo la ruta de la imagen.
            recurso = new UrlResource(rutaFotoAnterior.toUri());//>2: Convertimos el Path con la ubicación de la foto a una URI.
            log.error("No se pudo cargar la imagen: "+nombreFoto);
        }
        return recurso;
    }

    @Override
    public String guardar(MultipartFile archivo) throws IOException { //Metodo para modificar el nombre y guardar el archivo en la carpeta uploads
        String nombreArchivo = UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ",""); //>1: Se modifica el nomrbe del archivo, dandole uno unico
        Path rutaArchivo = obternerURL(nombreArchivo);  //Agrega el nombre del archivo a una ruta donde hace parte de "uploads".
        //Creamos una ruta con el nombre del archivo, donde haga parte de la carpeta "uploads" del proyecto.
        log.info(rutaArchivo.toString());   //Es como el System.out.println(); de Spring Boot. Obvio tiene un monton mas de caracteristicas
        Files.copy(archivo.getInputStream(), rutaArchivo);  //Guardamos el archivo en la ruta que le creamos antes.

        return nombreArchivo;
    }

    @Override
    public boolean eliminar(String nombreFoto) {    //Metodo que de existir elimina una imagen.

        if (nombreFoto != null && nombreFoto.length() > 0 ) {   //Si el cliente tiene ya una imagen.
            Path rutaFotoAnterior = obternerURL(nombreFoto);    //Agrega el nombre de la foto a una ruta donde hace parte de "uploads".
            File archivoFotoAnterior = rutaFotoAnterior.toFile();
            if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {    //Si la imagen ahora en formato archivo existe y se puede leer:
                archivoFotoAnterior.delete();   //Eliminela.
                return true;
            }
        }
        return false;
    }

    @Override
    public Path obternerURL(String nombreFoto) {    //Metodo que se encarga de incluir nombre foto a un Path en la carpeta "uploads"
        return Paths.get(CARPETA_UPLOADS).resolve(nombreFoto).toAbsolutePath();
        //Creamos una ruta con el nombre del archivo/foto, donde este haga parte de la carpeta "uploads" del proyecto.
    }
}

/*
 * >1: Como se van todas las imagenes subidas por los usuarios en la carpeta "uploads", puede existir la posibilidad de que
 *     se suban dos archivos con el mismo nombre, lo que generaria conflicto y problemas. Para eso apoyándonos en la clase
 *     UUID y su metodo .randomUUID() nos aseguramos de darle al nombre del archivo un random unico y solucionar ese problema.
 *
 * >2: En esta línea, desde el Path de la imagen/archivo la convertimos a una URI (Se utiliza para identificar
 *     de manera única un recurso)
 *
 * URI: Es una secuencia de caracteres que utilizada para identificar de manera única un recurso en Internet
 *      o en un sistema de archivos.
 *      Tienen este formato: scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
 *      Se ven de esta manera: http://www.ejemplo.com:8080/carpeta/archivo.html?param1=valor1#seccion2
 *
 * >3: Se agrega una cabecera a la respuesta HTTP que indica que el contenido debe ser tratado como una descarga adjunta
 *     ("attachment") y se especifica el nombre del archivo.
 *     HttpHeaders.CONTENT_DISPOSITION: Es una constante definida en la clase HttpHeaders y representa el nombre del encabezado HTTP que queremos configurar.
 *     "attachment; filename=\"" + recurso.getFilename() + "\"": Es el valor del encabezado Content-Disposition que se asignará a la cabecera. Aquí es donde se especifica la disposición del contenido y el nombre del archivo para la descarga adjunta.
 *     attachment: Es una indicación para el cliente de que el contenido debe ser tratado como una descarga adjunta, lo que significa que el cliente debería descargar el contenido como un archivo en lugar de mostrarlo directamente en el navegador.
 *     filename=\"...\": Esta parte se utiliza para especificar el nombre del archivo que se descargará. En este caso, se utiliza el método getFilename() del recurso para obtener el nombre del archivo y se coloca dentro de comillas dobles
 *     (esto es una buena práctica para manejar espacios y caracteres especiales en el nombre del archivo).
 *
 * Path:
 * Se utiliza para representar una ubicación en el sistema de archivos.
 *
 * @Service:
 *      La anotación `@Service` en Spring Boot se usa para marcar una clase como un "servicio".
 *      Esto significa que la clase realiza lógica de negocio o funcionalidad de servicio
 *      y es administrada por Spring para la inyección de dependencias en otras partes de la aplicación.
 * */
