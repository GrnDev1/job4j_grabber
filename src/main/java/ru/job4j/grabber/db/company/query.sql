SELECT p.name, c.name
FROM person p
         JOIN company c
              ON p.company_id = c.id
WHERE company_id != 5;


WITH t AS
         (SELECT c.name, COUNT(p.name) count
          FROM company c
                   JOIN person p
                        ON c.id = p.company_id
          GROUP BY c.name)

SELECT name, count
FROM t
WHERE count = (SELECT MAX(count) FROM t);