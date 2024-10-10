package org.auction.client.qna.interfaces;

import org.auction.client.common.dto.ResultDto;
import org.auction.client.jwt.UserPrincipal;
import org.auction.client.qna.interfaces.request.CreateAnswerRequest;
import org.auction.client.qna.interfaces.request.ModifyAnswerRequest;
import org.auction.client.qna.interfaces.response.AnswerResponse;
import org.auction.client.qna.interfaces.response.CreateAnswerResponse;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface AnswerApi {
	@Operation(summary = "답변 조회", description = "답변을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "답변 조회에 성공하였습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "404", description = "답변을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<AnswerResponse>> answerDetail(
		@Parameter(description = "답변 ID", required = true)
		long answerId
	);

	@Operation(summary = "답변 생성", description = "새로운 답변을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "답변이 성공적으로 생성되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "400", description = "회원이 일치하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<CreateAnswerResponse>> answerAdd(
		@Parameter(description = "사용자 객체 정보", required = true)
		UserPrincipal userPrincipal,
		@Parameter(description = "답변 생성 요청 데이터")
		CreateAnswerRequest createAnswerRequest
	);

	@Operation(summary = "답변 수정", description = "답변을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "답변이 성공적으로 수정되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "400", description = "회원이 일치하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "403", description = "수정, 삭제가 거부되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "404", description = "답변을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<AnswerResponse>> answerModify(
		@Parameter(description = "사용자 객체 정보", required = true)
		UserPrincipal userPrincipal,
		@Parameter(description = "답변 ID", required = true)
		Long answerId,
		@Parameter(description = "답변 수정 데이터")
		ModifyAnswerRequest modifyAnswerRequest
	);

	@Operation(summary = "답변 삭제", description = "답변을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "답변이 성공적으로 삭제되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "400", description = "회원이 일치하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "403", description = "수정, 삭제가 거부되었습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "404", description = "답변을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)))
	})
	ResponseEntity<ResultDto<Void>> answerRemove(
		@Parameter(description = "사용자 객체 정보", required = true)
		UserPrincipal userPrincipal,
		@Parameter(description = "삭제 요청 ID", required = true)
		Long answerId
	);
}
