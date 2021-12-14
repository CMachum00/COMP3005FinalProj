create or replace table Customer
	(Cust_id		int,
	 Card_num		varchar(16),
	 Billing_addr		varchar(50),
	 Shipping_addr		varchar(50),
	 U_name			varchar(30),
	 Pass			varchar(30),
	 primary key (Cust_id)
	);

create or replace table Book
	(Name			varchar(40), 
	 Auth			varchar(30), 
	 ISBN		        varchar(13),
	 Genre			varchar(15),
	 Pages			int,
	 Quant			int,
	 Price			int,
	 primary key (ISBN)
	);

create or replace table Publisher
	(
	 Publisher_id		int,
	 Name			varchar(30),
	 Addr			varchar(50),
	 Email			varchar(30),
	 Bank_acct		varchar(30),
	 primary key (Publisher_id)
	);

create or replace table Order
	(Order_id		int UNIQUE, 
	 ISBN			varchar(13), 
	 Quant			int,
	 Date			date,
	 Card_num		varchar(16),
	 Biling_addr		varchar(50),
	 Shipping_addr		varchar(50),
	 Status			varchar(25),
	 primary key (Order_id, ISBN),
	 foreign key (ISBN) references Book
	);

create or replace table Places
	(
	 Cust_id		int,
	 Order_id		int,
	 primary key (Cust_id, Order_id),
	 foreign key (Cust_id) references Customer,
	 froeign key (Order_id) references Order
	);

create or replace table Published
	(
	 ISBN			varchar(13),
	 Publisher_id		int,
	 Commission		int,
	 primary key (ISBN, Publisher_id),
	 foreign key (ISBN) references Book,
	 froeign key (Publisher_id) references Publisher
	);

insert into customer
values (100001, '1111222233334444', '123 Easy st', '123 Easy st', 'test', 'pass');

insert into publisher
values (100001, 'Haven Books', '23 Sunnyside St', 'havenBooks@gmail.com', '1001200230034004');

insert into book
values ('Julius Caesar', 'Shakespeare', '1111111111110', 'Drama', 243, 30, 15);

insert into published
values ('1111111111110', 100001, 15);

insert into book
values ('Hamlet', 'Shakespeare', '1111111111111', 'Drama', 276, 30, 15);

insert into published
values ('1111111111111', 100001, 15);

insert into book
values ('Hedda Gabbler', 'Isben', '1111111111112', 'Thriller', 90, 30, 15);
);

insert into published
values ('1111111111112', 100001, 10);


