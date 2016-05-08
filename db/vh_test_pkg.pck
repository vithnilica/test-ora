create or replace package vh_test_pkg is

procedure p1(n number, s varchar, d date);
procedure p2(x1 xmltype);
procedure p3(x1 vh_test_t1);
procedure p4(x1 vh_test_a1);
procedure p5(x1 clob);
procedure p6(x1 blob);
procedure p7(x1 vh_test_t2);
procedure p8(x1 out vh_test_t2);
function p9 return vh_test_t2;

end;
/
create or replace package body vh_test_pkg is

procedure p1(n number, s varchar, d date)is
begin
  vh_loguj('vh_test_pkg.p1 n:'||n||',s:'||s||',d:'||to_char(d,'dd.mm.yyyy hh24:mi:ss'));
end;

procedure p2(x1 xmltype)is
begin
  vh_loguj('vh_test_pkg.p2 x1:'||x1.getStringVal());
end;

procedure p3(x1 vh_test_t1)is
begin
  vh_loguj('vh_test_pkg.p3 n:'||x1.n||',s:'||x1.s||',d:'||to_char(x1.d,'dd.mm.yyyy hh24:mi:ss'));
end;

procedure p4(x1 vh_test_a1)is
begin
  if x1 is null then
    vh_loguj('vh_test_pkg.p4 null');
    return;
  end if;
  vh_loguj('vh_test_pkg.p4 count: '||x1.count||' ,[1]:'||x1(1));
end;

procedure p5(x1 clob)is
begin
  vh_loguj('vh_test_pkg.p5 x1:'||dbms_lob.getlength(x1)||' '||substr(x1,1,4000));
end;

procedure p6(x1 blob)is
begin
  vh_loguj('vh_test_pkg.p6 x1:'||dbms_lob.getlength(x1));
end;

procedure p7(x1 vh_test_t2)is
begin
  vh_loguj('vh_test_pkg.p7 n:'||x1.n||',s:'||x1.s||',d:'||to_char(x1.d,'dd.mm.yyyy hh24:mi:ss')||'c2.n:'||x1.c2.n||',c2.s:'||x1.c2.s||',c2.d:'||to_char(x1.c2.d,'dd.mm.yyyy hh24:mi:ss'));
end;

procedure p8(x1 out vh_test_t2)is
begin
  vh_loguj('vh_test_pkg.p8');
  x1:=vh_test_t2(1,'aaaa',sysdate,xmltype('<adsadsdsa/>'),vh_test_t1(1,'zzz',sysdate),vh_test_a1());
  x1.c3.extend();
  x1.c3(1):='Ahoj';
end;
function p9 return vh_test_t2 is
  x1 vh_test_t2;
begin
  vh_loguj('vh_test_pkg.f9');
  x1:=vh_test_t2(1,'aaaa',sysdate,xmltype('<adsadsdsa/>'),vh_test_t1(1,'zzz',sysdate),vh_test_a1());
  x1.c3.extend;
  x1.c3(1):='Ahoj';
  return x1;
end;

end;
/
