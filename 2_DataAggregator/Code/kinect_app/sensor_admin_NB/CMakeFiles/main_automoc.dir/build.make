# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/james/se_ws/src/sensor_admin/sensor_admin_NB

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/james/se_ws/src/sensor_admin/sensor_admin_NB

# Utility rule file for main_automoc.

# Include the progress variables for this target.
include CMakeFiles/main_automoc.dir/progress.make

CMakeFiles/main_automoc:
	$(CMAKE_COMMAND) -E cmake_progress_report /home/james/se_ws/src/sensor_admin/sensor_admin_NB/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold "Automoc for target main"
	/usr/bin/cmake -E cmake_automoc /home/james/se_ws/src/sensor_admin/sensor_admin_NB/CMakeFiles/main_automoc.dir/ Debug

main_automoc: CMakeFiles/main_automoc
main_automoc: CMakeFiles/main_automoc.dir/build.make
.PHONY : main_automoc

# Rule to build all files generated by this target.
CMakeFiles/main_automoc.dir/build: main_automoc
.PHONY : CMakeFiles/main_automoc.dir/build

CMakeFiles/main_automoc.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/main_automoc.dir/cmake_clean.cmake
.PHONY : CMakeFiles/main_automoc.dir/clean

CMakeFiles/main_automoc.dir/depend:
	cd /home/james/se_ws/src/sensor_admin/sensor_admin_NB && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/james/se_ws/src/sensor_admin/sensor_admin_NB /home/james/se_ws/src/sensor_admin/sensor_admin_NB /home/james/se_ws/src/sensor_admin/sensor_admin_NB /home/james/se_ws/src/sensor_admin/sensor_admin_NB /home/james/se_ws/src/sensor_admin/sensor_admin_NB/CMakeFiles/main_automoc.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/main_automoc.dir/depend

