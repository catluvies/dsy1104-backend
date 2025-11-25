package com.pasteleriamilsabores.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        // Define la carpeta "uploads" en la raíz del proyecto
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio para subir archivos.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Validar que el archivo no esté vacío
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Validar tipo de archivo (solo imágenes)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Solo se permiten archivos de imagen (JPG, PNG, GIF, WebP)");
        }

        // Validar tamaño (máximo 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("La imagen es muy grande. Tamaño máximo: 5MB");
        }

        // Normalizar nombre del archivo
        String originalFileName = file.getOriginalFilename();
        // Generar nombre único para evitar colisiones (ej: uuid_torta.jpg)
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        try {
            // Validar nombre de archivo
            if (fileName.contains("..")) {
                throw new RuntimeException("Nombre de archivo inválido " + fileName);
            }

            // Copiar archivo al destino (Reemplazando si existe)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo " + fileName, ex);
        }
    }
}
