package org.auction.domain.qna.domain.entity;

import org.auction.domain.common.domain.entity.TimeTrackableEntity;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "answer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerEntity extends TimeTrackableEntity {
	// TODO : Time 관련 수정 필요

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long answerId;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private QuestionEntity questionEntity;

	@Column(name = "deleted", nullable = false)
	@ColumnDefault("false")
	private boolean deleted;

	@Builder
	public AnswerEntity(
		String title,
		String content,
		QuestionEntity questionEntity
	) {
		this.title = title;
		this.content = content;
		this.questionEntity = questionEntity;
	}

	public void updateAnswer(
		String title,
		String content
	) {
		this.title = title;
		this.content = content;
	}

	public void deleteAnswer() {
		this.deleted = true;
	}

}
