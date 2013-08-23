SET IDENTITY_INSERT offender ON;
insert into offender(id, first_name, last_name, ssn, tin)  values
(1,'fn1', 'ln1',  'ssn111','tin111'),
(2,'fn1', 'ln1',  'ssn222','tin222'),
(3,'three', 'three3',  'ssn3','tin333'),
(4,'four', 'four4',  'ssn3','tin444'),
(5,'five', 'five5',  'ssn5'    ,'tin5'),
(6,'six', 'six6',  'ssn6'    ,'tin6'),
(7,'seven', 'seven7',  'ssn7','tin7'),
(8,'eight', 'eight8',  'ssn8','tin8'),
(9,'nine', 'nine9',  'ssn9','tin9'),
(10,'ten','ten10','ssn10','tin10');
SET IDENTITY_INSERT offender OFF;



SET IDENTITY_INSERT offender_Addr ON;
insert into offender_Addr (id,address1,	address2, address3,	address4, address5, address6, city,	state_code,	country_code, zip, ext,	offender_fk) values
(1,'a191','a192','','','','','ci19','ab','cc19','12341','1231','1'),
(2,'a1912','a1922','','','','','ci19','ab','cc19','12341','1231','2'),
(3,'a2113','a2223','','','','','ci21','ef','cc21','12342','1232','3'),
(4,'','a2224','','a2114','','','ci21','gh','cc21','12342','1232','4'),
(5,'a231','a232','','','','','ci23','ij','cc21','12342','1232','5'),
(6,'','a2426','','a2416','','','ci24','ef','cc21','12342','1232','6'),
(7,'a2517','a2527','','','','','ci25','ef','cc25','12342','1232','7'),
(8,'','a2628','','a2618','','','ci26','gh','cc21','12342','1232','8'),
(9,'a2719','a2729','','','','','ci27','kl','cc27','12343','1233','9'),
(10,'','a28210','','a28110','','','ci28','mn','cc28','12344','1234','10');
SET IDENTITY_INSERT offender_Addr OFF;