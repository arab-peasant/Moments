# 🎵 SoundGallery

A polished Android photo gallery where every photo can have a song attached — tap a photo to open it in a full-screen viewer with music that plays automatically, just like Instagram Stories but for your personal gallery.

Made a program that acts as Instagram stories.

---

## ✨ Features

- **Photo gallery** — grid view of all device photos, synced from MediaStore
- **Music attachment** — link any song from your device to any photo
- **Full-screen viewer** — swipe through photos; linked song plays automatically when you switch to a photo
- **Music player** — album art, waveform animation, seek bar, play/pause, skip prev/next
- **Dynamic backgrounds** — viewer background color extracted from the photo via Palette API
- **Liked photos** — heart any photo; filter by liked in the gallery
- **Background playback** — music keeps playing when you leave the app (foreground service)
- **Song picker** — searchable bottom sheet showing all audio on your device
- **Captions** — optional text caption per photo

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
git clone <your-repo>
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

## 📱 How to use

1. **Gallery** — your device photos appear in a grid. Photos with a song attached show a 🎵 badge.
2. **Tap a photo** → full-screen viewer opens; if a song is linked, it starts playing immediately.
3. **Swipe left/right** → next/previous photo; song changes automatically.
4. **Tap 🎵 (top right)** → song picker bottom sheet opens; search and select any song.
5. **Tap ✕ on the player** → unlinks the song from the current photo.
6. **Tap ♡** → toggles like; liked photos appear in the Liked tab.

---

## 🔧 Extending

**Add Spotify / streaming music:** Replace the `SongPickerBottomSheet` MediaStore query with a Spotify SDK search. The `AudioTrack` model accepts a URI string — pass a Spotify URI and update `ViewerViewModel` to handle streaming URIs via ExoPlayer's `DefaultDataSource`.

**Cloud sync:** Replace Room with a remote database (Firestore, Supabase) by swapping the `MediaRepository` data source. The ViewModels don't care where data comes from.

**Captions:** Call `viewModel.saveCaption(photoId, text)` from a dialog or inline edit field in the viewer.

**Sharing:** Use `FileProvider` + `ShareCompat` to share a photo. For sharing the "photo + song" combo as a video, use `MediaMuxer` to composite the image and audio into an MP4.

---

## 🐛 Known limitations

- Album art for songs without embedded art won't load (shows placeholder)
- Very long song titles ellipsize in the player card
- Font requires manual download (see Setup step 2)

---

## 📄 License

MIT
