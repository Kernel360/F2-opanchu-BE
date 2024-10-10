package org.auction.client.qna.application;

import static org.auction.client.common.code.MemberCode.*;

import java.util.List;

import org.auction.client.common.code.AuctionCode;
import org.auction.client.common.code.MemberCode;
import org.auction.client.common.code.QnACode;
import org.auction.client.exception.auction.AuctionNotFoundException;
import org.auction.client.exception.member.InvalidMemberException;
import org.auction.client.exception.member.MemberNotFoundException;
import org.auction.client.exception.qna.QnaNotAllowedException;
import org.auction.client.exception.qna.QnaNotFoundException;
import org.auction.client.qna.interfaces.request.CreateQuestionRequest;
import org.auction.client.qna.interfaces.request.ModifyQuestionRequest;
import org.auction.client.qna.interfaces.response.CreateQuestionResponse;
import org.auction.client.qna.interfaces.response.QuestionListResponse;
import org.auction.client.qna.interfaces.response.QuestionResponse;
import org.auction.domain.auction.domain.entity.AuctionEntity;
import org.auction.domain.auction.infrastructure.AuctionRepository;
import org.auction.domain.member.domain.entity.MemberEntity;
import org.auction.domain.member.infrastructure.MemberRepository;
import org.auction.domain.qna.domain.entity.QuestionEntity;
import org.auction.domain.qna.infrastructure.AnswerRepository;
import org.auction.domain.qna.infrastructure.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final MemberRepository memberRepository;
	private final AuctionRepository auctionRepository;

	@Transactional(readOnly = true)
	public Page<QuestionListResponse> findQuestionList(
		Long auctionId,
		Pageable pageable
	) {

		log.debug("find questionList started");

		AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new AuctionNotFoundException(AuctionCode.AUCTION_NOT_FOUND));

		Page<QuestionEntity> questionEntityList = questionRepository.findQuestionList(auctionEntity.getAuctionId(),
			pageable);

		List<QuestionListResponse> questionListResponses = questionEntityList.stream()
			.map(QuestionListResponse::toDto)
			.toList();

		log.debug("find questionList finished");

		return new PageImpl<>(questionListResponses, pageable, questionEntityList.getTotalElements());
	}

	@Transactional
	public CreateQuestionResponse addQuestion(
		Long memberId,
		Long auctionId,
		CreateQuestionRequest createQuestionRequest
	) {

		log.debug("add question started for memberId: {}, auctionId: {}, CreateQuestionRequest: {}", memberId,
			auctionId, createQuestionRequest);

		MemberEntity memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(MemberCode.MEMBER_NOT_FOUND));

		AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new AuctionNotFoundException(AuctionCode.AUCTION_NOT_FOUND));

		QuestionEntity questionEntity = QuestionEntity.builder()
			.title(createQuestionRequest.title())
			.content(createQuestionRequest.content())
			.memberEntity(memberEntity)
			.auctionEntity(auctionEntity)
			.build();

		questionRepository.save(questionEntity);

		log.debug("add question finished for memberId: {}, auctionId: {}, CreateQuestionRequest: {}", memberId,
			auctionId, createQuestionRequest);

		return CreateQuestionResponse.toDto(questionEntity);

	}

	@Transactional
	public QuestionResponse modifyQuestion(
		Long memberId,
		Long questionId,
		ModifyQuestionRequest modifyQuestionRequest
	) {

		log.debug("modify question started for memberId: {}, questionId: {}, ModifyQuestionRequest: {}", memberId,
			questionId, modifyQuestionRequest);

		MemberEntity memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(MemberCode.MEMBER_NOT_FOUND));

		QuestionEntity questionEntity = questionRepository.findByQuestionIdAndDeletedIsFalse(questionId)
			.orElseThrow(() -> new QnaNotFoundException(QnACode.QUESTION_NOT_FOUND));

		hasQuestionOwnership(questionEntity, memberEntity);

		validModifyAndRemove(questionEntity);

		questionEntity.updateQuestion(modifyQuestionRequest.title(), modifyQuestionRequest.content());

		log.debug("modify question finished for memberId: {}, questionId: {}, ModifyQuestionRequest: {}", memberId,
			questionId, modifyQuestionRequest);

		return QuestionResponse.toDto(questionEntity);

	}

	@Transactional
	public void removeQuestion(
		Long memberId,
		Long questionId
	) {

		log.debug("remove question started for memberId: {}, questionId: {}", memberId, questionId);

		MemberEntity memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(MemberCode.MEMBER_NOT_FOUND));

		QuestionEntity questionEntity = questionRepository.findByQuestionIdAndDeletedIsFalse(questionId)
			.orElseThrow(() -> new QnaNotFoundException(QnACode.QUESTION_NOT_FOUND));

		hasQuestionOwnership(questionEntity, memberEntity);

		validModifyAndRemove(questionEntity);

		questionEntity.deleteQuestion();

		log.debug("remove question finished for memberId: {}, questionId: {}", memberId, questionId);

	}

	public void validModifyAndRemove(
		QuestionEntity questionEntity
	) {

		if (answerRepository.existsByQuestionEntityAndDeletedIsFalse(questionEntity)) {
			throw new QnaNotAllowedException(QnACode.QUESTION_DENY);
		}

	}

	public void hasQuestionOwnership(
		QuestionEntity questionEntity,
		MemberEntity memberEntity
	) {
		if (!questionEntity.getMemberEntity().getMemberId().equals(memberEntity.getMemberId())) {
			throw new InvalidMemberException(INVALID_MEMBER);
		}
	}

}
