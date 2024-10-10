package org.auction.client.qna.interfaces;

import org.auction.client.common.dto.ResultDto;
import org.auction.client.jwt.UserPrincipal;
import org.auction.client.qna.interfaces.request.CreateQuestionRequest;
import org.auction.client.qna.interfaces.request.ModifyQuestionRequest;
import org.auction.client.qna.interfaces.response.CreateQuestionResponse;
import org.auction.client.qna.interfaces.response.QuestionListResponse;
import org.auction.client.qna.interfaces.response.QuestionResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface QuestionApi {
	@Operation(summary = "질문 조회", description = "질문을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "질문 목록 조회에 성공하였습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<Page<QuestionListResponse>>> questionList(
		@Parameter(description = "경매 ID", required = true)
		Long auctionId,
		@Parameter(description = "정렬 기준 필드 (예: createdAt, startPrice 등)", example = "createdAt")
		String sort,
		@Parameter(description = "정렬 방향 (ASC 또는 DESC)", example = "DESC")
		String direction,
		@ParameterObject
		@PageableDefault(page = 0, size = 10)
		Pageable pageable
	);

	@Operation(summary = "질문 생성", description = "질문을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "질문이 성공적으로 생성되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<CreateQuestionResponse>> questionAdd(
		@Parameter(description = "사용자 객체 정보", required = true)
		UserPrincipal userPrincipal,
		@Parameter(description = "질문 ID", required = true)
		Long questionId,
		@Parameter(description = "질문 생성 데이터")
		CreateQuestionRequest createQuestionRequest
	);

	@Operation(summary = "질문 수정", description = "질문을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "질문이 성공적으로 수정되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "400", description = "회원이 일치하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "403", description = "수정, 삭제가 거부되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "404", description = "질문를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<QuestionResponse>> questionModify(
		@Parameter(description = "사용자 객체 정보", required = true)
		UserPrincipal userPrincipal,
		@Parameter(description = "질문 ID", required = true)
		Long questionId,
		@Parameter(description = "질문 수정 데이터")
		ModifyQuestionRequest modifyQuestionRequest
	);

	@Operation(summary = "질문 삭제", description = "질문를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "질문이 성공적으로 삭제되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "400", description = "회원이 일치하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "403", description = "수정, 삭제가 거부되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<Void>> questionRemove(
		@Parameter(description = "사용자 객체 정보", required = true)
		UserPrincipal userPrincipal,
		@Parameter(description = "질문 ID", required = true)
		Long questionId
	);
}
