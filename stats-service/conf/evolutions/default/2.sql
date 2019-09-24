# --- !Ups
create table user_event (
  id                        int auto_increment not null,
  type                      varchar(128),
  content                   varchar(1024),
  constraint pk_user_event primary key (id)
);

# --- !Downs
drop table if exists user_event;
