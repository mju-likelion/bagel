package org.mjulikelion.bagel.repository;

import java.util.UUID;
import org.mjulikelion.bagel.model.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, UUID> {
}
