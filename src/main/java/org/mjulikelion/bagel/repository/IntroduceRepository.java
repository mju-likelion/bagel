package org.mjulikelion.bagel.repository;

import java.util.List;
import java.util.UUID;
import org.mjulikelion.bagel.model.Introduce;
import org.mjulikelion.bagel.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceRepository extends JpaRepository<Introduce, UUID> {
    List<Introduce> findAllByPartOrderBySequence(Part part);
}
