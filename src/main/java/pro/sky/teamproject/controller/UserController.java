package pro.sky.teamproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
            tags = "users"
    )
    @GetMapping("{id}/chatId")
    public ResponseEntity<Optional<User>> getUserByChatId(@Parameter(description = "идентификатор чата пользователя в БД")
                                                          @PathVariable long id) {
        Optional<User> foundUser = userService.findUserByChatId(id);
        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);
    }
}
