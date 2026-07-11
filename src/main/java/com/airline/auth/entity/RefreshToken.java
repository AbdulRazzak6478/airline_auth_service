package com.airline.auth.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "user_id", nullable = false)
    private User user;

    @Column( name = "token",  nullable = false, unique = true)
    private String token;

    @Column( name = "device_name")
    private String deviceName;

    @Column( name = "user_agent")
    private String userAgent;

    @Column( name = "ip_address")
    private String ipAddress;

    @Column( name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column( name = "revoked",  nullable = false)
    private Boolean revoked;

    @CreationTimestamp
    @Column( name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column( name = "updated_at", nullable = false)
    private Instant updatedAt;
}
