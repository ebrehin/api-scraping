package com.services.impl;

import com.dtos.FilmDTO;
import com.scrapper.FilmScraper;
import com.services.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

	private final FilmScraper scraper;

	@Override
	public FilmDTO scrape(String query) {
		return scraper.scrape(query);
	}
}
