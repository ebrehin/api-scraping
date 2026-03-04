package com.services;

import com.dtos.ArtistDTO;

import java.util.List;

public interface ArtistService {
    List<ArtistDTO> scrapeArtists(String filmQuery);
}
