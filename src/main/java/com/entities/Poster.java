package com.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Poster entity associated to Film.
 */
@Entity
@Table(name = "posters")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String path;

	@OneToOne
	@JoinColumn(name = "film_id")
	private Film film;
}
