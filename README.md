# 🎵 Moments 

- A polished Android photo gallery where every photo can have a song attached — tap a photo to open it in a full-screen viewer with music that plays automatically, just like Instagram Stories but private and personal. 
I've created this app because I couldn't find anything like it in the playstore. 
---

## ✨ Features

- **Photo gallery** — grid view of all device photos, synced from MediaStore
- **Music attachment** — link any song from your device to any photo
- **Description** — You can add a discription 

---

## 🏗 Architecture

```
app/
├── data/
│   ├── db/          — Room database (PhotoDao, SoundGalleryDatabase)
│   ├── model/       — PhotoEntry, AudioTrack data classes
│   └── repository/  — MediaRepository (MediaStore queries + Room sync)
├── di/              — Hilt DI module
├── service/         — MusicPlaybackService (Media3 MediaSessionService)
├── ui/
│   ├── gallery/     — GalleryFragment, GalleryViewModel, GalleryAdapter
│   ├── viewer/      — ViewerFragment, ViewerViewModel, ViewerPagerAdapter, WaveformView
│   └── picker/      — SongPickerBottomSheet, SongPickerAdapter
└── MainActivity.kt
```

**Tech stack:**
| Layer | Library |
|---|---|
| UI | View Binding, Material3, ConstraintLayout, ViewPager2 |
| Navigation | Jetpack Navigation + Safe Args |
| State | ViewModel, StateFlow, Coroutines |
| Database | Room + KSP |
| DI | Hilt |
| Media | Media3 ExoPlayer + MediaSessionService |
| Images | Glide 4 |
| Colors | AndroidX Palette |
| Async | Kotlin Coroutines |

---

## 🚀 Setup

### 1. Clone & open in Android Studio

```bash
git clone https://github.com/arab-peasant/Moments.git
```

Open in **Android Studio Hedgehog** (2023.1+) or newer.

### 2. Add the DM Serif Display font

Download from [Google Fonts](https://fonts.google.com/specimen/DM+Serif+Display) and place:

```
app/src/main/res/font/dm_serif_display_italic.ttf
```

Or delete the font references and use a system font — the app will still compile fine.

### 3. Build & run

```bash
./gradlew assembleDebug
```

Target a device or emulator running **Android 8.0+ (API 26)**. A real device is recommended for testing MediaStore access.

### 4. Grant permissions

On first launch the app requests:
- `READ_MEDIA_IMAGES` (Android 13+) or `READ_EXTERNAL_STORAGE`
- `READ_MEDIA_AUDIO` (Android 13+)

---
