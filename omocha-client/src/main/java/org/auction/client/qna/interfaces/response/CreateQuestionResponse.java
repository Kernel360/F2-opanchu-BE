package org.auction.client.qna.interfaces.response;

import java.time.LocalDateTime;

import org.auction.domain.qna.domain.entity.QuestionEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CreateQuestionResponse(
	String title,
	String content,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createAt

) {
	public static CreateQuestionResponse toDto(QuestionEntity questionEntity) {
		return new CreateQuestionResponse(
			questionEntity.getTitle(),
			questionEntity.getContent(),
			questionEntity.getCreatedAt()
		);
	}
}
