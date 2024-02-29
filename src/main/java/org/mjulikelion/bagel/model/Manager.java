package org.mjulikelion.bagel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "manager")
public class Manager {
    @Id
    @Column(updatable = false, unique = true, nullable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String password;
}
