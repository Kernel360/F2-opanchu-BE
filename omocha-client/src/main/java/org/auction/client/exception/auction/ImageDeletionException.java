package org.auction.client.exception.auction;

import org.auction.client.common.code.ImageCode;
import org.auction.client.exception.image.ImageException;

public class ImageDeletionException extends ImageException {
	public ImageDeletionException(
		ImageCode imageCode
	) {
		super(imageCode);
	}

	public ImageDeletionException(
		ImageCode imageCode,
		String detailMessage
	) {
		super(imageCode, detailMessage);
	}
}
