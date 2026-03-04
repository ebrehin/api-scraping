package com.controllers;

import com.dtos.ArtistDTO;
import com.dtos.FilmDTO;
import com.services.ArtistService;
import com.services.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scrap")
@RequiredArgsConstructor
public class ScrapingController {

	private final FilmService filmService;
	private final ArtistService artistService;

	@GetMapping("/film")
	public FilmDTO scrapeFilm(@RequestParam String query) {
		return filmService.scrape(query);
	}

	@GetMapping("/artist")
	public List<ArtistDTO> scrapeArtists(@RequestParam String query) {
		return artistService.scrapeArtists(query);
	}
}
