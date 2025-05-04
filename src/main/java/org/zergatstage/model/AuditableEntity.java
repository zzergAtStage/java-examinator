package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * <p>Abstract parent for all persistent entities that need
 * a synthetic primary key and audit timestamps.</p>
 */
@Getter
@SuperBuilder
@MappedSuperclass
public class AuditableEntity {
    /** Synthetic primary key (auto‑increment). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /** Timestamp automatically set on INSERT. */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /** Timestamp automatically updated on every UPDATE. */
    @UpdateTimestamp
    @Column(nullable = false)
    protected LocalDateTime updatedAt;

    public void setId(long id) {
        this.id = id;
    }
}
