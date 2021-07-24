package com.elldev.reactivechat.repository;

import com.elldev.reactivechat.entity.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String> {
    Optional<UserSession> findByToken(String token);
}
