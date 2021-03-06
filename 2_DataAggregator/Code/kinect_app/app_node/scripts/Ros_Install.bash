##########################################################
##							##
## File: Ros_Install.bash				##
## Author: James Kuczynski				##
## Email: jkuczyns@cs.uml.edu				##
## File Description: Installer file for ROS version.	##
##							##
## Created: April 4 2015 1:00pm				##
##							##
##########################################################

#!/bin/bash

#Determine Ubuntu version and other variables
UBUNTU=$( lsb_release -r | awk '{ print $2 }' )
ICON="~/se_ws/src/Software_Engineer_91.411_2/2_DataAggregator/Code/kinect_app/app_node/share/se_logo.xpm"
EXEC="~/se_ws/devel/lib/frontend_ui"

echo "Do you want this script to install dependencies (Y/n)?"
read DEPS

if [ "$DEPS" == "n" ] || [ "$DEPS" == "N" ]; then
    echo "Make sure to manually install dependencies"
else
    echo "Installing packages..."
    #sudo apt-get install libboost-all-dev
    #sudo apt-get install libqt4-*
    #sudo apt-get install libqjson0 libqjson-*
    #sudo apt-get install sqlite3 libsqlite3-dev
fi


extraDeps ()
{ 
    echo "Subversion 14.04.2 detected.  Installing additional dependencies..."
#    sudo apt-get install xserver-xorg-dev-lts-utopic mesa-common-dev-lts-utopic libxatracker-dev-lts-utopic libopenvg1-mesa-dev-lts-utopic libgles2-mesa-dev-lts-utopic libgles1-mesa-dev-lts-utopic libgl1-mesa-dev-lts-utopic libgbm-dev-lts-utopic libegl1-mesa-dev-lts-utopic
}

if [ "$UBUNTU" == "12.04" ]; then
    echo "Installing ROS Hydro for Ubuntu 12.04..."
    ROSV="hydro"
    #sudo sh -c 'echo "deb http://packages.ros.org/ros/ubuntu precise main" > /etc/apt/sources.list.d/ros-latest.list'
elif [ "$UBUNTU" == "12.10" ]; then
    echo "Installing ROS Hydro for Ubuntu 12.10..."
    ROSV="hydro"
    #sudo sh -c 'echo "deb http://packages.ros.org/ros/ubuntu quantal main" > /etc/apt/sources.list.d/ros-latest.list'
elif [ "$UBUNTU" == "13.04" ]; then
    echo "Installing ROS Hydro for Ubuntu 13.04"
    ROSV="hydro"
    #sudo sh -c 'echo "deb http://packages.ros.org/ros/ubuntu raring main" > /etc/apt/sources.list.d/ros-latest.list'
elif [ "$UBUNTU" == "13.10" ]; then
    echo "Installing ROS Hydro for Ubuntu 13.10"
    ROSV="indigo"
    #sudo sh -c 'echo "deb http://packages.ros.org/ros/ubuntu saucy main" > /etc/apt/sources.list.d/ros-latest.list'
elif [ "$UBUNTU" == "14.04" ]; then
    echo "Installing ROS Hydro for Ubuntu 14.04"
    SUBV=$( lsb_release -s -d | awk '{ print $2 }' )
    #echo "$SUBV"
    if [ "$SUBV" == "14.04.2" ]; then
        extraDeps
    fi
    ROSV="indigo"
    #sudo sh -c 'echo "deb http://packages.ros.org/ros/ubuntu trusty main" > /etc/apt/sources.list.d/ros-latest.list'
elif [ "$UBUNTU" == "14.10" ]; then
    echo "Ubuntu 14.10 detected."
    echo "Sorry, but this version is not yet supported."
elif [ "$UBUNTU" == "15.04" ]; then
    echo "Ubuntu 15.04 detected."
    echo "Sorry, but this version is not yet supported."
else
    echo "Unsupported Ubuntu version"
fi
  

#wget https://raw.githubusercontent.com/ros/rosdistro/master/ros.key -O - | sudo apt-key add -
#sudo apt-get update
#sudo apt-get install ros-"$ROSV"-desktop-full
#sudo rosdep init
#rosdep update
#source /opt/ros/"$ROSV"/setup.bash

echo "ROS installation complete."
echo "Installing pocketsphinx..."
#sudo apt-get install ros-"$ROSV"-pocketsphinx
#sudo apt-get install gstreamer0.10-gconf
echo "Installing ROS sound packages..."
#sudo apt-get install ros-"$ROSV"-sound*
#sudo apt-get install ros-"$ROSV"-openni*
#sudo apt-get install ros-"$ROSV"-openni2*

LOC="~/"

#mkdir $(LOC)/sensor-admin
#cd $(LOC)/sensor-admin
#git clone https://github.com/DeepBlue14/Software_Engineer_91.411_2.git

#build CMakeLists:
DIR=pwd
#sed -i 's/'$(DIR)'/beautiful/g' ./SoftEng.desktop
#chmod +x SoftEng.desktop
#cp SoftEng.desktop ~/Desktop

echo "Instalation complete."


