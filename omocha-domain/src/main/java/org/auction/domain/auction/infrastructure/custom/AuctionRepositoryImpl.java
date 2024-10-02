package org.auction.domain.auction.infrastructure.custom;

import static org.auction.domain.auction.domain.entity.QAuctionEntity.*;
import static org.auction.domain.image.domain.entity.QImageEntity.*;
import static org.springframework.util.ObjectUtils.*;

import java.util.List;
import java.util.Optional;

import org.auction.domain.auction.domain.entity.AuctionEntity;
import org.auction.domain.auction.infrastructure.condition.AuctionSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class AuctionRepositoryImpl implements AuctionRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public AuctionRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AuctionEntity> findByIdWithImages(Long id) {

		AuctionEntity auction = queryFactory
			.selectFrom(auctionEntity)
			.leftJoin(auctionEntity.images, imageEntity).fetchJoin()
			.where(auctionEntity.auctionId.eq(id))
			.fetchOne();

		return Optional.ofNullable(auction);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AuctionEntity> searchAuctionList(AuctionSearchCondition condition, Pageable pageable) {
		// 경매와 이미지를 Fetch Join으로 함께 조회
		JPAQuery<AuctionEntity> query = queryFactory
			.selectDistinct(auctionEntity)
			.from(auctionEntity)
			.leftJoin(auctionEntity.images, imageEntity).fetchJoin()
			.where(
				titleContains(condition.title())
			);

		for (Sort.Order o : pageable.getSort()) {
			PathBuilder<?> pathBuilder = new PathBuilder<>(
				auctionEntity.getType(),
				auctionEntity.getMetadata()
			);
			query.orderBy(new OrderSpecifier(
				o.isAscending() ? Order.ASC : Order.DESC,
				pathBuilder.get(o.getProperty())
			));
		}

		// 페이징 적용
		List<AuctionEntity> auctions = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		/*// DTO로 변환
		List<AuctionListResponse> content = auctions.stream()
			.map(auction -> {
				List<String> imageKeys = auction.getImages().stream()
					.map(ImageEntity::getS3Key)
					.collect(Collectors.toList());
				return new AuctionListResponse(
					auction.getAuctionId(),
					auction.getTitle(),
					auction.getStartPrice(),
					auction.getStartDate(),
					auction.getEndDate(),
					imageKeys
				);
			})
			.collect(Collectors.toList());*/

		JPAQuery<Long> countQuery = queryFactory
			.select(auctionEntity.count())
			.from(auctionEntity)
			.leftJoin(auctionEntity.images, imageEntity)
			.where(
				titleContains(condition.title())
			);

		return PageableExecutionUtils.getPage(auctions, pageable, countQuery::fetchOne);
	}

	private BooleanExpression titleContains(String title) {
		return isEmpty(title) ? null : auctionEntity.title.containsIgnoreCase(title);
	}

}
