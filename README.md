# Machine-Learning
###Focus on ML Algorithm,DL Algorithm,Community Detection and its Development in Fintech.

###But in this section, Focus on the application of ML Algorithm in Credit-Model.

I. Practice Background.
   
   In the Fintech, in order to decrease the overdue rate, Credit-checking of a person is necessary and usually work done by hand in traditional financial institution. As the development of the distributed system and the improvement of the computational capabilities, Machine Learning is playing more important role in Fintech. Obviously, with the help of the ML Algorithm, Credit-checking can be easily done by Machine and it can greatly free our body.
   
   In this section, use XGBOOST to model the bid-overdue rate and bid-return rate in given days. 
  
II. Credit-model goal.

   Model the bid-overdue rate and bid-return rate in special days, obtain Credit models to predict bid-overdue rate and bid-return in given days, such as 7 days or 30 days based on specific requirements.
  
III.Implemention architecture.

   In this section,show the implement architecture of the mchine learnig in credit model. intuitively, the architecture includes three aspects---Data layer, Modeling layer and Output layer. Data layer is the data input of the model, Modeling layer is a training layer for model algorithm and Output layer is the predict results. It's worth noting that data parpared for modeling layer can be multidimensional like users basic information,network information and others. ML algorithm determined by its complexity and accuracy, of course, beyond that the corresponding scenario is need to be considered as well. Many algorithm is compared based on our data, and finally, we choose XGboost. About the Xgboost Algorithm, the paper of Tian Qi can help you understand it. This is reference:https://github.com/dmlc/xgboost.
   <div align=center><img width="550" height="350" src="https://github.com/JackerGao/Machine-Learning/blob/master/image/p1.png"/></div>

IV. Simulation results
   
   In this section, simulations of the model is show. The picture is Credit model based on XGboost and our data. The results indicate that the Credit model can meet business need.
   <div align=center><img width="550" height="350" src="https://github.com/JackerGao/Machine-Learning/blob/master/image/p2.jpg"/></div>




