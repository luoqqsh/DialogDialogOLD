#===========开始配置===================================================================#
cmake_minimum_required(VERSION 3.10.2)#cmake最低版本,有3.4.1 ， 3.6.0 ， 3.10.2
message("")#cmake日志，在build中查看，需要执行ReBuild Project
# 创建一个变量 source_file 它的值就是src/main/cpp/ 所有的.cpp文件
message("CMAKE_SOURCE_DIR：${CMAKE_SOURCE_DIR}")#打印CMake路径
#===========导入静态库===================================================================#
#静态库导入(.a导入),比如库文件名为libget.a
#add_library(get STATIC IMPORTED)
#set_target_properties(get PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/xxx.a)
#添加完要在target_link_libraries添加相应的名字进行链接
#===========导入动态库===================================================================#
add_library(get SHARED IMPORTED)
set_target_properties(get PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/xxx.so)
#so文件在src/main/jniLibs目录下的情况，CMAKE_ANDROID_ARCH_ABI为Cpu架构，比如armeabi-v7a.
#动态库需要先在代码中通过System.loadLibrary("动态库名");进行关联添加后才能正常使用


# 使用 IMPORTED 标志告知 CMake 只希望将库导入到项目中
# 如果是静态库则将shared改为static
# 参数分别为：库、属性（SHARED、STATIC）、导入地址（IMPORTED_LOCATION）、so所在地址（.../xxx.so）
#=====================================================================================#
#为了确保 CMake 可以在编译时定位头文件。这样就可以使用 #include <xx> 引入
#否则需要使用 #include "path/xx" 头文件的查找路径
#include_directories(include)

# 批量引入所有源文件，创建一个变量 source_file 它的值就是src/main/cpp/ 目录下所有的.cpp文件,注意这里是相对路径
#file(GLOB source_file *.cpp *.c)

#添加其他预编译库， -L指向库文件的查找路径
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L/Users/xrxing/Desktop/FFtest/app/src/main/jniLibs/${ANDROID_ABI}")
#=====================================================================================#
# 添加FFmpeg编译库方法1
# 最终生成的名字，明确指定动态库.so，源文件
add_library(native-lib SHARED native-lib.cpp)
#add_library(native-lib SHARED ${source_file})
#message("source_file：${source_file}")

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
#find_library( # Sets the name of the path variable.定义临时变量指向liblog.so。意义不大 只能找系统的
#        log-lib
#        log)
target_link_libraries( # Specifies the target library.
        native-lib
        #        avcodec avdevice avfilter avformat avutil postproc swresample swscale
        #        ${log-lib})需定义临时变量，如上所示，意义不大。
        log)