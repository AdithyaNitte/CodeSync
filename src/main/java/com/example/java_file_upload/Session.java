package com.example.java_file_upload;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String sessionCode;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<JavaFile> javaFiles = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<JavaFile> getJavaFiles() {
		return javaFiles;
	}

	public void setJavaFiles(List<JavaFile> javaFiles) {
		this.javaFiles = javaFiles;
	}
    
    
}
