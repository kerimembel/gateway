package com.sattım.gateway.repository;

import com.sattım.gateway.bean.auth.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends MongoRepository<JwtToken, String> {
}
