package com.scrapper.impl;

import com.dtos.FilmDTO;
import com.scrapper.FilmScraper;
import org.springframework.stereotype.Service;

@Service
public class FilmScraperImpl implements FilmScraper {

	@Override
	public FilmDTO scrape(String query) {
		throw new UnsupportedOperationException("FilmScraper is not implemented yet");
	}
}
