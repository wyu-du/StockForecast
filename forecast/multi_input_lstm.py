# -*- coding: utf-8 -*-
"""
Created on Sun Jan 28 13:45:13 2018

@author: Wanyu Du
"""

import os
import pandas as pd
import numpy as np
import keras
from keras import Input
from keras import models
from keras import layers

industry="mine"
ths=[0.01,0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09]
for th in ths:
    data_dir='C:/Users/think/Desktop/stock_prediction/data'
    fname=os.path.join(data_dir,industry+'_with_texts_train'+str(th)+'.csv')
    fname2=os.path.join(data_dir,industry+'_with_texts_test'+str(th)+'.csv')
    model_path='model/'+industry+'_optimal_model_'+str(th)+'.h5'
        
    data=pd.read_csv(fname,encoding='gb2312')
    all_data=data.iloc[:,2:].fillna(0)
    all_data=np.array(all_data,dtype=float)
    data2=pd.read_csv(fname2,encoding='gb2312')
    all_data2=data2.iloc[:,2:].fillna(0)
    all_data2=np.array(all_data2,dtype=float)
        
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
            np.random.shuffle(rows)
            i += batch_size
                
            samples1=np.zeros((len(rows), lookback, 10))
            samples2=np.zeros((len(rows), lookback, 80))
            targets=np.zeros((len(rows), ))
            for j, row in enumerate(rows):
                indices=range(row-lookback, row)
                samples1[j]=data[indices,:10]
                samples2[j]=data[indices,10:data.shape[1]-1]
                targets[j]=data[row][data.shape[1]-1]
            yield {'num':samples1,'text':samples2}, targets
        
        
    train_gen=generator(all_data, lookback=lookback, step=step, min_index=0, max_index=bound, batch_size=batch_size)
    val_gen=generator(all_data, lookback=lookback, step=step, min_index=bound+1, max_index=len(all_data), batch_size=batch_size)
    test_gen=generator(all_data2, lookback=lookback, step=step, min_index=0, max_index=len(all_data2), batch_size=batch_size)
        
    train_steps=(bound-lookback)//batch_size
    val_steps=(len(all_data)-bound+1-lookback)//batch_size
    test_steps=(len(all_data2)-lookback)//batch_size
        
    # dropout LSTM
    num_input=Input(shape=(None,10),dtype='float32',name='num')
    encoded_num=layers.LSTM(64)(num_input)
    text_input=Input(shape=(None,80),dtype='float32',name='text')
    encoded_text=layers.LSTM(128)(text_input)
    concatenated=layers.concatenate([encoded_num,encoded_text],axis=-1)
    concatenated=layers.Dense(128,activation='relu')(concatenated)
    
    preds=layers.Dense(1,activation='sigmoid')(concatenated)
    model=models.Model([num_input,text_input],preds)
    model.compile(optimizer='rmsprop',loss='binary_crossentropy',metrics=['acc'])
    model.fit_generator(train_gen,epochs=20,steps_per_epoch=train_steps,
                            validation_data=val_gen,validation_steps=val_steps,
                            callbacks=callbacks_list,verbose=0)
    
    model=models.load_model(model_path)
    results=model.evaluate_generator(test_gen, steps=test_steps)
    print(results[1])
