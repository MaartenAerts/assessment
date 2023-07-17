create sequence word_relation_seq start with 1 increment by 50;
create table word_relation
(
    id          bigint not null,
    first_word  varchar(255) not null,
    second_word varchar(255) not null,
    type        varchar(255) not null,
    primary key (id)
)