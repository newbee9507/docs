package practice.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.shop.repository.PeopleQueryRepository;


@RestController
@RequestMapping("/api/paging")
@AllArgsConstructor
public class PagingController {

    private final PeopleQueryRepository queryRepository;

    @GetMapping("/paging")
    public void paging(Pageable pageable) {
        queryRepository.findAllPaging(pageable);
    }
}
