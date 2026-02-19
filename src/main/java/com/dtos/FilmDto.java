package com.dtos;

import lombok.*;

import java.util.List;

/**
 * Film response DTO.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmDTO {

	private String title;
	private Integer minAge;
	private Integer publicationYear;
	private List<ArtistDTO> artists;
	private PosterDTO poster;
}
