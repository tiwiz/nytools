#!/bin/bash

echo "Searching needed tools"
adbPath=`which adb`
scrcpyPath=`which scrcpy`

echo "ADB=$adbPath" >> nytools.config
echo "SCRCPY=$scrcpyPath" >> nytools.config

echo "Config file generated"