package practice.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.shop.dtos.PeopleDto;

import java.util.Objects;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2)
    private String name;

    @NotBlank
    @Positive
    private Integer age;

    @NotBlank
    @Column(length = 14, unique = true)
    private String personalNumber;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof People people)) return false;
        return Objects.equals(id, people.id) && Objects.equals(personalNumber, people.personalNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personalNumber);
    }

    protected People() {
    }

    public static People dtoToPeople(PeopleDto dto){
        return People.builder().name(dto.getName())
                .age(dto.getAge())
                .personalNumber(dto.getPersonalNumber())
                .build();
    }
}
