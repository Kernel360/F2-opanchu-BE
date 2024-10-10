package org.auction.domain.qna.infrastructure.custom;

import org.auction.domain.qna.domain.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryCustom {
	Page<QuestionEntity> findQuestionList(
		Long auctionId,
		Pageable pageable
	);
}
