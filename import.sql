DROP TABLE IF EXISTS security_raw;

CREATE TABLE security_raw(
  SecId int,
  SecMnem char(20),
  ProductSupType char(6),
  Product char(12),
  CUSIP char(9),
  Agency char(3),
  Prefix char(2),
  PrefixId int,
  IssAmt double,
  CollType char(4),
  IssDt char(8),
  MatDt char(8),
  OrigWac double,
  OrigWam int,
  OrigWala int,
  RetireDt char(8),
  IssueId int,
  PriceId int,
  TBAEligCode char(3),
  SsnCode char(3),
  InsSrc char(4),
  InsDt char(17),
  UpdSrc char(4),
  UpdDt char(17) )
  ROW FORMAT DELIMITED FIELDS TERMINATED BY "|";

LOAD DATA LOCAL INPATH 'GNM_SEC.DAT' OVERWRITE INTO TABLE security_raw;

