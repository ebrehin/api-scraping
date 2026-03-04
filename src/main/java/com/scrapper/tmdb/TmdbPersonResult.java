package com.scrapper.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TmdbPersonResult {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    @JsonProperty("popularity")
    private Double popularity;

    @JsonProperty("profile_path")
    private String profilePath;
}
