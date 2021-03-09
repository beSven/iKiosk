package de.besven.kiosk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Blob;

/**
 * This is the entity for the user with properties who describe them.
 * Some users can be a admin, then the admin-property is 'true'.
 * Users will be not delete, they have an 'false'-active-status.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    /**
     * Eindeutige Identifikationsnummer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS_GEN")
    @SequenceGenerator(allocationSize = 1, name = "SEQ_USERS_GEN", sequenceName = "SEQ_USERS")
    private Long id;

    /**
     * An own identifier for our users.
     * You need this for databases, who doesn't create an 'id'-field or PrimaryKeys
     */
    @NotBlank
    @Size(max = 250)
    private String uuid;

    /**
     * The firstname of our users.
     */
    @NotBlank
    @Size(max = 250)
    private String firstName;

    /**
     * The lastname of our users.
     */
    @NotBlank
    @Size(max = 250)
    private String lastName;

    /**
     * A nickname as an explicit identifier, who is build with the first
     * char from first- and lastname, the last char from the lastname and
     * if required, a numeric char.
     */
    @NotBlank
    @Size(max = 250)
    private String username;

    /**
     * If our users is an admin, this flag is 'true', else it's 'false'.
     */
    private Boolean admin;

    /**
     * If our users is active, this flag is 'true', else it's 'false'.
     * Users doesn't delete, they are 'not active' (false).
     */
    private Boolean active;

    /**
     * The pincode for our users.
     */
    @JsonIgnore
    private String pin;

    /**
     * The string for a picture-file, created by database.
     */
    private Blob picture;

    /**
     * Constructor with required parameter.
     *
     * @param uuid      the users identifier.
     * @param firstName the users first name
     * @param lastName  the users last name
     * @param username  the username
     */
    public User(@NotBlank @Size(max = 250) String uuid,
                @NotBlank @Size(max = 250) String firstName,
                @NotBlank @Size(max = 250) String lastName,
                @NotBlank @Size(max = 250) String username) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.setAdmin(false);
        this.setActive(true);
    }
}
