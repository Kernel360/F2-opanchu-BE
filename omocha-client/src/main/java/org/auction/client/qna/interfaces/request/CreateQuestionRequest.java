package org.auction.client.qna.interfaces.request;

public record CreateQuestionRequest(
	Long auctionId,
	String title,
	String content
) {
}
