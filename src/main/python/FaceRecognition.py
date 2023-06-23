import os, sys
import cv2 as cv
from datetime import datetime
from deepface import DeepFace

# The script requires execution with 3 additional file path arguments
#  1. A path containing all recognized images/faces
#  2. A path to read the image that should be recognized
#  3. A path to store the (prediction) result

# Defining the maximum loop time
operational_seconds = 0 + 60 * 5
print('Script may at most loop for ' + str(operational_seconds) + ' second(s)')

# Validating whether we have enough arguments
print('Validating arguments...')
assert len(sys.argv) == 6

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
    person = recognize(sys.argv[1], sys.argv[2])
    print("Detected " + person)

    # Store result
    file = open(sys.argv[3], 'w')
    file.write(person)
    file.close()

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