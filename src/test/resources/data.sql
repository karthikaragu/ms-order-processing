DELETE from `autosparescm`.`orderdetail`;
DELETE from `autosparescm`.`purchaseorder`;
DELETE from `autosparescm`.`orderstatus`;

INSERT INTO `autosparescm`.`orderstatus`
(`statuscode`,
`statusdescription`)
VALUES
('P','Placed');

INSERT INTO `autosparescm`.`orderstatus`
(`statuscode`,
`statusdescription`)
VALUES
('D','Dispatched');

INSERT INTO `autosparescm`.`purchaseorder`
(`orderid`,
`invoicedate`,
`orderedby`,
`amount`,
`orderstatus`)
VALUES
(1,'2020-08-13 14:24:28',1,2000.000,'P');

INSERT INTO `autosparescm`.`orderdetail`
(`orderdetailid`,
`orderid`,
`productid`,
`quantity`,
`amount`,
`orderdetailstatus`)
VALUES
(1,1,4,2,2000.000,'P');