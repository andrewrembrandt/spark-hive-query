DROP TABLE IF EXISTS security_raw;

CREATE TABLE security_raw(
  SecId integer,
  SecMnem char(20),
  ProductSupType char(6),
  Product char(12),
  CUSIP char(9),
  Agency char(3),
  Prefix char(2),
  PrefixId integer,
  IssAmt double,
  CollType char(4),
  IssDt date,
  MatDt date,
  OrigWac double,
  OrigWam integer,
  OrigWala integer,
  RetireDt date,
  IssueId integer,
  PriceId integer,
  TBAEligCode char(3),
  SsnCode char(3),
  InsSrc char(4),
  InsDt char(17),
  UpdSrc char(4),
  UpdDt char(17) )
  ROW FORMAT DELIMITED FIELDS TERMINATED BY "|";

LOAD DATA LOCAL INPATH 'GNM_SEC.DAT' OVERWRITE INTO TABLE security_raw;

