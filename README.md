## Table of Contents  
- [General](#general)
- [Overview of Procedure](#overview-of-procedure)
- [Installation](#installation)
- [Video Tutorials](#video-tutorials)
- [References](#references)


# TrackAnalyzer
<!-- toc -->

<a name="general"></a>
## General
> A flexible Fiji/ImageJ plugin to perform SPT analysis, analysis of diffusion, motion classification in batch-mode together with subsequent spot/trajectory 2D visualizaation and filtering.
<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/208313865-733583bd-864b-404e-82ee-b359d2a8bae6.png">
</p>

**_Note_:** This plugin (currently) deals with a wide range of file formats supported by Bio-Formats library thus reading almost any image format or multidimensional data as z-stacks, time series, or multiplexed images keeping metadata easily accessible. In case of loading a Leica Image File whose extension is **_.lif_** (storing several image series in the same file), this plugin is capable of extracting each image automatically as a single TIFF file, keeping the original pixel values, spatial and temporal calibration.
<a name="overview-of-procedure"></a>
## Overview of Procedure
  Within **_TrackAnalyzer_** both **"SPT-Batch"** and **"SPT-Viewer"** may deal with many time-lapse microscopy formats readable by Bio-formats library. Moreover, this pair of plugins may handle multi-series files as Leica file formats (.LIF) extracting each serie as a multi-dimensional tiff and being displayed as a stack keeping both original metadata and spatial-temporal calibration. Likewise, "SPT-Viewer" can display multiple spatially and temporally images, being mandatory that all slices from stack are same dimensionality and bit depth. These multi-dimensional image files may be in the way of ImageJ's stacks or hyperstacks, extending in this last case from X-Y-T to X-Y-Z-C-T dimensions (width, height, slices, channels and time frames).
 
A high-level overview of the toolbox procedure involved is shown. The processing actions consists of six main stages. First, we describe how to getting started with "SPT-Batch" and second, we provide guidelines for "SPT-Viewer":   

**(Step 1) .** At the beginning, the user must navigate to the appropriate directory where time-lapse files are saved. Also, user must supply the path in which the TrackMate ".XML" configuration file is located. This file contains both the data and parameters needed for tracking analysis in batch-mode thus user must ensure the absolute path provided is correct. Then "TrackAnalyzer" by means of the data-loading tool automatically will scan the selected folders for analysis.
<p align="center">
  <img width="900" height="450" src="https://user-images.githubusercontent.com/83207172/208314030-0f67b664-0225-43c4-b8a4-295c8e837fd4.png">
</p>

**(Step 2) .**  ***Overview of the procedure: SPTBatch.*** Once **"SPTBatch"** button is clicked, a wizard-like GUI will pop up. At first, user has to configure settings to select which data referred to spots, links, tracks or branchs prefers to export as a CSV file. Once all tracks have been read, trajectories will be decompose into "Short" or "Long", those trajectories being above the diffusion coefficient set in "Diff.Threshold" field will be labeled as "Mobile" and below as "Immobile". User can edit the corresponding settings for spot or track visualization. Similarly, whether "Photobleaching step analysis" is selected, the raw intensity profile along the different frames together with their background will be plotted for each trajectory. To measure the background of each spot for each frame in each trajectory, **"TrackAnalyzer"** allows user to accede to several methods (by using "Subtract Background").

In parallel whether the user checks for "Summary Output" and depending on spots, links and tracks chosen, **"TrackAnalyzer"** will generate a summary file pcontaining all values for each movie analyzed. Then if "TraJ: Trajectory Classifier" action is selected, CSV files regarding track characterization and subsequent motion classification will be saved. In case of selection "MSD-MSS plots", the plots of mean squared displacement (MSD) vs. time along with thescaling moments vs. each moment of displacement will be computed.
Also, if "Tracks to .XML file" is selected, information related to trajectories generated during analysis will be exported as XML file or if "Log to .TXT file" is chosen, useful information about ongoing operations printed in the log window while **"TrackAnalyzer"** runs will be saved. Furthermore, user will be able to export tracks as rois (RoiSet.zip) and corresponding overlays over frames are saved as TIFF file.  Whether "Plots as .PNG file" option is clicked, the user will be allowed to configure X-Y scatter-plots to plot any spot, link or track feature as a function of any other. When selecting "Chemotaxis Analysis Data" action, user will be enabled to plot and export as CSV files including some statistical values characterizing the trajectories form such as the center of mass (CM), forward migration indices (FMI), velocity (V) as well as directness (D). 
<p align="center">
  <img width="900" height="450" src="https://user-images.githubusercontent.com/83207172/208313945-34ac4d0b-970d-4f50-98e0-8ed00e229e8f.png">
</p>

**(Step 3) .** ***Overview of the procedure: SPTViewer.*** TrackMate benefits from HyperStack Displayer to display tracking results and this isleveraged to perform filtering actions. **"SPT-Viewer"** enables user to keep only those spots or end-trajectories which are relevant under user-defined criteria. Then filtering results can be immediately visualized. 
    
  Throughout the first wizard in **"SPT-Viewer"**, user may accomplish actions in order to perform spot filtering. To do so, spots will be drawn and  user will have access to the spot data-table  (ID, TRACK_ID, QUALITY, POSITION_X, POSITION_Y, FRAME, RADIUS, VISIBILITY, MEAN_INTENSITY, MEDIAN_INTENSITY...) characterizing each spot detection. Another advantage of **"SPT-Viewer"** can be found when "Spot Picker" option is selected to both dynamically and manually highlight any specific spot from data-table. This plugin offers three different approaches to remove non relevant spots however, they are not definitely removed because they are simply hidden then being available to retrieve them at any time:   
  
   - **Approach 1 .** It allows user to either enable or disable check-boxes corresponding to each spot separately or as a group. Thus this function facilitates user to manually make a selection of spots to be hidden (when check-boxes are disabled) or by contrast, retrieve them by re-enabling check-boxes.
   - **Approach 2 .** It allow user to dynamically toggling either inside or outside the spots which belong to a region of interest drawn by user using any of ImageJ's ROI tools. Then those spots in or out the user-defined ROI will be filtered out, respectively, depending on the toggling mode.
   - **Approach 3 .** This procedure enables user to configure the conditions to identify spot types through scrolling a feature-based histogram above or below a given threshold. Once terms related to spot features are set, they will be grouped into a specific spot type in order to filter out those spots which do not comply with them conversely, identifying those which fulfill them. These spot types relies on an indefinite number of thresholded features.  

<p align="center">
  <img width="800" height="450" src="https://user-images.githubusercontent.com/83207172/208314065-fa44424b-53a6-43ae-8f47-61c7984705b1.png">
</p>

 Finally, the user may manage to proceed with the actions to save each post-processed movie as a TIFF file whose overlay shows those relevant spots and tracks kept. Likewise, the user will be enabled to save the post-processed data-table which contains the features computed for each relevant spot or trajectory.


<a name="installation"></a>
## Installation

The ***TrackAnalyzer*** plugin may be installed in Fiji or ImageJ by following these steps:

**Note:** This needs to be done previously installing **"TrackAnalyzer"**. 

   - To visualize the wizard-like GUI that guides user thought the set of predefined steps in this plugin, user must navigate to  (https://drive.google.com/drive/folders/1hd1GjZqGwdsVI-ICQ83XqwgqXZeLucIb?usp=sharing), download from `plugins` folder the [JWizardComponent_.jar] and located it into the ImageJ/Fiji `plugins` subfolder.
  
   - In order to avoid any bugs while running the **"TraJClassifier"** motion classification routine, user must download from `jars` folder the ".jar" files and move them into the ImageJ/Fiji `jars`subfolder. 

**1.** In the event of not having ImageJ or Fiji already installed, please navigate through [https://imagej.nih.gov/ij/download.html](https://imagej.nih.gov/ij/download.html), download it and then, install it on a computer with Java pre-installed in either Windows, Mac OS or Linux systems.

**2.**  Once done, download the plugin JAR file named as [TrackAnalyzer_.jar](https://github.com/QuantitativeImageAnalysisUnitCNB/TrackAnalyzer_/blob/master/TrackAnalyzer_.jar) from repository.

**3.**  Move this file into the `ImageJ/Fiji "plugins" subfolder`, or by dragging and dropping into the `ImageJ/Fiji main window` or optionally, running through menu bar `"Plugins"` **→** `"Install"` **→**  `‘Path to File’`. After installing the plugin, ImageJ or Fiji must be restarted.

**_Note_:** 
<a name="video-tutorials"></a>
## Video Tutorials 
### Running SPTBatch for SPT analysis, analysis of diffusion and motion classification in batch-mode
### Running SPTViewer for spot/trajectory visualizatin and subsequent filtering




