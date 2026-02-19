package com.controllers;

import com.dtos.FilmDTO;
import com.services.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrap")
@RequiredArgsConstructor
public class ScrapingController {

	private final FilmService filmService;

	@GetMapping("/film")
	public FilmDTO scrape(@RequestParam String query) {
		return filmService.scrapeAndSave(query);
	}
}
