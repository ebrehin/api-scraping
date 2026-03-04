package com.scrapper.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TmdbSearchResponse {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("results")
    private List<TmdbPersonResult> results;

    @JsonProperty("total_results")
    private Integer totalResults;

    @JsonProperty("total_pages")
    private Integer totalPages;
}
