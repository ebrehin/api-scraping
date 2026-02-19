package com.mappers;

import com.dtos.FilmDTO;
import com.entities.Film;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilmMapper {

    FilmDTO toDto(Film film);
    Film toEntity(FilmDTO dto);
}
