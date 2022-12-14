package pro.sky.teamproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.repository.UsersRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void updateUserTest() {
        final Long id = 1L;
        final String userName = "UserDog";
        final Long chatId = 123456L;


        User editUser = new User();
        editUser.setId(id);
        editUser.setUserName(userName);
        editUser.setChatId(chatId);

        User expected = new User();
        expected.setId(id);
        expected.setUserName(userName);
        expected.setChatId(chatId);

        when(usersRepository.save(editUser))
                .thenReturn(editUser);
        assertThat(userService.updateUser(editUser))
                .isEqualTo(expected);
    }

    @Test
    public void findUserByChatIdTest() {
        final Long id = 1L;
        final String userName = "UserDog";
        final Long chatId = 123456L;

        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setChatId(chatId);

        User expected = new User();
        expected.setId(id);
        expected.setUserName(userName);
        expected.setChatId(chatId);

        when(usersRepository.findUserByChatId(chatId))
                .thenReturn(user);
        assertThat(userService.findUserByChatId(chatId))
                .isEqualTo(Optional.of(expected));
    }
    @Test
    public void postWarningMessageByIdTest()  {
        final Long id = 1L;
        final String userName = "UserDog";
        final Long chatId = 123456L;
        final boolean haveWarning = false;
        final boolean haveWarningChanged = true;

        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setChatId(chatId);
        user.setHaveWarning(haveWarning);

        User expected = new User();
        expected.setId(id);
        expected.setUserName(userName);
        expected.setChatId(chatId);
        expected.setHaveWarning(haveWarningChanged);



        when(userService.setWarningStatus(chatId))
                .thenReturn(Optional.ofNullable(expected));
        assertThat(userService.setWarningStatus(chatId))
                .isEqualTo(Optional.of(expected));


    }
}