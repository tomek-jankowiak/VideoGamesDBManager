CREATE OR REPLACE PACKAGE Menadzer AS
	PROCEDURE DodajZawodnika(
		pseudonim IN zawodnicy.pseudonim%TYPE,
		imie IN zawodnicy.pseudonim%TYPE,
		nazwisko IN zawodnicy.nazwisko%TYPE,
		kraj IN zawodnicy.kraj%TYPE,
		data_urodzenia IN zawodnicy.data_urodzenia%TYPE,
		id_druzyny IN zawodnicy.druzyna_id%TYPE,
		placa IN zawodnicy.placa%TYPE DEFAULT 0);
							
	PROCEDURE NowaDruzyna(
		nazwa IN druzyny.nazwa%TYPE,
		menadzer IN druzyny.menadzer%TYPE,
		region IN druzyny.region_nazwa_regionu%TYPE DEFAULT NULL);
		
	PROCEDURE NowyRegion(
		nazwa IN regiony.nazwa_regionu%TYPE);
		
	PROCEDURE ZarejestrujUdzialDruzynowy(
		id_druzyny IN druzyny.id%TYPE,
		nazwa_m IN mistrzostwa_druzynowe.nazwa%TYPE,
		data_m IN mistrzostwa_druzynowe.data%TYPE);
		
END Menadzer;
/

CREATE OR REPLACE PACKAGE BODY Menadzer AS
	PROCEDURE DodajZawodnika(
		pseudonim IN zawodnicy.pseudonim%TYPE,
		imie IN zawodnicy.pseudonim%TYPE,
		nazwisko IN zawodnicy.nazwisko%TYPE,
		kraj IN zawodnicy.kraj%TYPE,
		data_urodzenia IN zawodnicy.data_urodzenia%TYPE,
		id_druzyny IN zawodnicy.druzyna_id%TYPE,
		placa IN zawodnicy.placa%TYPE DEFAULT 0) AS
	BEGIN
		INSERT INTO Zawodnicy VALUES(pseudonim, imie, nazwisko, kraj, data_urodzenia, placa, id_druzyny);
	END;

	PROCEDURE NowaDruzyna(
		nazwa IN druzyny.nazwa%TYPE,
		menadzer IN druzyny.menadzer%TYPE,
		region IN druzyny.region_nazwa_regionu%TYPE DEFAULT NULL) AS
	BEGIN
		INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, nazwa, menadzer, region);
	END;
	
	PROCEDURE NowyRegion(
		nazwa IN regiony.nazwa_regionu%TYPE) AS
	BEGIN
		INSERT INTO regiony VALUES(nazwa);
	END;
	
	PROCEDURE ZarejestrujUdzialDruzynowy(
		id_druzyny IN druzyny.id%TYPE,
		nazwa_m IN mistrzostwa_druzynowe.nazwa%TYPE,
		data_m IN mistrzostwa_druzynowe.data%TYPE) AS
	BEGIN
		INSERT INTO udzialy_druzynowe(druzynowe_nazwa, druzynowe_data, druzyna_id)
					VALUES(nazwa_m, data_m, id_druzyny);
	END;

END Menadzer;
/

CREATE OR REPLACE PACKAGE Organizator AS
	PROCEDURE NoweMistrzostwa(
		nazwa IN mistrzostwa.nazwa%TYPE,
		data IN mistrzostwa.data%TYPE,
		organizator IN mistrzostwa.organizator%TYPE,
		lokalizacja IN mistrzostwa.lokalizacja%TYPE,
		typ IN CHAR,
		gra IN gry.tytul%TYPE,
		nagroda IN mistrzostwa.nagroda%TYPE DEFAULT NULL);
		
END Organizator;
/

