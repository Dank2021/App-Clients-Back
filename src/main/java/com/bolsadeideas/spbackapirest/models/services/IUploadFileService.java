package com.bolsadeideas.spbackapirest.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IUploadFileService {

    public Resource descargarFoto(String nombreFoto) throws MalformedURLException;
    public String guardar(MultipartFile archivo) throws IOException;
    public boolean eliminar(String nombreFoto);
    public Path obternerURL(String nombreFoto);
}
