package org.mjulikelion.bagel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "application")
@EntityListeners(AuditingEntityListener.class)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "student_id", length = 8)
    private String studentId;

    @Column(length = 100)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(length = 10)
    private String grade;

    @Enumerated(EnumType.STRING)
    @Column
    private Part part;

    @Column(length = 256)
    private String link;

    @CreatedDate
    private Date createdAt;

    @OneToMany(mappedBy = "application", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ApplicationIntroduce> introduces;

    @OneToMany(mappedBy = "application", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ApplicationAgreement> agreements;
}


