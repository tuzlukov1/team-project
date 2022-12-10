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
import pro.sky.teamproject.entity.OwnershipDogs;
import pro.sky.teamproject.services.OwnershipDogsService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;

@RestController
@RequestMapping("/ownershipDogs")
public class OwnershipDogsController {

    private final OwnershipDogsService ownershipDogsService;

    public OwnershipDogsController(OwnershipDogsService ownershipDogsService) {
        this.ownershipDogsService = ownershipDogsService;
    }

    @Operation(summary = "Создание новой пары (владелец и питомец) в БД, назначение испытательного срока",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод сохраненных данных в БД с присвоенным id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipDogs.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Данные по введенным id не найдены",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "идентификатор не может быть меньше или равен 0," +
                                    " колличество дней не может быть отрицательным",
                            content = @Content
                    )
            },
            tags = "OwnershipDogs"
    )
    @PostMapping
    public ResponseEntity<OwnershipDogs> createOwnershipDog(@Parameter(description = "идентификатор пользователя в БД из таблицы users_dog")
                                                            @RequestParam long userDogId,
                                                            @Parameter(description = "идентификатор животного в БД из таблицы animals_dog")
                                                            @RequestParam long animalDogId,
                                                            @Parameter(description = "колличество дней испытательного срока")
                                                            @RequestParam int probationDays) {
        if (userDogId <= 0 || animalDogId <= 0 || probationDays < 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipDogs foundOwnershipDog = ownershipDogsService.addOwnershipDog(userDogId, animalDogId, probationDays);
        if (foundOwnershipDog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipDog);
    }

    @Operation(
            summary = "Поиск пары (владелец и питомец) по идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные пары (владелец и питомец)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipDogs.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Данные по введенным id не найдены",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "идентификатор не может быть меньше или равен 0",
                            content = @Content
                    )
            },
            tags = "OwnershipDogs"
    )
    @GetMapping("{id}")
    public ResponseEntity<OwnershipDogs> getOwnershipDogInfo(@Parameter(description = "идентификатор пары (владелец и питомец)")
                                                             @PathVariable long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipDogs foundOwnershipDog = ownershipDogsService.findOwnershipDogById(id);
        if (foundOwnershipDog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipDog);
    }

    @Operation(summary = "Редактирование данных пары (владелец и питомец) в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipDogs.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Данные по введенным id не найдены",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "идентификатор не может быть меньше или равен 0",
                            content = @Content
                    )
            },
            tags = "OwnershipDogs"
    )
    @PutMapping
    public ResponseEntity<OwnershipDogs> updateOwnershipDog(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_dogs")
                                                            @RequestParam long id,
                                                            @Parameter(description = "идентификатор пользователя в БД из таблицы users_dog")
                                                            @RequestParam long userDogId,
                                                            @Parameter(description = "идентификатор животного в БД из таблицы animals_dog")
                                                            @RequestParam long animalDogId) {
        if (id <= 0 || userDogId <= 0 || animalDogId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipDogs foundOwnershipDog = ownershipDogsService.editOwnershipDog(id, userDogId, animalDogId);
        if (foundOwnershipDog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipDog);
    }

    @Operation(summary = "Редактирование продолжительности испытательного срока для пары (владелец и питомец) в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipDogs.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Данные по введенным id не найдены",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "идентификатор не может быть меньше или равен 0",
                            content = @Content
                    )
            },
            tags = "OwnershipDogs"
    )
    @PutMapping("/probation-days")
    public ResponseEntity<OwnershipDogs> updateProbationDaysOwnershipDog(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_dogs")
                                                                         @RequestParam long id,
                                                                         @Parameter(description = "колличество дней испытательного срока")
                                                                         @RequestParam int probationDays) {
        if (id <= 0 || probationDays < 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipDogs foundOwnershipDog = ownershipDogsService.editProbationDays(id, probationDays);
        if (foundOwnershipDog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipDog);
    }

    @Operation(
            summary = "Удаление данных пары (владелец и питомец) по идентификатору из БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные удалены"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Удаляемая запись не найдена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "идентификатор не может быть меньше или равен 0"
                    )
            },
            tags = "OwnershipDogs"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOwnershipDog(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_dogs")
                                                   @PathVariable long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipDogs foundOwnershipDog = ownershipDogsService.findOwnershipDogById(id);
        if (foundOwnershipDog == null) {
            return ResponseEntity.notFound().build();
        }
        ownershipDogsService.deleteOwnershipDog(id);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Редактирование статуса прохождения испытательного срока для пары (владелец и питомец) в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipDogs.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Данные по введенным id не найдены",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "идентификатор не может быть меньше или равен 0. " +
                                    "В статус принимаются только: \"Пройден\", \"Не пройден\", \"Не окончен\".",
                            content = @Content
                    )
            },
            tags = "OwnershipDogs"
    )
    @PutMapping("/passage-probation")
    public ResponseEntity<OwnershipDogs> updatePassageProbationOwnershipDog(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_dogs")
                                                                            @RequestParam long id,
                                                                            @Parameter(description = "вносится статус о прохождении испытательного срока",
                                                                                    example = "Пройден" + " / Не пройден / " + "Не окончен")
                                                                            @RequestParam String passageProbation) {
        if (id <= 0 ||
                !passageProbation.equalsIgnoreCase("Пройден") &&
                        !passageProbation.equalsIgnoreCase("Не пройден") &&
                        !passageProbation.equalsIgnoreCase("Не окончен")) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipDogs foundOwnershipDog = ownershipDogsService.editPassageProbation(id, passageProbation);
        if (foundOwnershipDog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipDog);
    }


    @Operation(
            summary = "Поиск записией пар (владелецев и питомцев) в БД по дате окончания испытательного срока " +
                    "или по присвоенному статусу прохождения испытательного срока. Поиск производиться только по" +
                    " одному из параметров. Если ни один из параметров не выбран, " +
                    "тогда выводится список всех владелецев и их питомцев.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список владелецев и питомцев",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = OwnershipDogs.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В статус принимаются только: \"пройден\", \"не пройден\", \"не окончен\". " +
                                    "Формат ввода даты должен соответствовать примеру: 2022-12-10",
                            content = @Content
                    )
            },
            tags = "OwnershipDogs"
    )
    @GetMapping
    public ResponseEntity<Collection<OwnershipDogs>> findOwnershipDogs(
            @Parameter(description = "Дата окончания испытательного срока, ввод данных строго по примеру: \"2022-12-10\"")
            @RequestParam(required = false) String endDateProbation,
            @Parameter(description = "Статус испытательного срока, вводяться только следующие ключевые значения: " +
                    "пройден / не пройден / не окончен")
            @RequestParam(required = false) String passageProbation) {
        if (endDateProbation != null && !endDateProbation.isBlank()) {
            try {
                LocalDate localDate = LocalDate.parse(endDateProbation);
                return ResponseEntity.ok(ownershipDogsService.findOwnershipDogsByEndDateProbation(localDate));
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (passageProbation != null && !passageProbation.isBlank()) {
            if (!passageProbation.equalsIgnoreCase("Пройден") &&
                    !passageProbation.equalsIgnoreCase("Не пройден") &&
                    !passageProbation.equalsIgnoreCase("Не окончен")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(ownershipDogsService.findOwnershipDogByPassageProbation(passageProbation));
        }
        return ResponseEntity.ok(ownershipDogsService.findAllOwnershipDogs());
    }
}
