package com.example.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String key;

    private String text;

    @JsonProperty("like_count")
    @Column(name = "like_count", columnDefinition = "int default 0", nullable = false, insertable = false)
    private int likeCount;

    @JsonProperty("share_count")
    @Column(name = "share_count", columnDefinition = "int default 0", nullable = false, insertable = false)
    private int shareCount;

    @JsonProperty("view_count")
    @Column(name = "view_count", columnDefinition = "int default 0", nullable = false, insertable = false)
    private int viewCount;

    @JsonProperty("is_media")
    @Column(name = "is_media", columnDefinition = "boolean default false", nullable = false)
    private boolean isMedia;

    @JsonProperty("is_blocked")
    @Column(name = "is_blocked", columnDefinition = "boolean default false", nullable = false, insertable = false)
    private boolean isBlocked;

    @JsonProperty("user_id")
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @JsonProperty("date_time")
    @Column(name = "date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dateTime;

}
