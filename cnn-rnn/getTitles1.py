# -*- coding: utf-8 -*-
"""
Created on Fri Nov 17 23:39:16 2017

@author: think
"""
import MySQLdb
import numpy as np
import pandas as pd

def conDB():
  conn=MySQLdb.connect(host="localhost",
                       port=3306,
                       passwd="1230",
                       user="root",
                       db="data2018",
                       charset='utf8')
  cur=conn.cursor()
  return cur,conn

def getOneBondInfo(table):
  sql="select concat(year,'-',month,'-',day),bid,title from %s "%table
  cur.execute(sql)
  outs=np.array(cur.fetchall())
  return outs

cur,conn=conDB()
table_name="news_sina_train"
out_file=u"news_sina_train.csv"
data=getOneBondInfo(table_name)
titles=[]
titles.append(["Date","Bid","Descript"])
for line in data:
    title=[]
    title.append(line[0]);title.append(line[1]);title.append(line[2])
    titles.append(title)
pd.DataFrame(titles).to_csv(out_file,index=False,header=False,sep="|")

