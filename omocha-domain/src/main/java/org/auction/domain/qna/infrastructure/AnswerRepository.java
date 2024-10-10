package org.auction.domain.qna.infrastructure;

import java.util.Optional;

import org.auction.domain.qna.domain.entity.AnswerEntity;
import org.auction.domain.qna.domain.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

	Optional<AnswerEntity> findByAnswerIdAndDeletedIsFalse(Long answerId);

	Optional<AnswerEntity> findByQuestionEntityAndDeletedIsFalse(QuestionEntity question);

	boolean existsByQuestionEntityAndDeletedIsFalse(QuestionEntity question);
}
