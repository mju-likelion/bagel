package org.mjulikelion.bagel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, length = 100)
    @NotNull
    private String userId;

    @Column(length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "student_id", length = 100)
    private String studentId;

    @Column(length = 100)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column
    private Byte grade;

    @Enumerated(EnumType.STRING)
    @Column
    private Part part;

    @Column(length = 256)
    private String link;

    @OneToMany(mappedBy = "application", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ApplicationIntroduce> introduces;

    @OneToMany(mappedBy = "application", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ApplicationAgreement> agreements;
}


