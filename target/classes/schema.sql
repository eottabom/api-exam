create table if not exists ACCOUNT
(
    ACCOUNT_NUM                  INT primary key,
    ACCOUNT_NAME                 VARCHAR(255) not null,
    MANAGEMENT_CODE              VARCHAR(255) not null,
) AS
SELECT *
  FROM CSVREAD('./src/main/resources/csv/account.csv');

create table if not exists TRANSACTION_DETAIL
(
    TRANSACTION_DATE       DATE,
    ACCOUNT_NUM             INT,
    TRANSACTION_NO         INT,
    AMOUNT                  INT,
    FEE                     INT,
    CANCEL_YN              VARCHAR(255)
) AS
SELECT *
FROM CSVREAD('./src/main/resources/csv/transactionDetail.csv');


create table if not exists MANAGEMENT
(
    MANAGEMENT_CODE              VARCHAR(255) not null primary key,
    MANAGEMENT_NAME              VARCHAR(255) not null
) AS
SELECT *
FROM CSVREAD('./src/main/resources/csv/management.csv');