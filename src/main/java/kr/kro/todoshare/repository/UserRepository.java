package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByLoginId(String loginId);

    User save(User user);

    void deleteById(Long id);
}
