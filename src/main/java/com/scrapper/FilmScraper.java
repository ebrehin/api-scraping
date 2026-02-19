package com.scrapper;

import com.dtos.FilmDTO;

public interface FilmScraper {
    FilmDTO scrape(String query);
}
