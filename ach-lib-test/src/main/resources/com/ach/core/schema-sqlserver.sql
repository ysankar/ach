create table revision_info(
  id int identity(1,1) not null,
  timestamp bigint not null,
  user_name varchar(255) null,
  constraint pk__revision_info primary key clustered (id asc)
);


create table claimant_registration(
  id bigint identity(1,1) not null,
  concatinated_lines varchar(255) null,
  registration1 varchar(255) null,
  registration2 varchar(255) null,
  registration3 varchar(255) null,
  registration4 varchar(255) null,
  registration5 varchar(255) null,
  registration6 varchar(255) null,
  version int null);

alter table claimant_registration add constraint pk__claimant_registration primary key clustered (id);

create table claimant(
  id bigint identity(1,1) not null,
  first_name varchar(255) null,
  last_name varchar(255) null,
  middle_name varchar(255) null,
  reference_no varchar(255) not null,
  ein varchar(255) null,
  ssn varchar(255) null,
  tin varchar(255) null,
  claimant_registration bigint not null);

alter table claimant add constraint pk__claimant primary key clustered (id);
alter table claimant add  constraint fk__claimant__claimant_registration foreign key(claimant_registration) references dbo.claimant_registration (id);
alter table claimant add  constraint uk__claimant UNIQUE NONCLUSTERED (claimant_registration);

create table claimant_address(
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
  claimant_fk bigint null);

alter table claimant_address add constraint pk__claimant_address primary key clustered (id);
alter table claimant_address add constraint fk__claimant_address__claimant foreign key(claimant_fk) references claimant (id);

create table claimant_claim(
  id bigint identity(1,1) not null,
  claim_identifier varchar(255) not null,
  control_number int not null,
  process_status varchar(255) null,
  recalc_required bit null,
  not_eligible_for_payment bit null,
  payment_inelig_reason varchar(255) null,
  status varchar(50) null,
  claimant_fk bigint null);

alter table claimant_claim add constraint pk__claimant_claim primary key clustered (id);
alter table claimant_claim   add  constraint fk__claimant_claim__claimant foreign key(claimant_fk) references dbo.claimant (id);

create table claim_activity (
  id bigint identity(1,1) not null,
  activity_date date NOT NULL,
  activity_type varchar (255) NOT NULL,
  comments varchar (255) NULL,
  status varchar (50) NOT NULL,
  system varchar (255) NULL,
  user_id  varchar (255)  NULL,
  version  int NULL,
  claimant_claim_fk bigint  null,
  constraint pk_claim_activity primary key clustered (id asc)
);

alter table claim_activity add  constraint fk__claim_activity__claimant_claim foreign key(claimant_claim_fk) references claimant_claim (id);
