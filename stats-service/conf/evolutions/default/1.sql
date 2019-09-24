# --- !Ups
create table user (
  id                            int auto_increment not null,
  nickname                      varchar(64),
  full_name                     varchar(255),
  kudos_qty                     int,
  constraint pk_user primary key (id)
);

# --- !Downs
drop table if exists user;
