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
		
	PROCEDURE ZarejestrujUdzialZawodnika(
		pseudonim IN zawodnicy.pseudonim%TYPE,
		indywidualne_id IN mistrzostwa.id%TYPE);

	PROCEDURE ZarejestrujUdzialDruzynowy(
		id_druzyny IN druzyny.id%TYPE,
		druzynowe_id IN mistrzostwa.id%TYPE);
		
	PROCEDURE UsunZawodnika(
        nick IN zawodnicy.pseudonim%TYPE);
        
    PROCEDURE ModyfikujZawodnika(
        nick IN zawodnicy.pseudonim%TYPE,
        nowa_placa IN zawodnicy.placa%TYPE);
		
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
		IF placa <= 0 THEN
			RAISE_APPLICATION_ERROR(-20103, 'Płaca musi być większa od 0!');
		END IF;
		
		INSERT INTO Zawodnicy VALUES(pseudonim, imie, nazwisko, kraj, data_urodzenia, placa, id_druzyny);
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20102, 'Istnieje już zawodnik o takim pseudonimie!');
	END;

	PROCEDURE NowaDruzyna(
		nazwa IN druzyny.nazwa%TYPE,
		menadzer IN druzyny.menadzer%TYPE,
		region IN druzyny.region_nazwa_regionu%TYPE DEFAULT NULL) AS
	BEGIN
		INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, nazwa, menadzer, region);
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20101, 'Istnieje już drużyna o takiej nazwie!');
	END;
	
	PROCEDURE NowyRegion(
		nazwa IN regiony.nazwa_regionu%TYPE) AS
	BEGIN
		INSERT INTO regiony VALUES(nazwa);
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20104, 'Istnieje już region o takiej nazwie!');
	END;
	
	PROCEDURE ZarejestrujUdzialDruzynowy(
		id_druzyny IN druzyny.id%TYPE,
		druzynowe_id IN mistrzostwa.id%TYPE) AS
	BEGIN
		INSERT INTO udzialy_druzynowe(druzyna_id, druzynowe_id)
					VALUES(id_druzyny, druzynowe_id);

	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20105, 'Uczestnik jest już zgłoszony na te zawody!');
	END;
    
	PROCEDURE ZarejestrujUdzialZawodnika(
		pseudonim IN zawodnicy.pseudonim%TYPE,
		indywidualne_id IN mistrzostwa.id%TYPE) AS
	BEGIN
		INSERT INTO udzialy_indywidualne(zawodnik_pseudonim, indywidualne_id)
					VALUES(pseudonim, indywidualne_id);

	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20105, 'Uczestnik jest już zgłoszony na te zawody!');
	END;
	
	PROCEDURE ModyfikujZawodnika(
        nick IN zawodnicy.pseudonim%TYPE,
        nowa_placa IN zawodnicy.placa%TYPE) AS
    BEGIN
		IF nowa_placa <= 0 THEN
			RAISE_APPLICATION_ERROR(-20103, 'Płaca musi być większa od 0!');
		END IF;
		
        UPDATE zawodnicy
        SET placa = nowa_placa
		WHERE pseudonim = nick;
    END;
    
    PROCEDURE UsunZawodnika(
		nick IN zawodnicy.pseudonim%TYPE) AS
    BEGIN
        DELETE FROM zawodnicy WHERE nick = pseudonim;
    END;
	
	FUNCTION PobierzIdDruzyny
		RETURN NUMBER AS
		vId druzyny.id%TYPE;
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
		
	PROCEDURE DodajWynikDruzynowy(
		pId_m IN udzialy_druzynowe.druzynowe_id%TYPE,
		pId_d IN udzialy_druzynowe.druzyna_id%TYPE,
		pWynik IN udzialy_druzynowe.wynik%TYPE,
		pProcent IN udzialy_druzynowe.procent_puli%TYPE);
		
	PROCEDURE DodajWynikIndywidualny(
		pId_m IN udzialy_indywidualne.indywidualne_id%TYPE,
		pPseudonim IN udzialy_indywidualne.zawodnik_pseudonim%TYPE,
		pWynik IN udzialy_indywidualne.wynik%TYPE,
		pProcent IN udzialy_indywidualne.procent_puli%TYPE);
		
	FUNCTION PobierzNazweOrganizatora
		RETURN VARCHAR2;
		
	FUNCTION PobierzLiczbeMistrzostw
		RETURN NUMBER;
		
	FUNCTION PobierzSumeNagrod
		RETURN NUMBER;
		
	FUNCTION PobierzNazweDruzyny(
		pIdDruzyny IN druzyny.id%TYPE)
		RETURN VARCHAR2;
		
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
		IF pData_end IS NOT NULL AND pData_end - pData_begin < 0 THEN
			RAISE_APPLICATION_ERROR(-20201, 'Data zakończenia musi być późniejsza od daty rozpoczęcia!');
		ELSIF pNagroda <= 0 THEN
			RAISE_APPLICATION_ERROR(-20202, 'Pula nagród musi być większa od 0!');
		END IF;
		
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
		vStatus mistrzostwa.status%TYPE;
	BEGIN
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
		
		IF pData_end IS NOT NULL AND pData_end - pData_begin < 0 THEN
			RAISE_APPLICATION_ERROR(-20201, 'Data zakończenia musi być późniejsza od daty rozpoczęcia!');
		ELSIF pNagroda <= 0 THEN
			RAISE_APPLICATION_ERROR(-20202, 'Pula nagród musi być większa od 0!');
		ELSIF pStatus <> vStatus THEN
			RAISE_APPLICATION_ERROR(-20203, 'Niepoprawny status mistrzostw!');
		END IF;	
		
		UPDATE mistrzostwa
		SET nazwa = pNazwa,
			data_begin = pData_begin,
			data_end = pData_end,
			lokalizacja = pLokalizacja,
			nagroda = pNagroda,
			status = pStatus
		WHERE id = pId;
	END;
	
	PROCEDURE DodajWynikDruzynowy(
		pId_m IN udzialy_druzynowe.druzynowe_id%TYPE,
		pId_d IN udzialy_druzynowe.druzyna_id%TYPE,
		pWynik IN udzialy_druzynowe.wynik%TYPE,
		pProcent IN udzialy_druzynowe.procent_puli%TYPE) AS
		vPula mistrzostwa.nagroda%TYPE;
		vNagroda udzialy_druzynowe.nagroda%TYPE;
        vCount NUMBER;
	BEGIN
        SELECT COUNT(*)
        INTO vCount
        FROM udzialy_druzynowe 
        WHERE druzynowe_id = pId_m;
        
		IF (pWynik IS NULL) OR (pWynik NOT BETWEEN 1 AND vCount) THEN
			RAISE_APPLICATION_ERROR(-20204, 'Niepoprawne miejsce!');
		ELSIF pProcent NOT BETWEEN 0 AND 100 THEN
			RAISE_APPLICATION_ERROR(-20205, 'Procent puli musi być z przedziału <0, 100>');
		END IF;
		
		SELECT nagroda
		INTO vPula
		FROM mistrzostwa
		WHERE id = pId_m;
		
		vNagroda := pProcent / 100 * vPula;
		
		UPDATE udzialy_druzynowe
		SET wynik = pWynik,
			nagroda = vNagroda,
			procent_puli = pProcent
		WHERE druzynowe_id = pId_m AND druzyna_id = pId_d;
	END;
	
	PROCEDURE DodajWynikIndywidualny(
		pId_m IN udzialy_indywidualne.indywidualne_id%TYPE,
		pPseudonim IN udzialy_indywidualne.zawodnik_pseudonim%TYPE,
		pWynik IN udzialy_indywidualne.wynik%TYPE,
		pProcent IN udzialy_indywidualne.procent_puli%TYPE) AS
		vPula mistrzostwa.nagroda%TYPE;
		vNagroda udzialy_druzynowe.nagroda%TYPE;
        vCount NUMBER;
	BEGIN
        SELECT COUNT(*)
        INTO vCount
        FROM udzialy_druzynowe 
        WHERE druzynowe_id = pId_m;
        
		IF (pWynik IS NULL) OR (pWynik NOT BETWEEN 1 AND vCount) THEN
			RAISE_APPLICATION_ERROR(-20204, 'Niepoprawne miejsce!');
		ELSIF pProcent NOT BETWEEN 0 AND 100 THEN
			RAISE_APPLICATION_ERROR(-20205, 'Procent puli musi być z przedziału <0, 100>');
		END IF;
		
		SELECT nagroda
		INTO vPula
		FROM mistrzostwa
		WHERE id = pId_m;
		
		vNagroda := pProcent / 100 * vPula;
		
		UPDATE udzialy_indywidualne
		SET wynik = pWynik,
			nagroda = vNagroda,
			procent_puli = pProcent
		WHERE indywidualne_id = pId_m AND zawodnik_pseudonim = pPseudonim;
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

	FUNCTION PobierzNazweDruzyny(
		pIdDruzyny IN druzyny.id%TYPE)
		RETURN VARCHAR2 AS
		vNazwa druzyny.nazwa%TYPE;
	BEGIN
		SELECT nazwa
		INTO vNazwa
		FROM druzyny
		WHERE id = pIdDruzyny;
		RETURN vNazwa;
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
	
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20301, 'Istnieje już studio o takiej nazwie!');
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
		IF placa IS NULL OR placa <= 0 THEN
			RAISE_APPLICATION_ERROR(-20303, 'Płaca musi być większa od 0!');
		END IF;
		
		INSERT INTO pracownicy_studia 
				VALUES(pesel, imie, nazwisko, placa, data_zatrudnienia, dzial, studio);
				
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20302, 'Istnieje już pracownik o takim numerze PESEL!');
	END;
	
	PROCEDURE EdytujPracownika(
		pPesel IN pracownicy_studia.pesel%TYPE,
		pPlaca IN pracownicy_studia.placa%TYPE,
		pDzial IN pracownicy_studia.dzial%TYPE DEFAULT NULL) AS
	BEGIN
		IF pPlaca IS NULL OR pPlaca <= 0 THEN
			RAISE_APPLICATION_ERROR(-20303, 'Płaca musi być większa od 0!');
		END IF;
		
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
		IF kategoria_wiekowa <= 0 THEN
			RAISE_APPLICATION_ERROR(-20305, 'Niepoprawna kategoria wiekowa!');
		ELSIF box_office <= 0 OR budzet <= 0 THEN
			RAISE_APPLICATION_ERROR(-20306, 'Box office i budżet muszą być większe od 0!');
		END IF;
		
		INSERT INTO gry VALUES(tytul, data_wydania, kategoria_wiekowa, box_office, budzet, studio, gatunek);
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20304, 'Istnieje już gra o takim tytule!');
	END;
		
	PROCEDURE NowyGatunek(
		gatunek IN gatunki.nazwa_gatunku%TYPE,
		nadgatunek IN gatunki.nazwa_nadgatunku%TYPE DEFAULT NULL) AS
	BEGIN 
		INSERT INTO gatunki VALUES(gatunek, nadgatunek);
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20307, 'Istnieje już taki gatunek!');
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
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20001, 'Istnieje już użytkownik o takiej nazwie!');
	END;
	
	PROCEDURE DodajMenadzera(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE) AS
	BEGIN
		INSERT INTO uzytkownicy VALUES(USER, 'MENADZER', pNazwa);
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20001, 'Istnieje już użytkownik o takiej nazwie!');
	END;
	
	PROCEDURE DodajOrganizatora(
		pNazwa IN uzytkownicy.nazwa_uzytkownika%TYPE) AS
	BEGIN
		INSERT INTO uzytkownicy VALUES(USER, 'ORGANIZATOR', pNazwa);
		
	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
			RAISE_APPLICATION_ERROR(-20001, 'Istnieje już użytkownik o takiej nazwie!');
	END;
	
	PROCEDURE UsunKonto AS
	BEGIN
		DELETE FROM uzytkownicy WHERE id = USER;
	END;
	
END Wspolne;
/

commit;
