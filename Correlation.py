import numpy as np
import pandas as pd

# https://pandas.pydata.org/pandas-docs/stable/reference/api/pandas.Series.corr.html#pandas.Series.corr

def histogram_intersection(a,b):
    v = np.minimum(a,b).sum()
    return v

Variance = pd.Series([.223,.324,.41,.51231])
Tweets = pd.Series([2,32,70,43])
print(Variance.corr(Tweets,method=histogram_intersection))
print(Tweets.corr(Variance,method=histogram_intersection))

