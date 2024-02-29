package org.mjulikelion.bagel.repository;

import java.util.List;
import org.mjulikelion.bagel.model.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, String> {
    List<Agreement> findAllByOrderBySequence();
}
