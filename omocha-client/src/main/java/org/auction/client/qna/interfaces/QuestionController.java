package org.auction.client.qna.interfaces;

import static org.auction.client.common.code.QnACode.*;

import org.auction.client.common.dto.ResultDto;
import org.auction.client.jwt.UserPrincipal;
import org.auction.client.qna.application.QuestionService;
import org.auction.client.qna.interfaces.request.CreateQuestionRequest;
import org.auction.client.qna.interfaces.request.ModifyQuestionRequest;
import org.auction.client.qna.interfaces.response.CreateQuestionResponse;
import org.auction.client.qna.interfaces.response.QuestionListResponse;
import org.auction.client.qna.interfaces.response.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class QuestionController implements QuestionApi {

	private final QuestionService questionService;

	// TODO : response 논의 후 결정 : 질문 답변 같이 보낼지
	@GetMapping("/{auctionId}/question-list")
	public ResponseEntity<ResultDto<Page<QuestionListResponse>>> questionList(
		@PathVariable(value = "auctionId") Long auctionId,
		@RequestParam(value = "sort", defaultValue = "createdAt") String sort,
		@RequestParam(value = "direction", defaultValue = "ASC") String direction,
		@PageableDefault(page = 0, size = 10)
		Pageable pageable
	) {

		log.debug("Get questionList started");

		Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortDirection, sort));

		Page<QuestionListResponse> questionListResponses = questionService.findQuestionList(auctionId, pageable);

		ResultDto<Page<QuestionListResponse>> resultDto = ResultDto.res(
			QUESTION_LIST_ACCESS_SUCCESS.getStatusCode(),
			QUESTION_LIST_ACCESS_SUCCESS.getResultMsg(),
			questionListResponses
		);

		log.debug("Get questionList finished");

		return ResponseEntity
			.status(QUESTION_LIST_ACCESS_SUCCESS.getStatusCode())
			.body(resultDto);

	}

	@PostMapping()
	public ResponseEntity<ResultDto<CreateQuestionResponse>> questionAdd(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		Long auctionId,
		@RequestBody CreateQuestionRequest createQuestionRequest
	) {
		log.info("received CreateQuestionRequest: {}", createQuestionRequest);
		log.debug("add question started");

		Long memberId = userPrincipal.getId();
		CreateQuestionResponse questionResponse = questionService.addQuestion(memberId, auctionId,
			createQuestionRequest);

		ResultDto<CreateQuestionResponse> resultDto = ResultDto.res(
			QUESTION_CREATE_SUCCESS.getStatusCode(),
			QUESTION_CREATE_SUCCESS.getResultMsg(),
			questionResponse
		);

		log.debug("add question finished");

		return ResponseEntity
			.status(QUESTION_CREATE_SUCCESS.getHttpStatus())
			.body(resultDto);

	}

	@PatchMapping("/{questionId}")
	public ResponseEntity<ResultDto<QuestionResponse>> questionModify(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable(value = "questionId") Long questionId,
		@RequestBody ModifyQuestionRequest modifyQuestionRequest
	) {

		log.info("received questionId : {} ModifyQuestionRequest: {}", questionId, modifyQuestionRequest);
		log.debug("modify question started");

		Long memberId = userPrincipal.getId();
		QuestionResponse questionResponse = questionService.modifyQuestion(memberId, questionId, modifyQuestionRequest);

		ResultDto<QuestionResponse> resultDto = ResultDto.res(
			QUESTION_MODIFY_SUCCESS.getStatusCode(),
			QUESTION_MODIFY_SUCCESS.getResultMsg(),
			questionResponse
		);

		log.debug("modify question finished");

		return ResponseEntity
			.status(QUESTION_MODIFY_SUCCESS.getHttpStatus())
			.body(resultDto);

	}

	@DeleteMapping("/{questionId}")
	public ResponseEntity<ResultDto<Void>> questionRemove(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable(value = "questionId") Long questionId
	) {

		log.info("received questionId: {}", questionId);
		log.debug("remove question started");

		Long memberId = userPrincipal.getId();

		questionService.removeQuestion(memberId, questionId);

		ResultDto<Void> resultDto = ResultDto.res(
			QUESTION_DELETE_SUCCESS.getStatusCode(),
			QUESTION_DELETE_SUCCESS.getResultMsg()
		);

		log.debug("remove question finished");

		return ResponseEntity
			.status(QUESTION_DELETE_SUCCESS.getStatusCode())
			.body(resultDto);
	}
}
