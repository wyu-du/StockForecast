# Chinese Stock Market Forecast
本项目使用深度学习和自然语言处理方法对沪深两市的A股价格涨跌进行预测，系统架构如下图所示。<br>
Our Project is aimed at predicting the price trend of individual stocks using deep learning and natural language processing, the system architecture is shown below.<br>
<img src="https://github.com/ddddwy/StockForecast/raw/master/img/system%20architecture.png">

1. 数据获取(Data Collection) [[代码|code](https://github.com/ddddwy/StockForecast/tree/master/crawler)]:<br>
使用Java的WebCollector爬虫框架编写多线程新闻爬虫，爬取新浪财经的个股新闻和个股资金流向数据，爬虫流程如图所示。<br>
Use Java web crawler framework 'WebCollector' to write multithreaded news crawlers, which can fetch the individual 
stock news and captial flow data from Sina Finance website, the process of the web crawler is shown below.<br>
<img src="https://github.com/ddddwy/StockForecast/raw/master/img/WebCollector%20core%20framework.png" width=60%>

2. 数据预处理(Data Preprocessing) [[代码|code]()]:
	* 新闻文本数据预处理(News text data preprocessing)：
		* 数据清洗：编写Python脚本删除异常符号和空字符。
		* Data Cleaning: Write Python script to delete abnormal characters and empty values.
		* 文本分词：引入自定义金融证券术语词典，使用结巴分词对中文文本进行分词。
		* Text Segmentation: Introduce custom financial terms dictionary, and use jieba text segmentation framework to segment the Chinese texts.
		<br>
		<table style='border:1px solid #e8e8e8;'>
		<thead>
			<tr>
				<th></th>
				<th>引入分词规则前 <br>Before introducing custom dictionary</th>
				<th>引入分词规则后 <br>After introducing custom dictionary</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th>RECALL</th>
				<td>87.5%</td>
				<td>95.2%</td>
			</tr>
			<tr>
				<th>PRECISION</th>
				<td>77.9%</td>
				<td>90.8%</td>
			</tr>
			<tr>
				<th>F-MEASURE</th>
				<td>82.4%</td>
				<td>92.9%</td>
			</tr>
		</tbody>
		</table>
		<br>
	* 财务数据预处理(Company financial data preprocessing)：
		* 缺失值处理：对于停盘的股票，将停盘时的股价设定为停盘前一天的收盘价。
		* Missing Value Processing: For the suspened stock, set the stock price to the closing price of the day before the stop trading day.
		* 数据归一化：采用z-score方法将不同财务指标缩放到同一量级。
		* Data Normalization: Use the z-score method to scale down different financial indicators to the same magnitude.
		
3. 模型建立(Model Building):
	* 基于事件的新闻短文本分类模型 [[代码](https://github.com/ddddwy/StockForecast/tree/master/cnn-rnn)]：
	基于新闻标题对股票市场的重要财经事件进行提取与分类。基于20000条新闻标题定义了80种金融事件类别。
	使用CNN-RNN进行事件分类建模，模型结构如图所示。
	* News Classification Model Based on News Headlines [[code](https://github.com/ddddwy/StockForecast/tree/master/cnn-rnn)]: 
	Classify the important financial issues based on the news headlines. Build CNN-RNN multi-classification 
	model to classify 80 financial issues based on 20000 news headlines, and the model structure is shown below.<br>
	<img src="https://github.com/ddddwy/StockForecast/raw/master/img/CNN-RNN.png"><br>
	* 个股涨跌预测模型 [[代码](https://github.com/ddddwy/StockForecast/tree/master/forecast)]：
	取第t-14天至第t-1天的文本数据和公司财务数据作为一个样本数据，分别输入LSTM循环神经网络中，进行时间序列分析，最后输出对第t天的股价涨跌预测结果。
	* Stock Trend Prediction Model [[code](https://github.com/ddddwy/StockForecast/tree/master/forecast)]: 
	Take the text data and the company financial data from the t-14 trading day to the t-1 trading day as one unit sample data, 
	feed into the 1-layer LSTM network to do time series analysis, and finally output the forecast result of stock price change on the t trading day.<br>
	<img src="https://github.com/ddddwy/StockForecast/raw/master/img/predict_model.png"><br>

4. 回测结果分析(Backtesting Result Analysis):
	* 训练集：2015年11月1日至2017年11月1日采矿业、医药制造业全部A股的个股新闻、资金流向和市盈率、市净率。
	* Training set: From 2015-11-01 to 2017-11-01, A shares stocks news data, captial flow data, PE and PB data from Sina Finance Website. 
	The stock includes all A-shares of the mining, pharmaceutical industries, 
	which are subject to the regulation of the China Securities Regulatory Commission.
	* 测试集：2017年11月1日至2018年1月12日采矿业、医药制造业全部A股的个股新闻、资金流向和市盈率、市净率。
	* Testing set: From 2017-11-01 to 2018-01-12, A shares stocks news data, captial flow data, PE and PB data from Sina Finance Website. 
	The stock includes all A-shares of the mining, pharmaceutical industries, 
	which are subject to the regulation of the China Securities Regulatory Commission.
		<br>
		<img src="https://github.com/ddddwy/StockForecast/raw/master/img/mine_results.png" width=45%>
		<img src="https://github.com/ddddwy/StockForecast/raw/master/img/pharmaceutical_results.png" width=45%>
		<br>
		<table style='border:1px solid #e8e8e8;'>
			<thead>
				<tr>
					<th>股价涨跌幅 <br>Stock Price Changeratio</th>
					<th>采矿业 <br>Mining industry</th>
					<th>医药制造业 <br>Pharmaceutical industry</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>1%</td>
					<td>73.2%</td>
					<td>71.82%</td>
				</tr>
				<tr>
					<td>2%</td>
					<td>71.74%</td>
					<td>77.36%</td>
				</tr>
				<tr>
					<td>3%</td>
					<td>76.22%</td>
					<td>76.94%</td>
				</tr>
				<tr>
					<td>4%</td>
					<td>64.29%</td>
					<td>64%</td>
				</tr>
				<tr>
					<td>5%</td>
					<td>77%</td>
					<td>71%</td>
				</tr>
				<tr>
					<td>6%</td>
					<td>74%</td>
					<td>76%</td>
				</tr>
				<tr>
					<td>7%</td>
					<td>65%</td>
					<td>60%</td>
				</tr>
				<tr>
					<td>8%</td>
					<td>70%</td>
					<td>60%</td>
				</tr>
				<tr>
					<td>9%</td>
					<td>70%</td>
					<td>50%</td>
				</tr>
			</tbody>
		</table>
	
* Reference:
	* [WebCollector](http://datahref.com/archives/category/webcollector%E6%95%99%E7%A8%8B)
	* [Second International Chinese Word Segmentation Bakeoff](http://sighan.cs.uchicago.edu/bakeoff2005/)
	* [Multi-class-text-classification-cnn-rnn](https://github.com/jiegzhan/multi-class-text-classification-cnn-rnn)
	* [Deep Learning with Python](https://www.manning.com/books/deep-learning-with-python)
