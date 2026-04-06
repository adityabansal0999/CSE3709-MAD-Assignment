# CSE3709 – Mobile Application Development Assignment

Name: Aditya Bansal
Section: CSE-8
University: BML Munjal University
Course: CSE3709 – Mobile Application Development

---

## GitHub Repository

https://github.com/adityabansal0999/CSE3709-MAD-Assignment

---

## Overview

This repository contains four Android projects developed as part of the Mobile Application Development assignment.

Each question is implemented as a separate Android Studio project. All projects are written in Java and tested on Android Emulator.

---

## Repository Structure

CSE3709-MAD-Assignment/
├── Q1_CurrencyConverter/
├── Q2_AudioVideoPlayer/
├── Q3_SensorData/
└── Q4_PhotoGallery/

Each folder contains a complete Android Studio project with its own code, layouts and resources.
## Requirements

- Android Studio
- Minimum SDK: API 24
- Language: Java
- Emulator or real Android device
---

## How to Run the Projects

1. Clone the repository:
   git clone https://github.com/adityabansal0999/CSE3709-MAD-Assignment.git

2. Open Android Studio

3. Click File → Open

4. Select any project folder (for example Q1_CurrencyConverter)

5. Wait for Gradle sync to complete

6. Run the app on emulator or real device

---

## Project 1: Currency Converter

Folder: Q1_CurrencyConverter

This app converts currency between INR, USD, EUR and JPY.

User enters an amount, selects source and target currencies using dropdowns and presses convert. Result is shown on the screen.

There is also a Settings screen where user can switch between Light and Dark theme. The selected theme is saved using SharedPreferences.

Project structure:

Q1_CurrencyConverter/
└── app/src/main/
├── java/com/example/q1_currencyconverter/
│   ├── MainActivity.java
│   └── SettingsActivity.java
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   └── activity_settings.xml
│   ├── values/
│   │   ├── strings.xml
│   │   ├── colors.xml
│   │   └── themes.xml
│   └── values-night/
│       └── themes.xml
└── AndroidManifest.xml

Concepts used:
Activities, Intents, Spinner, ArrayAdapter, SharedPreferences, Themes

---

## Project 2: Audio and Video Player

Folder: Q2_AudioVideoPlayer

This app has two sections: audio player and video player.

In audio section, user selects a file from device storage and can control playback using Play, Pause, Stop and Restart buttons. A SeekBar shows current position.

In video section, a video is streamed from a URL and similar controls are provided.

Project structure:

Q2_AudioVideoPlayer/
└── app/src/main/
├── java/com/example/q2_audiovideoplayer/
│   └── MainActivity.java
├── res/
│   ├── layout/
│   │   └── activity_main.xml
│   ├── values/
│   │   ├── strings.xml
│   │   └── colors.xml
│   └── xml/
│       └── network_security_config.xml
└── AndroidManifest.xml

Concepts used:
MediaPlayer, VideoView, ActivityResultLauncher, Internet permission

---

## Project 3: Sensor Data App

Folder: Q3_SensorData

This app reads and displays data from accelerometer, light sensor and proximity sensor.

Values update in real time. If a sensor is not available on device, a message is shown.

Project structure:

Q3_SensorData/
└── app/src/main/
├── java/com/example/q3_sensordata/
│   └── MainActivity.java
├── res/
│   ├── layout/
│   │   └── activity_main.xml
│   ├── values/
│   │   ├── strings.xml
│   │   └── colors.xml
└── AndroidManifest.xml

Concepts used:
SensorManager, SensorEventListener, Activity Lifecycle

---

## Project 4: Photo Gallery App

Folder: Q4_PhotoGallery

This app allows user to create or choose a folder, take photos using camera and store them in that folder.

Images are displayed in a grid using RecyclerView. On clicking an image, details like name, path, size and date are shown. User can delete the image with confirmation dialog.

Project structure:

Q4_PhotoGallery/
└── app/src/main/
├── java/com/example/q4_photogallery/
│   ├── MainActivity.java
│   ├── GalleryActivity.java
│   ├── ImageDetailsActivity.java
│   ├── ImageAdapter.java
│   └── ImageItem.java
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── activity_gallery.xml
│   │   ├── activity_image_details.xml
│   │   └── item_image.xml
│   ├── values/
│   │   ├── strings.xml
│   │   ├── colors.xml
│   │   └── themes.xml
│   └── xml/
│       └── file_paths.xml
└── AndroidManifest.xml

Concepts used:
Camera Intent, FileProvider, RecyclerView, Adapter, AlertDialog, File handling, Intents

---

## Screenshots

Screenshots of all projects are added inside their respective folders.

---

## Notes

All projects are implemented based on topics taught in class.
Code is properly structured and formatted.
Git history shows step by step development of each project.
