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
import pro.sky.teamproject.entity.AnimalCat;
import pro.sky.teamproject.services.CatService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/cat")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @Operation(summary = "Добавление данных о новом животном в БД.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "При внесении данных, id не указывается," +
                            " БД генерирует его самостоятельно и выводит в ответе",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalCat.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод сохраненных данных в БД с присвоенным id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalCat.class)
                            )
                    )
            },
            tags = "Shelter_Cat"
    )
    @PostMapping
    public AnimalCat createAnimal(@RequestBody AnimalCat animalCat) {
        return catService.addAnimal(animalCat);
    }

    @Operation(
            summary = "Поиск животного по его идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о животном",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalCat.class)
                            )
                    )
            },
            tags = "Shelter_Cat"
    )
    @GetMapping("{id}")
    public ResponseEntity<AnimalCat> getAnimalInfo(@Parameter(description = "идентификатор животного в БД")
                                                @PathVariable long id) {
        AnimalCat foundAnimalCat = catService.findAnimalById(id);
        if (foundAnimalCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundAnimalCat);
    }

    @Operation(summary = "Редактирование данных о животном в БД.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Для редактирования животного необходимо обязательно указать его идентификатор, " +
                            "если не будет указан, то будет создана новая запись в БД",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalCat.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnimalCat.class)
                            )
                    )
            },
            tags = "Shelter_Cat"
    )
    @PutMapping
    public AnimalCat editAnimal(@RequestBody AnimalCat animalCat) {
        return catService.editAnimal(animalCat);
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
            tags = "Shelter_Cat"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAnimal(@Parameter(description = "идентификатор животного в БД")
                                             @PathVariable long id) {
        AnimalCat foundAnimalCat = catService.findAnimalById(id);
        if (foundAnimalCat == null) {
            return ResponseEntity.notFound().build();
        }
        catService.deleteAnimal(id);
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
                                    array = @ArraySchema(schema = @Schema(implementation = AnimalCat.class))
                            )
                    )
            },
            tags = "Shelter_Cat"
    )
    @GetMapping
    public ResponseEntity<Collection<AnimalCat>> findAnimal(
            @Parameter(description = "Имя животного") @RequestParam(required = false) String name,
            @Parameter(description = "Порода животного") @RequestParam(required = false) String breed,
            @Parameter(description = "Возраст животного") @RequestParam(required = false) Integer age) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(catService.findAnimalByName(name));
        }
        if (breed != null && !breed.isBlank()) {
            return ResponseEntity.ok(catService.findAnimalByBreed(breed));
        }
        if (age != null && age > 0) {
            return ResponseEntity.ok(catService.findAnimalByAge(age));
        }
        Collection<AnimalCat> foundAnimalCat = catService.findAllAnimal();
        if (foundAnimalCat == null) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(foundAnimalCat);
        }
    }
}