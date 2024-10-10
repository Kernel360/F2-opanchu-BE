package org.auction.client.qna.interfaces;

import static org.auction.client.common.code.QnACode.*;

import org.auction.client.common.code.QnACode;
import org.auction.client.common.dto.ResultDto;
import org.auction.client.jwt.UserPrincipal;
import org.auction.client.qna.application.AnswerService;
import org.auction.client.qna.interfaces.request.CreateAnswerRequest;
import org.auction.client.qna.interfaces.request.ModifyAnswerRequest;
import org.auction.client.qna.interfaces.response.AnswerResponse;
import org.auction.client.qna.interfaces.response.CreateAnswerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answer")
public class AnswerController implements AnswerApi {

	private final AnswerService answerService;

	@GetMapping("/{answerId}")
	public ResponseEntity<ResultDto<AnswerResponse>> answerDetail(
		@PathVariable long answerId
	) {

		log.debug("get answerDetail started");

		AnswerResponse answerResponse = answerService.findAnswer(answerId);

		ResultDto<AnswerResponse> resultDto = ResultDto.res(
			QnACode.ANSWER_ACCESS_SUCCESS.getStatusCode(),
			QnACode.ANSWER_ACCESS_SUCCESS.getResultMsg(),
			answerResponse
		);

		log.debug("get answerDetail finished");

		return ResponseEntity
			.status(QnACode.ANSWER_ACCESS_SUCCESS.getStatusCode())
			.body(resultDto);

	}

	@PostMapping()
	public ResponseEntity<ResultDto<CreateAnswerResponse>> answerAdd(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody CreateAnswerRequest createAnswerRequest
	) {

		log.info("received CreateAnswerRequest : {}", createAnswerRequest);
		log.debug("add answer started");

		Long memberId = userPrincipal.getId();
		CreateAnswerResponse createAnswerResponse = answerService.addAnswer(memberId, createAnswerRequest);

		ResultDto<CreateAnswerResponse> resultDto = ResultDto.res(
			QnACode.ANSWER_CREATE_SUCCESS.getStatusCode(),
			QnACode.ANSWER_CREATE_SUCCESS.getResultMsg(),
			createAnswerResponse
		);

		log.debug("add answer finished");

		return ResponseEntity
			.status(QnACode.ANSWER_CREATE_SUCCESS.getHttpStatus())
			.body(resultDto);
	}

	@PatchMapping("/{answerId}")
	public ResponseEntity<ResultDto<AnswerResponse>> answerModify(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("answerId") Long answerId,
		@RequestBody ModifyAnswerRequest modifyAnswerRequest
	) {

		log.info("received answerId : {} , ModifyAnswerRequest : {}", answerId, modifyAnswerRequest);
		log.debug("modify answer started");

		Long memberId = userPrincipal.getId();
		AnswerResponse answerResponse = answerService.modifyAnswer(memberId, answerId, modifyAnswerRequest);

		ResultDto<AnswerResponse> resultDto = ResultDto.res(
			ANSWER_MODIFY_SUCCESS.getStatusCode(),
			ANSWER_MODIFY_SUCCESS.getResultMsg(),
			answerResponse
		);

		log.debug("modify answer finished");

		return ResponseEntity
			.status(ANSWER_MODIFY_SUCCESS.getHttpStatus())
			.body(resultDto);
	}

	@DeleteMapping("/{answerId}")
	public ResponseEntity<ResultDto<Void>> answerRemove(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable(value = "answerId") Long answerId
	) {

		log.info("received answerId : {} ", answerId);
		log.debug("remove answer started");

		Long memberId = userPrincipal.getId();
		answerService.removeAnswer(memberId, answerId);

		ResultDto<Void> resultDto = ResultDto.res(
			QnACode.ANSWER_DELETE_SUCCESS.getStatusCode(),
			QnACode.ANSWER_DELETE_SUCCESS.getResultMsg()
		);

		log.debug("remove answer finished");

		return ResponseEntity
			.status(QnACode.ANSWER_DELETE_SUCCESS.getHttpStatus())
			.body(resultDto);
	}
}
