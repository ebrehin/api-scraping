package com.services.impl;

import com.dtos.*;
import com.entities.*;
import com.mappers.FilmMapper;
import com.repositories.*;
import com.scrapper.FilmScraper;
import com.services.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
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
															.build()
											)
									)
							).toList();

					Film film = Film.builder()
							.title(scraped.getTitle())
							.publicationYear(scraped.getPublicationYear())
							.artists(artists)
							.build();

					Film savedFilm = filmRepository.save(film);

					Poster poster = Poster.builder()
							.title(scraped.getPoster().getTitle())
							.path(scraped.getPoster().getImageUrl())
							.film(savedFilm)
							.build();

					Poster savedPoster = posterRepository.save(poster);
					savedFilm.setPoster(savedPoster);

					return mapper.toDto(savedFilm);
				});
	}
}
