cmake_minimum_required(VERSION 2.8.3)
project(app_node)

## Find catkin macros and libraries
## if COMPONENTS list like find_package(catkin REQUIRED COMPONENTS xyz)
## is used, also find other catkin packages
find_package(catkin REQUIRED COMPONENTS
  geometry_msgs
  roscpp
  rospy
  sensor_msgs
  std_msgs
)

## System dependencies are found with CMake's conventions
# find_package(Boost REQUIRED COMPONENTS system)
find_package(Boost REQUIRED COMPONENTS system filesystem regex)

find_package(QJSON)

find_package(Qt REQUIRED COMPONENTS QtCore QtGui QtWidgets QtOpenGL QtXml QtSql)
include(${QT_USE_FILE})
ADD_DEFINITIONS(${QT_DEFINITIONS})

set(CMAKE_AUTOMOC ON)

## Uncomment this if the package has a setup.py. This macro ensures
## modules and global scripts declared therein get installed
## See http://ros.org/doc/api/catkin/html/user_guide/setup_dot_py.html
# catkin_python_setup()

################################################
## Declare ROS messages, services and actions ##
################################################

## To declare and build messages, services or actions from within this
## package, follow these steps:
## * Let MSG_DEP_SET be the set of packages whose message types you use in
##   your messages/services/actions (e.g. std_msgs, actionlib_msgs, ...).
## * In the file package.xml:
##   * add a build_depend and a run_depend tag for each package in MSG_DEP_SET
##   * If MSG_DEP_SET isn't empty the following dependencies might have been
##     pulled in transitively but can be declared for certainty nonetheless:
##     * add a build_depend tag for "message_generation"
##     * add a run_depend tag for "message_runtime"
## * In this file (CMakeLists.txt):
##   * add "message_generation" and every package in MSG_DEP_SET to
##     find_package(catkin REQUIRED COMPONENTS ...)
##   * add "message_runtime" and every package in MSG_DEP_SET to
##     catkin_package(CATKIN_DEPENDS ...)
##   * uncomment the add_*_files sections below as needed
##     and list every .msg/.srv/.action file to be processed
##   * uncomment the generate_messages entry below
##   * add every package in MSG_DEP_SET to generate_messages(DEPENDENCIES ...)

## Generate messages in the 'msg' folder
# add_message_files(
#   FILES
#   Message1.msg
#   Message2.msg
# )

## Generate services in the 'srv' folder
# add_service_files(
#   FILES
#   Service1.srv
#   Service2.srv
# )

## Generate actions in the 'action' folder
# add_action_files(
#   FILES
#   Action1.action
#   Action2.action
# )

## Generate added messages and services with any dependencies listed here
# generate_messages(
#   DEPENDENCIES
#   geometry_msgs#   sensor_msgs#   std_msgs
# )

###################################
## catkin specific configuration ##
###################################
## The catkin_package macro generates cmake config files for your package
## Declare things to be passed to dependent projects
## INCLUDE_DIRS: uncomment this if you package contains header files
## LIBRARIES: libraries you create in this project that dependent projects also need
## CATKIN_DEPENDS: catkin_packages dependent projects also need
## DEPENDS: system dependencies of this project that dependent projects also need
catkin_package(
#  INCLUDE_DIRS include
#  LIBRARIES app_node
#  CATKIN_DEPENDS geometry_msgs roscpp rospy sensor_msgs std_msgs
#  DEPENDS system_lib
)

###########
## Build ##
###########

## Specify additional locations of header files
## Your package locations should be listed before other locations
# include_directories(include)
include_directories(
  ${catkin_INCLUDE_DIRS}
)

## Declare a cpp library
# add_library(app_node
#   src/${PROJECT_NAME}/app_node.cpp
# )

## Declare a cpp executable
# add_executable(app_node_node src/app_node_node.cpp)

## Add cmake target dependencies of the executable/library
## as an example, message headers may need to be generated before nodes
# add_dependencies(app_node_node app_node_generate_messages_cpp)

## Specify libraries to link a library or executable target against
# target_link_libraries(app_node_node
#   ${catkin_LIBRARIES}
# )

#############
## Install ##
#############

# all install targets should use catkin DESTINATION variables
# See http://ros.org/doc/api/catkin/html/adv_user_guide/variables.html

## Mark executable scripts (Python etc.) for installation
## in contrast to setup.py, you can choose the destination
# install(PROGRAMS
#   scripts/my_python_script
#   DESTINATION ${CATKIN_PACKAGE_BIN_DESTINATION}
# )

## Mark executables and/or libraries for installation
# install(TARGETS app_node app_node_node
#   ARCHIVE DESTINATION ${CATKIN_PACKAGE_LIB_DESTINATION}
#   LIBRARY DESTINATION ${CATKIN_PACKAGE_LIB_DESTINATION}
#   RUNTIME DESTINATION ${CATKIN_PACKAGE_BIN_DESTINATION}
# )

## Mark cpp header files for installation
# install(DIRECTORY include/${PROJECT_NAME}/
#   DESTINATION ${CATKIN_PACKAGE_INCLUDE_DESTINATION}
#   FILES_MATCHING PATTERN "*.h"
#   PATTERN ".svn" EXCLUDE
# )

## Mark other files for installation (e.g. launch and bag files, etc.)
# install(FILES
#   # myfile1
#   # myfile2
#   DESTINATION ${CATKIN_PACKAGE_SHARE_DESTINATION}
# )

#############
## Testing ##
#############

## Add gtest based cpp test target and link libraries
# catkin_add_gtest(${PROJECT_NAME}-test test/test_app_node.cpp)
# if(TARGET ${PROJECT_NAME}-test)
#   target_link_libraries(${PROJECT_NAME}-test ${PROJECT_NAME})
# endif()

## Add folders to be run by python nosetests
# catkin_add_nosetests(test)

add_executable(main src/main.cpp
	       	src/AddDeviceGui.cpp
		src/AddUserGui.cpp
		src/CenterGui.cpp
		src/DeviceAbstractGui.cpp
		src/DevicesGui.cpp
		src/HelpGui.cpp
		src/HistoryGui.cpp
		src/LoginGui.cpp
		src/MasterGui.cpp
		src/Menu.cpp
		src/ModifyDeviceGui.cpp
		src/ModifyUserGui.cpp
		src/OpeningGui.cpp
		src/ResultsGui.cpp
		src/SplashGui.cpp
		src/UserAbstractGui.cpp
		src/UsersGui.cpp
		src/VideoGui.cpp
		src/ViewUserGui.cpp)
target_link_libraries(main ${catkin_LIBRARIES})
target_link_libraries(main ${QT_LIBRARIES})
target_link_libraries(main ${Boost_LIBRARIES})
target_link_libraries(main ${QJson})

#add_executable(main_3D src/main_3D.cpp
#		       src/Frame3D.cpp)
#target_link_libraries(main_3D ${catkin_LIBRARIES})
#target_link_libraries(main_3D ${QT_LIBRARIES})

#add_executable(main_3D src/main_3D.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/qglviewer.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/frame.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/manipulatedFrame.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/camera.cpp
#                      src/libQGLViewer-2.6.1/QGLViewer/constraint.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/mouseGrabber.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/keyFrameInterpolator.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/manipulatedCameraFrame.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/manipulatedFrame.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/saveSnapshot.cpp
#		       src/libQGLViewer-2.6.1/QGLViewer/vec.cpp)
	       
