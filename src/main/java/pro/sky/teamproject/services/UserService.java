package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.listener.TelegramBotUpdatesListener;
import pro.sky.teamproject.repository.UsersRepository;

import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UsersRepository usersRepository;


    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;

    }

    /**
     * сохранение данных о пользователе в БД UserGuest
     * при первом посещении телеграм бота.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param user данные о пользователе в формате JSON
     * @return запись в базе данных в формате JSON
     */
    public User updateUser(User user) {
        logger.debug("Was invoked method for edit user");
        return usersRepository.save(user);
    }

    /**
     * Поиск пользователя в БД по идентификатору чата.
     * Исползуется метод репозитория {@link UsersRepository#findUserByChatId(Long)}
     * @param id идентификатор чата пользователя, присваивается при регистрации
     *           через телеграм бота, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public Optional<User> findUserByChatId(Long id) {
        logger.debug("Was invoked method for get user info with chatId = {} ", id);
        return Optional.ofNullable(usersRepository.findUserByChatId(id));
    }

    /**
     * Изменение статуса пользователя в БД по haveWarning и отправка уведомления в чат-бот
     * Исползуется метод репозитория {@link UsersRepository#findUserByChatId(Long)}
     * @param chatId идентификатор чата пользователя, присваивается при регистрации
     *           через телеграм бота, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public Optional<User> setWarningStatus(Long chatId) {
        User user = usersRepository.findUserByChatId(chatId);
        user.setHaveWarning(true);
        updateUser(user);
        return Optional.ofNullable(user);
    }
}
