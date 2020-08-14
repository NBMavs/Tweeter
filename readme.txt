Read Me for Tweeter app:

Follow these instructions to get a copy of the Tweeter application up and running in an emulator on your local machine. 

Prerequisites:

    Android Studio 3.6 installed locally
    Anaconda 1.7.2 installed locally

Installing:

    From the source page of this repository, copy the url directly after "git clone" (excluding the git clone)
    From android studio splash menu, click “Get from Version Control”
    Make sure that “Repository URL” is selected in the left-hand column
    Paste in the url copied from the bitbucket repo
    Click "clone" in the bottom right corner
    
    When prompted, click yes to open the project immediately
    
Building/Running Application:

    If you don’t have an android emulator of API 30 installed, click on the tools drop- down menu, 
    open the AVD manager, click on “Create Virtual Device” in the lower left hand corner, and follow 
    the instructions to install an emulator with API 30. To run the application, click on “Run” from
    the top menu, and select “ Run ‘app’ ”
    
	For instructions on the use of the application, please see the user manual located 
    on page 56 of the final binder

Anaconda commands to set up environment for the notebook:

    conda create -n myenv python=3.7 
    pip install tensorflow==2.0
    conda install -c conda-forge numpy
    conda install -c conda-forge librosa
    conda install -c conda-forge matplotlib
    conda install Ipython
    conda install tqdm
    pip install pandas
