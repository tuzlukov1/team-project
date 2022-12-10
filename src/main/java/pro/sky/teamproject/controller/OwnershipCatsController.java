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
import pro.sky.teamproject.entity.OwnershipCats;
import pro.sky.teamproject.services.OwnershipCatsService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;

@RestController
@RequestMapping("/ownershipCats")
public class OwnershipCatsController {

    private final OwnershipCatsService ownershipCatsService;

    public OwnershipCatsController(OwnershipCatsService ownershipCatsService) {
        this.ownershipCatsService = ownershipCatsService;
    }

    @Operation(summary = "Создание новой пары (владелец и питомец) в БД, назначение испытательного срока",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод сохраненных данных в БД с присвоенным id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipCats.class)
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
            tags = "OwnershipCats"
    )
    @PostMapping
    public ResponseEntity<OwnershipCats> createOwnershipCat(@Parameter(description = "идентификатор пользователя в БД из таблицы users_cat")
                                                            @RequestParam long userCatId,
                                                            @Parameter(description = "идентификатор животного в БД из таблицы animals_cat")
                                                            @RequestParam long animalCatId,
                                                            @Parameter(description = "колличество дней испытательного срока")
                                                            @RequestParam int probationDays) {
        if (userCatId <= 0 || animalCatId <= 0 || probationDays < 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipCats foundOwnershipCat = ownershipCatsService.addOwnershipCat(userCatId, animalCatId, probationDays);
        if (foundOwnershipCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipCat);
    }

    @Operation(
            summary = "Поиск пары (владелец и питомец) по идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные пары (владелец и питомец)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipCats.class)
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
            tags = "OwnershipCats"
    )
    @GetMapping("{id}")
    public ResponseEntity<OwnershipCats> getOwnershipCatInfo(@Parameter(description = "идентификатор пары (владелец и питомец)")
                                                             @PathVariable long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipCats foundOwnershipCat = ownershipCatsService.findOwnershipCatById(id);
        if (foundOwnershipCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipCat);
    }

    @Operation(summary = "Редактирование данных пары (владелец и питомец) в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipCats.class)
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
            tags = "OwnershipCats"
    )
    @PutMapping
    public ResponseEntity<OwnershipCats> updateOwnershipCat(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_cats")
                                                            @RequestParam long id,
                                                            @Parameter(description = "идентификатор пользователя в БД из таблицы users_cat")
                                                            @RequestParam long userCatId,
                                                            @Parameter(description = "идентификатор животного в БД из таблицы animals_cat")
                                                            @RequestParam long animalCatId) {
        if (id <= 0 || userCatId <= 0 || animalCatId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipCats foundOwnershipCat = ownershipCatsService.editOwnershipCat(id, userCatId, animalCatId);
        if (foundOwnershipCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipCat);
    }

    @Operation(summary = "Редактирование продолжительности испытательного срока для пары (владелец и питомец) в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipCats.class)
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
            tags = "OwnershipCats"
    )
    @PutMapping("/probation-days")
    public ResponseEntity<OwnershipCats> updateProbationDaysOwnershipCat(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_cats")
                                                                         @RequestParam long id,
                                                                         @Parameter(description = "колличество дней испытательного срока")
                                                                         @RequestParam int probationDays) {
        if (id <= 0 || probationDays < 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipCats foundOwnershipCat = ownershipCatsService.editProbationDays(id, probationDays);
        if (foundOwnershipCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipCat);
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
            tags = "OwnershipCats"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOwnershipCat(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_cats")
                                                   @PathVariable long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        OwnershipCats foundOwnershipCat = ownershipCatsService.findOwnershipCatById(id);
        if (foundOwnershipCat == null) {
            return ResponseEntity.notFound().build();
        }
        ownershipCatsService.deleteOwnershipCat(id);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Редактирование статуса прохождения испытательного срока для пары (владелец и питомец) в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OwnershipCats.class)
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
            tags = "OwnershipCats"
    )
    @PutMapping("/passage-probation")
    public ResponseEntity<OwnershipCats> updatePassageProbationOwnershipCat(@Parameter(description = "идентификатор пары (владелец и питомец) из таблицы ownership_cats")
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
        OwnershipCats foundOwnershipCat = ownershipCatsService.editPassageProbation(id, passageProbation);
        if (foundOwnershipCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundOwnershipCat);
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
                                    array = @ArraySchema(schema = @Schema(implementation = OwnershipCats.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В статус принимаются только: \"пройден\", \"не пройден\", \"не окончен\". " +
                                    "Формат ввода даты должен соответствовать примеру: 2022-12-10",
                            content = @Content
                    )
            },
            tags = "OwnershipCats"
    )
    @GetMapping
    public ResponseEntity<Collection<OwnershipCats>> findOwnershipCats(
            @Parameter(description = "Дата окончания испытательного срока, ввод данных строго по примеру: \"2022-12-10\"")
            @RequestParam(required = false) String endDateProbation,
            @Parameter(description = "Статус испытательного срока, вводяться только следующие ключевые значения: " +
                    "пройден / не пройден / не окончен")
            @RequestParam(required = false) String passageProbation) {
        if (endDateProbation != null && !endDateProbation.isBlank()) {
            try {
                LocalDate localDate = LocalDate.parse(endDateProbation);
                return ResponseEntity.ok(ownershipCatsService.findOwnershipCatsByEndDateProbation(localDate));
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
            return ResponseEntity.ok(ownershipCatsService.findOwnershipCatByPassageProbation(passageProbation));
        }
        return ResponseEntity.ok(ownershipCatsService.findAllOwnershipCats());
    }
}
