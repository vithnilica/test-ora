create or replace type vh_test_t2 as object
(
  n number,
  s varchar2(1000),
  d date,
  x xmltype,
  t1 vh_test_t1,
  a1 vh_test_a1
)
/
