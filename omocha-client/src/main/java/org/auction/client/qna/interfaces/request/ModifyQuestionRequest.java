package org.auction.client.qna.interfaces.request;

public record ModifyQuestionRequest(
	String title,
	String content
) {
}
