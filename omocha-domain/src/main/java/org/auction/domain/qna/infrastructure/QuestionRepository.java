package org.auction.domain.qna.infrastructure;

import java.util.Optional;

import org.auction.domain.qna.domain.entity.QuestionEntity;
import org.auction.domain.qna.infrastructure.custom.QuestionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long>, QuestionRepositoryCustom {

	Optional<QuestionEntity> findByQuestionIdAndDeletedIsFalse(Long questionId);
}
