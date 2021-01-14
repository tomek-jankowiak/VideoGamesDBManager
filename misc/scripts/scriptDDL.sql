DROP SEQUENCE id_druzyny_SEQ;
DROP INDEX gra_box_office_idx;
DROP INDEX gra_kat_wiek_bmp_idx;
DROP INDEX druzyna_gra_bmp_idx;
DROP TABLE uzytkownicy;
DROP TABLE udzialy_druzynowe;
DROP TABLE udzialy_indywidualne;
DROP TABLE mistrzostwa_druzynowe;
DROP TABLE mistrzostwa_indywidualne;
DROP TABLE mistrzostwa;
DROP TABLE zawodnicy;
DROP TABLE druzyny;
DROP TABLE regiony;
DROP TABLE gry_na_platformach;
DROP TABLE gry;
DROP TABLE gatunki;
DROP TABLE platformy;
DROP TABLE pracownicy_studia;
DROP TABLE studia;

CREATE SEQUENCE id_druzyny_SEQ START WITH 1;

CREATE TABLE uzytkownicy (
	id  				  VARCHAR2(9) NOT NULL,
	typ					  VARCHAR2(30) NOT NULL,
	nazwa_uzytkownika	  VARCHAR2(70) NOT NULL UNIQUE
);

ALTER TABLE uzytkownicy ADD CONSTRAINT uzytkownicy_pk PRIMARY KEY ( id );

CREATE TABLE druzyny (
    id                    NUMBER(3, 0) NOT NULL,
    nazwa                 VARCHAR2(50) NOT NULL,
	menadzer			  VARCHAR2(70) NOT NULL,
    region_nazwa_regionu  VARCHAR2(30)
);

ALTER TABLE druzyny ADD CONSTRAINT druzyny_pk PRIMARY KEY ( id );

CREATE TABLE mistrzostwa_druzynowe (
    nazwa  		VARCHAR2(50) NOT NULL,
    data_begin  DATE NOT NULL
);

ALTER TABLE mistrzostwa_druzynowe ADD CONSTRAINT druzynowe_pk PRIMARY KEY ( nazwa,
                                                                data_begin );

CREATE TABLE gatunki (
    nazwa_gatunku          VARCHAR2(50) NOT NULL,
    nazwa_nadgatunku	   VARCHAR2(50)
);

ALTER TABLE gatunki ADD CONSTRAINT gatunki_pk PRIMARY KEY ( nazwa_gatunku );

CREATE TABLE gry (
    tytul              VARCHAR2(50) NOT NULL,
    data_wydania       DATE NOT NULL,
    kategoria_wiekowa  NUMBER(3, 0),
    box_office         NUMBER(10, 2),
    budzet             NUMBER(10, 2),
    studio_nazwa       VARCHAR2(50) NOT NULL,
	nazwa_gatunku	   VARCHAR2(50) NOT NULL
);

ALTER TABLE gry ADD CONSTRAINT gry_pk PRIMARY KEY ( tytul );

CREATE TABLE gry_na_platformach (
    gra_tytul                  VARCHAR2(50) NOT NULL,
    platforma_nazwa_platformy  VARCHAR2(40) NOT NULL
);

ALTER TABLE gry_na_platformach ADD CONSTRAINT gry_na_platformach_pk PRIMARY KEY ( gra_tytul,
                                                                                platforma_nazwa_platformy );

CREATE TABLE mistrzostwa_indywidualne (
    nazwa  		VARCHAR2(50) NOT NULL,
    data_begin  DATE NOT NULL
);

ALTER TABLE mistrzostwa_indywidualne ADD CONSTRAINT indywidualne_pk PRIMARY KEY ( nazwa,
                                                                      data_begin );

CREATE TABLE mistrzostwa (
    nazwa        VARCHAR2(50) NOT NULL,
    data_begin   DATE NOT NULL,
	data_end	 DATE,
    organizator  VARCHAR2(70) NOT NULL,
    lokalizacja  VARCHAR2(50) NOT NULL,
	typ			 VARCHAR(20) NOT NULL,
    nagroda      NUMBER(10, 2),
    gra_tytul    VARCHAR2(50) NOT NULL,
	status		 VARCHAR2(30)
);

ALTER TABLE mistrzostwa ADD CONSTRAINT mistrzostwa_pk PRIMARY KEY ( nazwa,
                                                                    data_begin );
																	

