package de.besven.kiosk.repository;

import de.besven.kiosk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository für Datenbank-Handling
 * Speziell für das Objekt 'User' erweiterte JpaRepository, um einfache Datenbankaufgaben auszuführen.
 * Zusätzliche eigene Methoden für das Objekt 'User' werden hier mit den benötigten Parametern
 * hinterlegt.
 * Die JpaRepository bezieht sich auf das Objekt 'User' und der Objekt-Identifier ist
 * vom Datentyp 'Long'.
 * Die UserRepository stellt die Datenbankaufgaben für die entsprechenden Services zur Verfügung.
 *
 * @author Berthold, Sven (programmer@besven.de)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * This is a named query and will find a user per UUID
     *
     * @param uuid
     */
    User getUserByUuid(String uuid);
}
