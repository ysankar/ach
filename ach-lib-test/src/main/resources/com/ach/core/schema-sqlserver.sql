create table revision_info(
  id int identity(1,1) not null,
  timestamp bigint not null,
  user_name varchar(255) null,
  constraint pk__revision_info primary key clustered (id asc)
);

create table security_nav_ref (
  id bigint not null,
  security_id bigint not null,
  source varchar(255) null,
  trade_date date,
  low_value numeric(19,2) null,
  high_value numeric(19,2) null);

create table transaction_type(
  id bigint identity(1,1) not null,
  code varchar(255) not null,
  version int null,
  constraint pk__transaction_type primary key clustered (id asc)
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

create table claimant_position(
  id bigint identity(1,1) not null,
  account_balance numeric(19, 2) null,
  position_start_date date not null,
  position_end_date date not null,
  position_type varchar(50) not null,
  share_balance numeric(19, 2) not null,
  version int null,
  calculated_date date null,
  comments varchar(255) null,
  method varchar(255) null,
  status varchar(255) null,
  claimant_claim_fk bigint not null,
  claim_proof_fk bigint null,
  security_fund_fk bigint null,
  deleted bit null,
  constraint pk__claimant_position primary key clustered (id asc)
);

alter table claimant_position add  constraint fk__claimant_position__claimant_claim foreign key(claimant_claim_fk) references claimant_claim (id);

alter table claimant_position  with check add  constraint fk__claimant_position__security_fund foreign key(security_fund_fk) references security_fund (id);

create table claimant_transactions(
  id bigint identity(1,1) not null,
  beginning_balance numeric(19, 2) null,
  commission numeric(19, 2) null,
  cost_basis numeric(19, 2) null,
  discrepency_ind bit null,
  ending_balance numeric(19, 2) null,
  fees numeric(19, 2) null,
  gain_loss numeric(19, 2) null,
  gross_amount numeric(19, 2) null,
  net_amount numeric(19, 2) null,
  proof_provided_ind bit null,
  quantity numeric(19, 2) not null,
  settlement_date datetime2(7) not null,
  trade_date datetime2(7) not null,
  transaction_code varchar(50) null,
  source_transaction_code varchar(50) null,
  source_transaction_type varchar(50) null,
  unit_price numeric(19, 2) not null,
  version int null,
  witholding numeric(19, 2) null,
  comments varchar(255) null,
  claimant_claim_fk bigint not null,
  claim_proof_fk bigint null,
  security_fund_fk bigint null,
  transaction_type_fk bigint not null,
  total_cost numeric(19, 2) null,
  deleted bit null,
  constraint pk__claimant_transactions primary key clustered(id asc)
);

alter table claimant_transactions  add  constraint fk__claimant_transactions__claimant_claim foreign key(claimant_claim_fk) references claimant_claim (id);

alter table claimant_transactions add  constraint fk__claimant_transactions__security_fund foreign key(security_fund_fk) references security_fund (id);

alter table claimant_transactions  add  constraint fk__claimant_transactions__transaction_type foreign key(transaction_type_fk) references transaction_type (id);


create table claimant_claim_factor (
  claimant_claim_id bigint not null,
  claim_factor_type_id bigint not null,
  claim_factor_harm_amt numeric(19,4) null,
  date_calculated date null,
  numeric_harm_data_1 numeric(19,4) null,
  character_harm_data_1 varchar(255) null,
  constraint pk__claimant_claim_factor primary key clustered(claimant_claim_id asc, claim_factor_type_id asc)
);

alter table claimant_claim_factor add constraint fk__claimant_claim_factor__claimant_claim foreign key (claimant_claim_id) references claimant_claim (id);
alter table claimant_claim_factor add constraint fk__claimant_claim_factor__claim_factor_type foreign key (claim_factor_type_id) references claim_factor_type (id);


create table claimant_claim_factor_aud(
  claimant_claim_id bigint not null,
  claim_factor_type_id bigint not null,
  claim_factor_harm_amt numeric(19,4) null,
  date_calculated date null,
  numeric_harm_data_1 numeric(19,4) null,
  rev int not null,
  revtype smallint null,
  constraint pk__claimant_claim_factor_aud primary key clustered (claimant_claim_id asc, claim_factor_type_id asc, rev asc)
);

alter table claimant_claim_factor_aud add  constraint fk__claimant_claim_factor_aud__revision_info foreign key(rev) references revision_info (id);

create table claim_harm_amount(
  claimant_claim_id bigint not null,
  qualifier varchar(255) not null,
  harm_amount numeric(19, 4) not null,
  date_calculated date not null,
  constraint pk__claim_harm_amount primary key clustered (claimant_claim_id asc, qualifier asc)
);

alter table claim_harm_amount add constraint fk__claim_harm_amount__claimant_claim foreign key (claimant_claim_id) references claimant_claim (id);

create table claim_harm_amount_aud(
  claimant_claim_id bigint not null,
  qualifier varchar(255) not null,
  harm_amount numeric(19, 4) not null,
  date_calculated date not null,
  rev int not null,
  revtype smallint null,
  constraint pk__claim_harm_amount_aud primary key clustered (claimant_claim_id asc, qualifier asc, rev asc)
);





create table claimant_position_aud(
  id int not null,
  account_balance numeric(19, 2) null,
  position_start_date date not null,
  position_end_date date not null,
  position_type varchar(50) not null,
  share_balance numeric(19, 2) not null,
  version int null,
  calculated_date date null,
  comments varchar(255) null,
  method varchar(255) null,
  status varchar(255) null,
  claimant_claim_fk bigint not null,
  claim_proof_fk bigint null,
  security_fund_fk bigint null,
  deleted bit null,
  rev int not null,
  revtype smallint null,
  constraint pk__claimant_position_aud primary key clustered (id asc, rev asc)
);


alter table claimant_position_aud add  constraint fk__claimant_position_aud__revision_info foreign key(rev) references revision_info (id);



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

create table claimant_distribution_aud(
	id bigint not null,
	rev int not null,
	revtype smallint null,
	category varchar(255) null,
	category2 varchar(255) null,
	category3 varchar(255) null,
	distribution_amount numeric(19, 4) null,
	calculated_date datetime2(7) null,
	distribution_type varchar(255) null,
	source varchar(255) null,
	status varchar(255) null,
	claimant_claim_id bigint null,
	not_eligible_for_payment bit null,
	deminimis_calculated bit null,
    constraint pk__claimant_distribution_aud primary key clustered (id asc,rev asc)
	
);

alter table claimant_distribution_aud add  constraint fk__claimant_distribution_aud__revision_info foreign key(rev) references revision_info (id);
