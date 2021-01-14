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
		
	FUNCTION PobierzIdDruzyny
		RETURN NUMBER;
		
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
	
	FUNCTION PobierzIdDruzyny
		RETURN NUMBER AS
	BEGIN
		SELECT id
		INTO vId
		FROM druzyny
		WHERE menadzer = (SELECT nazwa_uzytkownika
						  FROM uzytkownicy
                          WHERE id = USER
                          );
		RETURN vId;
	END;

END Menadzer;
/

CREATE OR REPLACE PACKAGE Organizator AS
	PROCEDURE NoweMistrzostwa(
		pNazwa IN mistrzostwa.nazwa%TYPE,
		pData_begin IN mistrzostwa.data_begin%TYPE,
		pData_end IN mistrzostwa.data_end%TYPE,
		pOrganizator IN mistrzostwa.organizator%TYPE,
		pLokalizacja IN mistrzostwa.lokalizacja%TYPE,
		pTyp IN mistrzostwa.typ%TYPE,
		pGra IN gry.tytul%TYPE,
		pNagroda IN mistrzostwa.nagroda%TYPE DEFAULT NULL);
		
	PROCEDURE EdytujMistrzostwa(
		pId IN mistrzostwa.id%TYPE,
		pNazwa IN mistrzostwa.nazwa%TYPE,
		pData_begin IN mistrzostwa.data_begin%TYPE,
		pData_end IN mistrzostwa.data_end%TYPE,
		pLokalizacja IN mistrzostwa.lokalizacja%TYPE,
		pTyp IN mistrzostwa.typ%TYPE,
		pNagroda IN mistrzostwa.nagroda%TYPE,
		pStatus IN mistrzostwa.status%TYPE);
		
	FUNCTION PobierzNazweOrganizatora
		RETURN VARCHAR2;
		
	FUNCTION PobierzLiczbeMistrzostw
		RETURN NUMBER;
		
	FUNCTION PobierzSumeNagrod
		RETURN NUMBER;
		
END Organizator;
/

CREATE OR REPLACE PACKAGE BODY Organizator AS
	PROCEDURE NoweMistrzostwa(
		pNazwa IN mistrzostwa.nazwa%TYPE,
		pData_begin IN mistrzostwa.data_begin%TYPE,
		pData_end IN mistrzostwa.data_end%TYPE,
		pOrganizator IN mistrzostwa.organizator%TYPE,
		pLokalizacja IN mistrzostwa.lokalizacja%TYPE,
		pTyp IN mistrzostwa.typ%TYPE,
		pGra IN gry.tytul%TYPE,
		pNagroda IN mistrzostwa.nagroda%TYPE DEFAULT NULL) AS
		vId mistrzostwa.id%TYPE;
		vStatus mistrzostwa.status%TYPE;
	BEGIN
		vId := id_mistrzostw_SEQ.NEXTVAL;
		
		IF CURRENT_DATE - pData_begin < 0 THEN
			vStatus := 'przed rozpoczęciem';
		ELSIF pData_end IS NOT NULL THEN
			IF CURRENT_DATE - pData_end < 0 THEN
				vStatus := 'trwają';
			ELSE
				vStatus := 'zakończone';
			END IF;
		ELSE
			vStatus := 'trwają';
		END IF;
		
		INSERT INTO mistrzostwa 
		VALUES(vId, pNazwa, pData_begin, pData_end, pOrganizator, 
				pLokalizacja, pTyp, pNagroda, pGra, vStatus);
		
		IF pTyp = 'Indywidualne' THEN
			INSERT INTO mistrzostwa_indywidualne VALUES(vId);
		ELSIF pTyp = 'Drużynowe' THEN
			INSERT INTO mistrzostwa_druzynowe VALUES(vId);
		END IF;
	END;
	
	PROCEDURE EdytujMistrzostwa(
		pId IN mistrzostwa.id%TYPE,
		pNazwa IN mistrzostwa.nazwa%TYPE,
		pData_begin IN mistrzostwa.data_begin%TYPE,
		pData_end IN mistrzostwa.data_end%TYPE,
		pLokalizacja IN mistrzostwa.lokalizacja%TYPE,
		pTyp IN mistrzostwa.typ%TYPE,
		pNagroda IN mistrzostwa.nagroda%TYPE,
		pStatus IN mistrzostwa.status%TYPE) AS
	BEGIN		
		UPDATE mistrzostwa
		SET nazwa = pNazwa,
			data_begin = pData_begin,
			data_end = pData_end,
			lokalizacja = pLokalizacja,
			nagroda = pNagroda,
			status = pStatus
		WHERE id = pId;
	END;
	
	FUNCTION PobierzNazweOrganizatora
		RETURN VARCHAR2 AS
		vNazwa uzytkownicy.nazwa_uzytkownika%TYPE;
	BEGIN
		SELECT nazwa_uzytkownika
		INTO vNazwa
		FROM uzytkownicy
		WHERE id = USER;
		RETURN vNazwa;
	END;

	FUNCTION PobierzLiczbeMistrzostw
		RETURN NUMBER AS
		vLiczba NUMBER;
	BEGIN
		SELECT COUNT(*)
		INTO vLiczba
		FROM mistrzostwa
		WHERE organizator = (SELECT nazwa_uzytkownika
							 FROM uzytkownicy
							 WHERE id = USER);
		RETURN vLiczba;
	END;
	
	FUNCTION PobierzSumeNagrod
		RETURN NUMBER AS
		vSuma NUMBER;
	BEGIN
		SELECT SUM(nagroda)
		INTO vSuma
		FROM mistrzostwa
		GROUP BY organizator HAVING(organizator = (
								SELECT nazwa_uzytkownika
								FROM uzytkownicy
								WHERE id = USER));
		RETURN vSuma;
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