CREATE OR REPLACE PACKAGE BODY Organizator AS
	PROCEDURE NoweMistrzostwa(
		nazwa IN mistrzostwa.nazwa%TYPE,
		data IN mistrzostwa.data%TYPE,
		organizator IN mistrzostwa.organizator%TYPE,
		lokalizacja IN mistrzostwa.lokalizacja%TYPE,
		typ IN CHAR,
		gra IN gry.tytul%TYPE,
		nagroda IN mistrzostwa.nagroda%TYPE DEFAULT NULL) AS
	BEGIN 
		INSERT INTO mistrzostwa VALUES(nazwa, data, organizator, lokalizacja, nagroda, gra);
		IF typ = 'i' THEN
			INSERT INTO mistrzostwa_indywidualne VALUES(nazwa, data);
		ELSIF typ = 'd' THEN
			INSERT INTO mistrzostwa_druzynowe VALUES(nazwa, data);
		END IF;
	END;

END Organizator;
/

CREATE OR REPLACE PACKAGE Prezes AS
	PROCEDURE NoweStudio(
		nazwa IN studia.nazwa%TYPE,
		data_zalozenia IN studia.data_zalozenia%TYPE DEFAULT SYSDATE,
		prezes IN studia.prezes%TYPE DEFAULT NULL);
		
	PROCEDURE DodajPracownika(
		pesel IN pracownicy_studia.pesel%TYPE,
		imie IN pracownicy_studia.imie%TYPE,
		nazwisko IN pracownicy_studia.nazwisko%TYPE,
		placa IN pracownicy_studia.placa%TYPE,
		studio IN pracownicy_studia.studio_nazwa%TYPE,
		data_zatrudnienia IN pracownicy_studia.data_zatrudnienia%TYPE DEFAULT SYSDATE,
		dzial IN pracownicy_studia.dzial%TYPE DEFAULT NULL);
		
	PROCEDURE EdytujPracownika(
		pPesel IN pracownicy_studia.pesel%TYPE,
		pPlaca IN pracownicy_studia.placa%TYPE,
		pDzial IN pracownicy_studia.dzial%TYPE DEFAULT NULL);
		
	PROCEDURE UsunPracownika(
		pPesel IN pracownicy_studia.pesel%TYPE);
		
	PROCEDURE WydajGre(
		tytul IN gry.tytul%TYPE,
		data_wydania IN gry.data_wydania%TYPE,
		gatunek IN gry.nazwa_gatunku%TYPE,
		studio IN gry.studio_nazwa%TYPE,
		kategoria_wiekowa IN gry.kategoria_wiekowa%TYPE DEFAULT NULL,
		box_office IN gry.box_office%TYPE DEFAULT NULL,
		budzet IN gry.budzet%TYPE DEFAULT NULL);
		
	PROCEDURE PrzypiszGreDoPlatformy(
		tytul IN gry.tytul%TYPE,
		platforma IN platformy.nazwa_platformy%TYPE);
		
	PROCEDURE NowyGatunek(
		gatunek IN gatunki.nazwa_gatunku%TYPE,
		nadgatunek IN gatunki.nazwa_nadgatunku%TYPE DEFAULT NULL);
		
	FUNCTION PobierzNazweStudia 
		RETURN VARCHAR2;
			
END Prezes;
/

