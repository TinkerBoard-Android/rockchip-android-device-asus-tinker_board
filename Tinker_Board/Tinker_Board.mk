#
# Copyright 2014 The Android Open-Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# First lunching is S, api_level is 31
PRODUCT_SHIPPING_API_LEVEL := 31
PRODUCT_DTBO_TEMPLATE := $(LOCAL_PATH)/dt-overlay.in
PRODUCT_BOOT_DEVICE := ff0f0000.dwmmc
PRODUCT_SDMMC_DEVICE := ff0c0000.dwmmc
include device/rockchip/common/build/rockchip/DynamicPartitions.mk
include device/asus/tinker_board/Tinker_Board/BoardConfig.mk
include device/rockchip/common/BoardConfig.mk
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base.mk)
# Inherit from those products. Most specific first.
$(call inherit-product, device/rockchip/rk3288/device.mk)
$(call inherit-product, device/rockchip/common/device.mk)

DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/../overlay

PRODUCT_CHARACTERISTICS := tablet

PRODUCT_NAME := Tinker_Board
PRODUCT_DEVICE := Tinker_Board
PRODUCT_BRAND := rockchip
PRODUCT_MODEL := Tinker_Board
PRODUCT_MANUFACTURER := asus
PRODUCT_AAPT_PREF_CONFIG := hdpi
#
## add Rockchip properties
#
PRODUCT_PROPERTY_OVERRIDES += ro.sf.lcd_density=280
