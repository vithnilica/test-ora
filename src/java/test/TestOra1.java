package test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLXML;
import java.sql.Struct;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.StructDescriptor;

public class TestOra1 {

	public static void main(String[] args) throws Exception {
		//Class.forName("oracle.jdbc.driver.OracleDriver");//nacitani driveru potreba pro stare jdbc
		//https://docs.oracle.com/javase/tutorial/jdbc/basics/sqlxml.html
		//java.lang.NoClassDefFoundError: oracle/xdb/XMLType (je potreba xdb6.jar http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-112010-090769.html)
		//java.lang.NoClassDefFoundError: oracle/xml/binxml/BinXMLMetadataProvider (je potreba xmlparserv2.jar {ORACLE_HOME}/oracle/produce/{VERSION_NUMBER}/lib/xmlparserrv2.jar)

		
		Connection con=DriverManager.getConnection("jdbc:oracle:thin:@azaix2b:1522:TEST152", "customer", "customer");

		//zakladni typy
		System.out.println("zacatek p1");
		CallableStatement p1 = con.prepareCall("{call vh_test_pkg.p1(:n,:s,:d)}");
		p1.setBigDecimal("n", new BigDecimal("1000000000000000000000"));
		p1.setString("s", "Příliš žluťoučký kůň úpěl ďábelské ódy.");
		p1.setDate("d", new Date(System.currentTimeMillis()));
		p1.execute();
		p1.close();
		con.commit();
		System.out.println("konec p1");
		
		//xmltype
		System.out.println("zacatek p2");
		SQLXML p2x1 = con.createSQLXML();
		Writer p2x1w=p2x1.setCharacterStream();
		p2x1w.write("<aaaaa/>");
		p2x1w.close();
		CallableStatement p2 = con.prepareCall("{call vh_test_pkg.p2(:x1)}");
		p2.setSQLXML("x1", p2x1);
		p2.execute();
		p2.close();
		con.commit();		
		System.out.println("konec p2");

		//objekt
		System.out.println("zacatek p3");
		//Object p3x1a[] = { new BigDecimal("1000000000000000000000"), "Příliš žluťoučký kůň úpěl ďábelské ódy.", new Date(0)};
		//Struct p3x1 = con.createStruct("VH_TEST_T1", p3x1a);
		
		//ohejbak ktery zajisti fungovani i po pridani nove polozky do typu
		StructDescriptor p3x1d=StructDescriptor.createDescriptor("VH_TEST_T1", con);
		//System.out.println(p3x1d.descType());
		//System.out.println(p3x1d.toXMLString());
		Object p3x1a[] = new Object[p3x1d.getLength()];
		p3x1a[0]=new BigDecimal("1000000000000000000000");
		p3x1a[1]="Příliš žluťoučký kůň úpěl ďábelské ódy.";
		p3x1a[2]=new Date(System.currentTimeMillis());
		Struct p3x1 = con.createStruct("VH_TEST_T1", p3x1a);		
		
		CallableStatement p3 = con.prepareCall("{call vh_test_pkg.p3(:x1)}");
		p3.setObject("x1", p3x1);
		p3.execute();
		p3.close();
		con.commit();	
		System.out.println("konec p3");
		
		
		//pole
		System.out.println("zacatek p4");
		String[] p4x1a={"Příliš žluťoučký kůň úpěl ďábelské ódy.","Ahoj"};
		//Array p4x1=con.createArrayOf("VH_TEST_A1", p4x1a);
		Array p4x1 = ((OracleConnection)con).createOracleArray("VH_TEST_A1",p4x1a);
		
		CallableStatement p4 = con.prepareCall("{call vh_test_pkg.p4(:x1)}");
		p4.setObject("x1", p4x1);
		p4.execute();
		p4.close();
		con.commit();	
		System.out.println("konec p4");
		
		//clob
		System.out.println("zacatek p5");
		CallableStatement p5 = con.prepareCall("{call vh_test_pkg.p5(:x1)}");
		p5.setClob("x1", new StringReader("Příliš žluťoučký kůň úpěl ďábelské ódy."));
		p5.execute();
		p5.close();
		con.commit();	
		System.out.println("konec p5");
		
		//blob
		System.out.println("zacatek p6");
		CallableStatement p6 = con.prepareCall("{call vh_test_pkg.p6(:x1)}");
		p6.setBlob("x1", new ByteArrayInputStream(new byte[10]));
		p6.execute();
		p6.close();
		con.commit();	
		System.out.println("konec p6");
		
		
		//slozenej objekt
		//objekt
		System.out.println("zacatek p7");
		//ohejbak ktery zajisti fungovani i po pridani nove polozky do typu
		StructDescriptor p7x1d=StructDescriptor.createDescriptor("VH_TEST_T2", con);
		//System.out.println(p7x1d.descType());
		//System.out.println(p7x1d.toXMLString());
		Object p7x1a[] = new Object[p7x1d.getLength()];
		p7x1a[0]=new BigDecimal("1000000000000000000000");
		p7x1a[1]="Příliš žluťoučký kůň úpěl ďábelské ódy.";
		p7x1a[2]=new Date(System.currentTimeMillis());
		
		SQLXML p7x1c1 = con.createSQLXML();
		Writer p7x1c1w=p7x1c1.setCharacterStream();
		p7x1c1w.write("<aaaaa/>");
		p7x1c1w.close();
		p7x1a[3]=p7x1c1;
		
		Struct p7x1 = con.createStruct("VH_TEST_T2", p7x1a);		
		
		CallableStatement p7 = con.prepareCall("{call vh_test_pkg.p7(:x1)}");
		p7.setObject("x1", p7x1);
		p7.execute();
		p7.close();
		con.commit();		
		System.out.println("konec p7");
		
		//volani procedury s out parametrem
		System.out.println("zacatek p8");
		CallableStatement p8 = con.prepareCall("{call vh_test_pkg.p8(:x1)}");
		//p8.registerOutParameter("x1", java.sql.Types.STRUCT);
		p8.registerOutParameter("x1", OracleTypes.STRUCT,"VH_TEST_T2");
		p8.execute();
		Struct p8x1=(Struct) p8.getObject("x1");
		System.out.println(p8x1.getAttributes()[0]);
		System.out.println(p8x1.getAttributes()[1]);
		System.out.println(p8x1.getAttributes()[2]);
		System.out.println(p8x1.getAttributes()[3]);
		p8.close();
		con.commit();
		System.out.println("konec p8");
		
		//volani funkce
		System.out.println("zacatek p9");
		CallableStatement p9 = con.prepareCall("{call :x1:=vh_test_pkg.p9}");
		p9.registerOutParameter("x1", OracleTypes.STRUCT,"VH_TEST_T2");
		p9.execute();
		Struct p9x1=(Struct) p9.getObject("x1");
		System.out.println(p9x1.getAttributes()[0]);
		System.out.println(p9x1.getAttributes()[1]);
		System.out.println(p9x1.getAttributes()[2]);
		System.out.println(p9x1.getAttributes()[3]);
		p9.close();
		con.commit();
		System.out.println("konec p9");
		
		
		
		//indexovana plsql tabulka
		System.out.println("zacatek p10");
		CallableStatement p10 = con.prepareCall("{call vh_test_pkg.p10(?,?)}");
		String[] p10x1v = { "aaa", "bbb", "ccc" };
		((OracleCallableStatement)p10).setPlsqlIndexTable(1, p10x1v, p10x1v.length, p10x1v.length, OracleTypes.VARCHAR, 4000);
		//2, maxLen, elemSqlType, elemMaxLen
		((OracleCallableStatement)p10).registerIndexTableOutParameter(2, 1000, OracleTypes.VARCHAR, 4000);
		p10.execute();
		//Object o=((OracleCallableStatement)p10).getPlsqlIndexTable(2);
		//System.out.println(o.getClass().getCanonicalName());
		String[] p10x2=(String[])((OracleCallableStatement)p10).getPlsqlIndexTable(2);
		System.out.println(p10x2[0]);
		p10.close();
		con.commit();
		System.out.println("konec p10");		
		
		con.close();
	}

}

