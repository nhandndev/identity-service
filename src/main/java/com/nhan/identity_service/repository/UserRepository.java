package com.nhan.identity_service.repository;

import com.nhan.identity_service.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
  boolean existsByuserName(String userName);

  Optional<Users> findByuserName(String userName);
}
