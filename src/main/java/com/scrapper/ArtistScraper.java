package com.scrapper;

import com.dtos.ArtistDTO;

import java.util.List;

public interface ArtistScraper {
    List<ArtistDTO> scrape(String query);
}
