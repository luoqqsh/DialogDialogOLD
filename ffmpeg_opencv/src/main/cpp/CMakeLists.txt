#cmake最低版本
cmake_minimum_required(VERSION 3.10.2)
# 创建一个变量 source_file 它的值就是src/main/cpp/ 所有的.cpp文件
# 使用 IMPORTED 标志告知 CMake 只希望将库导入到项目中
# 如果是静态库则将shared改为static
# 参数分别为：库、属性（SHARED、STATIC）、导入地址（IMPORTED_LOCATION）、so所在地址（.../xxx.so）
#=====================================================================================#
#为了确保 CMake 可以在编译时定位头文件。这样就可以使用 #include <xx> 引入
#否则需要使用 #include "path/xx" 头文件的查找路径
#include_directories(include)
# 创建一个变量 source_file 它的值就是src/main/cpp/ 所有的.cpp文件,注意这里是相对路径
#file(GLOB source_file *.cpp *.c)
#添加其他预编译库，-L指向库文件的查找路径
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L/Users/xrxing/Desktop/FFtest/app/src/main/jniLibs/${ANDROID_ABI}")
#=====================================================================================#
# 添加FFmpeg编译库方法1
add_library(native-lib SHARED native-lib.cpp)
#add_library(native-lib SHARED ${source_file})
#message("source_file：${source_file}")
#message("CMAKE_SOURCE_DIR：${CMAKE_SOURCE_DIR}")
#message("ANDROID_ABI：${ANDROID_ABI}")
#add_library(ffmpeg-invoke SHARED
#        ${CMAKE_SOURCE_DIR}/include/cmdutils.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg_filter.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg_hw.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg_opt.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg-invoke.cpp)

# 添加FFmpeg编译库方法2
#add_library(ffmpeg
#        SHARED
#        IMPORTED)
#set_target_properties(ffmpeg
#        PROPERTIES IMPORTED_LOCATION
#        ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libffmpeg.so)
#=====================================================================================#
# 以上是示例，下面添加自己需要的编译库
#add_library(ffmpegcmd SHARED
#        ${CMAKE_SOURCE_DIR}/include/cmdutils.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg_filter.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg_hw.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg_opt.c
#        ${CMAKE_SOURCE_DIR}/include/ffmpeg-invoke.cpp
#        )
#=====================================================================================#
find_library( # Sets the name of the path variable.
        log-lib
        log)
target_link_libraries( # Specifies the target library.
        native-lib
#        avcodec avdevice avfilter avformat avutil postproc swresample swscale
        ${log-lib})