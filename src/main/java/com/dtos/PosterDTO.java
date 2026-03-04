package com.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosterDTO {

        private String imdbId;
        private String title;
        private String imageUrl;
}
