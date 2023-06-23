import os, sys
import pandas as pd
from datetime import datetime
from sklearn import svm
from sentence_transformers import SentenceTransformer

# Script assumes that additional file path arguments are provided during execution:
#   1. A file representing the (training) data set
#   2. A file representing the input set
#   3. A file representing the output set
#   4. A file to request output prediction
#   5. A file to request script termination

# Defining the maximum loop time
operational_seconds = 0 + 60 * 5
print('Script may at most loop for ' + str(operational_seconds) + ' second(s)')

# Defining a function to train model
def train_model() :
    data = pd.read_csv(sys.argv[1])
    xs = data['X']
    ys = data['Y']
    xs = bert.encode(xs)
    classifier.fit(xs, ys)

# Defining a function to load inputs
def load_inputs() :
    file = open(sys.argv[2], 'r')
    inputs = file.readlines()
    file.close
    return inputs

# Defining a function to write outputs
def write_outputs(ys) :
    file = open(sys.argv[3], 'w')
    file.write(str(ys[0]))
    n = len(ys)
    if n > 1 :
        for i in range(1, n): file.write('\n' + str(ys[i]))
    file.close()

# Defining a function to predict and store outputs
def predict(bert, classifier) :
    xs = load_inputs()
    xs = bert.encode(xs)
    ys = classifier.predict(xs)
    write_outputs(ys)

# Defining a function to check whether input has arrived
def should_predict() :
    return os.path.exists(sys.argv[4])

# Defining a function to check whether the script should terminate prematurely
def should_terminate() :
    return os.path.exists(sys.argv[5])

# Defining a function to delete the prediction flag
def delete_prediction_flag():
    os.remove(sys.argv[4])

# Defining a function to delete the termination flag
def delete_termination_flag():
    os.remove(sys.argv[5])

# Selecting the classifier and transformer model
print('Selecting classifier and transformer model...')
classifier = svm.SVC(kernel='poly', C=6, probability=True)
bert = SentenceTransformer('bert-base-nli-mean-tokens')

# Training the model
print('Training the model')
train_model()

# Looping
print('Starting to loop... ')
start_time = datetime.now()
while True :
    elapsed_time = datetime.now() - start_time
    if elapsed_time.total_seconds() > operational_seconds :
        break
    elif should_terminate() :
        delete_termination_flag()
        break
    elif should_predict() :
        predict(bert, classifier)
        delete_prediction_flag()

# Confirming termination
print('Terminating...')
sys.exit(0)