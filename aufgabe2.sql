-- 2.1
SELECT auftrnr, datum FROM auftrag WHERE kundnr IN (SELECT nr FROM kunde WHERE name = 'Fahrrad Shop');

-- 2.2
SELECT auftrnr, datum FROM auftrag WHERE kundnr = SOME (SELECT nr FROM kunde WHERE name = 'Fahrrad Shop');

-- 2.3
SELECT auftrnr, datum FROM auftrag WHERE EXISTS(SELECT nr FROM kunde WHERE name = 'Fahrrad Shop' AND kunde.nr == auftrag.kundnr);
