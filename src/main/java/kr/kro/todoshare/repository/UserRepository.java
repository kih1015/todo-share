package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {

    User findById(Long id);

    User save(User user);

    void deleteById(Long id);
}
