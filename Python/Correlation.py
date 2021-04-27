# importing pandas as pd
import pandas as pd
  
# Making data frame from the csv file
readFile = "Data\\COR_DATA.csv"
df = pd.read_csv(readFile)

  
# Printing the first 10 rows of the data frame for visualization
output = df.corr(method ='pearson')
print(output)
