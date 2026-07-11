package com.airline.auth.entity;


import com.airline.auth.enums.Role;
import com.airline.auth.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table( name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name ="first_name", nullable = false)
    private String firstName;

    @Column( name = "last_name", nullable = false)
    private String lastName;

    @Column( name = "email", nullable = false)
    private String email;

    @Column( name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column( name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column( name = "status", nullable = false)
    private UserStatus status;


    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column( name = "role", nullable = false)
    private Role role;

    @Column( name = "email_verified")
    private boolean emailVerified;

    @CreationTimestamp
    @Column( name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column( name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany( mappedBy = "user")
    private List<RefreshToken> refreshTokens;

}
