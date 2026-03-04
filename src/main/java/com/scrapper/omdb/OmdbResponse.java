package com.scrapper.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmdbResponse {

	@JsonProperty("Title")
	private String title;

	@JsonProperty("Year")
	private String year;

	@JsonProperty("Actors")
	private String actors;

	@JsonProperty("Director")
	private String director;

	@JsonProperty("Poster")
	private String poster;

@JsonProperty("imdbID")
        private String imdbId;

        @JsonProperty("Response")
	private String response;

	@JsonProperty("Error")
	private String error;

	public String getTitle() {
		return title;
	}

	public String getYear() {
		return year;
	}

	public String getActors() {
		return actors;
	}

	public String getDirector() {
		return director;
	}

	public String getPoster() {
		return poster;
	}

public String getImdbId() {
                return imdbId;
        }

        public String getResponse() {
		return response;
	}

	public String getError() {
		return error;
	}

	public boolean isFalse() {
		return response != null && response.equalsIgnoreCase("False");
	}
}
