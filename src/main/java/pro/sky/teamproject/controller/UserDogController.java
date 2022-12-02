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
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.services.UserDogService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/userDog")
public class UserDogController {

    private final UserDogService userDogService;

    public UserDogController(UserDogService userDogService) {
        this.userDogService = userDogService;
    }

    @Operation(
            summary = "Поиск пользователя по его идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDog.class)
                            )
                    )
            },
            tags = "Dog_Owners"
    )
    @GetMapping("{id}")
    public ResponseEntity<UserDog> getUserInfo(@Parameter(description = "идентификатор пользователя в БД")
                                            @PathVariable long id) {
        UserDog foundUser = userDogService.findUserById(id);
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
                            schema = @Schema(implementation = UserDog.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDog.class)
                            )
                    )
            },
            tags = "Dog_Owners"
    )
    @PutMapping
    public UserDog editUser(@RequestBody UserDog userDog) {
        return userDogService.updateUser(userDog);
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
            tags = "Dog_Owners"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "идентификатор пользователя в БД")
                                           @PathVariable long id) {
        UserDog foundUser = userDogService.findUserById(id);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        userDogService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Поиск пользователя по его идентификатору чата в БД. " +
                    "chatId присваивается при регистрации пользователя через " +
                    "телеграм бота",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDog.class)
                            )
                    )
            },
            tags = "Dog_Owners"
    )
    @GetMapping("{id}/chatId")
    public ResponseEntity<Optional<UserDog>> getUserInfoByChatId(@Parameter(description = "идентификатор чата пользователя в БД")
                                                              @PathVariable long id) {
        Optional<UserDog> foundUser = userDogService.findUserByChatId(id);
        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);
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
                                    array = @ArraySchema(schema = @Schema(implementation = UserDog.class))
                            )
                    )
            },
            tags = "Dog_Owners"
    )
    @GetMapping
    public ResponseEntity<Collection<UserDog>> findUser(
            @Parameter(description = "Имя пользователя в мессенджере Telegram") @RequestParam(required = false) String userName,
            @Parameter(description = "Имя введеное пользователем при регистрации") @RequestParam(required = false) String fullName,
            @Parameter(description = "Номер телефона пользователя") @RequestParam(required = false) Long phone) {
        if (userName != null && !userName.isBlank()) {
            return ResponseEntity.ok(userDogService.findUserByUserName(userName));
        }
        if (fullName != null && !fullName.isBlank()) {
            return ResponseEntity.ok(userDogService.findUserByFullName(fullName));
        }
        if (phone != null && phone > 0) {
            return ResponseEntity.ok(userDogService.findUserByPhone(phone));
        }
        Collection<UserDog> foundUsers = userDogService.findAllUsers();
        if (foundUsers == null) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(foundUsers);
        }
    }
}
