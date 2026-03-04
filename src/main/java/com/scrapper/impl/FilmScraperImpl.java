package com.scrapper.impl;

import com.dtos.ArtistDTO;
import com.dtos.FilmDTO;
import com.dtos.PosterDTO;
import com.scrapper.FilmScraper;
import com.scrapper.omdb.OmdbResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FilmScraperImpl implements FilmScraper {

	private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{4})");

	private final RestTemplate restTemplate;
	private final String apiKey;
	private final String baseUrl;

	public FilmScraperImpl(
			RestTemplate restTemplate,
			@Value("${omdb.api-key}") String apiKey,
			@Value("${omdb.base-url:https://www.omdbapi.com/}") String baseUrl
	) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
		this.baseUrl = baseUrl;
	}

	@Override
	public FilmDTO scrape(String query) {
		URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("t", query)
				.queryParam("apikey", apiKey)
				.build()
				.encode()
				.toUri();

		OmdbResponse response = restTemplate.getForObject(uri, OmdbResponse.class);

		if (response == null || response.isFalse()) {
			String error = response != null ? response.getError() : "No response from OMDb";
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, error);
		}

		List<ArtistDTO> artists = parseArtists(response.getActors(), response.getDirector());
		PosterDTO poster = buildPoster(response.getImdbId(), response.getPoster(), response.getTitle());

		return FilmDTO.builder()
				.title(response.getTitle())
				.publicationYear(parseYear(response.getYear()))
				.artists(artists)
				.poster(poster)
				.build();
	}

	private static List<ArtistDTO> parseArtists(String actors, String director) {
		List<ArtistDTO> result = new ArrayList<>();

		// parse directors first
		if (director != null && !director.isBlank() && !"N/A".equalsIgnoreCase(director)) {
			for (String raw : director.split(",")) {
				String name = raw.trim();
				if (!name.isEmpty()) {
					int lastSpace = name.lastIndexOf(' ');
					result.add(ArtistDTO.builder()
							.firstName(lastSpace > 0 ? name.substring(0, lastSpace) : name)
							.lastName(lastSpace > 0 ? name.substring(lastSpace + 1) : null)
							.build());
				}
			}
		}

		if (actors == null || actors.isBlank() || "N/A".equalsIgnoreCase(actors)) {
			return result;
		}

		String[] parts = actors.split(",");
		for (String raw : parts) {
			String name = raw.trim();
			if (name.isEmpty()) {
				continue;
			}
			int lastSpace = name.lastIndexOf(' ');
			String firstName = lastSpace > 0 ? name.substring(0, lastSpace) : name;
			String lastName = lastSpace > 0 ? name.substring(lastSpace + 1) : null;
			result.add(ArtistDTO.builder()
					.firstName(firstName)
					.lastName(lastName)
					.build());
		}
		return result;
	}

	private static PosterDTO buildPoster(String imdbId, String posterUrl, String title) {
		String url = posterUrl == null || "N/A".equalsIgnoreCase(posterUrl) ? "" : posterUrl;
		String safeTitle = title == null ? "" : title + " poster";
		return PosterDTO.builder()
				.imdbId(imdbId)
				.title(safeTitle)
				.imageUrl(url)
				.build();
	}

	private static Integer parseYear(String yearText) {
		if (yearText == null) {
			return null;
		}
		Matcher matcher = YEAR_PATTERN.matcher(yearText);
		if (!matcher.find()) {
			return null;
		}
		return Integer.parseInt(matcher.group(1));
	}

}
