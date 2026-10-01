# LiveCast Studio 📡🎥

**LiveCast Studio** is a professional Android live streaming broadcast studio application designed for Facebook Live and YouTube Live. It supports real-time camera broadcasting, screen capture sharing, broadcast HUD controls, live chat comments, and local Room database session history.

## 🚀 GitHub Actions Auto-APK Build

This repository includes a pre-configured GitHub Actions workflow (`.github/workflows/build-apk.yml`) that automatically compiles and packages the APK whenever you push code or trigger it manually.

### How to Download APK from GitHub:
1. Push this project to your GitHub repository (via AI Studio's **Export / Push to GitHub** button).
2. Go to your repository on GitHub.
3. Click on the **Actions** tab at the top.
4. Select the latest workflow run: **"Build Android APK"**.
5. Scroll down to the **Artifacts** section at the bottom of the page.
6. Click on **`LiveCastStudio-Release-APK`** or check the **Releases** tab to download your ready-to-install `.apk` file!

---

## 🛠️ Building Locally

If you clone the repository locally on your computer:

```bash
# Make gradlew executable
chmod +x gradlew

# Build Release APK
./gradlew assembleRelease

# Output APK path:
# app/build/outputs/apk/release/app-release.apk
```

---

## ✨ Features

- **Facebook Live Integration:** Select between personal timeline, creator pages, and community groups.
- **YouTube Live Integration:** Channel selection, ultra-low latency streaming, and custom privacy controls (Public, Unlisted, Private).
- **Dual Broadcast Modes:** Real CameraX camera feed or system Screen Live capture.
- **Live Broadcast Studio HUD:** Real-time duration timer, bitrate & FPS metrics, mic mute/unmute, and camera flip.
- **Interactive Live Chat:** Real-time viewer comments feed with host pinned announcements.
- **Local Room Database:** Persistent storage and analytics for past live broadcast sessions.