CREATE OR REPLACE PACKAGE BODY Prezes AS
	PROCEDURE NoweStudio(
		nazwa IN studia.nazwa%TYPE,
		data_zalozenia IN studia.data_zalozenia%TYPE DEFAULT SYSDATE,
		prezes IN studia.prezes%TYPE DEFAULT NULL) AS
	BEGIN
		INSERT INTO studia VALUES(nazwa, data_zalozenia, prezes);
	END;
	
	PROCEDURE DodajPracownika(
		pesel IN pracownicy_studia.pesel%TYPE,
		imie IN pracownicy_studia.imie%TYPE,
		nazwisko IN pracownicy_studia.nazwisko%TYPE,
		placa IN pracownicy_studia.placa%TYPE,
		studio IN pracownicy_studia.studio_nazwa%TYPE,
		data_zatrudnienia IN pracownicy_studia.data_zatrudnienia%TYPE DEFAULT SYSDATE,
		dzial IN pracownicy_studia.dzial%TYPE DEFAULT NULL) AS
	BEGIN
		INSERT INTO pracownicy_studia 
				VALUES(pesel, imie, nazwisko, placa, data_zatrudnienia, dzial, studio);
	END;
	
	PROCEDURE EdytujPracownika(
		pPesel IN pracownicy_studia.pesel%TYPE,
		pPlaca IN pracownicy_studia.placa%TYPE,
		pDzial IN pracownicy_studia.dzial%TYPE DEFAULT NULL) AS
	BEGIN
		UPDATE pracownicy_studia
		SET placa = pPlaca,
			dzial = pDzial
		WHERE pesel = pPesel;
	END;
		
	PROCEDURE UsunPracownika(
		pPesel IN  pracownicy_studia.pesel%TYPE) AS
	BEGIN
		DELETE FROM pracownicy_studia
		WHERE pesel = pPesel;
	END;
		
	PROCEDURE WydajGre(
		tytul IN gry.tytul%TYPE,
		data_wydania IN gry.data_wydania%TYPE,
		gatunek IN gry.nazwa_gatunku%TYPE,
		studio IN gry.studio_nazwa%TYPE,
		kategoria_wiekowa IN gry.kategoria_wiekowa%TYPE DEFAULT NULL,
		box_office IN gry.box_office%TYPE DEFAULT NULL,
		budzet IN gry.budzet%TYPE DEFAULT NULL) AS
	BEGIN
		INSERT INTO gry VALUES(tytul, data_wydania, kategoria_wiekowa, box_office, budzet, studio, gatunek);
	END;
	
	PROCEDURE PrzypiszGreDoPlatformy(
		tytul IN gry.tytul%TYPE,
		platforma IN platformy.nazwa_platformy%TYPE) AS
	BEGIN 
		INSERT INTO gry_na_platformach VALUES(tytul, platforma);
	END;
	
	PROCEDURE NowyGatunek(
		gatunek IN gatunki.nazwa_gatunku%TYPE,
		nadgatunek IN gatunki.nazwa_nadgatunku%TYPE DEFAULT NULL) AS
	BEGIN 
		INSERT INTO gatunki VALUES(gatunek, nadgatunek);
	END;
	
	FUNCTION PobierzNazweStudia 
		RETURN VARCHAR2 AS
		vNazwa studia.nazwa%TYPE;
	BEGIN
		SELECT nazwa
		INTO vNazwa
		FROM studia
		WHERE prezes = (SELECT nazwa_uzytkownika
                        FROM uzytkownicy
                        WHERE id = USER
                        );
		RETURN vNazwa;
	END;
		
END Prezes;
/

CREATE OR REPLACE PACKAGE Wspolne AUTHID DEFINER AS
	FUNCTION PobierzTypUzytkownika RETURN uzytkownicy.typ%TYPE;
	
	PROCEDURE DodajPrezesa(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE);
	
	PROCEDURE DodajMenadzera(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE);
		
	PROCEDURE DodajOrganizatora(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE);
		
	PROCEDURE UsunKonto;

END Wspolne;
/

CREATE OR REPLACE PACKAGE BODY Wspolne AS
	FUNCTION PobierzTypUzytkownika RETURN uzytkownicy.typ%TYPE AS
		vTyp uzytkownicy.typ%TYPE;
	BEGIN
		SELECT typ
		INTO vTyp
		FROM uzytkownicy
		WHERE id = USER;
		
		RETURN vTyp;
	END;
	
	PROCEDURE DodajPrezesa(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE) AS
	BEGIN
		INSERT INTO uzytkownicy VALUES(USER, 'PREZES', pNazwa);
	END;
	
	PROCEDURE DodajMenadzera(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE) AS
	BEGIN
		INSERT INTO uzytkownicy VALUES(USER, 'MENADZER', pNazwa);
	END;
	
	PROCEDURE DodajOrganizatora(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE) AS
	BEGIN
		INSERT INTO uzytkownicy VALUES(USER, 'ORGANIZATOR', pNazwa);
	END;
	
	PROCEDURE UsunKonto AS
	BEGIN
		DELETE FROM uzytkownicy WHERE id = USER;
	END;
	
END Wspolne;
/

commit;