package practice.shop.repository;

import com.querydsl.core.QueryModifiers;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import practice.shop.domain.People;
import practice.shop.dtos.PeopleDto;

import java.util.List;

import static practice.shop.domain.QPeople.people;

@Repository
@RequiredArgsConstructor
public class PeopleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<People> findAllPaging(Pageable pageable) {
        List<People> pagingList = queryFactory.select(people)
                .from(people)
                .orderBy()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(people.count())
                            .from(people);

        return PageableExecutionUtils
                .getPage(pagingList, pageable, countQuery::fetchOne);
    }

}
