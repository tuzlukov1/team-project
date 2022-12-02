package pro.sky.teamproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.teamproject.entity.AnimalDog;
import pro.sky.teamproject.services.DogService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/dog")
public class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @Operation(summary = "Добавление данных о новом животном в БД.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "При внесении данных, id не указывается," +
                            " БД генерирует его самостоятельно и выводит в ответе",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalDog.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод сохраненных данных в БД с присвоенным id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalDog.class)
                            )
                    )
            },
            tags = "Shelter_dog"
    )
    @PostMapping
    public AnimalDog createAnimal(@RequestBody AnimalDog animalDog) {
        return dogService.addAnimal(animalDog);
    }

    @Operation(
            summary = "Поиск животного по его идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о животном",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalDog.class)
                            )
                    )
            },
            tags = "Shelter_dog"
    )
    @GetMapping("{id}")
    public ResponseEntity<AnimalDog> getAnimalInfo(@Parameter(description = "идентификатор животного в БД")
                                                @PathVariable long id) {
        AnimalDog foundAnimal = dogService.findAnimalById(id);
        if (foundAnimal == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundAnimal);
    }

    @Operation(summary = "Редактирование данных о животном в БД.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Для редактирования животного необходимо обязательно указать его идентификатор, " +
                            "если не будет указан, то будет создана новая запись в БД",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalDog.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalDog.class)
                            )
                    )
            },
            tags = "Shelter_dog"
    )
    @PutMapping
    public AnimalDog editAnimal(@RequestBody AnimalDog animalDog) {
        return dogService.editAnimal(animalDog);
    }

    @Operation(
            summary = "Удаление данных о животном по его идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные удалены"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Удаляемая запись не найдена"
                    )
            },
            tags = "Shelter_dog"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAnimal(@Parameter(description = "идентификатор животного в БД")
                                             @PathVariable long id) {
        AnimalDog foundAnimal = dogService.findAnimalById(id);
        if (foundAnimal == null) {
            return ResponseEntity.notFound().build();
        }
        dogService.deleteAnimal(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Поиск животных по имени, или породе, или возрасту, поиск производиться только по одному из параметров. " +
                    "Если ни один из параметров не выбран, тогда выводится список всех животных приюта.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список животных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AnimalDog.class))
                            )
                    )
            },
            tags = "Shelter_dog"
    )
    @GetMapping
    public ResponseEntity<Collection<AnimalDog>> findAnimal(
            @Parameter(description = "Имя животного") @RequestParam(required = false) String name,
            @Parameter(description = "Порода животного") @RequestParam(required = false) String breed,
            @Parameter(description = "Возраст животного") @RequestParam(required = false) Integer age) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(dogService.findAnimalByName(name));
        }
        if (breed != null && !breed.isBlank()) {
            return ResponseEntity.ok(dogService.findAnimalByBreed(breed));
        }
        if (age != null && age > 0) {
            return ResponseEntity.ok(dogService.findAnimalByAge(age));
        }
        Collection<AnimalDog> foundAnimal = dogService.findAllAnimal();
        if (foundAnimal == null) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(foundAnimal);
        }
    }
}