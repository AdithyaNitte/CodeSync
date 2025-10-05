package com.example.java_file_upload;



import com.example.java_file_upload.JavaFile;
import com.example.java_file_upload.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JavaFileRepository extends JpaRepository<JavaFile, Long> {
    List<JavaFile> findBySession(Session session);
}
  