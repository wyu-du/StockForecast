# -*- coding: utf-8 -*-
"""
Created on Sun Jan 28 16:00:38 2018

@author: Wanyu Du
"""

from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import VotingClassifier,GradientBoostingClassifier
import pandas as pd
import numpy as np
import os

def load_data(label1="A",label2="C",industry="mine"):
    out_dir='C:/Users/think/Desktop/stock_prediction/data2'
    fname=os.path.join(out_dir,industry+'_'+str(th)+label1+'.csv')
    fname2=os.path.join(out_dir,industry+'_'+str(th)+label2+'.csv')
  
    prop1_df=pd.read_csv(fname,encoding='gb2312')
    
    prop2_df=pd.read_csv(fname2,encoding='gb2312')
    prop2_df["preds"]=prop2_df["preds"]-np.mean(prop2_df["preds"])
    prop2_df["preds"]=prop2_df["preds"]/np.std(prop2_df["preds"])
    
    x=np.zeros((len(prop1_df),2))
    x[:,0]=prop1_df["preds"].values
    x[:,1]=prop2_df["preds"].values
    y=np.array(prop2_df["targets"].values)
    return x,y

industry="medicine"
ths=[0.01,0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09]
for th in ths:
    train_x,train_y=load_data(label1="A",label2="C",industry=industry)
    test_x,test_y=load_data(label1="B",label2="D",industry=industry)
    clf1=LogisticRegression()
    clf2=GradientBoostingClassifier()
    eclf=VotingClassifier(estimators=[('lr',clf1),('dt',clf2)],voting='hard')
    clf1.fit(train_x,train_y)  
    clf2.fit(train_x,train_y)
    eclf.fit(train_x,train_y)
    print(str(clf1.score(test_x,test_y))+'\t'+str(clf2.score(test_x,test_y))
    +'\t'+str(eclf.score(test_x,test_y)))
