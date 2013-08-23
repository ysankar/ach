SET IDENTITY_INSERT claimant_registration ON;
insert into claimant_registration(id,concatinated_lines) values
(1,'crl1'),
(2,'crl2'),
(3,'crl3'),
(4,'crl4'),
(5,'crl5'),
(6,'crl6'),
(7,'crl7'),
(8,'crl8'),
(9,'crl9'),
(10,'crl10');

SET IDENTITY_INSERT claimant_registration OFF;


SET IDENTITY_INSERT claimant ON;

-- Below Claimants fail the validation Offender DB as their firstname and lastname match with claimants in Offender Database
insert into claimant(id, first_name, last_name, reference_no, ssn, tin, claimant_registration )  values
(1,'fn1', 'ln1', '1', 'ssn1',''    ,'1'),
(2,'fn1', 'ln1', '2', 'ssn2',''    ,'2');

-- Below Claimants fail the validation Offender DB as their ssn or tin match with one of claimants in Offender Database
insert into claimant(id, first_name, last_name, reference_no, ssn, tin, claimant_registration )  values
(3,'fn3', 'ln3', '3', 'ssn3',''    ,'3'),
(4,'fn4', 'ln4', '4', 'ssn3',''    ,'4'),
(9,'fn9', 'ln9', '9', 'ssn5','tin5','9'),
(10,'fn10','ln10','10','ssn6',''    ,'10');


-- Below Claimant fail the validation Offender DB as address of this claimant match with one of claimants in Offender Database
insert into claimant(id, first_name, last_name, reference_no, ssn, tin, claimant_registration )  values
(5,'fn5', 'ln5', '5', ''    ,'tin1','5');

insert into claimant(id, first_name, last_name, reference_no, ssn, tin, claimant_registration )  values
(6,'fn6', 'ln6', '6', ''    ,'tin1','6'),
(7,'fn7', 'ln7', '7', 'ssn4','tin4','7'),
(8,'fn8', 'ln8', '8', 'ssn4','tin4','8');
SET IDENTITY_INSERT claimant OFF;

SET IDENTITY_INSERT claimant_claim ON;
insert into claimant_claim(id, claim_identifier, control_number, claimant_fk, process_status ) values
(1, '101','201','1','OFFENDER CHECK'),
(2, '102','202','2','OFFENDER CHECK'),
(3, '103','203','3','OFFENDER CHECK'),
(4, '104','204','4','OFFENDER CHECK'),
(5, '105','205','5','OFFENDER CHECK'),
(6, '106','206','6','OFFENDER CHECK'),
(7, '107','207','7','OFFENDER CHECK'),
(8, '108','208','8','OFFENDER CHECK'),
(9, '109','209','9','OFFENDER CHECK'),
(10, '110','210','10','OFFENDER CHECK');
SET IDENTITY_INSERT claimant_claim OFF;

SET IDENTITY_INSERT claimant_address ON;
insert into claimant_address (id,address1,	address2, address3,	address4, address5, address6, city,	state_code,	country_code, zip, ext,	claimant_fk) values
(1,'a191','a192','','','','','ci19','ab','cc19','12341','1231','1'),
(2,'a191','a192','','','','','ci19','ab','cc19','12341','1231','2'),
(3,'a211','a222','','','','','ci21','ef','cc21','12342','1232','3'),
(4,'','a222','','a211','','','ci21','gh','cc21','12342','1232','4'),
(5,'a231','a232','','','','','ci23','ij','cc21','12342','1232','5'),
(6,'a261','a242','','a241','','','ci24','ef','cc21','12342','1232','6'),
(7,'a251','a252','','','','','ci25','ef','cc25','12342','1232','7'),
(8,'a281','a262','','a261','','','ci26','gh','cc21','12342','1232','8'),
(9,'a271','a272','','','','','ci27','kl','cc27','12343','1233','9'),
(10,'','a282','','a281','','','ci28','mn','cc28','12344','1234','10');
SET IDENTITY_INSERT claimant_address OFF;




