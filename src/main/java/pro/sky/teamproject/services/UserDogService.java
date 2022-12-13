package pro.sky.teamproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.UserCat;
import pro.sky.teamproject.entity.UserDog;
import pro.sky.teamproject.repository.UsersDogRepository;

import java.util.Collection;

@Service
public class UserDogService {

    private final Logger logger = LoggerFactory.getLogger(UserDogService.class);

    private final UsersDogRepository usersDogRepository;

    public UserDogService(UsersDogRepository usersDogRepository) {
        this.usersDogRepository = usersDogRepository;
    }

    /**
     * Поиск пользователя в БД по идентификатору.
     * Исползуется метод репозитория {@link JpaRepository#findById(Object)}
     * @param id идентификатор пользователя, не может быть null
     * @return данные о пользователе в формате JSON
     */
    public UserDog findUserDogById(Long id) {
        logger.debug("Was invoked method for get userDog info with id = {} ", id);
        UserDog userDog = usersDogRepository.findById(id).orElse(null);
        logger.warn("Response {} ", userDog);
        return userDog;
    }

    public UserDog findUserDogByChatId(Long userId) {
        logger.debug("Was invoked method for get userDog info with id = {} ", userId);
        UserDog userDog = usersDogRepository.findUserDogByUserId(userId);
        logger.warn("Response {} ", userDog);
        return userDog;
    }

    /**
     * Редактирование данных о пользователе в БД.
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param userDog данные о пользователе в формате JSON
     * @return измененная запись в базе данных в формате JSON
     */
    public UserDog updateUserDog(UserDog userDog) {
        logger.debug("Was invoked method for edit userDog");
        return usersDogRepository.save(userDog);
    }

    /**
     * Удаление записи о пользователе из БД по идентификатору.
     * Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор пользователя, не может быть null
     */
    public void deleteUserDog(long id) {
        logger.debug("Was invoked method for delete userDog");
        usersDogRepository.deleteById(id);
    }

    /**
     * Поиск пользователя в БД по FullName.
     * Исползуется метод репозитория {@link UsersDogRepository#findUserDogByFullNameIgnoreCase(String)}
     * @param fullName имя введенное пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserDog> findUserDogByFullName(String fullName) {
        logger.debug("Was invoked method for get userDog info with full name");
        return usersDogRepository.findUserDogByFullNameIgnoreCase(fullName);
    }

    /**
     * Поиск пользователя в БД по номеру телефона.
     * Исползуется метод репозитория {@link UsersDogRepository#findUserDogByPhone(Long)}
     * @param phone номер телефона введенный пользователем при регистрации через телеграм бота
     * @return список пользователей в формате JSON
     */
    public Collection<UserDog> findUserDogByPhone(Long phone) {
        logger.debug("Was invoked method for get userDog info with phone number");
        return usersDogRepository.findUserDogByPhone(phone);
    }

    /**
     * Поиск пользователя в БД по айди юзера.
     * Исползуется метод репозитория {@link UsersDogRepository#findUserDogByUserId(Long)}
     * @param userId ид пользователя из таблицы users
     * @return список пользователей в формате JSON
     */
    public Collection<UserDog> findUserDogByUserId(Long userId) {
        logger.debug("Was invoked method for get userDog info with phone number");
        return (Collection<UserDog>) usersDogRepository.findUserDogByUserId(userId);
    }

    /**
     * Показывает все записи пользователей в БД.
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * @return список всех пользователей из БД, в фомате JSON
     */
    public Collection<UserDog> findAllUsersDog() {
        logger.debug("Was invoked method for find all");
        return usersDogRepository.findAll();
    }
}
