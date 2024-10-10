package org.auction.client.exception.qna;

import org.auction.client.common.code.QnACode;

public class QnaNotFoundException extends QnaException {
	public QnaNotFoundException(QnACode qnaCode) {
		super(qnaCode);
	}

	public QnaNotFoundException(QnACode qnaCode, String detailMessage) {
		super(qnaCode, detailMessage);
	}
}
