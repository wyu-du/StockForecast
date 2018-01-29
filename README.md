# StockForecast
使用深度学习和自然语言处理方法对沪深两市的A股价格涨跌进行预测，系统架构如下图所示。<br>
Predict the price trend of individual stocks using deep learning and natural language processing, the system architecture is shown below.<br>
![](https://github.com/ddddwy/StockForecast/blob/master/img/system%20architecture.png)

1. 数据获取(Data Collection) [[代码|code](https://github.com/ddddwy/StockForecast/tree/master/crawler)]:<br>
使用Java的WebCollector爬虫框架编写多线程新闻爬虫，爬取新浪财经的个股新闻和个股资金流向数据，爬虫流程如图所示。<br>
Use Java web crawler framework 'WebCollector' to write multithreaded news crawlers, which can fetch the individual 
stock news and captial flow data from Sina Finance website, the process of the web crawler is shown below.<br>
![](https://github.com/ddddwy/StockForecast/blob/master/img/WebCollector%20core%20framework.png =300)

2. 数据预处理(Data Preprocessing) [[代码|code]()]:
	* 新闻文本数据预处理(News text data preprocessing)：
		* 数据清洗：编写Python脚本删除异常符号和空字符。
		* Data Cleaning: Write Python script to delete abnormal characters and empty values.
		* 文本分词：引入自定义金融证券术语词典，使用结巴分词对文本进行分割。
		* Text Segmentation: Introduce custom financial terms dictionary, and use jieba text segmentation framework to segment the Chinese texts.
	* 财务数据预处理(Company financial data preprocessing)：
		* 缺失值处理：对于停盘的股票，将停盘时的股价设定为停盘前一天的收盘价。
		* Missing Value Processing: For the suspened stock, set the stock price to the closing price of the day before the stop trading day.
		* 数据归一化：采用minmax方法将不同财务指标缩放到同一量级。
		* Data Normalization: Use the min-max method to scale down different financial indicators to the same magnitude.
		
3. 模型建立(Model Building) [[代码|code]()]:
	* 基于事件的新闻短文本分类模型：基于新闻标题对股票市场的重要财经事件进行提取与分类。基于20000条新闻标题定义了80种金融事件类别。
	使用CNN-RNN进行事件分类建模，模型结构如图所示。
	* News Classification Model Based on News Headlines: Classify the important financial issues based on the news headlines. Build CNN-RNN multi-classification 
	model to classify 80 financial issues based on 20000 news headlines, and the model structure is shown below.
	![](https://github.com/ddddwy/StockForecast/blob/master/img/CNN-RNN.png =300)
	* 个股涨跌预测模型：取第t-14天至第t-1天的文本数据和公司财务数据作为一个样本数据，输入LSTM循环神经网络中，进行时间序列分析，最后输出对第t天的股价涨跌预测结果。
	* Stock Trend Prediction Model: Take the text data and the company financial data from the t-14 trading day to the t-1 trading day as one unit sample data, 
	feed into the 1-layer LSTM network to do time series analysis, and finally output the forecast result of stock price change on the t trading day.
	![](https://github.com/ddddwy/StockForecast/blob/master/img/LSTM.png =300)

4. 回测结果分析(Backtesting Result Analysis):

