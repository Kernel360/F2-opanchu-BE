package org.auction.client.qna.interfaces.response;

import org.auction.domain.qna.domain.entity.QuestionEntity;

public record QuestionResponse(
	Long questionId,
	String title,
	String content
) {
	public static QuestionResponse toDto(
		QuestionEntity questionEntity
	) {
		return new QuestionResponse(
			questionEntity.getQuestionId(),
			questionEntity.getTitle(),
			questionEntity.getContent()
		);

	}
}
