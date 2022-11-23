package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.repository.UsersRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Поиск пользователя в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id идентификатор пользователя, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public User findUserById(Long id) {
        logger.debug("Was invoked method for get user info with id = {} ", id);
        User user = usersRepository.findById(id).orElse(null);
        logger.warn("Response {} ", user);
        return user;
    }

    /**
     * Редактирование данных о пользователе в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param user данные о пользователе в формате JSON
     * @return измененная запись в базе данных в формате JSON
     */
    public User updateUser(User user) {
        logger.debug("Was invoked method for edit user");
        return usersRepository.save(user);
    }

    /**
     * Удаление записи о пользователе из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор пользователя, не может быть null
     */
    public void deleteUser(long id) {
        logger.debug("Was invoked method for delete user");
        usersRepository.deleteById(id);
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
     * Поиск пользователя в БД по UserName.
     * Исползуется метод репозитория {@link UsersRepository#findUsersByUserNameIgnoreCase(String)}
     * @param userName имя пользователя в мессенджере Telegram
     * @return список пользователей в формате JSON
     */
    public Collection<User> findUserByUserName(String userName) {
        logger.debug("Was invoked method for get user info with user name");
        return usersRepository.findUsersByUserNameIgnoreCase(userName);
    }

    /**
     * Поиск пользователя в БД по FullName.
     * Исползуется метод репозитория {@link UsersRepository#findUsersByFullNameIgnoreCase(String)}
     * @param fullName имя введенное пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<User> findUserByFullName(String fullName) {
        logger.debug("Was invoked method for get user info with full name");
        return usersRepository.findUsersByFullNameIgnoreCase(fullName);
    }

    /**
     * Поиск пользователя в БД по номеру телефона.
     * Исползуется метод репозитория {@link UsersRepository#findUsersByPhone(Long)}
     * @param phone номер телефона введенный пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<User> findUserByPhone(Long phone) {
        logger.debug("Was invoked method for get user info with phone number");
        return usersRepository.findUsersByPhone(phone);
    }

    /**
     * Показывает все записи пользователей в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех пользователей из БД, в фомате JSON
     */
    public Collection<User> findAllUsers() {
        logger.debug("Was invoked method for find all");
        return usersRepository.findAll();
    }
}
