package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.User;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    User findUserByChatId(final Long chatId);
}
