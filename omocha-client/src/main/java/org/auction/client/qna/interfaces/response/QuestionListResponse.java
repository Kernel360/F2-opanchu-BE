package org.auction.client.qna.interfaces.response;

import java.time.LocalDateTime;

import org.auction.domain.qna.domain.entity.QuestionEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

public record QuestionListResponse(
	Long questionId,
	Long questionMemberId,
	String title,
	String content,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime updatedAt
) {
	public static QuestionListResponse toDto(
		QuestionEntity questionEntity
	) {
		return new QuestionListResponse(
			questionEntity.getQuestionId(),
			questionEntity.getMemberEntity().getMemberId(),
			questionEntity.getTitle(),
			questionEntity.getContent(),
			questionEntity.getCreatedAt(),
			questionEntity.getUpdatedAt()
		);
	}
}
