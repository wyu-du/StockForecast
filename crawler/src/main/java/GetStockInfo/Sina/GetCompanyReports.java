package GetStockInfo.Sina;

import org.springframework.jdbc.core.JdbcTemplate;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;


public class GetCompanyReports extends BreadthCrawler{
	String bid="";

	public GetCompanyReports(String crawlPath, boolean autoParse,String id) {
		super(crawlPath, autoParse);
		this.bid=id;
	}
	
	public void visit(Page page, CrawlDatums next){
		JdbcTemplate jdbcTemplate=JDBCHelper.getJdbcTemplate("reports_company_sina");
        if (page.matchUrl("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_Show/kind/search/rptid/.*phtml")) {
        	String url=page.url();
        	String title = page.select("div.content>h1").text();
            String time = page.select("div.creab>span:eq(3)").text().substring(3);
            String year=time.substring(0,4);
            String month=time.substring(5,7);
            String day=time.substring(8,10);
            String institution=page.select("div.creab>span:eq(1)").text().substring(3);
        	String content=page.select("div.blk_container>p").text();
        	
			if (jdbcTemplate != null) {
			    int updates=jdbcTemplate.update("insert into reports_company_sina"
			        +" (bid,title,year,month,day,hour,minute,second,institution,content,url) value(?,?,?,?,?,?,?,?,?,?,?)",
			        this.bid,title,year,month,day,"","","",institution, content,url);
			    if(updates==1){
			        System.out.println("mysql插入成功");
			    }
			}
        }
    }
	
	



}
