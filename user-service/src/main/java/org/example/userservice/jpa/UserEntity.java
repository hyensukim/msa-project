package org.example.userservice.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String email;

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, unique = true)
    private String userId;

    @Column(nullable=false, unique = true)
    private String encryptedPw ;
}
