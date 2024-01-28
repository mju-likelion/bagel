package org.mjulikelion.bagel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "major")
public class Major {
    @Id
    @Column(updatable = false, unique = true, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
