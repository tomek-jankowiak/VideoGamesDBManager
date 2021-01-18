INSERT INTO uzytkownicy VALUES('organizator1', 'ORGANIZATOR', 'ESL');
INSERT INTO uzytkownicy VALUES('organizator2', 'ORGANIZATOR', 'PGL');

INSERT INTO uzytkownicy VALUES('prezes1', 'PREZES', 'Gabe Newell');

INSERT INTO uzytkownicy VALUES('menadzer1', 'MENADZER', 'Ocelote');
INSERT INTO uzytkownicy VALUES('menadzer2', 'MENADZER', 'YamatoCannon');
INSERT INTO uzytkownicy VALUES('menadzer3', 'MENADZER', 'Atomium');
INSERT INTO uzytkownicy VALUES('menadzer4', 'MENADZER', 'Schunurious');
INSERT INTO uzytkownicy VALUES('menadzer5', 'MENADZER', 'Frank');
INSERT INTO uzytkownicy VALUES('menadzer6', 'MENADZER', 'Ben Spoont');
INSERT INTO uzytkownicy VALUES('menadzer7', 'MENADZER', 'TheSlasH');
INSERT INTO uzytkownicy VALUES('menadzer8', 'MENADZER', 'Nico');

INSERT INTO regiony VALUES('EU');

INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'G2', 'Ocelote', 'EU');
INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'Fnatic', 'YamatoCannon', 'EU');
INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'Schalke 04', 'Atomium', 'EU');
INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'MAD Lions', 'Schunurious', 'EU');
INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'Rogue', 'Frank', 'EU');
INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'Misfits Gaming', 'Ben Spoont', 'EU');
INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'SK Gaming', 'TheSlasH', 'EU');
INSERT INTO druzyny VALUES(id_druzyny_SEQ.NEXTVAL, 'Team Vitality', 'Nico', 'EU');

INSERT INTO zawodnicy VALUES('Wunder', 'Martin', 'Hansen', 'Dania', TO_DATE('1998-11-09', 'YYYY-MM-DD'), 13000, 3);
INSERT INTO zawodnicy VALUES('Jankos', 'Marcin', 'Jankowski', 'Polska', TO_DATE('1995-07-23', 'YYYY-MM-DD'), 13000, 3);
INSERT INTO zawodnicy VALUES('Caps', 'Rasmus', 'Winther', 'Dania', TO_DATE('1990-11-17', 'YYYY-MM-DD'), 15000, 3);
INSERT INTO zawodnicy VALUES('Rekkles', 'Martin', 'Larsson', 'Szwecja', TO_DATE('1996-09-20', 'YYYY-MM-DD'), 15000, 3);
INSERT INTO zawodnicy VALUES('Mikyx', 'Mihael', 'Mehle', 'Słowenia', TO_DATE('1998-11-02', 'YYYY-MM-DD'), 13000, 3);

INSERT INTO studia 
VALUES('Valve', TO_DATE('1996-08-24', 'YYYY-MM-DD'), 'Gabe Newell');

INSERT INTO gry 
VALUES('Counter-Strike: Global Offensive', TO_DATE('2012-08-21', 'YYYY-MM-DD'), 18, 1200000000, 700000, 'Valve', 'Strzelanka');

INSERT INTO gry 
VALUES('Counter-Strike 1.6', TO_DATE('2003-09-12', 'YYYY-MM-DD'), 16, 3500000, 100000, 'Valve', 'Strzelanka');

INSERT INTO mistrzostwa
VALUES(id_mistrzostw_SEQ.NEXTVAL, 'ESL ONE: Rio 2020', TO_DATE('2020-11-09', 'YYYY-MM-DD'), TO_DATE('2020-11-22', 'YYYY-MM-DD'), 
		'ESL', 'Rio de Janeiro', 'Drużynowe', 2000000, 'Counter-Strike: Global Offensive', 'zakończone');
		
INSERT INTO mistrzostwa
VALUES(id_mistrzostw_SEQ.NEXTVAL, 'PGL Major Stockholm 2021', TO_DATE('2021-10-23', 'YYYY-MM-DD'), TO_DATE('2021-11-07', 'YYYY-MM-DD'), 
		'PGL', 'Stockholm', 'Drużynowe', 2000000, 'Counter-Strike: Global Offensive', 'przed rozpoczęciem');
		
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(3, 1);
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(4, 1);
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(5, 1);
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(6, 1);
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(7, 1);
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(8, 1);
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(9, 1);
INSERT INTO udzialy_druzynowe (druzyna_id, druzynowe_id) VALUES(10, 1);

INSERT INTO udzialy_indywidualne (indywidualne_id, zawodnik_pseudonim) VALUES(21, 'Wunder');
INSERT INTO udzialy_indywidualne (indywidualne_id, zawodnik_pseudonim) VALUES(21, 'Jankos');
INSERT INTO udzialy_indywidualne (indywidualne_id, zawodnik_pseudonim) VALUES(21, 'Caps');
INSERT INTO udzialy_indywidualne (indywidualne_id, zawodnik_pseudonim) VALUES(21, 'Mikyx');
 
		
commit;