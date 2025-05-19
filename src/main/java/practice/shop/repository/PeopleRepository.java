package practice.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.shop.domain.People;

public interface PeopleRepository extends JpaRepository<People, Long> {
}
