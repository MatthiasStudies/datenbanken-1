-- 2.1
SELECT auftrnr, datum FROM auftrag WHERE kundnr IN (SELECT nr FROM kunde WHERE name = 'Fahrrad Shop');

-- 2.2
SELECT auftrnr, datum FROM auftrag WHERE kundnr = SOME (SELECT nr FROM kunde WHERE name = 'Fahrrad Shop');

-- 2.3
SELECT auftrnr, datum FROM auftrag WHERE EXISTS (SELECT nr FROM kunde WHERE name = 'Fahrrad Shop' AND kunde.nr = auftrag.kundnr);

-- 2.4
SELECT kundnr, count(*), min(datum) AS "von", max(datum) AS "bis" FROM auftrag GROUP BY kundnr ORDER BY kundnr;

-- 2.5
SELECT kundnr, count(*), min(datum) AS "von", max(datum) AS "bis"
    FROM auftrag
        GROUP BY kundnr
            HAVING count(*) = 1
                ORDER BY kundnr;

-- 2.6
SELECT nr, name, auftrnr FROM kunde JOIN auftrag ON kunde.nr = auftrag.kundnr ORDER BY nr;

-- 2.7
SELECT nr, kunde.name, auftrnr, personal.name AS "mitarbeiter" FROM kunde
    JOIN auftrag ON kunde.nr = auftrag.kundnr
    JOIN personal ON auftrag.persnr = personal.persnr
                           ORDER BY nr;

-- 2.8
SELECT name, sum(ap.gesamtpreis) FROM kunde k
    JOIN auftrag a on k.nr = a.kundnr
    JOIN auftragsposten ap on a.auftrnr = ap.auftrnr
        GROUP BY k.nr
            ORDER BY sum(ap.gesamtpreis) DESC LIMIT 1;
-- 2.8.1
-- SELECT name, max(umsatz) FROM (SELECT name, sum(ap.gesamtpreis) as umsatz FROM kunde k
--                                                              JOIN auftrag a on k.nr = a.kundnr
--                                                              JOIN auftragsposten ap on a.auftrnr = ap.auftrnr
--                    GROUP BY k.nr) as kundeByUmsatz

-- 2.9 ?????
WITH kundeByUmsatz AS (SELECT name, sum(ap.gesamtpreis) AS umsatz FROM kunde k
    JOIN auftrag a on k.nr = a.kundnr
    JOIN auftragsposten ap on a.auftrnr = ap.auftrnr
      GROUP BY k.nr)  SELECT name, umsatz FROM kundeByUmsatz WHERE umsatz = (SELECT max(umsatz) FROM kundeByUmsatz);


-- 2.10
CREATE VIEW KundenUmsatz AS
    SELECT name, sum(ap.gesamtpreis) AS umsatz FROM kunde k
        JOIN auftrag a on k.nr = a.kundnr
        JOIN auftragsposten ap on a.auftrnr = ap.auftrnr
            GROUP BY k.nr;

SELECT name, umsatz FROM KundenUmsatz;

-- 2.11
DROP VIEW KundenUmsatz;