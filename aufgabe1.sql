-- 1.1
SELECT * FROM teilestamm;

-- 1.2
SELECT * FROM teilestamm WHERE bezeichnung LIKE '%City%';

-- 1.3
SELECT * FROM Auftragsposten ORDER BY gesamtpreis DESC LIMIT 1;

-- 1.4
SELECT count(*) as "Kunden count" FROM kunde;
SELECT count(*) as "Personal count" FROM personal;
SELECT count(*) as "Teilestamm count" FROM teilestamm;

-- 1.5
SELECT min(datum) as "von", max(datum) as "bis" FROM auftrag;

-- 1.6
SELECT * FROM auftrag WHERE auftrnr = 2;
SELECT * FROM kunde WHERE  nr = 3;
SELECT * FROM personal WHERE persnr = 5;
SELECT * FROM personal WHERE persnr = 1; --Vorgestzter

-- 1.7
SELECT (SELECT bezeichnung FROM teilestamm WHERE teilestamm.teilnr = lager.teilnr), bestand  FROM lager WHERE bestand >= 1 ORDER BY bestand ASC;


-- 1.8
SELECT DISTINCT teilnr FROM lieferung ORDER BY teilnr DESC;

-- 1.9
SELECT teilnr AS "Teilnummer", bezeichnung AS "Bezeichnung", preis AS "Bruto-Preis" FROM teilestamm WHERE preis > 30;

-- 1.10
SELECT einzelteilnr, anzahl FROM teilestruktur WHERE oberteilnr = 300001 AND anzahl > 100 AND einheit = 'CM';
