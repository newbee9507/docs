package practice.shop.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.shop.domain.People;
import practice.shop.dtos.PeopleDto;
import practice.shop.repository.PeopleRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/docs")
public class DocsController {

    private final PeopleRepository repository;

    @GetMapping("/info/{id}")
    public ResponseEntity<People> info(@PathVariable @Positive Long id){
        return new ResponseEntity<>(repository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/info/all")
    public ResponseEntity<List<People>> infoAll(Pageable pageable){
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<People> save(@RequestBody @Valid PeopleDto dto){
        People newPeople = People.dtoToPeople(dto);
        return new ResponseEntity<>(repository.save(newPeople), HttpStatus.CREATED);
    }
}
