package org.mjulikelion.bagel.repository;

import java.util.List;
import org.mjulikelion.bagel.model.Introduce;
import org.mjulikelion.bagel.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroduceRepository extends JpaRepository<Introduce, String> {
    List<Introduce> findAllByPartOrderBySequence(Part part);
}
