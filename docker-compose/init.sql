CREATE TABLE accounts(
 id serial PRIMARY KEY,
 name VARCHAR (50) UNIQUE NOT NULL,
 password VARCHAR (50) NOT NULL,
 email VARCHAR (355) UNIQUE NOT NULL,
 created_on TIMESTAMP NOT NULL
);

INSERT INTO public.accounts(
 	id, name, password, email, created_on)
	VALUES (default, 'Kraytsman','password','kraytsman@gmail.com',CURRENT_TIMESTAMP);
INSERT INTO public.accounts(
 	id, name, password, email, created_on)
	VALUES (default, 'VMGnomad94','hash','vmgnomad94@gmail.com',CURRENT_TIMESTAMP);
