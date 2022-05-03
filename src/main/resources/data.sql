INSERT INTO roles (Name) VALUES ('ROLE_USER');
INSERT INTO roles (Name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (Name) VALUES ('ROLE_MANAGER');


insert into users
select * from products;
select * from customers;
select * from orders;
DELETE FROM orders WHERE id=1;
insert into products(description,gst,name,partnumber,picture,price,unit,status) values("Mobile","10","Apple","ABC1234","apple.png","70000",0,1);
insert into products(description,gst,name,partnumber,picture,price,unit,status) values("Mobile","10","samsung","ABC1232","samsung.png","50000",0,1);
insert into products(description,gst,name,partnumber,picture,price,unit,status) values("Watch","10","Boat","ABC1235","boat.png","2000",0,1);
insert into products(description,gst,name,partnumber,picture,price,unit,status) values("Watch","10","Realmi","ABC1236","realmi.png","3000",0,1);
insert into products(description,gst,name,partnumber,picture,price,unit,status) values("Laptop","10","lenovo","ABC1237","lenovo.png","70000",0,1);
insert into products(description,gst,name,partnumber,picture,price,unit,status) values("Laptop","10","Apple","ABC1238","apple.png","70000",0,1);
UPDATE orders SET is_full_order = '0' WHERE id = 2;
UPDATE products SET picture = 'sumsung.png' ,price=50000 WHERE id = 2;
UPDATE products SET price = '13000' WHERE id = 2;
UPDATE products SET price = '43000' WHERE id = 3;
UPDATE products SET status = 1 WHERE id = 4;
UPDATE products SET status = 1 WHERE id = 5;
UPDATE products SET status = 1 WHERE id = 6;
UPDATE products SET status = 1 WHERE id = 7;