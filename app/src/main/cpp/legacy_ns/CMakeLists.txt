cmake_minimum_required(VERSION 3.4.1)

file(GLOB SRC_FILES
        */*.cc
        */*/*.c
        */*/*.cc
        */*/*/*.c
        ns-lib.cpp
        )
#set(libs_include_DIR
#        common_audio/signal_processing
#        common_audio/signal_processing/include
#        common_audio/third_party/fft4g
#        common_audio/third_party/spl_sqrt_floor
#        modules/audio_processing/legacy_ns
#        rtc_base
#        rtc_base/numerics
#        system_wrappers/include
#        )

add_library(legacy_ns-lib SHARED ${SRC_FILES})
#include_directories(${libs_include_DIR})
include_directories(./)
add_definitions(
        -DWEBRTC_ANDROID
        -DWEBRTC_POSIX
)

find_library(log-lib log)

target_link_libraries(legacy_ns-lib ${log-lib})