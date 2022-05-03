create table manga
(
    id              integer not null,
    title           text    not null,
    alt_title       text,
    url             text    not null,
    public_url      text    not null,
    rating          REAL    not null,
    is_nsfw         integer not null,
    cover_url       text    not null,
    large_cover_url text,
    state           text,
    author          text,
    source          text    not null,
    constraint manga_pk
        primary key (id)
);

create table tags
(
    id     integer not null,
    title  text    not null,
    key    text    not null,
    source text    not null,
    constraint tags_pk
        primary key (id)
);

create table manga_tags
(
    manga_id integer not null,
    tag_id   integer not null,
    constraint manga_tags_pk
        primary key (tag_id, manga_id),
    foreign key (manga_id) references manga
        on delete cascade,
    foreign key (tag_id) references tags
        on delete restrict
);

create table users
(
    id                        integer not null,
    email                     text    not null,
    password                  text    not null,
    nickname                  text,
    favourites_sync_timestamp integer,
    history_sync_timestamp    integer,
    constraint users_pk
        primary key (id autoincrement)
);

create table categories
(
    id         integer not null,
    created_at integer not null,
    sort_key   integer not null,
    title      text    not null,
    "order"    text    not null,
    user_id    integer not null,
    constraint categories_pk
        primary key (user_id, id),
    foreign key (user_id) references users
        on delete cascade
);

create table favourites
(
    manga_id    integer not null,
    category_id integer not null,
    created_at  integer not null,
    user_id     integer not null,
    constraint favourites_pk
        primary key (user_id, manga_id, category_id),
    foreign key (manga_id) references manga,
    foreign key (user_id) references users,
    constraint favourites_categories_id_fk
        foreign key (category_id) references categories (id)
);

create table history
(
    manga_id   integer not null,
    created_at integer not null,
    updated_at integer not null,
    chapter_id integer not null,
    page       integer not null,
    scroll     real    not null,
    user_id    integer not null,
    constraint history_pk
        primary key (user_id, manga_id),
    foreign key (manga_id) references manga,
    foreign key (user_id) references users
        on delete cascade
);

create unique index users_email_uindex
    on users (email);


