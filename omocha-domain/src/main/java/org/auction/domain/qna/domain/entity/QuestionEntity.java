package org.auction.domain.qna.domain.entity;

import org.auction.domain.auction.domain.entity.AuctionEntity;
import org.auction.domain.common.domain.entity.TimeTrackableEntity;
import org.auction.domain.member.domain.entity.MemberEntity;
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

@Entity(name = "question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity extends TimeTrackableEntity {

	// TODO : Time 관련 수정 필요
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long questionId;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_id", nullable = false)
	private AuctionEntity auctionEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberEntity memberEntity;

	@Column(name = "deleted", nullable = false)
	@ColumnDefault("false")
	private boolean deleted;

	@Builder
	public QuestionEntity(
		String title,
		String content,
		AuctionEntity auctionEntity,
		MemberEntity memberEntity
	) {
		this.title = title;
		this.content = content;
		this.auctionEntity = auctionEntity;
		this.memberEntity = memberEntity;
	}

	public void updateQuestion(
		String title,
		String content
	) {
		this.title = title;
		this.content = content;
	}

	public void deleteQuestion() {
		this.deleted = true;
	}

}
