package org.auction.client.common.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QnACode {

	// EXPLAIN: 200 OK 응답 코드
	QUESTION_CREATE_SUCCESS(HttpStatus.OK, "문의가 성공적으로 생성되었습니다."),
	QUESTION_MODIFY_SUCCESS(HttpStatus.OK, "문의가 성공적으로 수정되었습니다."),
	QUESTION_DELETE_SUCCESS(HttpStatus.OK, "문의가 성공적으로 삭제되었습니다."),
	QUESTION_ACCESS_SUCCESS(HttpStatus.OK, "문의 조회에 성공하였습니다."),
	QUESTION_LIST_ACCESS_SUCCESS(HttpStatus.OK, "문의 목록 조회에 성공하였습니다."),

	ANSWER_CREATE_SUCCESS(HttpStatus.OK, "답변이 성공적으로 생성되었습니다."),
	ANSWER_MODIFY_SUCCESS(HttpStatus.OK, "답변이 성공적으로 수정되었습니다."),
	ANSWER_DELETE_SUCCESS(HttpStatus.OK, "답변이 성공적으로 삭제되었습니다."),
	ANSWER_ACCESS_SUCCESS(HttpStatus.OK, "답변 조회에 성공하였습니다."),
	ANSWER_LIST_ACCESS_SUCCESS(HttpStatus.OK, "답변 목록 조회에 성공하였습니다."),

	// EXPLAIN: 403 ERROR
	QUESTION_DENY(HttpStatus.FORBIDDEN, "수정,삭제가 거부되었습니다."),
	ANSWER_DENY(HttpStatus.FORBIDDEN, "수정,삭제가 거부되었습니다."),

	// EXPLAIN: 404 ERROR
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "문의를 찾을 수 없습니다."),

	ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다."),

	// EXPLAIN: 415 ERROR

	//허용 글자 수 초과
	// AUCTION_WRONG_STATUS(HttpStatus.UNPROCESSABLE_ENTITY, "경매의 상태가 잘못 되었습니다."),

	// EXPLAIN: 500 SERVER ERROR
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String resultMsg;

	// 필요하다면 상태 코드를 반환하는 메서드 추가
	public int getStatusCode() {
		return httpStatus.value();
	}
}
