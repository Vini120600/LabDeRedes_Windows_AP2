package org.comeia.project.repository;

import java.util.Optional;

import org.comeia.project.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MusicRepository extends JpaRepository<Music, Long>, JpaSpecificationExecutor<Music> {

	Optional<Music> findByIdAndDeletedIsFalse(long id);
}

