package com.scrapper.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmdbResponse {

	@JsonProperty("Title")
	private String title;

	@JsonProperty("Year")
	private String year;

	@JsonProperty("Actors")
	private String actors;

	@JsonProperty("Poster")
	private String poster;

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

	public String getPoster() {
		return poster;
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
