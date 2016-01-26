
DROP TABLE IF EXISTS Obtenir;
DROP TABLE IF EXISTS Avoir;
DROP TABLE IF EXISTS Diplome;
DROP TABLE IF EXISTS Competences;
DROP TABLE IF EXISTS Utilisateur;

CREATE TABLE IF NOT EXISTS Utilisateur (
  IdU int(6) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  Mail varchar(128) NOT NULL,
  Nom varchar(256) NOT NULL,
  Prenom varchar(256) NOT NULL,
  Mdp varchar(256) NOT NULL,
  Admin tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE Utilisateur 
  ADD CONSTRAINT UK_UTILISATEUR UNIQUE KEY Mail (Mail);

CREATE TABLE IF NOT EXISTS Competences (
  idC int(6) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  NomC varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS Diplome (
  idD int(6) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  NomD varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS Avoir 
(
  idU int(6) NOT NULL,
  idC int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE Avoir
  ADD CONSTRAINT PK_AVOIR PRIMARY KEY (IdU,idC), ADD KEY FK_Avoir_idC (idC),
  ADD CONSTRAINT FK_Avoir_idC FOREIGN KEY (idC) REFERENCES Competences (idC),
  ADD CONSTRAINT FK_Avoir_IdU FOREIGN KEY (IdU) REFERENCES Utilisateur (IdU);


CREATE TABLE IF NOT EXISTS Obtenir (
  DateD date DEFAULT NULL,
  IdU int(6) NOT NULL,
  idD int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE Obtenir
 ADD PRIMARY KEY (IdU,idD),
 ADD CONSTRAINT FK_Obtenir_idD FOREIGN KEY (idD) REFERENCES Diplome (idD),
 ADD CONSTRAINT FK_Obtenir_IdU FOREIGN KEY (IdU) REFERENCES Utilisateur (IdU);