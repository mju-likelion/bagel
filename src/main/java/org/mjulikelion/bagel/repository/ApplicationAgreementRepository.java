package org.mjulikelion.bagel.repository;

import java.util.UUID;
import org.mjulikelion.bagel.model.ApplicationAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationAgreementRepository extends JpaRepository<ApplicationAgreement, UUID> {
}
