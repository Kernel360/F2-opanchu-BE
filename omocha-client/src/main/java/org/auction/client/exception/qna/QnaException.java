package org.auction.client.exception.qna;

import org.auction.client.common.code.QnACode;

import lombok.Getter;

@Getter
public class QnaException extends RuntimeException {
	private final QnACode qnaCode;
	private final String detailMessage;

	public QnaException(
		QnACode qnaCode
	) {
		super(qnaCode.getResultMsg());
		this.qnaCode = qnaCode;
		this.detailMessage = qnaCode.getResultMsg();
	}

	public QnaException(
		QnACode qnaCode,
		String detailMessage
	) {
		super(qnaCode.getResultMsg());
		this.qnaCode = qnaCode;
		this.detailMessage = detailMessage;
	}
}
