package org.mjulikelion.bagel.repository;

import java.util.UUID;
import org.mjulikelion.bagel.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, UUID> {
}
