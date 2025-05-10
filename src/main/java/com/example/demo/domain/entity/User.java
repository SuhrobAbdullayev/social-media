package com.example.demo.domain.entity;

import com.example.demo.domain.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends DateAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    @NotNull
    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @NotNull
    @Size(min = 4, max = 50)
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "is_active", columnDefinition = "boolean default true", nullable = false, insertable = false)
    private Boolean isActive;

    @Column(name = "is_deleted", columnDefinition = "boolean default false", nullable = false, insertable = false)
    private Boolean isDeleted;

    @Column(name = "is_blocked", columnDefinition = "boolean default false", nullable = false, insertable = false)
    private Boolean isBlocked;

    @Column(name = "refresh_token", unique = true, length = 1000)
    private String refreshToken;
}
