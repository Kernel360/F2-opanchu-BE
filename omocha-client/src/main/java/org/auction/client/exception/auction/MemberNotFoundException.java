package org.auction.client.exception.auction;

import org.auction.client.common.code.MemberCode;
import org.auction.client.exception.member.MemberException;

public class MemberNotFoundException extends MemberException {
	public MemberNotFoundException(
		MemberCode memberCode
	) {
		super(memberCode);
	}

	public MemberNotFoundException(
		MemberCode memberCode,
		String detailMessage
	) {
		super(memberCode, detailMessage);
	}
}
