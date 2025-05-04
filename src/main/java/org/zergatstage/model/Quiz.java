package org.zergatstage.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Immutable blueprint that defines what an assessment contains.
 * Versioning allows editing future publishes without breaking
 * historical attempts.
 * @author father
 */
@Getter
@SuperBuilder
@Entity
@Table(
        indexes = {
                @Index(name = "idx_quiz_author", columnList = "author_id"),
                @Index(name = "idx_quiz_published", columnList = "publishedAt")
        })
public class Quiz extends AuditableEntity{

    /** Human‑readable exam title. */
    @Column(nullable = false, length = 128)
    private String title;
    /** Short Markdown / HTML description. */
    @Column(length = 1024)
    private String description;

    /** Monotonically increasing revision. */
    @Column(nullable = false)
    private int version;

    /** Cached total points (sum of question points). */
    @Column(nullable = false)
    private int totalPoints;

    /** Author/owner of the quiz. */
    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    /** Null until published, then immutable. */
    private LocalDateTime publishedAt;

    /** Sections that belong to this quiz. */
    @OneToMany(mappedBy = "quiz",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<Section> sections;
    /**
     * Computes the next version and detaches the entity,
     * so a new record can be saved when the author republishes.
     */
    public Quiz createNewRevision() {
        Quiz clone =  Quiz.builder().build();
        clone.title        = this.title;
        clone.description  = this.description;
        clone.version      = this.version + 1;
        clone.author       = this.author;
        // sections will be deep‑copied by application service
        return clone;
    }
    public Quiz author(User appUser) {
        this.setAuthor(appUser);
        return this;
    }

}





