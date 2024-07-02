--delete from cards;
--delete from money_transactions;
--delete from users;
--delete from accounts;
-- password: djibril09D!
insert into users(user_id,email,first_name,last_name,username,password,number,street,zip_code,is_admin) values (1,'cisse09@gmail.com','Djibril','Cisse','djibril09','$2a$10$FpDmrpQfzTFoZCBtH9F7Ye0zzFSPXm4Dtmo4Vg5jeocsggLemJese',3,'Patission','12321',1);
insert into users(user_id,email,first_name,last_name,username,password,number,street,zip_code,is_admin) values (2,'kennun@aueb.gr','Kendrick','Nunn','djibril091','$2a$10$FpDmrpQfzTFoZCBtH9F7Ye0zzFSPXm4Dtmo4Vg5jeocsggLemJese',3,'Kifisias','12333',0);
insert into users(user_id,email,first_name,last_name,username,password,number,street,zip_code,is_admin) values (3,'kosloy@gmail.com','Kostas','Sloukas','djibril092','$2a$10$FpDmrpQfzTFoZCBtH9F7Ye0zzFSPXm4Dtmo4Vg5jeocsggLemJese',3,'Vouliagmenis','12334',0);

insert into accounts(account_number,user_id, amount, currency) values (1,1,45,'EUR');
insert into accounts(account_number,user_id, amount, currency) values (2,2,4500000,'EUR');
insert into accounts(account_number,user_id, amount, currency) values (3,2,450,'EUR');

insert into cards(card_id,pin,expiration,activated,account_number) values (123456789012,'0000','2027-11-26',1,1);
insert into cards(card_id,pin,expiration,activated,account_number) values (123456789013,'0001','2028-11-26',1,1);
insert into cards(card_id,pin,expiration,activated,account_number) values (123456789015,'0001','2028-11-26',1,1);
insert into cards(card_id,pin,expiration,activated,account_number) values (123456789014,'1561','2030-11-26',1,2);
insert into cards(card_id,pin,expiration,activated,account_number) values (123456789016,'1561','2031-11-26',1,2);
insert into cards(card_id,pin,expiration,activated,account_number) values (123456789017,'1561','2031-11-26',0,2);

insert into money_transactions(transaction_id,trans_date,amount, account_number,transaction_type) values (3,'2024-01-01',20,1,'DEPOSIT');
insert into money_transactions(transaction_id,trans_date,amount, account_number,transaction_type) values (4,'2025-01-01',20,1,'DEPOSIT');
insert into money_transactions(transaction_id,trans_date,amount, account_number,transaction_type) values (5,'2024-01-01',30,2,'WITHDRAWAL');
insert into money_transactions(transaction_id,trans_date,amount, account_number,transaction_type) values (6,'2025-01-01',26,2,'WITHDRAWAL');
insert into money_transactions(transaction_id,trans_date,amount, account_number,transaction_type) values (7,'2024-01-01',20,2,'DEPOSIT');
insert into money_transactions(transaction_id,trans_date,amount, account_number,transaction_type) values (8,'2025-01-01',20,2,'DEPOSIT');