package org.mjulikelion.bagel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "introduce")
public class Introduce {
    @Id
    @Column(updatable = false, unique = true, nullable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "max_length", nullable = false)
    private Short maxLength;

    @Column(nullable = false)
    private Byte sequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Part part;

    @OneToMany(mappedBy = "introduce", orphanRemoval = true)
    @JsonIgnore
    private List<ApplicationIntroduce> applicationIntroduce;
}
