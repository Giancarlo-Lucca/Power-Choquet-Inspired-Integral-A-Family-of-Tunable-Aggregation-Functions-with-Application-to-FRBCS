# Power-Choquet-Inspired-Integral-A-Family-of-Tunable-Aggregation-Functions-with-Application-to-FRBCS
This repository is related with a publication in the IEEE International Conference on Fuzzy Systems (FUZZ-IEEE). This repository provide the Java project with the source code related with the Choquet integral with their application in the Fuzzy Reasoning Method (FRM) combined with the power inspired approach. It consider a well-known Fuzzy Rule-Based Classification System (FRBCS), the Fuzzy Association Rule-based Classification method for High-Dimensional problems (FARC-HD). 

It is important to mention that the FARC-HD original code as well as the complete set of dataset are freely available at KELL dataset repository (https://keel.es). Which can be cited as:

[FARC-HD] J. Alcala-Fdez, R. Alcalá, F. Herrera. A Fuzzy Association Rule-Based Classification Model for High-Dimensional Problems with Genetic Rule Selection and Lateral Tuning. IEEE Transactions on Fuzzy Systems 19:5 (2011) 857-872.

[KEEL] I. Triguero, S. González, J. M. Moyano, S. García, J. Alcalá-Fdez, J. Luengo, A. Fernández, M. J. del Jesus, L. Sánchez, F. Herrera. KEEL 3.0: An Open Source Software for Multi-Stage Analysis in Data Mining International Journal of Computational Intelligence Systems 10 (2017) 1238-1249, . 

# Repository structure
In the master branch you will find the java project divided into two folders, (i) FARC-HD-Choquet/src, containing the project files and (ii) ORIGINAL wich contain the core of the code. In the second folder the main different .txt files that are related with the execution are:
- DataSet.txt: A list of datasets that can be used as toy examples which can be setted in the paramether file
- accTest.txt: The detailed classification obtained in test
- accTrain.txt: The detailed classification in training
- MembershipFunctions.txt: The relation of the values used in the triangular membership function per variable
- Param.txt: The configuration file (detailed above)
- RuleBase.txt: The generated rules as well as their confidence factor

We also provides a .xlsx file containing the obtaining the detailed results in training and test in the cross-validation step. Also, to better understand the learning process of the q exponent we provide in the folder FAR-HD the original paper (which can also be fount at: "https://sci2s.ugr.es/keel/pdf/algorithm/articulo/alcala_herrera_IEEE_TFS(Association_Classification_for_High_Dimensional_Problems).pdf").


# Parameters
To run the code you have to open the project in an IDE and config a parameter set-up, which is available at the file 'param.txt'. The parameters related with the program are the ones orignaly sugested by the authors combined with the new FRM and are composed as the following:

----
algorithm = FARCHD

inputData = "./data/iris/iris-10-1tra.dat" "./data/iris/iris-10-1tra.dat" "./data/iris/iris-10-1tst.dat" (can be replaced by different datasets, as metioned we picked the ones from keel dataset repositoby)
outputData = "accTraining.txt" "accTest.txt" "membershipFunctions.txt"  "ruleBase.txt" "time.txt"  "hora.txt"  "rules.txt"  "evolution.txt" (these last 4 files are commented in the bull file)

Seed = 53743421
Number of Labels = 5
Minimum Support = 0.05
Minimum Confidence = 0.8
Deph of Trees = 3
K = 2
Maximum Evaluations = 20000
Population Size = 50
Alpha = 0.02
Bits per Gene = 30
Type of Inference = 0 - Winning Rule (3), 1 - Additive Combination (AC), 2 - Choquet integral and 3 - Probabilistic Sum (PS).
Type of fuzzy measure = Power inspired measures (See Table 1 of the paper).
Type of aggregation =  1 (The standard Choquet integral).


---
# Download and Citation Request
Each aproach is related with the Choquet integral and its generalizations as published in the literature licensed under CC BY-NC 4.0.. Please, in case this code or any function is used for scientific or academic purposes, include a citation to this paper as one of the following:

1 - E. Barrenechea, H. Bustince, J. Fernandez, D. Paternain, and J. Sanz. Using the Choquet integral in the fuzzy reasoning method of fuzzy rule-based classification systems. Axioms, 2(2):208–223, 2013.

2 - G. Lucca, J. Sanz, G. Dimuro, B. Bedregal, M. Asiain, M. Elkano, and H. Bustince. CC-integrals: Choquet-like copula-based aggregation functions and its application in fuzzy rule-based classification systems. Knowledge-Based Systems, 119:32 – 43, 2017.

3 - G. Lucca, J. Sanz, G. Pereira Dimuro, B. Bedregal, R. Mesiar, A. Kolesárová, and H. Bustince Sola. Pre-aggregation functions: construction and an application. IEEE Transactions on Fuzzy Systems, 24(2):260–272, April 2016.

4, 5 - G. Lucca, J. Sanz, G. Dimuro, B. Bedregal, H. Bustince, and R. Mesiar. CF-integrals: A new family of pre-aggregation functions with application to fuzzy rule-based
