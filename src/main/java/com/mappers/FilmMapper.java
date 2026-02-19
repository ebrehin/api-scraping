package com.mappers;

import com.dtos.FilmDTO;
import com.dtos.PosterDTO;
import com.entities.Film;
import com.entities.Poster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilmMapper {

    FilmDTO toDto(Film film);
    Film toEntity(FilmDTO dto);

    @Mapping(target = "imageUrl", source = "path")
    PosterDTO toDto(Poster poster);

    @Mapping(target = "path", source = "imageUrl")
    Poster toEntity(PosterDTO dto);
}
