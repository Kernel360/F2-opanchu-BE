package org.auction.client.exception;

import static org.auction.client.common.code.AuctionCode.*;

import org.auction.client.common.dto.ResultDto;
import org.auction.client.exception.auction.AuctionException;
import org.auction.client.exception.image.ImageException;
import org.auction.client.exception.jwt.JwtTokenException;
import org.auction.client.exception.member.MemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuctionException.class)
	public ResponseEntity<ResultDto<Object>> handleAuctionException(
		AuctionException e,
		HttpServletRequest request
	) {
		log.error("errorCode: {}, url: {}, message: {}",
			e.getAuctionCode(), request.getRequestURI(), e.getDetailMessage(), e);

		ResultDto<Object> resultDto = ResultDto.res(
			e.getAuctionCode().getStatusCode(),
			e.getAuctionCode().getResultMsg()
		);
		return ResponseEntity
			.status(e.getAuctionCode().getHttpStatus())
			.body(resultDto);
	}

	@ExceptionHandler(ImageException.class)
	public ResponseEntity<ResultDto<Object>> handleAuctionException(
		ImageException e,
		HttpServletRequest request
	) {
		log.error("errorCode: {}, url: {}, message: {}",
			e.getImageCode(), request.getRequestURI(), e.getDetailMessage(), e);

		ResultDto<Object> resultDto = ResultDto.res(
			e.getImageCode().getStatusCode(),
			e.getImageCode().getResultMsg()
		);
		return ResponseEntity
			.status(e.getImageCode().getHttpStatus())
			.body(resultDto);
	}

	@ExceptionHandler(JwtTokenException.class)
	public ResponseEntity<ResultDto<Object>> handleAuctionException(
		JwtTokenException e,
		HttpServletRequest request
	) {
		log.error("errorCode: {}, url: {}, message: {}",
			e.getJwtCode(), request.getRequestURI(), e.getDetailMessage(), e);

		ResultDto<Object> resultDto = ResultDto.res(
			e.getJwtCode().getStatusCode(),
			e.getJwtCode().getResultMsg()
		);
		return ResponseEntity
			.status(e.getJwtCode().getHttpStatus())
			.body(resultDto);
	}

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ResultDto<Object>> handleAuctionException(
		MemberException e,
		HttpServletRequest request
	) {
		log.error("errorCode: {}, url: {}, message: {}",
			e.getMemberCode(), request.getRequestURI(), e.getDetailMessage(), e);

		ResultDto<Object> resultDto = ResultDto.res(
			e.getMemberCode().getStatusCode(),
			e.getMemberCode().getResultMsg()
		);
		return ResponseEntity
			.status(e.getMemberCode().getHttpStatus())
			.body(resultDto);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ResultDto<Object>> handleHttpMediaTypeNotSupported(
		HttpMediaTypeNotSupportedException e,
		HttpServletRequest request
	) {
		log.error("errorCode: {}, url: {}, message: {}",
			UNSUPPORTED_MEDIA_TYPE, request.getRequestURI(), e.getMessage(), e);

		ResultDto<Object> resultDto = ResultDto.res(
			UNSUPPORTED_MEDIA_TYPE.getStatusCode(),
			UNSUPPORTED_MEDIA_TYPE.getResultMsg()
		);
		return ResponseEntity
			.status(UNSUPPORTED_MEDIA_TYPE.getHttpStatus())
			.body(resultDto);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResultDto<Object>> handleGeneralException(
		Exception e,
		HttpServletRequest request
	) {
		log.error("url: {}, message: {}", request.getRequestURL(), e.getMessage(), e);
		ResultDto<Object> resultDto = ResultDto.res(
			INTERNAL_SERVER_ERROR.getStatusCode(),
			INTERNAL_SERVER_ERROR.getResultMsg()
		);
		return ResponseEntity
			.status(INTERNAL_SERVER_ERROR.getHttpStatus())
			.body(resultDto);
	}
}