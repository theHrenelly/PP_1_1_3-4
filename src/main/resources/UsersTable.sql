create table if not exists users
(
    id       bigint auto_increment primary key,
    name     varchar(64) not null,
    lastName varchar(64) not null,
    age      tinyint not null
);