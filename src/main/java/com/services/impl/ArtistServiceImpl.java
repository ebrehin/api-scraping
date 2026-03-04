package com.services.impl;

import com.dtos.ArtistDTO;
import com.scrapper.ArtistScraper;
import com.services.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistScraper artistScraper;

    @Override
    public List<ArtistDTO> scrapeArtists(String query) {
        return artistScraper.scrape(query);
    }
}
