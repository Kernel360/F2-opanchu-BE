package org.auction.client.qna.interfaces.request;

public record CreateAnswerRequest(
	Long questionId,
	String title,
	String content
) {
}
