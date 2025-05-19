package practice.shop.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PeopleDto {

    @NotBlank
    private String name;

    @Positive
    private Integer age;

    @NotBlank
    private String personalNumber;
}
