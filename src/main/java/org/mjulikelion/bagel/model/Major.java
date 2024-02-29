package org.mjulikelion.bagel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
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

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private byte sequence;

    @OneToMany(mappedBy = "major")
    @JsonIgnore
    private List<Application> applications;
}