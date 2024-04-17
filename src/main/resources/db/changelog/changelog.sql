--liquibase formatted sql
--changeset user:1
create table category (
id serial primary key,
title varchar(255)
);

create table news (
id serial primary key,
title varchar(255),
text varchar(255),
date timestamp,
category_id bigint references category(id)
);