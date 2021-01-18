DROP ROLE Prezes_rola;
DROP ROLE Menadzer_rola;
DROP ROLE Organizator_rola;

CREATE ROLE Prezes_rola;
GRANT EXECUTE ON Prezes TO Prezes_rola;
GRANT SELECT ON pracownicy_studia TO Prezes_rola;
GRANT SELECT ON gatunki TO Prezes_rola;
GRANT SELECT ON platformy TO Prezes_rola;
GRANT SELECT ON gry TO Prezes_rola;

CREATE ROLE Menadzer_rola;
GRANT EXECUTE ON Menadzer TO Menadzer_rola;
GRANT SELECT ON regiony TO Menadzer_rola;
GRANT SELECT ON zawodnicy TO Menadzer_rola;
GRANT SELECT ON mistrzostwa TO Menadzer_rola;
GRANT SELECT ON udzialy_druzynowe TO Menadzer_rola;
GRANT SELECT ON udzialy_indywidualne TO Menadzer_rola;

CREATE ROLE Organizator_rola;
GRANT EXECUTE ON Organizator TO Organizator_rola;
GRANT SELECT ON gry TO Organizator_rola;
GRANT SELECT ON mistrzostwa TO Organizator_rola;
GRANT SELECT ON udzialy_druzynowe TO Organizator_rola;
GRANT SELECT ON udzialy_indywidualne TO Organizator_rola;
GRANT SELECT ON druzyny TO Organizator_rola;
GRANT SELECT ON zawodnicy TO Organizator_rola;

GRANT EXECUTE ON Wspolne TO PUBLIC;
