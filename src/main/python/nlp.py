import os, sys
import pandas as pd
from datetime import datetime
from sklearn import svm
from sentence_transformers import SentenceTransformer

# The script requires execution with 6 additional file path arguments
#  1. A path to receive termination requests
#  2. A path to receive training requests
#  3. A path to receive prediction requests
#  4. A path to access training material
#  5. A path to access inputs
#  6. A path to store  outputs

# Defining the maximum loop time
operational_seconds = 0 + 60 * 5
print('Script may at most loop for ' + str(operational_seconds) + ' second(s)')

# Validating
assert len(sys.argv) == 7

# Selecting the classifier and transformer model
print('Selecting classifier and transformer model...')
classifier = svm.SVC(kernel='poly', C=6, probability=True)
bert = SentenceTransformer('bert-base-nli-mean-tokens')

# Defining a function to confirm whether a termination request has arrived
def should_terminate():
    return os.path.exists(sys.argv[1])

# Defining a function to confirm whether a training request has arrived
def should_train():
    return os.path.exists(sys.argv[2])

# Defining a function to confirm whether a prediction request has arrived
def should_predict():
    return os.path.exists(sys.argv[3])

# Defining a function to mark the termination request as completed
def complete_termination():
    if os.path.exists(sys.argv[1]):
        os.remove(sys.argv[1])

# Defining a function to mark the training request as completed
def complete_training():
    if os.path.exists(sys.argv[2]):
        os.remove(sys.argv[2])

# Defining a function to mark the prediction request as completed
def complete_prediction():
    if os.path.exists(sys.argv[3]):
        os.remove(sys.argv[3])

# Defining a function to train the model
def train():
    data = pd.read_csv(sys.argv[4])
    xs = data['X']
    ys = data['Y']
    xs = bert.encode(xs)
    classifier.fit(xs, ys)

# Defining a function to load inputs
def load_inputs() :
    file = open(sys.argv[5], 'r')
    inputs = file.readlines()
    file.close
    return inputs

# Defining a function to write outputs
def write_outputs(ys) :
    file = open(sys.argv[6], 'w')
    file.write(str(ys[0]))
    n = len(ys)
    if n > 1 :
        for i in range(1, n): file.write('\n' + str(ys[i]))
    file.close()

# Defining a function to perform predictions
def predict():
    xs = load_inputs()
    xs = bert.encode(xs)
    ys = classifier.predict(xs)
    write_outputs(ys)

# Looping
print('Starting to loop... ')
start_time = datetime.now()
while True :
    elapsed_time = datetime.now() - start_time
    if elapsed_time.total_seconds() > operational_seconds :
        print('Stopping to loop as allocated operation time has been reached...')
        break
    elif should_terminate():
        print('Received a termination request...')
        complete_termination()
        break
    elif should_train():
        print('Received a training request...')
        train()
        complete_training()
        print('Completed training...')
    elif should_predict():
        print('Received a prediction request...')
        predict()
        complete_prediction()
        print('Completed prediction...')

# Confirming termination
print('Terminating...')
sys.exit(0)