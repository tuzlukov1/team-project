package pro.sky.teamproject.services;

import org.springframework.stereotype.Service;
import pro.sky.teamproject.entity.User;
import pro.sky.teamproject.repository.UsersRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<User> findUserByChatId(Long id) {
        return Optional.ofNullable(usersRepository.findUserByChatId(id));
    }

    public void createUser(User user) {
        usersRepository.save(user);
    }
}
