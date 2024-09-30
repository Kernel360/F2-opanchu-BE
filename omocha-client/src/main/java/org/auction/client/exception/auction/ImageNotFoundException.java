package org.auction.client.exception.auction;

import org.auction.client.common.code.ImageCode;
import org.auction.client.exception.image.ImageException;

public class ImageNotFoundException extends ImageException {
	public ImageNotFoundException(
		ImageCode imageCode
	) {
		super(imageCode);
	}

	public ImageNotFoundException(
		ImageCode imageCode,
		String detailMessage
	) {
		super(imageCode, detailMessage);
	}
}
