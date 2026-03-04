package com.services;

import com.dtos.FilmDTO;

public interface FilmService {
    FilmDTO scrape(String query);
}
