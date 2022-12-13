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
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.entity.UserReport;
import pro.sky.teamproject.services.UserReportService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userReports")
public class UserReportController {

    private final UserReportService userReportService;

    public UserReportController(UserReportService userReportService) {
        this.userReportService = userReportService;
    }

    @Operation(
            summary = "список людей, что просрочили отправку отчета более чем на 2 дня",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserReport.class)
                            )
                    )
            },
            tags = "User_Reports"
    )
    @GetMapping
    public Optional<List<UserReport>> findUsersMoreThanTwoDaysReports(){
        return userReportService.findUsersMoreThanTwoDaysReports();
    }

    @Operation(
            summary = "список отчетов, написанных пользователем с указанным " +
                    "идентификатором в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены данные о пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserReport.class)
                            )
                    )
            },
            tags = "User_Reports"
    )
    @GetMapping("{id}")
    Optional<List<UserReport>> getUserReportsByUserId(@Parameter(description = "идентификатор пользователя в БД")
                                                            @PathVariable long id) {
        return userReportService.findUserReportsByUserId(id);
    }

}
