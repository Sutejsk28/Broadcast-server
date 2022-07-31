package com.sutej.broadcast.repository;

import com.sutej.broadcast.modals.SubCast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcastRepository  extends JpaRepository<SubCast, Long> {
    Optional<List<SubCast>> findBysubcastName(String subcastName);
}