CREATE TABLE platformy (
    nazwa_platformy VARCHAR2(40) NOT NULL
);

ALTER TABLE platformy ADD CONSTRAINT platformy_pk PRIMARY KEY ( nazwa_platformy );

CREATE TABLE pracownicy_studia (
    pesel              VARCHAR2(11) NOT NULL,
    imie               VARCHAR2(50) NOT NULL,
    nazwisko           VARCHAR2(50) NOT NULL,
    placa              NUMBER(6, 2) NOT NULL,
    data_zatrudnienia  DATE NOT NULL,
    dzial              VARCHAR2(40),
    studio_nazwa       VARCHAR2(50) NOT NULL
);

ALTER TABLE pracownicy_studia ADD CONSTRAINT pracownicy_pk PRIMARY KEY ( pesel );

CREATE TABLE regiony (
    nazwa_regionu VARCHAR2(30) NOT NULL
);

ALTER TABLE regiony ADD CONSTRAINT regiony_pk PRIMARY KEY ( nazwa_regionu );

CREATE TABLE studia (
    nazwa           VARCHAR2(50) NOT NULL,
    data_zalozenia  DATE NOT NULL,
    prezes          VARCHAR2(70) NOT NULL
);

ALTER TABLE studia ADD CONSTRAINT studia_pk PRIMARY KEY ( nazwa );

CREATE TABLE udzialy_druzynowe (
    wynik            NUMBER(3, 1),
    druzyna_id       NUMBER(3, 0) NOT NULL,
    druzynowe_nazwa  VARCHAR2(50) NOT NULL,
    druzynowe_data   DATE NOT NULL
);

ALTER TABLE udzialy_druzynowe
    ADD CONSTRAINT udzialy_druzynowe_pk PRIMARY KEY ( druzyna_id,
                                                     druzynowe_nazwa,
                                                     druzynowe_data );

CREATE TABLE udzialy_indywidualne (
    wynik               NUMBER(3, 1),
    indywidualne_nazwa  VARCHAR2(50) NOT NULL,
    indywidualne_data   DATE NOT NULL,
    zawodnik_pseudonim  VARCHAR2(50) NOT NULL
);

ALTER TABLE udzialy_indywidualne
    ADD CONSTRAINT udzialy_indywidualne_pk PRIMARY KEY ( indywidualne_nazwa,
                                                        indywidualne_data,
                                                        zawodnik_pseudonim );

CREATE TABLE zawodnicy (
    pseudonim       VARCHAR2(50) NOT NULL,
    imie            VARCHAR2(50) NOT NULL,
    nazwisko        VARCHAR2(50) NOT NULL,
    kraj            VARCHAR2(50) NOT NULL,
    data_urodzenia  DATE NOT NULL,
    placa           NUMBER(6, 2),
    druzyna_id      NUMBER(3, 0) NOT NULL
);

ALTER TABLE zawodnicy ADD CONSTRAINT zawodnicy_pk PRIMARY KEY ( pseudonim );

ALTER TABLE druzyny
    ADD CONSTRAINT druzyna_region_fk FOREIGN KEY ( region_nazwa_regionu )
        REFERENCES regiony ( nazwa_regionu );
		
ALTER TABLE druzyny
	ADD CONSTRAINT druzyna_menadzer_fk FOREIGN KEY (menadzer)
		REFERENCES uzytkownicy ( nazwa_uzytkownika )
		ON DELETE CASCADE;
		
ALTER TABLE mistrzostwa_druzynowe
    ADD CONSTRAINT druzynowe_mistrzostwa_fk FOREIGN KEY ( nazwa,
                                                          data_begin )
        REFERENCES mistrzostwa ( nazwa,
								 data_begin )
		ON DELETE CASCADE;

ALTER TABLE gatunki
    ADD CONSTRAINT gatunek_nadgatunek_fk FOREIGN KEY ( nazwa_nadgatunku )
        REFERENCES gatunki ( nazwa_gatunku );

ALTER TABLE gry
    ADD CONSTRAINT gra_gatunek_fk FOREIGN KEY ( nazwa_gatunku )
        REFERENCES gatunki ( nazwa_gatunku );

ALTER TABLE gry_na_platformach
    ADD CONSTRAINT gra_na_platformie_gra_fk FOREIGN KEY ( gra_tytul )
        REFERENCES gry ( tytul );

