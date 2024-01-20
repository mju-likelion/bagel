package org.mjulikelion.bagel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "agreement")
public class Agreement {
    @Id
    @Column(updatable = false, unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    private byte sequence;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @OneToMany(mappedBy = "agreement", orphanRemoval = true)
    private List<ApplicationAgreement> applicationAgreement;
}
