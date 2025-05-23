package practice.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.shop.domain.People;

public interface PeopleRepository extends JpaRepository<People, Long> {
}
