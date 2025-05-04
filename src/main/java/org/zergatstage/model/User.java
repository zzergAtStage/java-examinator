package org.zergatstage.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * Platform account for a learner or an administrator.
 *
 * @author father
 */

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@Table(name = "_users",
        indexes = {
                @Index(name = "uk_user_username", columnList = "username", unique = true)
        })
public class User extends AuditableEntity {
    /** Login name chosen by the user. */
    @Column(nullable = false, length = 64, unique = true)
    private String username;

    /** Optional for self‑paced practice; empty for guest users. */
    @Column(length = 255)
    private String email;

    /** Bi‑directional pointer; keeps history of attempts. */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<QuizAttempt> attempts;
}
