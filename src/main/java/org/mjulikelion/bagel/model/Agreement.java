package org.mjulikelion.bagel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
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
    private String id;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    private byte sequence;

    @CreatedDate
    @JsonIgnore
    private Date createdAt;

    @LastModifiedDate
    @JsonIgnore
    private Date updatedAt;

    @OneToMany(mappedBy = "agreement", orphanRemoval = true)
    @JsonIgnore
    private List<ApplicationAgreement> applicationAgreement;
}
