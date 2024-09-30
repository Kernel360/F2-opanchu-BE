package org.auction.client.auction.application;

import static org.auction.client.common.code.AuctionCode.*;
import static org.auction.client.common.code.ImageCode.*;
import static org.auction.client.common.code.MemberCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.auction.client.auction.interfaces.request.CreateAuctionRequest;
import org.auction.client.auction.interfaces.response.AuctionDetailResponse;
import org.auction.client.auction.interfaces.response.CreateAuctionResponse;
import org.auction.client.exception.auction.AuctionNotFoundException;
import org.auction.client.exception.image.ImageDeletionException;
import org.auction.client.exception.image.ImageNotFoundException;
import org.auction.client.exception.member.MemberNotFoundException;
import org.auction.client.image.application.AwsS3Service;
import org.auction.domain.auction.domain.entity.AuctionEntity;
import org.auction.domain.auction.domain.enums.AuctionStatus;
import org.auction.domain.auction.infrastructure.AuctionRepository;
import org.auction.domain.image.domain.entity.ImageEntity;
import org.auction.domain.image.infrastructure.ImageRepository;
import org.auction.domain.member.domain.entity.MemberEntity;
import org.auction.domain.member.infrastructure.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final AwsS3Service awsS3Service;
	private final ImageRepository imageRepository;
	private final MemberRepository memberRepository;

	// TODO: OAUTH2.0 구현되면 User 추가 필요
	@Transactional
	public CreateAuctionResponse addAuction(
		CreateAuctionRequest request,
		List<MultipartFile> images,
		Long memberId
	) {

		MemberEntity memberEntity = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

		AuctionEntity auctionEntity = AuctionEntity.builder()
			.title(request.title())
			.content(request.content())
			.startPrice(request.startPrice())
			.bidUnit(request.bidUnit())
			.auctionStatus(AuctionStatus.BIDDING)
			.auctionType(request.auctionType())
			.startDate(request.startDate())
			.endDate(request.endDate())
			.memberEntity(memberEntity)
			.build();

		if (images == null || images.isEmpty()) {
			// TODO: exception 수정 필요
			throw new ImageNotFoundException(IMAGE_NOT_FOUND);
		}

		for (MultipartFile image : images) {
			// image upload
			String s3Key = awsS3Service.uploadFile(image);
			String fileName = image.getOriginalFilename();

			// Create ImageEntity
			ImageEntity imageEntity = ImageEntity.builder()
				.fileName(fileName)
				.s3Key(s3Key)
				.auctionEntity(auctionEntity)
				.build();

			auctionEntity.addImage(imageEntity);
		}

		auctionRepository.save(auctionEntity);
		log.debug("Create auction post finished");
		return CreateAuctionResponse.toDto(auctionEntity);
	}

	// 게시글 상세 조회
	@Transactional(readOnly = true)
	public AuctionDetailResponse findAuctionDetails(
		Long auctionId
	) {

		AuctionEntity auctionEntity = auctionRepository.findByIdWithImages(auctionId)
			.orElseThrow(() -> new AuctionNotFoundException(AUCTION_NOT_FOUND));

		List<String> imageKeys = auctionEntity.getImages().stream()
			.map(ImageEntity::getS3Key)
			.collect(Collectors.toList());

		log.debug("Find auction finished with images {}", imageKeys);
		log.debug("Find auction finished with auctionId {}", auctionId);
		return new AuctionDetailResponse(
			auctionEntity.getTitle(),
			auctionEntity.getContent(),
			auctionEntity.getStartPrice(),
			auctionEntity.getBidUnit(),
			auctionEntity.getAuctionType(),
			auctionEntity.getStartDate(),
			auctionEntity.getEndDate(),
			imageKeys
		);
	}

	@Transactional
	public void removeAuction(
		Long auctionId
	) {
		AuctionEntity auctionEntity = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new AuctionNotFoundException(AUCTION_NOT_FOUND));

		try {
			// 이미지 삭제
			List<ImageEntity> images = auctionEntity.getImages();
			for (ImageEntity image : images) {
				awsS3Service.deleteFile(image.getS3Key());
			}
		} catch (Exception e) {
			throw new ImageDeletionException(IMAGE_DELETION_ERROR);
		}

		log.debug("Remove auction finished with auctionId {}", auctionId);

		auctionRepository.delete(auctionEntity);

	}
}
