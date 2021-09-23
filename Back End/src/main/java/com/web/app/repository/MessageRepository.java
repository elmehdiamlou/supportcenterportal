package com.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.app.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
