package practice.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import practice.shop.domain.People;
import practice.shop.dtos.PeopleDto;

import static practice.shop.domain.QPeople.people;

@Repository
@RequiredArgsConstructor
public class PeopleQueryRepository {

    private final JPAQueryFactory queryFactory;

}
