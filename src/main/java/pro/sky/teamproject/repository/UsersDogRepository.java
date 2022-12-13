package pro.sky.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.teamproject.entity.UserDog;

import java.util.List;

@Repository
public interface UsersDogRepository extends JpaRepository<UserDog, Long> {

    List<UserDog> findUserDogByFullNameIgnoreCase(String name);

    List<UserDog> findUserDogByPhone(Long phone);

    UserDog findUserDogByUserId(Long id);
}
