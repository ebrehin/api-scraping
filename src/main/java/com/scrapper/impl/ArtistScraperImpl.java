package com.scrapper.impl;

import com.dtos.ArtistDTO;
import com.scrapper.ArtistScraper;
import com.scrapper.tmdb.TmdbPersonResult;
import com.scrapper.tmdb.TmdbSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class ArtistScraperImpl implements ArtistScraper {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public ArtistScraperImpl(
            RestTemplate restTemplate,
            @Value("${tmdb.api-key}") String apiKey,
            @Value("${tmdb.base-url:https://api.themoviedb.org/3}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<ArtistDTO> scrape(String query) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search/person")
                .queryParam("query", query)
                .queryParam("api_key", apiKey)
                .build()
                .encode()
                .toUri();

        TmdbSearchResponse response = restTemplate.getForObject(uri, TmdbSearchResponse.class);

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No artist found for: " + query);
        }

        return response.getResults().stream()
                .map(this::toDto)
                .toList();
    }

    private ArtistDTO toDto(TmdbPersonResult person) {
        String name = person.getName() == null ? "" : person.getName().trim();
        int lastSpace = name.lastIndexOf(' ');
        String firstName = lastSpace > 0 ? name.substring(0, lastSpace) : name;
        String lastName  = lastSpace > 0 ? name.substring(lastSpace + 1) : null;
        return ArtistDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
