BEGIN TRANSACTION;
SET TRANSACTION READ WRITE;

SELECT count(*) FROM lieferung;

-- noinspection SqlWithoutWhere
DELETE FROM lieferant;

SELECT count(*) FROM lieferung;
-- ON DELETE CASCADE löscht Lieferungen wenn Lieferant gelöscht wird

ROLLBACK TRANSACTION;

SELECT count(*) FROM lieferung;

