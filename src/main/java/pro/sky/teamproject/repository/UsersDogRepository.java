package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.UserDog;

import java.util.List;

@Repository
public interface UsersDogRepository extends JpaRepository<UserDog, Long> {

    UserDog findUserByChatId(final Long chatId);

    List<UserDog> findUsersByUserNameIgnoreCase(String name);

    List<UserDog> findUsersByFullNameIgnoreCase(String name);

    List<UserDog> findUsersByPhone(Long phone);
}
