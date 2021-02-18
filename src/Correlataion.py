import numpy as np
import pandas as pd

#https://pandas.pydata.org/pandas-docs/stable/reference/api/pandas.Series.corr.html#pandas.Series.corr

def histogram_intersection(a,b):
    v = np.minimum(a,b).sum.round(decimals=3)
    return v

Variance = pd.Series("Need to import the standard deviations and then square them")
Tweets = pd.Series("Need to find Twitter API and count # of tweets")

Variance.corr(Tweets,method=histogram_intersection())

