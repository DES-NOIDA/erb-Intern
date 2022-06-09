use employee_referral_leaderboard;

create table EMPLOYEES(
EMPLOYEE_ID bigint NOT NULL AUTO_INCREMENT,
EMPLOYEE_NAME varchar(255),
EMPLOYEE_TGI varchar(255) unique,
ONBOARD_POINTS bigint default 0,
EXTRA_POINTS bigint default 0,
NUMBER_OF_REFERALS int default 0,
TOTAL_POINTS bigint default 0, 
PRIMARY KEY (EMPLOYEE_ID)
);

create table USER(
USER_ID bigint NOT NULL  AUTO_INCREMENT,
USERNAME varchar(255),
PASSWORD varchar(255),
primary key (USER_ID)
);

create table REFERRAL(
REFERRAL_ID bigint NOT NULL AUTO_INCREMENT,
REFERRAL_NAME varchar(255),
REFERRAL_TGI varchar(255),
CANDIDATE_ID varchar(255) unique,
ONBOARDED varchar(25),
REFERRAL_POINTS bigint,
DATE_OF_REFERRAL date ,
DATE_OF_PROBATION date,
DATE_OF_JOINING date ,
POSITION_OFFERED varchar(255),
DIVERSITY varchar(25),
PROBATION varchar(25),
OFFER varchar(25),
REFERRAL_STATUS varchar(50),
EMPLOYEE_ID bigint,
REFERREE_TGI varchar(255),
PRIMARY KEY (REFERRAL_ID),
Foreign key (EMPLOYEE_ID) references EMPLOYEES(EMPLOYEE_ID)
ON DELETE CASCADE
);

create table POINTS(
POINT_KEY varchar(255) unique,
VALUE  bigint 
);

INSERT INTO EMPLOYEES (EMPLOYEE_NAME, EMPLOYEE_TGI)
VALUES ('Srishti Bhagat', 'T0263271');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Mudita Kaushik', 'T0262741');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Priyansh Sharma', 'T0263164');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Abhijeet Jha', 'T0263273');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME, EMPLOYEE_TGI)
VALUES ('Alok Singh Bhadauria', 'T0262528');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME, EMPLOYEE_TGI)
VALUES ('Amanul Haque', 'T0263272');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Eshan sharma', 'T0263163');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Geetakshi Bhardwaj', 'T0263269');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Nirali Saxena', 'T0263275');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Swati Gupta', 'T0265228');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Pratham Singhal', 'T0262449');
INSERT INTO EMPLOYEES (EMPLOYEE_NAME,  EMPLOYEE_TGI)
VALUES ('Vishwas Jain', 'T0262426');

INSERT INTO USER (USERNAME,  PASSWORD)
VALUES ('HRAdmin', '$2a$10$ibcduYykkHr10AcJiFeES.wETnzXyciws3uCltTlfqKmi.o99caVS');

insert into points(point_key,value)
values("onboarded", 1000);
insert into points(point_key,value)
values("probation_completed", 1000);
insert into points(point_key,value)
values("not_onboarded", -300);
insert into points(point_key,value)
values("probation_not_completed", -300);
Insert into points(point_key,value)
values("successful", 2500);
Insert into points(point_key,value)
values("diversity1", 1000);
Insert into points(point_key,value)
values("diversity2", 1500);
Insert into points(point_key,value)
values("diversity3", 2500);
insert into points(point_key,value)
values("unsuccessful", -500);
 
select * from EMPLOYEES;
select * from USER;
select * from REFERRAL;
select * from POINTS;