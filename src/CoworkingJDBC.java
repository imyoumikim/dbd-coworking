import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CoworkingJDBC {			// 공유오피스 스키마를 위한 JDBC/MySQL 프로그램

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Connection conn = DriverManager.getConnection(Secret.url,Secret.user,Secret.pw);
        Statement stmt = conn.createStatement();

        int menu;					// 메뉴 번호

        Scanner sc = new Scanner(System.in);
        System.out.println("<프로그램 시작>");

        while(true){					// 종료 번호 누르기 전까지 계속 반복
            System.out.println("1. 입주 기업의 오피스 정보 조회\n2. 원하는 지역, 편의 시설의 오피스 정보 조회 \n3. 종료");
            System.out.print("원하는 기능의 메뉴 번호를 입력하세요: ");
            menu = sc.nextInt();		// 메뉴 입력 받음

            if(menu == 1) {	// 1. 입주 기업의 오피스 정보 조회
                System.out.print("\n입주 기업의 이름을 입력하세요: ");
                String name = sc.next();		// 입주 기업 이름

                try {	// 입력 받은 기업 이름을 where 조건에 넣음으로 조건에 따른 결과를 출력
                    String sql1 = "SELECT c.comp_name, o.office_id, building, room_num, region, address "
                            + "FROM coworking.office o, coworking.contract c "
                            + "WHERE o.office_id = c.office_id and c.comp_name = '" + name + "'";

                    ResultSet rset = stmt.executeQuery(sql1);

                    System.out.println("검색 결과 >>");
                    while(rset.next()) {

                        System.out.println("Company name = "+ rset.getString(1) + " / Office id = "+rset.getString(2)
                                + " / Building = "+rset.getString(3)+" / Room # = " + rset.getString(4)
                                + " / Region = "+rset.getString(5) + " / Address = " + rset.getString(6) + "\n");
                    }

                }catch(SQLException sqle){
                    System.out.println("SQLException : "+sqle);
                }
            }
            else if(menu == 2) { //2. 원하는 지역, 편의 시설의 오피스 정보 조회
                System.out.println("\n당신의 조건에 부합하는 오피스를 검색합니다.");
                System.out.print("원하는 지역을 입력하세요: ");
                String region = sc.next();
                System.out.println("선호하는 편의 시설의 최소 조건을 입력하세요('예'는 1, '아니오'는 0)");
                System.out.print("주말 이용 가능: ");
                String wk = sc.next();
                System.out.print("공유 주방: ");
                String kc = sc.next();
                System.out.print("편의점: ");
                String cs = sc.next();
                System.out.print("프린터: ");
                String pr = sc.next();

                try {
                    String sql2 = "SELECT office_id, building, room_num, address "
                            + "FROM office "
                            + "WHERE region = '"+ region + "' and office_id in (select office_id "
                            + "FROM amenity WHERE weekend = '" + wk + "' and kitchen = '" + kc
                            + "' and store = '" + cs + "' and printer = '" + pr + "')";

                    ResultSet rset = stmt.executeQuery(sql2);

                    System.out.println("\n검색 결과 >>");

                    while(rset.next()) {
                        System.out.println("Office id = "+ rset.getString(1) + " / Building = "+rset.getString(2)
                                + " / Room# = "+rset.getString(3) + " / Address = " + rset.getString(4));
                    }
                    System.out.println();

                }catch(SQLException sqle){
                    System.out.println("SQLException : "+sqle);
                }
            }
            else if(menu == 3) {
                System.out.println(">>> 프로그램을 종료합니다.");
                break;
            }
        }

        stmt.close();
        conn.close();

    }

}
