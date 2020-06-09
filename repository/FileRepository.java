package org.comeia.project.repository;

import java.util.Optional;

import org.comeia.project.domain.File_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileRepository extends JpaRepository<File_, Long>, JpaSpecificationExecutor<File_> {

	Optional<File_> findByIdAndDeletedIsFalse(long id);
}

