package pro.sky.teamproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.repository.UsersRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        final String fullName = "User";
        final Long phone = 89994561122L;

        User editUser = new User();
        editUser.setId(id);
        editUser.setUserName(userName);
        editUser.setChatId(chatId);
        editUser.setFullName(fullName);
        editUser.setPhone(phone);

        User expected = new User();
        expected.setId(id);
        expected.setUserName(userName);
        expected.setChatId(chatId);
        expected.setFullName(fullName);
        expected.setPhone(phone);

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
        final String fullName = "User";
        final Long phone = 89994561122L;

        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setChatId(chatId);
        user.setFullName(fullName);
        user.setPhone(phone);

        User expected = new User();
        expected.setId(id);
        expected.setUserName(userName);
        expected.setChatId(chatId);
        expected.setFullName(fullName);
        expected.setPhone(phone);

        when(usersRepository.findUserByChatId(chatId))
                .thenReturn(user);
        assertThat(userService.findUserByChatId(chatId))
                .isEqualTo(Optional.of(expected));
    }

}