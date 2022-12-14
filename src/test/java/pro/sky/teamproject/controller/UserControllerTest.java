package pro.sky.teamproject.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.repository.UsersRepository;
import pro.sky.teamproject.services.UserService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersRepository usersRepository;

    @SpyBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void getUserInfoByChatIdPositiveTest() throws Exception {
        final Long id = 1L;
        final String userName = "User";
        final Long chatId = 123456L;

        User user = new User();
        user.setId(id);
        user.setChatId(chatId);
        user.setUserName(userName);

        when(usersRepository.findUserByChatId(any(Long.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/" + id +"/chatId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userName").value(userName))
                .andExpect(jsonPath("$.chatId").value(chatId));
    }

    @Test
    public void getUserInfoByChatIdNegativeTest() throws Exception {
        final long id = 999888L;

        when(usersRepository.findUserByChatId(ArgumentMatchers.eq(id)))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/userDog/" + id +"/chatId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postWarningMessageByIdTest() throws Exception {
        final Long id = 1L;
        final String userName = "User";
        final Long chatId = 123456L;
        final boolean haveWarning = false;
        final boolean haveWarningChanged = true;

        User user = new User();
        user.setId(id);
        user.setChatId(chatId);
        user.setUserName(userName);
        user.setHaveWarning(haveWarning);



        when(usersRepository.findUserByChatId(any(Long.class)))
                .thenReturn(user);
        when(userService.setWarningStatus(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));



        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/" + id +"/chatId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userName").value(userName))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.haveWarning").value(haveWarningChanged));
    }
}
