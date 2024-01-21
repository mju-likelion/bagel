package org.mjulikelion.bagel.repository;

import java.util.UUID;
import org.mjulikelion.bagel.model.ApplicationIntroduce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationIntroduceRepository extends JpaRepository<ApplicationIntroduce, UUID> {
}
