package com.example.java_file_upload;

import  com.example.java_file_upload.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findBySessionCode(String sessionCode);
    
    
}
