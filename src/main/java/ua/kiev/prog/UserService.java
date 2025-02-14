package ua.kiev.prog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CustomUser findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public CustomUser getUsersById(long id){
        return userRepository.findCustomUserByById(id);
    }

    @Transactional
    public void deleteUsers(List<Long> ids) {
        ids.forEach(id -> {
            Optional<CustomUser> user = userRepository.findById(id);
            user.ifPresent(u -> {
                if ( ! AppConfig.ADMIN_LOGIN.equals(u.getLogin())) {
                    userRepository.deleteById(u.getId());
                }
            });
        });
    }

    @Transactional
    public boolean addUser(String login, String passHash,
                           UserRole role, String email,
                           String phone,
                           String address) {
        if (userRepository.existsByLogin(login))
            return false;

        CustomUser user = new CustomUser(login, passHash, role, email, phone, address);
        userRepository.save(user);

        return true;
    }

    @Transactional
    public void updateUser(String login, String email, String phone, String address) {
        CustomUser user = userRepository.findByLogin(login);
        if (user == null)
            return;

        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);

        userRepository.save(user);
    }

    @Transactional
    public void updateUserStatus(String login, boolean status) {
        CustomUser user = userRepository.findByLogin(login);
        if (user == null)
            return;
            user.setStatus(status);
            userRepository.save(user);
    }
}
