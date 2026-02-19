package com.services.impl;

import com.dtos.*;
import com.entities.*;
import com.mappers.FilmMapper;
import com.repositories.*;
import com.scrapper.FilmScraper;
import com.services.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

	private final FilmRepository filmRepository;
	private final ArtistRepository artistRepository;
	private final PosterRepository posterRepository;
	private final FilmScraper scraper;
	private final FilmMapper mapper;

	@Override
	public FilmDTO scrapeAndSave(String query) {

		FilmDTO scraped = scraper.scrape(query);

		return filmRepository.findByTitle(scraped.getTitle())
				.map(mapper::toDto)
				.orElseGet(() -> {

					List<Artist> artists = scraped.getArtists().stream()
							.map(a -> artistRepository
									.findByFirstNameAndLastName(a.getFirstName(), a.getLastName())
									.orElseGet(() ->
											artistRepository.save(
													Artist.builder()
															.firstName(a.getFirstName())
															.lastName(a.getLastName())
															.age(a.getAge())
															.build()
											)
									)
							).toList();

					Film film = Film.builder()
							.title(scraped.getTitle())
							.minAge(scraped.getMinAge())
							.publicationYear(scraped.getPublicationYear())
							.artists(artists)
							.build();

					Film savedFilm = filmRepository.save(film);

					Poster poster = Poster.builder()
							.title(scraped.getPoster().getTitle())
							.path(scraped.getPoster().getImageUrl())
							.film(savedFilm)
							.build();

					posterRepository.save(poster);

					return mapper.toDto(savedFilm);
				});
	}
}
