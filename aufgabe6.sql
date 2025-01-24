CREATE TABLE Studiengang
(
    id     INT PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    kürzel VARCHAR(10)  NOT NULL UNIQUE
);


CREATE TABLE Stundenplan
(
    id             INT PRIMARY KEY,
    studiengang_id INT NOT NULL,
    FOREIGN KEY (studiengang_id) REFERENCES Studiengang (id)
);



CREATE TABLE Dozent
(
    id        INT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    vorname   VARCHAR(255) NOT NULL,
    akd_titel VARCHAR(255),
    email     VARCHAR(255) NOT NULL
);

CREATE TABLE Professor
(
    id         INT PRIMARY KEY,
    dozent_id  INT          NOT NULL,
    FOREIGN KEY (dozent_id) REFERENCES Dozent (id),
    büro       VARCHAR(255) NOT NULL,
    sprechzeit VARCHAR(255) NOT NULL
);

CREATE TABLE Mitarbeiter
(
    id         INT PRIMARY KEY,
    dozent_id  INT          NOT NULL,
    FOREIGN KEY (dozent_id) REFERENCES Dozent (id),
    büro       VARCHAR(255) NOT NULL,
    sprechzeit VARCHAR(255) NOT NULL
);

CREATE TABLE Gebäude
(
    id   INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    nr   INT          NOT NULL
);

CREATE TABLE Raum
(
    id         INT PRIMARY KEY,
    gebäude_id INT          NOT NULL,
    FOREIGN KEY (gebäude_id) REFERENCES Gebäude (id),
    name       VARCHAR(255) NOT NULL,
    nr         INT          NOT NULL
);

CREATE TABLE Veranstaltung
(
    id                  INT PRIMARY KEY,
    primary_dozent_id   INT          NOT NULL,
    FOREIGN KEY (primary_dozent_id) REFERENCES Dozent (id),
    secondary_dozent_id INT,
    FOREIGN KEY (secondary_dozent_id) REFERENCES Dozent (id),
    type                VARCHAR(255) NOT NULL,
    wochentag           VARCHAR(255) NOT NULL,
    beginn              TIME         NOT NULL,
    ende                TIME         NOT NULL,
    room_id_1           INT          NOT NULL,
    FOREIGN KEY (room_id_1) REFERENCES Raum (id),
    room_id_2           INT,
    FOREIGN KEY (room_id_2) REFERENCES Raum (id),
    fachsemester        INT          NOT NULL,
    häufigkeit          VARCHAR(255) NOT NULL,
    name                VARCHAR(255) NOT NULL,
    stundenplan_id      INT          NOT NULL,
    FOREIGN KEY (stundenplan_id) REFERENCES Stundenplan (id)
);




