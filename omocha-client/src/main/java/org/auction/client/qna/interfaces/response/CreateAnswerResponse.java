package org.auction.client.qna.interfaces.response;

import org.auction.domain.qna.domain.entity.AnswerEntity;

public record CreateAnswerResponse(
	Long questionId,
	String title,
	String content
) {
	public static CreateAnswerResponse toDto(
		AnswerEntity answerEntity
	) {
		return new CreateAnswerResponse(
			answerEntity.getQuestionEntity().getQuestionId(),
			answerEntity.getTitle(),
			answerEntity.getContent()
		);

	}
}
