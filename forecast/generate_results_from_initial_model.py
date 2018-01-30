# -*- coding: utf-8 -*-
"""
Created on Sun Jan 28 13:45:13 2018

@author: Wanyu Du
"""

import os
import pandas as pd
import numpy as np
import keras
from keras import models
from keras import layers
from keras.optimizers import RMSprop
from tqdm import tqdm

industry="medicine"
ths=[0.01,0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09]
for th in tqdm(ths):
    data_dir='C:/Users/think/Desktop/stock_prediction/data'
    out_dir='C:/Users/think/Desktop/stock_prediction/data2/'
    if not os.path.exists(out_dir):
        os.makedirs(out_dir)
    fname=os.path.join(data_dir,industry+'_with_texts_train'+str(th)+'.csv')
    fname2=os.path.join(data_dir,industry+'_with_texts_test'+str(th)+'.csv')
    model_path='models/'+industry+'/optimal_model_'+str(th)+'_text2.h5'
    
    
    data=pd.read_csv(fname,encoding='gb2312')
    all_data=data.iloc[:,2:].fillna(0)
    all_data=np.array(all_data,dtype=float)
    all_data=np.asarray(all_data).astype('float32')
    data2=pd.read_csv(fname2,encoding='gb2312')
    all_data2=data2.iloc[:,2:].fillna(0)
    all_data2=np.array(all_data2,dtype=float)
    all_data2=np.asarray(all_data2).astype('float32')
    
    callbacks_list=[
           keras.callbacks.EarlyStopping(monitor='acc', patience=1,),
           keras.callbacks.ModelCheckpoint(filepath=model_path, 
                                         monitor='val_loss', save_best_only=True,)
    ]
    
    lookback=12
    step=14
    batch_size=140
    bound=bound=len(all_data)*3//4
    
    def generator(data, lookback, step, min_index, max_index, batch_size):
        i=min_index+lookback
        while 1:
            if i+batch_size>=max_index:
                i=min_index+lookback
            rows=np.arange(i, min(i+batch_size, max_index), step)
            i += batch_size
            
            samples=np.zeros((len(rows), lookback, data.shape[1]-11))
            targets=np.zeros((len(rows), ))
            for j, row in enumerate(rows):
                indices=range(rows[j]-lookback, rows[j])
                samples[j]=data[indices,10:data.shape[1]-1]
                targets[j]=data[rows[j]][data.shape[1]-1]
            yield samples, targets
    
    train_gen=generator(all_data, lookback=lookback, step=step, min_index=0, max_index=bound, batch_size=batch_size)
    val_gen=generator(all_data, lookback=lookback, step=step, min_index=bound+1, max_index=len(all_data), batch_size=batch_size)
    test_gen=generator(all_data2, lookback=lookback, step=step, min_index=0, max_index=len(all_data2), batch_size=batch_size)
    all_gen=generator(all_data, lookback=lookback, step=step, min_index=0, max_index=len(all_data), batch_size=batch_size)
    
    train_steps=(bound-lookback)//batch_size
    val_steps=(len(all_data)-bound+1-lookback)//batch_size
    test_steps=(len(all_data2)-lookback)//batch_size
    all_steps=(len(all_data)-lookback)//batch_size
    
    # dropout LSTM
    model=models.Sequential()
    model.add(layers.LSTM(64, input_shape=(None, all_data.shape[1]-11)))
    model.add(layers.Dense(1,activation='sigmoid'))
    model.compile(optimizer=RMSprop(),loss='binary_crossentropy',metrics=['acc'])
    history=model.fit_generator(train_gen, steps_per_epoch=train_steps, epochs=20, 
                                validation_data=val_gen, validation_steps=val_steps,
                                callbacks=callbacks_list, verbose=0)
    
    # generate trainset prediction results
    model=models.load_model(model_path)
    preds=model.predict_generator(all_gen, steps=all_steps)[:,0]
    targets=[]
    for i in range(13,all_steps*batch_size,14):
        targets.append(all_data[i,all_data.shape[1]-1])
    targets=np.array(targets)
    results=pd.DataFrame()
    results["preds"]=preds
    results["targets"]=targets
    results.to_csv(out_dir+industry+'_'+str(th)+'C.csv',index=False)
    
    # generate testset prediction results
    preds2=model.predict_generator(test_gen, steps=test_steps)[:,0]
    targets2=[]
    for i in range(13,test_steps*batch_size,14):
        targets2.append(all_data2[i,all_data.shape[1]-1])
    targets2=np.array(targets2)
    results2=pd.DataFrame()
    results2["preds"]=preds2
    results2["targets"]=targets2
    results2.to_csv(out_dir+industry+'_'+str(th)+'D.csv',index=False)
