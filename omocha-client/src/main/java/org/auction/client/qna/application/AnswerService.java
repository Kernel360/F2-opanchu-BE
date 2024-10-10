package org.auction.client.qna.application;

import static org.auction.client.common.code.MemberCode.*;

import org.auction.client.common.code.AuctionCode;
import org.auction.client.common.code.MemberCode;
import org.auction.client.common.code.QnACode;
import org.auction.client.exception.auction.AuctionNotFoundException;
import org.auction.client.exception.member.InvalidMemberException;
import org.auction.client.exception.member.MemberNotFoundException;
import org.auction.client.exception.qna.QnaNotFoundException;
import org.auction.client.qna.interfaces.request.CreateAnswerRequest;
import org.auction.client.qna.interfaces.request.ModifyAnswerRequest;
import org.auction.client.qna.interfaces.response.AnswerResponse;
import org.auction.client.qna.interfaces.response.CreateAnswerResponse;
import org.auction.domain.auction.domain.entity.AuctionEntity;
import org.auction.domain.auction.infrastructure.AuctionRepository;
import org.auction.domain.member.domain.entity.MemberEntity;
import org.auction.domain.member.infrastructure.MemberRepository;
import org.auction.domain.qna.domain.entity.AnswerEntity;
import org.auction.domain.qna.domain.entity.QuestionEntity;
import org.auction.domain.qna.infrastructure.AnswerRepository;
import org.auction.domain.qna.infrastructure.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final MemberRepository memberRepository;
	private final AuctionRepository auctionRepository;

	@Transactional(readOnly = true)
	public AnswerResponse findAnswer(
		Long questionId
	) {

		log.debug("find answer started for questionId: {}", questionId);

		// TODO : 추후 리팩토링 예정
		QuestionEntity questionEntity = questionRepository.findByQuestionIdAndDeletedIsFalse(questionId)
			.orElseThrow(() -> new QnaNotFoundException(QnACode.QUESTION_NOT_FOUND));

		AnswerEntity answerEntity = answerRepository.findByQuestionEntityAndDeletedIsFalse(questionEntity)
			.orElseThrow(() -> new QnaNotFoundException(QnACode.ANSWER_NOT_FOUND));

		log.debug("find answer finished for questionId: {}", questionId);

		return AnswerResponse.toDto(
			answerEntity
		);

	}

	@Transactional
	public CreateAnswerResponse addAnswer(
		Long memberId,
		CreateAnswerRequest createAnswerRequest
	) {

		log.debug("add answer started for memberId: {} , CreateAnswerRequest : {}", memberId, createAnswerRequest);

		// TODO : 추후 리팩토링
		MemberEntity memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(MemberCode.MEMBER_NOT_FOUND));

		QuestionEntity questionEntity = questionRepository.findByQuestionIdAndDeletedIsFalse(
				createAnswerRequest.questionId())
			.orElseThrow(() -> new QnaNotFoundException(QnACode.QUESTION_NOT_FOUND));

		AuctionEntity auctionEntity = auctionRepository.findById(questionEntity.getAuctionEntity().getAuctionId())
			.orElseThrow(() -> new AuctionNotFoundException(AuctionCode.AUCTION_NOT_FOUND));

		hasAuctionOwnership(auctionEntity, memberEntity);

		AnswerEntity answerEntity = AnswerEntity.builder()
			.title(createAnswerRequest.title())
			.content(createAnswerRequest.content())
			.questionEntity(questionEntity)
			.build();

		log.debug("add answer finished for memberId: {} , CreateAnswerRequest : {}", memberId, createAnswerRequest);

		answerRepository.save(answerEntity);

		return CreateAnswerResponse.toDto(answerEntity);

	}

	@Transactional
	public AnswerResponse modifyAnswer(
		Long memberId,
		Long answerId,
		ModifyAnswerRequest modifyAnswerRequest
	) {

		log.debug("modify answer started for memberId: {} , answerId: {}, ModifyAnswerRequest : {}", memberId, answerId,
			modifyAnswerRequest);

		MemberEntity memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(MemberCode.MEMBER_NOT_FOUND));
		AnswerEntity answerEntity = answerRepository.findByAnswerIdAndDeletedIsFalse(answerId)
			.orElseThrow(() -> new QnaNotFoundException(QnACode.ANSWER_NOT_FOUND));

		hasAnswerOwnership(answerEntity, memberEntity);

		answerEntity.updateAnswer(modifyAnswerRequest.title(), modifyAnswerRequest.content());

		log.debug("modify answer finished for memberId: {} , answerId: {}, ModifyAnswerRequest : {}", memberId,
			answerId,
			modifyAnswerRequest);

		return AnswerResponse.toDto(answerEntity);

	}

	@Transactional
	public void removeAnswer(
		Long memberId,
		Long answerId
	) {

		log.debug("remove answer started for memberId: {} , answerId: {}", memberId, answerId);

		MemberEntity memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(MemberCode.MEMBER_NOT_FOUND));

		AnswerEntity answerEntity = answerRepository.findByAnswerIdAndDeletedIsFalse(answerId)
			.orElseThrow(() -> new QnaNotFoundException(QnACode.ANSWER_NOT_FOUND));

		hasAnswerOwnership(answerEntity, memberEntity);

		answerEntity.deleteAnswer();

		log.debug("remove answer finished for memberId: {} , answerId: {}", memberId, answerId);
	}

	public void hasAnswerOwnership(
		AnswerEntity answerEntity,
		MemberEntity memberEntity
	) {
		if (!answerEntity.getQuestionEntity()
			.getAuctionEntity()
			.getMemberEntity()
			.getMemberId()
			.equals(memberEntity.getMemberId())) {
			throw new InvalidMemberException(INVALID_MEMBER);
		}

	}

	public void hasAuctionOwnership(
		AuctionEntity auctionEntity,
		MemberEntity memberEntity
	) {
		if (!auctionEntity.getMemberEntity().getMemberId().equals(memberEntity.getMemberId())) {
			throw new InvalidMemberException(INVALID_MEMBER);
		}
	}

}
