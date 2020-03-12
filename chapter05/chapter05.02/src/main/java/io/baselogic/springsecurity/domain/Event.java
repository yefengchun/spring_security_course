package io.baselogic.springsecurity.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;

/**
 * An {@link Event} is an item on a calendar that contains an owner (the person who created it), an attendee
 * (someone who was invited to the event), when the event will occur, a summary, and a description. For simplicity, all
 * fields are required.
 *
 * @author mickknutson
 *
 */
// Document Annotations:
@Document(collection="events")

// Lombok Annotations:
//@Builder // NOTE: This does not work with Mongo JPA
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Persistable<Integer>, Serializable {

    @Id
    private Integer id;

    @NotEmpty(message = "Summary is required")
    private String summary;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "When is required (yyyy-MM-dd HH:mm)")
    private Calendar when;


    @NotNull(message = "Owner is required")
    @DBRef
    private AppUser owner;
    @DBRef
    private AppUser attendee;

    private Boolean persisted = Boolean.FALSE;


    @PersistenceConstructor
    public Event(Integer id,
                 String summary,
                 String description,
                 Calendar when,
                 AppUser owner,
                 AppUser attendee) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.when = when;
        this.owner = owner;
        this.attendee = attendee;
    }

    @Override
    public boolean isNew() {
        return !persisted;
    }


    // Setter / Getter generated by Lombok

} // The End...