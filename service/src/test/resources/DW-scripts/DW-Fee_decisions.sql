SELECT
    fd.decision_number AS maksupäätöksen_numero,
    fd.id as maksupäätös_id,
    lower(fd.valid_during) AS alkupvm,
    upper(fd.valid_during) AS loppupvm,
    fd.decision_type AS huojennustyyppi,
    fd.family_size AS perhekoko,
    fdc.child_id AS lapsi_id,
    fdc.final_fee AS lapsikohtainen_maksu,
    fdc.placement_type AS toimintamuoto,
    ca.name AS palvelualue,
    d.name AS toimipaikka,
    d.dw_cost_center AS kustannuspaikka
FROM fee_decision fd
    JOIN fee_decision_child fdc on fd.id = fdc.fee_decision_id
    JOIN daycare d ON fdc.placement_unit_id = d.id
    JOIN care_area ca ON d.care_area_id = ca.id
WHERE fd.status = 'SENT'
    AND fd.decision_number IS NOT NULL -- ei tuoda effican päätöksiä
    AND current_date - INTERVAL '3 months' <= upper(fd.valid_during);