LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CPP_EXTENSION := .cpp .cc
LOCAL_MODULE    := shadow-app
LOCAL_SRC_FILES := shadow-app.cpp

ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_CFLAGS += -ffast-math -mtune=atom -mssse3 -mfpmath=sse
endif

include $(BUILD_SHARED_LIBRARY)