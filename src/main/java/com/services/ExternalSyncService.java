package com.services;

import com.dtos.ArtistDTO;
import com.dtos.FilmDTO;
import com.dtos.PosterDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalSyncService {

    private final RestTemplate restTemplate;

    @Value("${sync.artist.url:http://localhost:8083/api/artistes}")
    private String artistApiUrl;

    @Value("${sync.poster.url:http://localhost:8081/api/posters}")
    private String posterApiUrl;

    public void sync(FilmDTO scrapedFilm) {
        if (scrapedFilm == null) {
            return;
        }

        syncArtists(scrapedFilm.getArtists());
        syncPoster(scrapedFilm.getPoster());
    }

    private void syncArtists(List<ArtistDTO> artists) {
        if (artists == null || artists.isEmpty()) {
            return;
        }

        List<RemoteArtist> existingArtists = fetchArtists();

        for (ArtistDTO artist : artists) {
            if (artist == null) {
                continue;
            }

            String nom = normalize(artist.getLastName());
            String prenom = normalize(artist.getFirstName());
            if (nom.isBlank() || prenom.isBlank()) {
                continue;
            }

            boolean alreadyExists = existingArtists.stream()
                    .anyMatch(existing -> equalsIgnoreCase(existing.getNom(), nom)
                            && equalsIgnoreCase(existing.getPrenom(), prenom));

            if (alreadyExists) {
                continue;
            }

            RemoteArtist payload = new RemoteArtist(nom, prenom, 0);
            try {
                restTemplate.postForEntity(artistApiUrl, payload, Void.class);
                existingArtists.add(payload);
            } catch (Exception ex) {
                log.warn("Unable to sync artist {} {} to {}", prenom, nom, artistApiUrl, ex);
            }
        }
    }

    private void syncPoster(PosterDTO poster) {
        if (poster == null) {
            return;
        }

        String titre = normalize(poster.getTitle());
        String url = normalize(poster.getImageUrl());
        if (titre.isBlank() || url.isBlank()) {
            return;
        }

        List<RemotePoster> existingPosters = fetchPosters();
        boolean alreadyExists = existingPosters.stream()
                .anyMatch(existing -> equalsIgnoreCase(existing.getTitre(), titre)
                        && equalsIgnoreCase(existing.getUrl(), url));

        if (alreadyExists) {
            return;
        }

        RemotePoster payload = new RemotePoster("", url, titre);
        try {
            restTemplate.postForEntity(posterApiUrl, payload, Void.class);
        } catch (Exception ex) {
            log.warn("Unable to sync poster {} to {}", titre, posterApiUrl, ex);
        }
    }

    private List<RemoteArtist> fetchArtists() {
        try {
            ResponseEntity<RemoteArtist[]> response = restTemplate.getForEntity(artistApiUrl, RemoteArtist[].class);
            RemoteArtist[] body = response.getBody();
            if (body == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(body));
        } catch (Exception ex) {
            log.warn("Unable to fetch artists from {}", artistApiUrl, ex);
            return new ArrayList<>();
        }
    }

    private List<RemotePoster> fetchPosters() {
        try {
            ResponseEntity<RemotePoster[]> response = restTemplate.getForEntity(posterApiUrl, RemotePoster[].class);
            RemotePoster[] body = response.getBody();
            if (body == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(body));
        } catch (Exception ex) {
            log.warn("Unable to fetch posters from {}", posterApiUrl, ex);
            return new ArrayList<>();
        }
    }

    private static boolean equalsIgnoreCase(String left, String right) {
        return normalize(left).equalsIgnoreCase(normalize(right));
    }

    private static String normalize(String value) {
        return Objects.toString(value, "").trim();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RemoteArtist {
        private String nom;
        private String prenom;
        private Integer age;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RemotePoster {
        private String id;
        private String url;
        private String titre;
    }
}
