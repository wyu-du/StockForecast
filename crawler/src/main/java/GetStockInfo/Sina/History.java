package GetStockInfo.Sina;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import GetStockInfo.Sina.JDBCHelper;
import GetStockInfo.Sina.GetCompanyReports;
import GetStockInfo.Sina.GetIndustryReports;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;


public class History {
	public static void main(String[] args) throws Exception {
		List rows=getBond();
		for(int i=0;i<rows.size();i++){
			Map result=(Map) rows.get(i);
			String bid=(String) result.get("bid");
			String industryname=(String)result.get("industryname");
			
			//开启爬虫
			getNews(bid);
			getCompanyReports(bid);
			getIndustryReports(bid,industryname);
		}
	}
	
	public static List getBond(){
    	JdbcTemplate jdbcTemplate = null;
    	List rows=new ArrayList<>();
    	try {
    	    	jdbcTemplate = JDBCHelper.createMysqlTemplate("bond_list",
    	            "jdbc:mysql://localhost/bond_info?useUnicode=true&characterEncoding=utf8",
    	            "root", "1230", 5, 30);
    	    
    	    	/*将数据库中的bid取出*/
    	    	rows = jdbcTemplate.queryForList("SELECT * FROM bond_list");
        	
    	    } catch (Exception ex) {
        	    jdbcTemplate = null;
        	    System.out.println("getBond:mysql未开启或JDBCHelper.createMysqlTemplate中参数配置不正确!");
        	    return null;
			}
    	return rows;
	}
	
    public static void getNews(String bid) throws Exception {
    	JdbcTemplate jdbcTemplate = null;
    	try {
    	    jdbcTemplate = JDBCHelper.createMysqlTemplate("news_sina",
    	            "jdbc:mysql://localhost/bond_info?useUnicode=true&characterEncoding=utf8",
    	            "root", "1230", 5, 30);
    	    
    	    	/*创建数据表*/
        	    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS news_sina ("
        	            + "id int(11) NOT NULL AUTO_INCREMENT,"
        	            + "bid varchar(50) NOT NULL,"
        	            + "title varchar(100),year varchar(5),month varchar(3),day varchar(3),hour varchar(3),minute varchar(3),content longtext,url varchar(200),"
        	            + "PRIMARY KEY (id)"
        	            + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
        	    System.out.println("成功创建数据表 news_sina");
    	    } catch (Exception ex) {
        	    jdbcTemplate = null;
        	    System.out.println("mysql未开启或JDBCHelper.createMysqlTemplate中参数配置不正确!");
			}
    	
	        GetNews crawler = new GetNews("crawler",true,bid);
	        if (bid.substring(0,1).equals("0")) {
				String id="sz"+bid;
				for(int i=1;i<100;i++){
		            crawler.addSeed(new CrawlDatum("http://vip.stock.finance.sina.com.cn/corp/view/vCB_AllNewsStock.php?symbol="+id+"&Page="+i)
		                    .meta("depth", "1"));
		        }
		        crawler.addRegex("http://finance.sina.com.cn/stock/.*shtml");
		        crawler.setThreads(30);
		        crawler.start(2);
			}else if(bid.substring(0,1).equals("6")){
				String id="sh"+bid;
				for(int i=1;i<100;i++){
		            crawler.addSeed(new CrawlDatum("http://vip.stock.finance.sina.com.cn/corp/view/vCB_AllNewsStock.php?symbol="+id+"&Page="+i)
		                    .meta("depth", "1"));
		        }
		        crawler.addRegex("http://finance.sina.com.cn/stock/.*shtml");
		        crawler.setThreads(30);
		        crawler.start(2);
			}
    }
    
    public static void getCompanyReports(String id) throws Exception {
    	JdbcTemplate jdbcTemplate = null;
    	try {
    	    jdbcTemplate = JDBCHelper.createMysqlTemplate("reports_company_sina",
    	            "jdbc:mysql://localhost/bond_info?useUnicode=true&characterEncoding=utf8",
    	            "root", "1230", 5, 30);
    	    
    	    	/*创建数据表*/
        	    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reports_company_sina ("
        	            + "id int(11) NOT NULL AUTO_INCREMENT,"
        	            + "year varchar(5),month varchar(3),day varchar(3),hour varchar(10),minute varchar(10),second varchar(10),bid varchar(50),"
        	            + "title varchar(100),institution varchar(300),content longtext,url varchar(200),"
        	            + "PRIMARY KEY (id)"
        	            + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
        	    System.out.println("成功创建数据表 reports_company_sina");
        	
    	    } catch (Exception ex) {
        	    jdbcTemplate = null;
        	    System.out.println("mysql未开启或JDBCHelper.createMysqlTemplate中参数配置不正确!");
			}
    	    
    	
        GetCompanyReports crawler = new GetCompanyReports("crawler", true,id);
        if (id.substring(0,1).equals("0")) {
			String bid="sz"+id;
			for(int i=1;i<100;i++){
	            crawler.addSeed(new CrawlDatum("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_List/kind/search/index.phtml?symbol="+bid+"&t1=all&p="+i)
	                    .meta("depth", "1"));
	        }
	        crawler.addRegex("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_Show/kind/search/rptid/.*phtml");
	        crawler.setThreads(30);
	        crawler.start(2);
		}
        else if (id.substring(0,1).equals("6")) {
			String bid="sh"+id;
			for(int i=1;i<100;i++){
	            crawler.addSeed(new CrawlDatum("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_List/kind/search/index.phtml?symbol="+bid+"&t1=all&p="+i)
	                    .meta("depth", "1"));
	        }
	        crawler.addRegex("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_Show/kind/search/rptid/.*phtml");
	        crawler.setThreads(30);
	        crawler.start(2);
		}
    }
    
    public static void getIndustryReports(String bid,String bname) throws Exception {
    	JdbcTemplate jdbcTemplate = null;
    	try {
    	    jdbcTemplate = JDBCHelper.createMysqlTemplate("reports_industry_sina",
    	            "jdbc:mysql://localhost/bond_info?useUnicode=true&characterEncoding=utf8",
    	            "root", "1230", 5, 30);
    	    
    	    	/*创建数据表*/
        	    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reports_industry_sina ("
        	            + "id int(11) NOT NULL AUTO_INCREMENT,"
        	            + "year varchar(5),month varchar(3),day varchar(3),hour varchar(10),minute varchar(10),second varchar(10),bid varchar(50),"
        	            + "title varchar(100),institution varchar(300),content longtext,url varchar(200),"
        	            + "PRIMARY KEY (id)"
        	            + ") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
        	    System.out.println("成功创建数据表 reports_industry_sina");
        	
    	    } catch (Exception ex) {
        	    jdbcTemplate = null;
        	    System.out.println("mysql未开启或JDBCHelper.createMysqlTemplate中参数配置不正确!");
			}
  	
        GetIndustryReports crawler = new GetIndustryReports("crawler", true,bid,bname);
        String bin=URLEncoder.encode(bname,"gb2312");
        for(int i=1;i<100;i++){
            crawler.addSeed(new CrawlDatum("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_List/kind/search/index.phtml?industry="+bin+"&t1=all&p="+i)
                    .meta("depth", "1"));
        }
        crawler.addRegex("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_Show/kind/search/rptid/.*phtml");
        crawler.setThreads(30);
        crawler.start(2);
    }
}
