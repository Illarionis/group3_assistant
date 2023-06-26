import os, sys
import cv2 as cv
from datetime import datetime
from deepface import DeepFace

# The script requires execution with 5 additional file path arguments
#  1. A path to receive termination requests
#  2. A path to receive prediction requests
#  3. A path to access known faces
#  4. A path to access inputs
#  5. A path to store  outputs

# Defining the maximum loop time
operational_seconds = 0 + 60 * 5
print('Script may at most loop for ' + str(operational_seconds) + ' second(s)')

# Validating whether we have enough arguments
print('Validating arguments...')
assert len(sys.argv) == 6

# Defining a function to confirm whether a termination request has arrived
def should_terminate():
    return os.path.exists(sys.argv[1])

# Defining a function to confirm whether a prediction request has arrived
def should_predict():
    return os.path.exists(sys.argv[2])

# Defining a function to mark the termination request as completed
def on_termination_completed():
    os.remove(sys.argv[1])

# Defining a function to mark the prediction request as completed
def on_prediction_completed():
    os.remove(sys.argv[2])

# Returns only the name of the person if the name is given as a path: folder1/folder2/Name of The Person (1).jpg
def get_name_of_img(input_string):
    # Find the index after the last "/"
    start_index = input_string.rfind("/") + 1
    # Find the index before the "("
    end_index = input_string.find("(") - 1

    sub_string = input_string[start_index:end_index]

    return sub_string

def recognize(img_full_path, database_full_path):
    # DeepFace.find() returns the a list of dataframes
    dataframe_list = DeepFace.find(img_path=img_full_path,
                                   db_path=database_full_path,
                                   model_name="Facenet512",
                                   enforce_detection=False)
    # dataframe_list contains only one dataframe - in the [0] cell
    dataframe = dataframe_list[0]
    # dataframe contains a list of the most probable matches in descending order of matching likelihood
    # best_match stores the most likely identification
    best_match = dataframe.iloc[0,0]
    # We return the name of the identified person
    return get_name_of_img(best_match)

# Defining the prediction function
def predict():
    # Attempting to recognize the face
    print('Attemping to recognize person...')
    person = recognize(sys.argv[4], sys.argv[3])
    print("Detected " + person)

    # Store result
    file = open(sys.argv[5], 'w')
    file.write(person)
    file.close()

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
        predict()
        delete_prediction_flag()

# Confirming termination
print('Terminating...')
sys.exit(0)