package org.auction.client.qna.interfaces.request;

public record ModifyAnswerRequest(
	Long answerId,
	String title,
	String content
) {
}
