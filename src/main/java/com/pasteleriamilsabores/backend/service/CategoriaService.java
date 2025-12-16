package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.CategoriaDTO;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Categoria;
import com.pasteleriamilsabores.backend.repository.CategoriaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CategoriaDTO> listarActivas() {
        return categoriaRepository.findByActivaTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO buscarPorId(long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        return convertirADTO(categoria);
    }

    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        categoria.setImagenUrl(categoriaDTO.getImagenUrl());
        categoria.setActiva(categoriaDTO.getActiva() != null ? categoriaDTO.getActiva() : true);

        Categoria guardada = categoriaRepository.save(categoria);
        return convertirADTO(guardada);
    }

    public CategoriaDTO actualizarCategoria(long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        // Solo actualizar imagen si se envía una nueva (preservar existente si es null)
        if (categoriaDTO.getImagenUrl() != null) {
            categoria.setImagenUrl(categoriaDTO.getImagenUrl());
        }
        categoria.setActiva(categoriaDTO.getActiva());

        Categoria actualizada = categoriaRepository.save(categoria);
        return convertirADTO(actualizada);
    }

    public void eliminarCategoria(long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO convertirADTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setImagenUrl(categoria.getImagenUrl());
        dto.setActiva(categoria.getActiva());
        return dto;
    }
}
