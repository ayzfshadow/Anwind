# 有关在Android Studio中使用CMake的更多信息，请阅读
# 文档: https://d.android.com/studio/projects/add-native-code.html

# 设置生成本机库所需的CMake的最低版本。

cmake_minimum_required(VERSION 3.10.2)

# 创建并命名库，将其设置为静态
# 或共享，并提供其源代码的相对路径。
# 您可以定义多个库，然后CMake为您构建它们。
# Gradle会自动将共享库打包到APK中。

add_library( # 设置库的名称。
             shadow-app

             # 将库设置为共享库。
             SHARED

             # 提供源文件的相对路径。
             shadow-app.cpp )

# 搜索指定的预构建库并将路径存储为
# 变量。因为CMake在搜索路径中包含系统库
# 默认情况下，您只需要指定公共NDK库的名称
# 您要添加。CMake验证库是否存在于
# 正在完成构建。

find_library( # 设置路径变量的名称。
              log-lib

              # 指定要创建的NDK库的名称
              # 你想让CMake定位。
              log )

# 指定CMake应链接到目标库的库。你
# 可以链接多个库，例如您在此中定义的库
# 生成脚本、预生成的第三方库或系统库。

target_link_libraries( # 指定目标库。
                       shadow-app

                       # 将目标库链接到日志库
                       # 包含在NDK中。
                       ${log-lib} )
SET(CMAKE_CXX_FLAGS_RELEASE  "${CMAKE_CXX_FLAGS_RELEASE} -mllvm -fla -mllvm -sub")