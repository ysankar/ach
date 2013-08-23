create table offender(
  id bigint identity(1,1) not null,
  first_name varchar(255) null,
  last_name varchar(255) null,
  ssn varchar(255) null,
  tin varchar(255) null, 
  middle_name varchar(255) null,
  contact_info varchar(255)  null,
  status varchar(255) null,
  comments varchar(255) null);
alter table offender add constraint pk__offender primary key clustered (id);

create table offender_addr(
  id bigint identity(1,1) not null,
  address1 varchar(255) null,
  address2 varchar(255) null,
  address3 varchar(255) null,
  address4 varchar(255) null,
  address5 varchar(255) null,
  address6 varchar(255) null,
  city varchar(255) null,
  state_code varchar(255) null,
  country_code varchar(255) null,
  zip varchar(255) null,
  ext varchar(255) null,
  offender_fk bigint null);
alter table offender_addr add constraint pk__offender_addr primary key clustered (id);
alter table offender_addr add constraint fk__offender_addr__offender foreign key(offender_fk) references offender(id);
