package org.comeia.project.repository;

import java.util.Optional;

import org.comeia.project.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VideoRepository extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {

	Optional<Video> findByIdAndDeletedIsFalse(long id);
}

