package com.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Film entity scraped and persisted.
 */
@Entity
@Table(name = "films")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String title;

	private Integer publicationYear;

	@ManyToMany
	@JoinTable(
			name = "film_artists",
			joinColumns = @JoinColumn(name = "film_id"),
			inverseJoinColumns = @JoinColumn(name = "artist_id")
	)
	private List<Artist> artists;

	@OneToOne(mappedBy = "film", cascade = CascadeType.ALL)
	private Poster poster;
}
