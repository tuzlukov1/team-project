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
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.services.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Поиск пользователя по его идентификатору в БД.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            },
            tags = "Users"
    )
    @GetMapping("{id}")
    public ResponseEntity<User> getUserInfo(@Parameter(description = "идентификатор пользователя в БД")
                                            @PathVariable long id) {
        User foundUser = userService.findUserById(id);
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
                            schema = @Schema(implementation = User.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вывод измененных данных в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            },
            tags = "Users"
    )
    @PutMapping
    public User editUser(@RequestBody User user) {
        return userService.updateUser(user);
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
            tags = "Users"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "идентификатор пользователя в БД")
                                           @PathVariable long id) {
        User foundUser = userService.findUserById(id);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
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
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            },
            tags = "Users"
    )
    @GetMapping("{id}/chatId")
    public ResponseEntity<Optional<User>> getUserInfoByChatId(@Parameter(description = "идентификатор чата пользователя в БД")
                                                              @PathVariable long id) {
        Optional<User> foundUser = userService.findUserByChatId(id);
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
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    )
            },
            tags = "Users"
    )
    @GetMapping
    public ResponseEntity<Collection<User>> findUser(
            @Parameter(description = "Имя пользователя в мессенджере Telegram") @RequestParam(required = false) String userName,
            @Parameter(description = "Имя введеное пользователем при регистрации") @RequestParam(required = false) String fullName,
            @Parameter(description = "Номер телефона пользователя") @RequestParam(required = false) Long phone) {
        if (userName != null && !userName.isBlank()) {
            return ResponseEntity.ok(userService.findUserByUserName(userName));
        }
        if (fullName != null && !fullName.isBlank()) {
            return ResponseEntity.ok(userService.findUserByFullName(fullName));
        }
        if (phone != null && phone > 0) {
            return ResponseEntity.ok(userService.findUserByPhone(phone));
        }
        Collection<User> foundUsers = userService.findAllUsers();
        if (foundUsers == null) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(foundUsers);
        }
    }
}
