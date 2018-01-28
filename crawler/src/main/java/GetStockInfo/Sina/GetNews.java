package GetStockInfo.Sina;

import org.springframework.jdbc.core.JdbcTemplate;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class GetNews extends BreadthCrawler{
	String bid="";
	public GetNews(String crawlPath, boolean autoParse,String bid) {
		super(crawlPath, autoParse);
		this.bid=bid;
	}
	
	public void visit(Page page, CrawlDatums next){
		JdbcTemplate jdbcTemplate=JDBCHelper.getJdbcTemplate("news_sina");
        if (page.matchUrl("http://finance.sina.com.cn/stock/.*shtml")) {
        	String url=page.url();
        	String title = page.select("h1#artibodyTitle").text();
            String time = page.select("span.time-source").text();
            String year=time.substring(0,4);
            String month=time.substring(5,7);
            String day=time.substring(8,10);
            String hour=time.substring(11,13);
            String minute=time.substring(14,16);
            String content=page.select("div#artibody.article.article_16>p").text();
    			
    		if (jdbcTemplate != null) {
    			int updates=jdbcTemplate.update("insert into news_sina_2017"
    			     +" (bid,title,year,month,day,hour,minute,second,content,url) value(?,?,?,?,?,?,?,?,?,?)",
    			     this.bid,title, year,month,day,hour,minute,"",content,url);
    			if(updates==1){
    			    System.out.println("mysql插入成功");
    			}
    		}
        }
    }
	
	



}
