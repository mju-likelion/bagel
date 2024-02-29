package org.mjulikelion.bagel.repository;

import java.util.UUID;
import org.mjulikelion.bagel.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, UUID> {
}
