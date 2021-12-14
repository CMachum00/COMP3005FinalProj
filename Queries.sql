
_______________________________
select count(*) from customer
where u_name = 'user' 
--used to find if a user exists for account ('user' is replaced with input from java [system.in])
--used mainly for creating new account (is the username available)
_______________________________
select count(*) from customer
where u_name = 'user' and pass = 'pass'
-- user and pass are replaced with user input
--used to determine if an account exists or not (if count > 0 then account exists and credentials are good)
_______________________________
select cust_id from customer
where u_name = 'user' and pass = 'pass'
--used to get cust_id for use withing java application, cust_id is set to a variable to be tracked
_______________________________
insert into [table_name]
values ([var1], [var2], etc...)
--to be used to insert any records into the DB, ex: new customer upon registration, or a new order into the orders
_______________________________
UPDATE book
SET quant = (select quant from book where isbn = 'inputISBN') -2
WHERE isbn='inputISBN'

--used to update the quantity of books in the store after orders are placed
--"2" and the "ISBN" would be repaced with the quantity and ISBN from new orders placed (java variables from system.in) 
_______________________________
select genre, SUM(orders.quant) as total_sales
from orders join book on book.isbn=orders.isbn 
group by genre 
_______________________________
select auth, SUM(orders.quant) as total_sales 
from orders join book on book.isbn=orders.isbn 
group by auth 
--used for reports, will get sales numbers for each author
_______________________________
select * from book where b_name == 'input'
--used to get the book info when customer is searching for a book
_______________________________
with ei as
(
select card_num, billing_addr, shipping_addr from customer
where cust_id = 123456
	)
insert into orders
values([info from input])
--used to retrieve pre-entered info for placing an order, if customer chooses to use pre-entered info they can, otherwise they can enter new info
_______________________________
select o.isbn, publisher_id, sum(o.quant) as books_sold, commission,price
from orders as o join book on book.isbn = o.isbn join published on o.isbn = published.isbn
group by o.isbn, publisher_id, commission, price
--sales vs expenditures SQL


--These are the queries used, there are certain ones that I did not include as they are just small variations of the above queries (ex: insert into or select-where statements)
--All specific examples can be found within the Java code if you would like to take a look

