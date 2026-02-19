package com.services;

import com.dtos.FilmDTO;

public interface FilmService {
    FilmDTO scrapeAndSave(String query);
}
