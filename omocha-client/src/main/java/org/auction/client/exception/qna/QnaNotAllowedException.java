package org.auction.client.exception.qna;

import org.auction.client.common.code.QnACode;

public class QnaNotAllowedException extends QnaException {
	public QnaNotAllowedException(QnACode qnaCode) {
		super(qnaCode);
	}

	public QnaNotAllowedException(QnACode qnaCode, String detailMessage) {
		super(qnaCode, detailMessage);
	}
}
