package com.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Artist entity persisted in database.
 */
@Entity
@Table(name = "artists")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;

	@ManyToMany(mappedBy = "artists")
	private List<Film> films;
}