ALTER TABLE gry_na_platformach
    ADD CONSTRAINT gra_na_platformie_platforma_fk FOREIGN KEY ( platforma_nazwa_platformy )
        REFERENCES platformy ( nazwa_platformy );

ALTER TABLE gry
    ADD CONSTRAINT gra_studio_fk FOREIGN KEY ( studio_nazwa )
        REFERENCES studia ( nazwa );

ALTER TABLE mistrzostwa_indywidualne
    ADD CONSTRAINT indywidualne_mistrzostwa_fk FOREIGN KEY ( nazwa,
                                                             data_begin )
        REFERENCES mistrzostwa ( nazwa,
                                 data_begin )
		ON DELETE CASCADE;

ALTER TABLE mistrzostwa
    ADD CONSTRAINT mistrzostwa_gra_fk FOREIGN KEY ( gra_tytul )
        REFERENCES gry ( tytul );
				
ALTER TABLE mistrzostwa
	ADD CONSTRAINT mistrzostwa_uzytkownicy_fk FOREIGN KEY ( organizator )
		REFERENCES uzytkownicy ( nazwa_uzytkownika )
		ON DELETE CASCADE;

ALTER TABLE studia
	ADD CONSTRAINT studio_prezes_fk FOREIGN KEY ( prezes )
		REFERENCES uzytkownicy ( nazwa_uzytkownika )
		ON DELETE CASCADE;
		
ALTER TABLE pracownicy_studia
    ADD CONSTRAINT pracownik_studio_fk FOREIGN KEY ( studio_nazwa )
        REFERENCES studia ( nazwa )
		ON DELETE CASCADE;

ALTER TABLE udzialy_druzynowe
    ADD CONSTRAINT udzial_druzynowy_druzyna_fk FOREIGN KEY ( druzyna_id )
        REFERENCES druzyny ( id )
		ON DELETE CASCADE;

ALTER TABLE udzialy_druzynowe
    ADD CONSTRAINT udzial_druz_mist_druz_fk FOREIGN KEY ( druzynowe_nazwa,
                                                          druzynowe_data )
        REFERENCES mistrzostwa_druzynowe ( nazwa,
										   data_begin )
		ON DELETE CASCADE;

ALTER TABLE udzialy_indywidualne
    ADD CONSTRAINT udzial_ind_mist_ind_fk FOREIGN KEY ( indywidualne_nazwa,
                                                        indywidualne_data )
        REFERENCES mistrzostwa_indywidualne ( nazwa,
											  data_begin )
		ON DELETE CASCADE;

ALTER TABLE udzialy_indywidualne
    ADD CONSTRAINT udzial_indywidualny_zawodnik_fk FOREIGN KEY ( zawodnik_pseudonim )
        REFERENCES zawodnicy ( pseudonim )
		ON DELETE CASCADE;

ALTER TABLE zawodnicy
    ADD CONSTRAINT zawodnik_druzyna_fk FOREIGN KEY ( druzyna_id )
        REFERENCES druzyny ( id )
		ON DELETE CASCADE;


CREATE OR REPLACE PROCEDURE ZarejestrujUdzialIndywidualny(
	pseudonim IN zawodnicy.pseudonim%TYPE,
	nazwa_m IN mistrzostwa_indywidualne.nazwa%TYPE,
	data_m IN mistrzostwa_indywidualne.data%TYPE) AS
BEGIN
	INSERT INTO udzialy_indywidualne(indywidualne_nazwa, indywidualne_data, zawodnik_pseudonim)
				VALUES(nazwa_m, data_m, pseudonim);
END;
/

CREATE INDEX gra_box_office_idx ON gry(box_office);

CREATE BITMAP INDEX gra_kat_wiek_bmp_idx ON gry(kategoria_wiekowa);

CREATE BITMAP INDEX druzyna_gra_bmp_idx ON druzyny(tytul)
FROM druzyny d, udzialy_druzynowe ud, mistrzostwa_druzynowe md, mistrzostwa m, gry g
WHERE d.id = ud.druzyna_id
AND ud.druzynowe_nazwa = md.nazwa
AND ud.druzynowe_data = md.data
AND md.nazwa = m.nazwa
AND md.data = m.data
AND m.gra_tytul = g.tytul;

commit;
