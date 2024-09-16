create table company(
    id integer not null,
    "name" character varying,
    constraint company_pkey primary key (id)
);

create table person(
    id integer not null,
    "name" character varying,
    company_id integer references company(id),
    constraint person_pkey primary key (id)
);


select * from company;
select * from person;

insert into company(id, name) values
    (1, 'Sber'),
    (2, 'Yandex'),
    (3, 'T-bank'),
    (4, 'Rostelecom'),
    (5, 'Apple'),
    (6, 'Amazon');

insert into person(id, name, company_id) values
    (1, 'Kirill', 1),
    (2, 'Alex', 1),
    (3, 'Sergey', 2),
    (4, 'Ivan', 2),
    (5, 'Evgen', 2),
    (6, 'Arsen', 3),
    (7, 'Petr', 4),
    (8, 'Nathaniel', 4),
    (9, 'Keith', 5),
    (10, 'Brennan', 5),
    (11, 'Dior', 5),
    (12, 'Sviatoslav', 5),
    (13, 'Elliot', 5),
    (14, 'Edward', 6),
    (15, 'Jeffrey', 6),
    (16, 'Shannon', 6),
    (17, 'Dmitrii', 6),
    (18, 'Vladlen', 6),
    (19, 'Drake', 6),
    (13, 'Elliot', 5),
    (20, 'Oliver', 2);

-- имена всех person, которые не состоят в компании с id = 5;
-- название компании для каждого человека.
select c.name, p.name
from person p
    join company c on c."id" = p.company_id
    where p.company_id != 5;


-- Необходимо выбрать название компании с максимальным количеством
-- человек + количество человек в этой компании.
-- Нужно учесть, что таких компаний может быть несколько.
select c.name Компания, count(p.name) количество
from person p
    join company c on c.id = p.company_id
group by c.name
    having count(p.company_id) = (select max(counted)
        from (select count(p.company_id) as counted
        from person p
        group by p.company_id)
);
