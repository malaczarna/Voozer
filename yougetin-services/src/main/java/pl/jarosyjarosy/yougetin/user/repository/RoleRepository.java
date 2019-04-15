package pl.jarosyjarosy.yougetin.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.user.model.Role;
import pl.jarosyjarosy.yougetin.user.model.RoleType;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findByUserId(Long userId);

    void deleteByUserId(Long id);

    List<Role> findByType(RoleType role);

    List<Role> findByTypeIn(List<RoleType> roles);
}
