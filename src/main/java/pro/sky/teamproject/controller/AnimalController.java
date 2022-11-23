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
import pro.sky.teamproject.entity.Animal;
import pro.sky.teamproject.services.AnimalService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/animal")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "Добавление данных о новом животном в БД.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "При внесении данных, id не указывается," +
                            " БД генерирует его самостоятельно и выводит в ответе",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Animal.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод сохраненных данных в БД с присвоенным id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Animal.class)
                            )
                    )
            },
            tags = "Animals"
    )
    @PostMapping
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalService.addAnimal(animal);
    }

    @Operation(
            summary = "Поиск животного по его идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о животном",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Animal.class)
                            )
                    )
            },
            tags = "Animals"
    )
    @GetMapping("{id}")
    public ResponseEntity<Animal> getAnimalInfo(@Parameter(description = "идентификатор животного в БД")
                                                @PathVariable long id) {
        Animal foundAnimal = animalService.findAnimalById(id);
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
                            schema = @Schema(implementation = Animal.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Animal.class)
                            )
                    )
            },
            tags = "Animals"
    )
    @PutMapping
    public Animal editAnimal(@RequestBody Animal animal) {
        return animalService.editAnimal(animal);
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
            tags = "Animals"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAnimal(@Parameter(description = "идентификатор животного в БД")
                                             @PathVariable long id) {
        Animal foundAnimal = animalService.findAnimalById(id);
        if (foundAnimal == null) {
            return ResponseEntity.notFound().build();
        }
        animalService.deleteAnimal(id);
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
                                    array = @ArraySchema(schema = @Schema(implementation = Animal.class))
                            )
                    )
            },
            tags = "Animals"
    )
    @GetMapping
    public ResponseEntity<Collection<Animal>> findAnimal(
            @Parameter(description = "Имя животного") @RequestParam(required = false) String name,
            @Parameter(description = "Порода животного") @RequestParam(required = false) String breed,
            @Parameter(description = "Возраст животного") @RequestParam(required = false) Integer age) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(animalService.findAnimalByName(name));
        }
        if (breed != null && !breed.isBlank()) {
            return ResponseEntity.ok(animalService.findAnimalByBreed(breed));
        }
        if (age != null && age > 0) {
            return ResponseEntity.ok(animalService.findAnimalByAge(age));
        }
        Collection<Animal> foundAnimal = animalService.findAllAnimal();
        if (foundAnimal == null) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(foundAnimal);
        }
    }
}
