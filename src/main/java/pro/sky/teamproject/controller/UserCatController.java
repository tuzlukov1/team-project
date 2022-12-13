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
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.services.UserCatService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/userCat")
public class UserCatController {

    private final UserCatService userCatService;

    public UserCatController(UserCatService userCatService) {
        this.userCatService = userCatService;
    }

    @Operation(
            summary = "Поиск пользователя по его идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserCat.class)
                            )
                    )
            },
            tags = "Cat_Owners"
    )
    @GetMapping("{id}")
    public ResponseEntity<UserCat> getUserInfo(@Parameter(description = "идентификатор пользователя в БД")
                                            @PathVariable long id) {
        UserCat foundUser = userCatService.findUserCatById(id);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);
    }

    @Operation(summary = "Редактирование данных о пользователе в БД.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Для редактирования пользователя необходимо обязательно указать его идентификатор, " +
                            "если не будет указан, то будет создана новая запись в БД, " +
                            "а также не забудьте ввести данные в поле chatId, " +
                            "иначе вы не сможете связаться с пользователем через telegram bot",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserCat.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserCat.class)
                            )
                    )
            },
            tags = "Cat_Owners"
    )
    @PutMapping
    public UserCat editUser(@RequestBody UserCat userCat) {
        return userCatService.updateUserCat(userCat);
    }

    @Operation(
            summary = "Удаление данных о пользователе по его идентификатору в БД.",
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
            tags = "Cat_Owners"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "идентификатор пользователя в БД")
                                           @PathVariable long id) {
        UserCat foundUser = userCatService.findUserCatById(id);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        userCatService.deleteUserCat(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Поиск пользователей по имени из мессенджера Telegram, " +
                    "или по имени введеному при регистрации через Telegram bot, " +
                    "или по номеру телефона, поиск производиться только по одному из параметров. " +
                    "Если ни один из параметров не выбран, тогда выводится список всех пользователей.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список пользователей",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = UserCat.class))
                            )
                    )
            },
            tags = "Cat_Owners"
    )
    @GetMapping
    public ResponseEntity<Collection<UserCat>> findUser(
            @Parameter(description = "ID пользователя в мессенджере Telegram") @RequestParam(required = false) Long userId,
            @Parameter(description = "Имя введеное пользователем при регистрации") @RequestParam(required = false) String fullName,
            @Parameter(description = "Номер телефона пользователя") @RequestParam(required = false) Long phone) {
        if (userId != null) {
            return ResponseEntity.ok(userCatService.findUserCatByUserId(userId));
        }
        if (fullName != null && !fullName.isBlank()) {
            return ResponseEntity.ok(userCatService.findUserCatByFullName(fullName));
        }
        if (phone != null && phone > 0) {
            return ResponseEntity.ok(userCatService.findUserCatByPhone(phone));
        }
        Collection<UserCat> foundUsers = userCatService.findAllUsersCat();
        if (foundUsers == null) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(foundUsers);
        }
    }
}
