package org.auction.client.qna.interfaces.response;

import java.time.LocalDateTime;

import org.auction.domain.qna.domain.entity.AnswerEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AnswerResponse(
	String title,
	String content,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt
) {
	public static AnswerResponse toDto(
		AnswerEntity answerEntity
	) {
		return new AnswerResponse(
			answerEntity.getTitle(),
			answerEntity.getContent(),
			answerEntity.getCreatedAt()
		);

	}
}
