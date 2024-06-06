create table if not exists users (
    id bigserial not null primary key,
    username varchar(100) unique not null,
    password varchar(255) not null
);


create table if not exists user_profiles (
    id bigserial not null primary key,
    user_id bigserial not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(100) unique not null,
    image_name varchar(100) default 'default',
    image_type varchar(100) default 'img/png',
    image_url varchar(255) default '/api/v1/users/photos/default.png'
);