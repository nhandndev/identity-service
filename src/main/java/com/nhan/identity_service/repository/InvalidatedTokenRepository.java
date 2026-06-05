package com.nhan.identity_service.repository;

import com.nhan.identity_service.entity.InvalidatedTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedTokens,String> {
    boolean existsById(String id);

}
