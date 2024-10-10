package org.auction.domain.qna.infrastructure.custom;

import static org.auction.domain.qna.domain.entity.QQuestionEntity.*;

import java.util.List;

import org.auction.domain.qna.domain.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public QuestionRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<QuestionEntity> findQuestionList(Long auctionId, Pageable pageable) {

		JPAQuery<QuestionEntity> query = queryFactory
			.selectFrom(questionEntity)
			.where(questionEntity.auctionEntity.auctionId.eq(auctionId))
			.where(questionEntity.deleted.isFalse());

		for (Sort.Order o : pageable.getSort()) {
			PathBuilder<?> pathBuilder = new PathBuilder<>(
				questionEntity.getType(),
				questionEntity.getMetadata()
			);
			query.orderBy(new OrderSpecifier(
				o.isAscending() ? Order.ASC : Order.DESC,
				pathBuilder.get(o.getProperty())
			));
		}

		// 페이징 적용
		List<QuestionEntity> questions = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(questionEntity.count())
			.from(questionEntity);

		return PageableExecutionUtils.getPage(questions, pageable, countQuery::fetchOne);
	}

}
