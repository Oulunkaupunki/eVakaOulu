SELECT
    now() AT TIME ZONE 'Europe/Helsinki'   AS aikaleima,
    d.name                                 AS toimintayksikkö,
    d.id                                   AS toimintayksikkö_id,
    d.opening_date                         AS toimintayksikön_alkupvm,
    d.closing_date                         AS toimintayksikön_loppupvm,
    d.type                                 AS toimintamuoto,
    d.provider_type                        AS järjestämistapa,
    ca.name                                AS palvelualue,
    ca.id                                  AS palvelualue_id,
    d.dw_cost_center                       AS dw_kustannuspaikka,
    (SELECT count(*)
     FROM placement p
     WHERE p.unit_id = d.id
         AND p.start_date <= (current_date AT TIME ZONE 'Europe/Helsinki')::date
         AND (current_date AT TIME ZONE 'Europe/Helsinki')::date <= p.end_date
    )                                      AS toimintayksikön_lapsimäärä,
    (SELECT count(*)
     FROM placement p
     WHERE p.unit_id = d.id
         AND p.start_date < date_trunc('month', (current_date AT TIME ZONE 'Europe/Helsinki')::date)
         AND date_trunc('month', (current_date AT TIME ZONE 'Europe/Helsinki')::date) < p.end_date --Edellisen kuun viimeisen päivän mukaan
    )                                      AS toimintayksikön_lapsimäärä_ed_kuun_lopussa,
    dg.name                                AS ryhmä,
    dg.id                                  AS ryhmä_id,
    dg.start_date                          AS ryhmän_alkupvm,
    dg.end_date                            AS ryhmän_loppupvm,
    dc.amount                              AS henkilökuntaa_ryhmässä,
    sum(
            CASE
                WHEN sar.arrived IS NOT NULL
                    THEN ROUND(
                                    EXTRACT(
                                            EPOCH FROM (
                                            LEAST(sar.departed, timezone('Europe/Helsinki', (current_date + interval '1' day)::date::timestamp)) -
                                            GREATEST(sar.arrived, timezone('Europe/Helsinki', current_date::timestamp))
                                        )
                                        ) / 3600 / 7.75 * sar.occupancy_coefficient / 7,
                                    4
                    )
                ELSE sa.count
                END
        )                          AS henkilökuntaa_läsnä,
    (SELECT count(*)
     FROM daycare_group_placement dgp
     WHERE dgp.daycare_group_id = dg.id
         AND dgp.start_date <= (current_date AT TIME ZONE 'Europe/Helsinki')::date
         AND (current_date AT TIME ZONE 'Europe/Helsinki')::date <= dgp.end_date
    )                                      AS ryhmän_lapsimäärä,
    (SELECT count(*)
     FROM daycare_group_placement dgp
     WHERE dgp.daycare_group_id = dg.id
         AND dgp.start_date < date_trunc('month', (current_date AT TIME ZONE 'Europe/Helsinki')::date)
         AND date_trunc('month', (current_date AT TIME ZONE 'Europe/Helsinki')::date) < dgp.end_date --Edellisen kuun viimeisen päivän mukaan
    )                                      AS ryhmän_lapsimäärä_ed_kuun_lopussa
FROM daycare_group dg
    JOIN daycare d on dg.daycare_id = d.id
    JOIN care_area ca on ca.id = d.care_area_id
    JOIN daycare_caretaker dc ON dg.id = dc.group_id
    AND dc.start_date <= (current_date AT TIME ZONE 'Europe/Helsinki')::date
    AND (dc.end_date >= (current_date AT TIME ZONE 'Europe/Helsinki')::date or dc.end_date is null)
    LEFT JOIN staff_attendance sa ON dg.id = sa.group_id
    AND sa.date = (current_date AT TIME ZONE 'Europe/Helsinki')::date
    LEFT JOIN (
    SELECT group_id, arrived, departed, occupancy_coefficient
    FROM staff_attendance_realtime
    WHERE departed IS NOT NULL
      AND type NOT IN ('OTHER_WORK', 'TRAINING')
    UNION ALL
    SELECT group_id, arrived, departed, occupancy_coefficient
    FROM staff_attendance_external
    WHERE departed IS NOT NULL
) sar ON sar.group_id = dg.id AND (date(arrived) = (current_date AT TIME ZONE 'Europe/Helsinki')::date OR date(departed) = (current_date AT TIME ZONE 'Europe/Helsinki')::date)
WHERE ((current_date AT TIME ZONE 'Europe/Helsinki')::date - interval '3 months' <= d.closing_date OR d.closing_date is null)
    AND ((current_date AT TIME ZONE 'Europe/Helsinki')::date - interval '3 months' <= dg.end_date OR dg.end_date is null)
GROUP BY
    d.name,
    d.id,
    d.opening_date,
    d.closing_date,
    d.type,
    d.provider_type,
    ca.name,
    ca.id,
    d.dw_cost_center,
    dg.name,
    dg.id,
    dg.start_date,
    dg.end_date,
    dc.amount
